all: main

main:
	@mkdir out
	@echo 'INFO java 8 is required.'
	@echo 'Creating ./main sh script'
	@javac -d out -sourcepath src src/com/raczy/Main.java
	@echo '#!/bin/sh \n java -classpath out com.raczy.Main $$1 $$2' >main
	chmod +x ./main

clean:
	rm -rf ./main
	rm -rf ./out
