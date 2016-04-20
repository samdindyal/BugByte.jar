build:
	mkdir -p bin
	javac -cp $CLASSPATH:lib/AppleJavaExtensions.jar:src src/*.java -d bin
	echo "Main-Class: BugByteLauncher" > Manifest
	jar cfvm BugByte.jar Manifest res -C bin .
clean:
	rm -vrf Manifest BugByte.jar bin
