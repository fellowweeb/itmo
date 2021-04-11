--- 1
hostname
md C:\temp
net share temp=C:\temp /grant:everyone,FULL

--- 2
echo "xcopy /z C:\Windows\test.jpg \\DESKTOP-UBFLL71\temp\" > task.bat

--- 3
schtasks /create /sc MINUTE /tn scheduled /tr "task.bat"
