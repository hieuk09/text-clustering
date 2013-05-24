SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar

javac -cp %CLASSPATH% WVToolYaleOutput.java
java -cp %CLASSPATH%;. WVToolYaleOutput 