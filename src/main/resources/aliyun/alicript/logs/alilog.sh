#!/usr/bin/env bash

# Log analysis for aliyun log files:
# resources/aliyun/alicript/logs/chameleon-aliyun-batch-bvfvsv.log

# input log file
log="$1"
# output data file
dat=$(echo $log | cut -f 1 -d '.')

# params of interest
params=(rwRatio mpl k1 k2 k3 VC/#T WCF/#T BV/#T FV/#T SV/#T)
abortParams=(VC/#T WCF/#T BV/#T FV/#T SV/#T)

# clear the output data file first
> $dat
> $dat-0.5.dat
> $dat-1.0.dat
> $dat-4.0.dat
> $dat-9.0.dat

STAT_LINE=6
RECORD_LINE=8

declare -a rwRatioArray=(0.5 1.0 4.0)
declare -a mplArray=(5 10 15 20 25 30)
declare -a rvsiArray=(100 110 111 200 201 211 212 221)

declare -A param_val_map
declare -A ratio_rvsi_abort_map

while read line; do
	((lno++))
	
	if ((lno == STAT_LINE)); then
		# collecting parameter values
		for param in "${params[@]}"; do
			val=$(echo $line | grep -Po "$param=[^,]*[,}]" | awk -F'=|,|}' '{print $2}')
			param_val_map[$param]=$val
		done
	fi
	
	if ((lno == RECORD_LINE)); then
		rwRatio=${param_val_map[rwRatio]}
#		echo $rwRatio
		mpl=${param_val_map[mpl]}
#		echo $mpl
		rvsi=${param_val_map[k1]}${param_val_map[k2]}${param_val_map[k3]}
#		echo $rvsi
		
		for param in "${abortParams[@]}"; do
			rvsi_abort_dat=("${rvsi_abort_dat[@]}" ${param_val_map[$param]})
		done
		ratio_rvsi_abort_map[$rwRatio, $mpl, $rvsi]="${rvsi_abort_dat[@]}"
		
		unset rvsi_abort_dat
		lno=0
	fi
done < $log

for rwRatio in "${rwRatioArray[@]}"; do
#	echo $rwRatio
	for mpl in "${mplArray[@]}"; do
#		echo $mpl
		for rvsi in "${rvsiArray[@]}"; do
#			echo $rvsi
			aborts=${ratio_rvsi_abort_map[$rwRatio, $mpl, $rvsi]}
			abortsArray=("${abortsArray[@]}" "${aborts[@]}")
		done
#		echo $rwRatio" "$mpl" ""${abortsArray[@]}"

		# store by rwRatios
		(IFS=$'\t'; echo $mpl" ""${abortsArray[@]}"; unset IFS) >> $dat"-"$rwRatio".dat"
		unset aborts
		unset abortsArray
	done
done

# writing column labels at the beginning of each file
for rvsi in "${rvsiArray[@]}"; do
	for param in "${abortParams[@]}"; do
		abortLbl=$(echo $param | cut -d "/" -f1)
		rvsi_abort_txt=("${rvsi_abort_txt[@]}" ${abortLbl,,}$rvsi)  # to smallcase
	done
done

label="mpl ""${rvsi_abort_txt[@]}"

for rwRatio in "${rwRatioArray[@]}"; do
	sed -i '1i\'"$label" $dat"-"$rwRatio".dat"
done
