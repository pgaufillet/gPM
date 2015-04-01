@rem ==  Helper script used to launch the tools ==
@rem This script check the required environment variables, and run the 'java' tool
@rem on the specified JAR file

@setlocal

if not "%GPM_HOME%" == "" goto checkGpmHome

set GPM_HOME=%~dp0

echo.
echo NOTE: GPM_HOME is not defined, assuming GPM_HOME=%GPM_HOME%
echo.

:checkGpmHome
if exist "%GPM_HOME%\lib" goto launch

echo.
echo ERROR: GPM_HOME seems to be defined to an invalid directory, or gPM installation is incorrect.
echo GPM_HOME = %GPM_HOME%
echo.
goto error

:launch

@rem Check JAVA_HOME
if not "%JAVA_HOME%" == "" goto checkJavaHome

echo.
echo ERROR: JAVA_HOME is not defined in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation  (JRE 1.5.0 minimum)
echo.
goto error

:checkJavaHome
if exist "%JAVA_HOME%\bin\java.exe" goto launch

echo.
echo ERROR: JAVA_HOME seems to be defined to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation (JRE 1.5.0 minimum)
echo.
goto error

:launch

set EXEC_JAR="%GPM_HOME%/lib/%1.jar"

:getargs
shift
set ARGS=%ARGS% %1 
if "%1"=="" goto endargs
goto getargs

:endargs

if exist "%GPM_HOME%\log4j.properties" "%JAVA_HOME%\bin\java.exe" -DGPM_HOME=%GPM_HOME% -Dlog4j.configuration=file:%GPM_HOME%/log4j.properties -jar %EXEC_JAR% %ARGS%
if not exist "%GPM_HOME%\log4j.properties" "%JAVA_HOME%\bin\java.exe" -DGPM_HOME=%GPM_HOME% -jar %EXEC_JAR% %ARGS%

goto end

:error
@endlocal
set ERROR_CODE=1

:end
