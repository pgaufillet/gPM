@rem ==  Script used to build the gPM packages ==
@rem Note: This script must be executed from its directory only

@setlocal

@echo off

echo -------------------------------------------------
echo  This script builds the gPM packages for a release.
echo  It *must* be executed from its directory.
echo . 
echo  Please note that this script does *no* validation at all.
echo  Ensure that your build environment is OK, and that gPM can be packaged.
echo -------------------------------------------------
echo .
echo Press a key to continue or Ctrl-C to abort.
echo .

pause

echo .
echo Ok, let's go !

echo .
echo Cleaning the build tree...

call mvn -q clean

echo .
echo Building the JAR / WAR files...

call mvn > make_dist.log

echo .
echo Generating the Javadoc...

call mvn javadoc:javadoc >> make_dist.log

cd dist

echo .
echo Cleaning the 'dist' tree...

call mvn clean >> ../make_dist.log

echo .
echo Building the packages in 'dist'...

call mvn >> ../make_dist.log

echo ------------------------
echo Packaging is done !
echo .
echo If no errors were displayed, the packages should hopefully be available
echo in the 'dist/target' directory.
echo Note: Logs are available in 'make_dist.log' file if needed.
echo ------------------------

