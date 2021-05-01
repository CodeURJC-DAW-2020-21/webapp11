set BASE=%cd%

rem 1. BUILD ANGULAR PROYECT

cd %BASE%
cd../frontend/angular/marketplace

echo %cd%

echo Angular proyect shall be built now

echo Press any key to confirm

pause

call ng build --prod --base-href="/new/"

set ORIGIN_FILENAME=%cd%\dist\marketplace\*
set DEST_FILENAME=%BASE%\..\backend\src\main\resources\static\new\*
set DEL_FILENAME=%BASE%\..\backend\src\main\resources\static\new

echo %ORIGIN_FILENAME%
echo %DEST_FILENAME%

rem Ensure old compiled files does not exist

rmdir %DEL_FILENAME% /S /Q

xcopy /E %ORIGIN_FILENAME% %DEST_FILENAME% 