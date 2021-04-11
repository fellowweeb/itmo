#!/bin/bash

counter=0
N=$1
K=$2
while [[ "$counter" -ne "$K" ]]
do
    ./newmem.bash "$N" &
    counter=$(($counter+1))
    sleep 1s
done