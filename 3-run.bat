if "%JAVA_CLIENT_HOME%"=="" (
    call 0-settings.bat
)

echo off
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo        [44;93m!                 Now we are starting java client...                      ![0m
echo        [44;93m+-------------------------------------------------------------------------+[0m
echo on

chcp %CODE_PAGE%
cls

call %MVNW% exec:java -Dfile.encoding=UTF-8 -D"exec.mainClass"="com.codenjoy.dojo.Runner" -D"exec.args"="%GAME_TO_RUN% %BOARD_URL%"

call :ask

goto :eof

:ask
    echo off
    echo        [44;93m+---------------------------------+[0m
    echo        [44;93m!    Press any key to continue    ![0m
    echo        [44;93m+---------------------------------+[0m
    echo on
    pause >nul
goto :eof
