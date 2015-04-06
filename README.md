![alt tag](https://raw.github.com/samdindyal/BugByte/master/res/logo_full.png)

This is an application, written in Java as an assignment for CPS406 (Intro to Software Engineering), which emulates a bug report system. It implements user accounts and a database of bugs of which users can add, remove and edit. Because this is an emulation, information will not be transmitted through the internet and will only be emulated through offline files in which we plan to store the appropriate serializable objects.


**Note** OS X users will have to link the AppleJavaExtensions library on compilation. The terminal command is as follows:

javac -cp $CLASSPATH:lib/AppleJavaExtensions.jar BugByteLauncher.java
