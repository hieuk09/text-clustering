SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar
for %%f in ("..\..\lib\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

javac -cp %CLASSPATH% WVToolWordNetExample.java
java -cp %CLASSPATH%;. -Dwvtool.wnconfig=file_properties.xml WVToolWordNetExample 