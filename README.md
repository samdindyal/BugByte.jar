![alt tag](https://raw.github.com/samdindyal/BugByte/master/res/logo_full.png)
---
<br>

![alt screenshot](img/bugbytelogin.png)

This is an application, written in Java as an assignment for CPS406 (Intro to Software Engineering), which emulates a bug report system. It implements user accounts and a database of bugs of which users can add, remove and edit. Because this is an emulation, information will not be transmitted through the internet and will only be emulated through offline files in which we plan to store the appropriate serializable objects.


---
<br>

## Compilation
<br>

***Please note that OS X/Linux and Windows users must compile BugByteLauncher differently.***


<p>You can build an executable jar file by running the "make_jar.sh" script on OS X and Linux or the "make_jar.bat" batch file on Windows. This will also compile everything in addition to the jar creation.</p>

#### OS X/Linux:
```bash
javac -cp $CLASSPATH:lib/AppleJavaExtensions.jar BugByteLauncher.java
```

### Windows:
```bash
javac -cp ".;lib/AppleJavaExtensions.jar" BugByteLauncher.java
```
