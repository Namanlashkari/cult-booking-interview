@echo off
REM Compile and run the whole test suite on Windows. Needs ONLY a JDK (17+).
REM No Maven, no JUnit, no network. Finds a working JDK automatically; you can
REM force one with:  set JAVA_HOME=C:\path\to\jdk  &&  run-tests.bat
setlocal enabledelayedexpansion
cd /d "%~dp0"

set "JAVAC="
set "JAVA="

REM 1. Explicit JAVA_HOME, if it actually runs.
if defined JAVA_HOME (
  call :try_javac "%JAVA_HOME%\bin\javac.exe"
)

REM 2. javac on the PATH, if it actually runs.
if not defined JAVAC (
  for %%J in (javac.exe) do call :try_javac "%%~$PATH:J"
)

REM 3. Common install locations.
if not defined JAVAC (
  for %%D in (
    "%ProgramFiles%\Eclipse Adoptium\jdk-21"
    "%ProgramFiles%\Eclipse Adoptium\jdk-17"
    "%ProgramFiles%\Java\jdk-21"
    "%ProgramFiles%\Java\jdk-17"
    "%ProgramFiles%\Microsoft\jdk-21"
    "%ProgramFiles%\Microsoft\jdk-17"
  ) do call :try_javac "%%~D\bin\javac.exe"
)

if not defined JAVAC (
  echo ERROR: could not find a working JDK ^(need Java 17+^).
  echo Install one or set JAVA_HOME to a JDK, then re-run.
  exit /b 1
)
echo Using javac: %JAVAC%

if exist out rmdir /s /q out
mkdir out

dir /s /b src\main\java\*.java src\test\java\*.java > sources.txt
"%JAVAC%" -d out @sources.txt
del sources.txt

"%JAVA%" -cp out com.cultfit.testkit.TestRunner ^
  com.cultfit.service.BookingServiceTest ^
  com.cultfit.service.PricingServiceTest ^
  com.cultfit.service.ScheduleServiceTest

endlocal
exit /b %ERRORLEVEL%

:try_javac
REM %1 = candidate javac path. Accept only if `javac -version` succeeds.
if defined JAVAC goto :eof
set "_cand=%~1"
if "%_cand%"=="" goto :eof
if not exist "%_cand%" goto :eof
"%_cand%" -version >nul 2>&1
if errorlevel 1 goto :eof
set "JAVAC=%_cand%"
set "JAVA=%~dp1java.exe"
goto :eof
