package com.raczy;

import com.raczy.ds.*;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2 || !args[0].equals("--type")) {
            System.out.println("Invalid arguments. Flag --type is required.");
            System.exit(1);
        }

        DataStructure<String> ds;
        switch (args[1]) {
            case "bst":
                ds = new BinarySearchTree<>();
                break;
            case "rbt":
                ds = new RedBlackTree<>();
                break;
            case "hmap":
                ds = new HashTableAdapter<>();
                break;
            case "bloom":
                ds = new BloomFilter<>(100, 0.01);
            default:
                System.out.println("Invalid --type value.");
                System.exit(2);
                return;
        }

        // handle type
        Main app = new Main(ds);
        Scanner reader = new Scanner(System.in);
        int n = reader.nextInt();
        reader.nextLine();
        ArrayList<Cmd> cmds = new ArrayList<>();

        for(int i = 0; i < n; i++) {
            cmds.add(app.parse(reader.nextLine()));
        }
        app.execute(cmds);
    }


    static class Cmd {
        enum Type {
            INSERT("insert"),
            LOAD("load"),
            DELETE("delete"),
            FIND("find"),
            MIN("min"),
            MAX("max"),
            SUCCESSOR("successor"),
            INORDER("inorder");

            private final String value;

            Type(String value) {
                this.value = value;
            }

            public Method getMethod() throws NoSuchMethodException {
                switch (this) {
                    case INSERT:
                    case DELETE:
                    case FIND:
                    case SUCCESSOR:
                        return DataStructure.class.getMethod(value, Object.class);
                    case MAX:
                    case MIN:
                    case INORDER:
                        return DataStructure.class.getMethod(value);
                    default:
                        return null;
                }
            }

            @Override
            public String toString() {
                return this.value;
            }
        }

        private Type type;
        private String arg;

        public Cmd(Type type, String arg) {
            this.type = type;
            this.arg = arg;
        }

        public String getArg() {
            return arg;
        }

        public Type getType() {
            return type;
        }
    }

    private DataStructure<String> ds;

    Main(DataStructure<String> ds) {
        this.ds = ds;
    }

    public void execute(ArrayList<Cmd> cmds) {
        try {
            for (Cmd cmd : cmds) {
                Method m = cmd.getType().getMethod();
                String s;
                switch (cmd.getType()) {
                    case INSERT:
                    case DELETE:
                        m.invoke(ds, cmd.getArg());
                        break;
                    case FIND:
                        boolean res = (Boolean) m.invoke(ds, cmd.getArg());
                        System.out.println((res) ? 1 : 0);
                        break;
                    case MIN:
                    case MAX:
                        s = (String) m.invoke(ds);
                        System.out.println((s != null) ? s : "");
                        break;
                    case INORDER:
                        m.invoke(ds);
                        break;
                    case SUCCESSOR:
                        s = (String) m.invoke(ds, cmd.getArg());
                        System.out.println((s != null) ? s : "");
                        break;
                    case LOAD:
                        load(cmd.getArg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());
        }
    }

    private void load(String filename) {
        File file = new File(filename);
        String result;
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            result = new String(data, "UTF-8");
        } catch (Exception e) {
            System.out.println("File:" + filename + "does not exist");
            return;
        }

        String[] words = result.replaceAll("[^a-zA-Z ]", "").split("\\s+");
        for(String word: words) {
            ds.insert(word);
        }
    }

    public Cmd parse(String str) {
        String[] words = str.replaceAll("[^a-zA-Z0-9 ]", "").split("\\s+");

        if (words.length == 0) {
            return null;
        }
        Cmd.Type type = Cmd.Type.valueOf(words[0].toUpperCase());
        String[] args = Arrays.copyOfRange(words, 1, words.length);

        String trimmed = null;
        if (args.length != 0) {
            trimmed = args[0].replaceAll("^[^a-zA-Z0-9]*|[^a-zA-Z0-9]*$", "");
        }
        return new Cmd(type, trimmed);
    }

    public DataStructure<String> getDs() {
        return ds;
    }
}
