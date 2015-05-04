
PongController: src/controller/*.java src/model/*.java src/view/*.java
	javac -d bin src/controller/*.java src/model/*.java src/view/*.java -classpath bin


all:
	javac -d bin src/controller/*.java src/model/*.java src/view/*.java -classpath bin
