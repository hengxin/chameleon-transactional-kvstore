#!/usr/bin/env bash

# input log file
log="$1"
# output data file
dat=$(echo $log | cut -f 1 -d '.')".dat"

# params of interest
params=(sizeOfKeyspace rwRatio issueDelay replicationDelay otherDelay BV/#T FV/#T SV/#T)

# clear the output data file first
> $dat
# the first line for params
echo ${params[@]} >> $dat

RECORD_LINES=5

while read line; do
	((lno++))
	record+=$line' '
	
	if ! ((lno % $RECORD_LINES)); then
		for param in "${params[@]}"; do
			if [ "$param" == "otherDelay" ]; then   # format: otherDelay [val]
			    val=$(echo $record | grep -Po "otherDelay \[\K[^\]]+")
			else                                    # format: param=val, or param=val}
				val=$(echo $record | grep -Po "$param=[^,]*[,}]" | awk -F'=|,|}' '{print $2}')
			fi
			data=("${data[@]}" $val)
		done
		
		(IFS=$'\t'; echo "${data[*]}"; unset IFS) >> $dat
		
		record=""
		unset data
	fi
done < $log
