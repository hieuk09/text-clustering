SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar

set CLASSPATH=..\..\wvtool.jar
for %%f in ("..\..\lib\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

javac -cp %CLASSPATH% WVToolExample.java
java -cp %CLASSPATH%;. WVToolExample 