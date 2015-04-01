call mvn clean
call mvn -DskipTests
rmdir /S /Q  %CATALINA_HOME%\webapps\gpm
copy gpm-ui-application\target\gpm-ui-application-2.0.x-SNAPSHOT.war %CATALINA_HOME%\webapps\gpm.war
call %CATALINA_HOME%\bin\startup.bat
start iexplore http://localhost:8080/gpm
