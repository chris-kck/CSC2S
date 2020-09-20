all:	./bin/Flow.class

# define a makefile variable for the java compiler
JCC = javac
# make runs first target which is default in this case
default: ./bin/Flow.class

#Hacky wildcard * method to compile all in 1 go
./bin/Flow.class: ./src/Sequential/Flow.java
	$(JCC) -d ./bin -g ./src/Sequential/*.java
	$(JCC) -d ./pbin -g ./src/Parallel/*.java

#Creates javadocs
docs:
	javadoc -classpath bin/ -d doc/ src/Sequential/*.java
	javadoc -classpath bin/ -d pdoc/ src/Parallel/*.java

# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) ./bin/*.class
	$(RM) ./pbin/*.class

sequential: ./bin/Flow.class
	@java -cp ./bin Flow  $(params)

parallel:	./pbin/Flow.class
	@java -cp ./pbin Flow  $(params)