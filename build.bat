SET JAVA_HOME=C:\Anwendungen\Java\Java SDK 6
SET ANTPATH=C:\Anwendungen\Entwicklung\Apache Ant 1.7\bin

SET PATH=%JAVA_HOME%\bin;%ANTPATH%;%PATH%


ant -Dfile.encoding=UTF-8 dist -f build.xml & pause
