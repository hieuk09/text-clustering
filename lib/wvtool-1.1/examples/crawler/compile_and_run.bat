SETLOCAL ENABLEDELAYEDEXPANSION

set CLASSPATH=..\..\wvtool.jar
for %%f in ("..\..\lib\*.jar") do set CLASSPATH=!CLASSPATH!;%%f

javac -cp %CLASSPATH% WVToolCrawlerExample.java
java -cp %CLASSPATH%;. WVToolCrawlerExample 