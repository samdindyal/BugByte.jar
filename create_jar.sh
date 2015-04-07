#!/bin/bash
javac -cp $CLASSPATH:lib/AppleJavaExtensions.jar BugByteLauncher.java
jar cfm BugByte.jar Manifest *.class res 
