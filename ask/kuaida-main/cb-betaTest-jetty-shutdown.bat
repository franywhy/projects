title=main.war

@echo off&setlocal EnableDelayedExpansion
set Port=
set Dstport=8080
}
for /F "usebackq skip=4 tokens=2,5" %%a in (`"netstat -ano -p tcp"`) do (
  for /F "tokens=2 delims=:" %%k in ("%%a") do (
    set  Port=%%k
  )
  echo !Port! %%b >>portandpid.txt
)
for /F "tokens=2 delims=:" %%c in ("%1") do (
    set  Port=%%c
  )
for /F "tokens=1,2 delims= " %%d in (portandpid.txt) do (
	echo %%d 
	echo %Dstport%
    if %%d == %Dstport% taskkill /f /pid %%e
  )
del portandpid.txt	
set Port=
set Dstport=

set Port=
set Dstport=17009
}
for /F "usebackq skip=4 tokens=2,5" %%a in (`"netstat -ano -p tcp"`) do (
  for /F "tokens=2 delims=:" %%k in ("%%a") do (
    set  Port=%%k
  )
  echo !Port! %%b >>portandpid.txt
)
for /F "tokens=2 delims=:" %%c in ("%1") do (
    set  Port=%%c
  )
for /F "tokens=1,2 delims= " %%d in (portandpid.txt) do (
	echo %%d 
	echo %Dstport%
    if %%d == %Dstport% taskkill /f /pid %%e
  )
del portandpid.txt	
