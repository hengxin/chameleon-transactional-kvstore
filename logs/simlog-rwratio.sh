#!/usr/bin/env bash

# input log file
log="$1"
# output data file
dat=$(echo $log | cut -f 1 -d '.')".dat"

# params=(sizeOfKeyspace rwRatio issueDelay replicationDelay twopcDelay k1 k2 k3 BV/#T FV/#T SV/#T)
# params of interest
params=(rwRatio issueDelay k1 k2 k3 VC/#T WCF/#T BV/#T FV/#T SV/#T)
abortParams=(VC/#T WCF/#T BV/#T FV/#T SV/#T)

# clear the output data file first
> $dat
# the first line for params
# echo ${params[@]} >> $dat

RECORD_LINE=5

declare -a issueDelayArray=(5 10 12 15 20)
declare -a rvsiArray=(100 110 111 200 201 211)
declare -A param_val_map
declare -A issueDelay_rvsi_abort_map

while read line; do
	((lno++))
	record+=$line' '
	
	if ! ((lno % $RECORD_LINE)); then
		for param in "${params[@]}"; do
			# format: param=val, or param=val}
			val=$(echo $record | grep -Po "$param=[^,]*[,}]" | awk -F'=|,|}' '{print $2}')
			param_val_map[$param]=$val
		done
		
		issueDelay=${param_val_map[issueDelay]}
		rvsi=${param_val_map[k1]}${param_val_map[k2]}${param_val_map[k3]}
		
		for param in "${abortParams[@]}"; do
			rvsi_abort_dat=("${rvsi_abort_dat[@]}" ${param_val_map[$param]})
		done
		
		issueDelay_rvsi_abort_map[$issueDelay, $rvsi]="${rvsi_abort_dat[@]}"
		unset rvsi_abort_dat
		
		record=""
	fi
done < $log

# for key in "${!issueDelay_rvsi_abort_map[@]}"; do
# 	echo $key
# 	echo ${issueDelay_rvsi_abort_map[$key]}
# #	(IFS=$'\t'; echo "$key" "${ratio_issueDelay_rvsi_abort_map[$key]}"; unset IFS) >> $dat
# done

for issueDelay in "${issueDelayArray[@]}"; do
	for rvsi in "${rvsiArray[@]}"; do
		aborts=${issueDelay_rvsi_abort_map[$issueDelay, $rvsi]}
		abortsArray=("${abortsArray[@]}" "${aborts[@]}")
	done

	(IFS=$'\t'; echo $issueDelay" ""${abortsArray[@]}"; unset IFS) >> $dat
	unset aborts
	unset abortsArray
done

# writing column labels at the beginning of the file
for rvsi in "${rvsiArray[@]}"; do
	for param in "${abortParams[@]}"; do
		abortLbl=$(echo $param | cut -d "/" -f1)
		rvsi_abort_txt=("${rvsi_abort_txt[@]}" ${abortLbl,,}$rvsi)  # to smallcase
	done
done

label="issueDelay ""${rvsi_abort_txt[@]}"
sed -i '1i\'"$label" $dat
