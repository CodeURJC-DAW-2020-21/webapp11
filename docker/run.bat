
set BASE=%cd%

rem 1. BUILD THE APPLICATION

cd %BASE%
cd../backend

echo %cd%

echo Make sure you maven and docker hub installed with docker-compose included

echo Press any key to confirm 

pause

call mvn clean install

set BUILD_FILENAME=%cd%\target\marketplace-0.0.1-SNAPSHOT.jar
set DEST_FILENAME=%BASE%\marketplace.jar

echo %BUILD_FILENAME%

echo %DEST_FILENAME%

rem Ensure old compuled file does not exist

del %DEST_FILENAME%

xcopy %BUILD_FILENAME% %DEST_FILENAME%*

rem 2. CREATE THE DOCKER IMAGE (PACKAGE THE DB AND APP)

cd %BASE%

rem Build the docker image

docker build -t dawhost/marketplace .

rem Login to docker hub

docker login --username=dawhost

rem Push the image to docker

docker push dawhost/marketplace

pause






