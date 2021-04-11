--- 1
systeminfo > systeminfo.txt
wmic logicaldisk > wmic_logicaldisk.txt

--- 2
md TEST
copy *.txt TEST
cd TEST

--- 3
type *.txt  1>merged.txt


--- 4
for /F %%i in ('"dir /O:-D /B *.txt | more +1"') do del /Q %%i