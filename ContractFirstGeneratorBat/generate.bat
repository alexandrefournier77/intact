@echo off

:: Constantes
FOR /F "tokens=*" %%i in ('FINDSTR /B /V "#" application.properties') do set %%i

echo.
echo  ##############################
echo  ## Contract First Generator ##
echo  ##############################
echo.
echo  Here are the selected parameters:
echo.
echo  PROJECT LOCATION:  %default.projects.path%
echo  CREATE EXAMPLE:    %default.add.example.endpoint%
echo  GROUP ID:          %default.group.id%
echo  ARTIFACT ID:       %default.artifact.id%
echo  NAMESPACE:         %default.namespace%
echo  SERVICE NAME:      %default.service.name%
echo  SERVICE ENDPOINTS: %default.endpoints%

echo.
echo.
set /p CONFIRM= Do you confirm the above parameters are correct? (Y/[N]): 

IF /I "%CONFIRM%" NEQ "Y" GOTO END
echo.
echo.
echo  Generating files...
echo.
java -jar -Dspring.config.location=./ ContractFirstGenerator-1.0.0-SNAPSHOT.jar
echo.
echo  Files successfuly generated!
echo.
echo  Opening the folder containing the new project...
echo.
set projectpath="%default.projects.path%/%default.artifact.id%/"
%SystemRoot%\explorer.exe %projectpath:/=\%
GOTO EOF

:END
echo.
echo Nothing executed. The Contract First Generator ended.
echo.

:EOF
PAUSE