.PHONY: run compile clean

test-1: compile
	java Main 300 50 10 1

test-2: compile
	java Main 150 50 2 1

test-3: compile
	java Main 200 50 4 1 1

run: compile
	java Main $(STUDENTS) $(SEATS) $(LOOPS) $(TIME)

compile: Main.java Student.java Bus.java BusStop.java
	javac Main.java Student.java Bus.java BusStop.java

clean:
	rm -f *.class
