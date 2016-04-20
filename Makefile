build:
	mkdir -p build
	javac -cp $CLASSPATH:lib/AppleJavaExtensions.jar:src src/*.java -d build
	echo "Main-Class: BugByteLauncher" > Manifest
	jar cfvm BugByte.jar Manifest res -C build .
clean:
	rm -vrf Manifest BugByte.jar build
