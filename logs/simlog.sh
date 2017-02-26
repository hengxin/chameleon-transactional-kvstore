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

declare -A param_val_map
declare -A ratio_issueDelay_rvsi_abort_map

while read line; do
	((lno++))
	record+=$line' '
	
	if ! ((lno % $RECORD_LINE)); then
		for param in "${params[@]}"; do
			# format: param=val, or param=val}
			val=$(echo $record | grep -Po "$param=[^,]*[,}]" | awk -F'=|,|}' '{print $2}')
			# data=("${data[@]}" $val)
			param_val_map[$param]=$val
		done
		
		rwRatio=${param_val_map[rwRatio]}
		issueDelay=${param_val_map[issueDelay]}
		rvsi=${param_val_map[k1]}${param_val_map[k2]}${param_val_map[k3]}
		
		for param in "${abortParams[@]}"; do
			rvsi_abort_dat=("${rvsi_abort_dat[@]}" ${param_val_map[$param]})
		done
		# only keep one copy for each $rvsi
		ratio_issueDelay_rvsi_abort_map[$rwRatio $issueDelay $rvsi]="${rvsi_abort_dat[@]}"
		unset rvsi_abort_dat
		
		record=""
		
		# (IFS=$'\t'; echo "${data[*]}"; unset IFS) >> $dat
		# unset data
	fi
done < $log

for key in "${!ratio_issueDelay_rvsi_abort_map[@]}"; do
	# echo $key
	# echo ${ratio_issueDelay_rvsi_abort_map[$key]}
	(IFS=$'\t'; echo "$key" "${ratio_issueDelay_rvsi_abort_map[$key]}"; unset IFS) >> $dat
done

sort -k1,1 -k2,2 -k3,3 -k4,4 -k5,5 -n $dat > tmp && mv tmp $dat

# writing column labels at the beginning of the file
# for rvsi in "${rvsiArray[@]}"; do
# 	for param in "${abortParams[@]}"; do
# 		abortLbl=$(echo $param | cut -d "/" -f1)
# 		rvsi_abort_txt=("${rvsi_abort_txt[@]}" ${abortLbl,,}$rvsi)  # to smallcase
# 	done
# done

label="rwRatio issueDelay k1k2k3 ${abortParams[@]}"
sed -i '1i\'"$label" $dat

# if [ "$param" == "otherDelay" ]; then   # format: otherDelay [val]
# 	val=$(echo $record | grep -Po "otherDelay \[\K[^\]]+")
