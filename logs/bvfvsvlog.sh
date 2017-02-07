#!/usr/bin/env bash

log="$1"
lno=0
RECORD_LINES=5

dat=$(echo $log | cut -f 1 -d '.')".dat"

> $dat
echo "keyspace  rwRatio issueDelay  otherDelay  bv  fv  sv" >> $dat

float="[0-9]*\.*[0-9]*"

while read line; do
	((lno++))
	record+=$line' '
	if ! ((lno % $RECORD_LINES)); then
		for param in sizeOfKeyspace rwRatio issueDelay otherDelay; do
			data=("${data[@]}" $(echo $record | grep -o "$param \[$float\]" | grep -o "$float"))
		done

		for rvsi in BV FV SV; do
			data=("${data[@]}" $(echo $record | grep -o "$rvsi/#T=$float" | grep -o "$float"))
		done

		echo ${data[@]} >> $dat
		
		record=""
		unset data
	fi
done < $log
