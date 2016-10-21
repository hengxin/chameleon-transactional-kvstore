#!/bin/bash - 
#===============================================================================
#
#          FILE: alibatch.sh
# 
#         USAGE: ./alibatch.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Hengfeng Wei (ant-hengxin)
#  ORGANIZATION: ICS, NJU
#       CREATED: 2016-09-18 20:14
#      REVISION:  ---
#===============================================================================

BATCH_JAR="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/target/aliyun/batch.jar"

ALION="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/alion.sh"
ALIEAN="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/aliean.sh"

ALIYUN_DIR="aliyun/client/"
siteProperties="$ALIYUN_DIR""site-client.properties"
cfProperties="$ALIYUN_DIR""cf-client.properties"
toProperties="$ALIYUN_DIR""to-client.properties"

##### for missing #####
$ALIEAN
$ALION
java -jar $BATCH_JAR 9 25 "2-1-2" $siteProperties $cfProperties $toProperties 2> error-bath.log >> batch.log
$ALIEAN
$ALION
java -jar $BATCH_JAR 9 30 "2-1-2" $siteProperties $cfProperties $toProperties 2> error-bath.log >> batch.log
$ALIEAN
##### for missing #####

##### batch #####
rwRatios=(4 0.5 1 9)
# rvsis=("1-0-0" "2-0-0" "1-1-0" "1-0-2" "2-2-1" "2-1-2")
rvsis=()
otherrvsis=("1-2-0" "1-3-0" "1-0-1" "2-1-1" "2-2-2" "3-0-0")
mpls=(5 10 15 20 25 30)

$ALIEAN
for rwRatio in "${rwRatios[@]}"; do
	for rvsi in "${rvsis[@]}"; do
		for mpl in "${mpls[@]}"; do
			$ALION
			java -jar $BATCH_JAR $rwRatio $mpl $rvsi $siteProperties $cfProperties $toProperties 2> error-bath.log >> batch.log
			$ALIEAN
		done
	done
done

echo "ALL DONE!"