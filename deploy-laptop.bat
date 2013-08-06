@SET SUBDIR=%~dp0

@call mvn clean install -Papplication -DskipTests

@cd "C:\Program Files\Apache\apache-tomcat-7.0.37"

@ECHO "Clean old version"
@del webapps\scholagest-app.war
@rmdir /s /q webapps\scholagest-app

@ECHO "Deploy new version"
@xcopy C:\Users\cla\workspaces\java-workspace\scholagest\app\target\scholagest-app.war webapps\

@ECHO "Start tomcat"
@call bin\startup.bat

@cd %SUBDIR%