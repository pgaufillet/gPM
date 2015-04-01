@rem == Helper script to install the non-redistributable JAR in local Maven repository ==

@echo off
setlocal 

set ACTIVATION_JAR=activation-${activation.version}.jar
set MAIL_JAR=mail-${javamail.version}.jar
set JTA_JAR=jta-${jta.version}.jar
set OJDBC_JAR=ojdbc14-${oraclesql.version}.jar
set STRUTS_LAYOUT_JAR=struts-layout-${struts.layout.version}.jar

echo ---------------------------------------------------------
echo This script installs some non-redistributable JAR in your
echo local Maven repository.
echo It is required to have the JAR files (which must
echo be downloaded manually) available in this directory:
echo .
echo ** Sun libraries (required):
echo JavaMail (v${javamail.version}, Java Activation Framework (v${activation.version}),
echo Java Transaction API (v${jta.version})
echo .
echo They can be downloaded at
echo http://java.sun.com/products/javamail/downloads/index.html
echo http://java.sun.com/products/javabeans/glasgow/jaf.html
echo http://java.sun.com/products/jta
echo .
echo The local files must be named: %MAIL_JAR%, %ACTIVATION_JAR%, %JTA_JAR%
echo .
echo ** Struts layout (required):
echo It can be downloaded at 
echo http://struts.improve-technologies.com/download.html
echo .
echo The local file must be named %STRUTS_LAYOUT_JAR%
echo .
echo ** Oracle(tm) JDBC thin driver (optional):
echo It can be downloaded at 
echo http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/index.html
echo .
echo The local file must be named %OJDBC_JAR%
echo .
echo .
echo Note: This script requires the Maven2 tool installed and referenced in your path.
echo ---------------------------------------------------------
echo .
echo Press a key to continue, or Ctrl-C to stop now.
pause 

call mvn install:install-file -DgroupId=javax.mail -DartifactId=mail -Dversion=${javamail.version} -Dpackaging=jar -Dfile=%MAIL_JAR% 
call mvn install:install-file -DgroupId=javax.activation -DartifactId=activation -Dversion=${activation.version} -Dpackaging=jar -Dfile=%ACTIVATION_JAR%
call mvn install:install-file -DgroupId=javax.transaction -DartifactId=jta -Dversion=${jta.version} -Dpackaging=jar -Dfile=%JTA_JAR%
call mvn install:install-file -DgroupId=struts-layout -DartifactId=struts-layout -Dversion=${struts.layout.version} -Dpackaging=jar -DgeneratePom=true -Dfile=%STRUTS_LAYOUT_JAR%

if not exist %OJDBC_JAR% goto end

call mvn install:install-file -DgroupId=oracle -DartifactId=ojdbc14 -Dversion=${oraclesql.version} -Dpackaging=jar -DgeneratePom=true -Dfile=%OJDBC_JAR%

:end
