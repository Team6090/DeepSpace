@echo off

::
:: git-comp: A Simple wrapper script to be used at competitions.
:: This script allows the use of an alternate repository.
::

:: The competition repository. Our server automatically resolves
:: comp.server.
set COMP_REPO=git@192.168.0.100:~/FRC6090-2019
:: The password to the remote. The server is configured to have a password
:: of "1" because it is simple and easy to type.
set COMP_PASS=1

:: Add the alt remote
git remote add alt %COMP_REPO%

:: Fetch the current branch
for /f "delims=" %%i in ('git branch') do set CURRENT_BRANCH=%%i
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