SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar

javac -cp %CLASSPATH% WVToolDictionaryExample.java
java -cp %CLASSPATH%;. WVToolDictionaryExample 