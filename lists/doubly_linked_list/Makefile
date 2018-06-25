CC = gcc

all: dlist.o

dlist.o: main.c dllist.c
	$(CC) main.c dllist.c -o $@

clean:
	-rm -rf *.o
