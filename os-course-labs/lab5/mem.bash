#!/bin/bash

report_log="report.log"

echo "" > $report_log

array=()
counter=0

while true
do
    array+=(1 2 3 4 5 6 7 8 9 10)
    if [[ $(($counter % 100000)) -eq 0 ]]
    then
        echo ${#array[@]} >> $report_log
    fi
    counter=$(($counter+1))
done

