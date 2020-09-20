all:	./bin/Flow.class

# define a makefile variable for the java compiler
JCC = javac
# make runs first target which is default in this case
default: ./bin/Flow.class

#Hacky wildcard * method to compile all in 1 go
./bin/Flow.class: ./src/Flow.java
	$(JCC) -d ./bin -g ./src/*.java

#Creates javadocs
docs:
	javadoc -classpath bin/ -d doc/ src/*.java

# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) ./bin/*.class

parallel:	./bin/Flow.class
	@java -cp ./bin Flow  $(params)