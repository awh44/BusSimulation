.PHONY: run compile clean

run: compile
	java Main

compile: Main.java Student.java Bus.java BusStop.java
	javac Main.java Student.java Bus.java BusStop.java

clean:
	rm -f *.class
