--- 1
net start >> services1.txt

--- 2
REG add "HKLM\SYSTEM\CurrentControlSet\services\Dnscache" /v Start /t REG_DWORD /d 4 /f --- reboot

timeout 5

net start >> services2.txt

fc /N services1.txt services2.txt

REG add "HKLM\SYSTEM\CurrentControlSet\services\Dnscache" /v Start /t REG_DWORD /d 2 /f --- reboot
