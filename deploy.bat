@SET SUBDIR=%~dp0

@call mvn clean install -Papplication

@c:
@cd "c:\Program Files\Apache Software Foundation\apache-tomcat-7.0.26"

@ECHO "Clean old version"
@del webapps\scholagest-app.war
@rmdir /s /q webapps\scholagest-app

@ECHO "Deploy new version"
@xcopy D:\Programming\eclipse-workspace\Scholagest\app\target\scholagest-app.war webapps\

@ECHO "Start tomcat"
@call bin\startup.bat

@d:
@cd %SUBDIR%