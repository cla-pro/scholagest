@SET SUBDIR=%~dp0

@call mvn install -Papplication -DskipTests

@c:
@cd "C:\Program Files\Apache Software Foundation\apache-tomcat-7.0.26"

@ECHO "Clean old version"
@del webapps\scholagest-app.war
@rmdir /s /q webapps\scholagest-app

@ECHO "Deploy new version"
@xcopy D:\Programming\eclipse-workspace\scholagest\app\target\scholagest-app.war webapps\

@ECHO "Start tomcat"
@call bin\startup.bat

@d:
@cd %SUBDIR%