all:	./bin/BasinClassification.class

# define a makefile variable for the java compiler
JCC = javac
# make runs first target which is default in this case
default: ./bin/BasinClassification.class

#Hacky wildcard * method to compile all in 1 go
./bin/BasinClassification.class: ./src/BasinClassification.java
	$(JCC) -d ./bin -g ./src/*.java

#Creates javadocs
docs:
	javadoc -classpath bin/ -d doc/ src/*.java

# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) ./bin/*.class

serial: ./bin/BasinClassification.class
	@java -cp ./bin BasinClassification  $(params)

parallel: ./bin/BasinClassification.class
	@java -cp ./bin ParallelBasinClassification $(params)