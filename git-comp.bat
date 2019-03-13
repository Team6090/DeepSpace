@echo off

::
:: git-comp: A Simple wrapper script to be used at competitions.
:: This script allows the use of an alternate repository.
::

:: The competition repository.
set USERNAME=git
set HOST=192.168.0.100
set COMP_REPO=%USERNAME%@%HOST%:~/repo

:: Add the alt remote
git remote add alt %COMP_REPO%

:: Fetch the current branch
git branch | findstr * > tmp
for /f "delims=" %%i in (tmp) do set CURRENT_BRANCH=%%i
del tmp
set CURRENT_BRANCH=%CURRENT_BRANCH:~2%

::
:: Add the alt options
::
if "%1" == "push" (
    echo PASSWORD: %COMP_PASS%
    git push alt %CURRENT_BRANCH%
) else if "%1" == "pull" (
    echo PASSWORD: %COMP_PASS%
    git pull alt %CURRENT_BRANCH%
) else (
    git %*
)

:: Remove the alt remote
git remote remove alt