@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.

@echo off

if "%MAVEN_BATCH_PAUSE%"=="" set MAVEN_BATCH_PAUSE=pause

if "%MAVEN_BATCH_ECHO%"=="" set MAVEN_BATCH_ECHO=on

@REM Executes a child command in a separate process
@REM $1 : title
@REM $2 : The 1st command line parameter.
@REM $3 : The 2nd command line parameter.
@REM ...
set TITLE=%1
shift

set CMD=%1
shift

@REM This label provides a target to cmd /c exit /b to return exit code 1
@REM for Solaris compatibility...
:exit_batch
@endlocal & exit /b %exitcode%

:osflag
@REM Determine the command interpreter to use.
for /f "usebackq break=on tokens=*" %%A in (`chcp 2^>nul`) do set "_chcp=%%A"
echo %_chcp% | find /I /N "65001" ^>nul
if not errorlevel 1 goto oem
for /f "tokens=2 delims=:" %%A in ('chcp') do (
    if not "%%A"=="" (
        set "_chcp=%%A"
    )
)
if "%_chcp%"=="1" set _chcp=65001

@REM Setup the local scope for the nested batch script
setlocal enabledelayedexpansion

@REM Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >nul 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is not set to a valid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@REM Setup the command line

set CLASSPATH=%APP_HOME%\maven\boot\plexus-classworlds-2.6.0.jar

@REM Execute Maven
"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%CLASSPATH%" org.codehaus.plexus.classworlds.launcher.Launcher %CMD%

if %ERRORLEVEL% neq 0 (
    set exitcode=%ERRORLEVEL%
    goto exit_batch
)

:end
@endlocal & exit /b %exitcode%

:fail
set exitcode=1
goto exit_batch
