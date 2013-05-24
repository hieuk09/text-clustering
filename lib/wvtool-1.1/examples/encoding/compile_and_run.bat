SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar

javac -cp %CLASSPATH% WVToolExample.java
java -cp %CLASSPATH%;. WVToolExample 