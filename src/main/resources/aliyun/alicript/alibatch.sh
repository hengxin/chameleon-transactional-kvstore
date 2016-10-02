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

##### for test only #####
# $ALIEAN
# $ALION
# java -jar $BATCH_JAR 4 2 "2-2-1" $siteProperties $cfProperties $toProperties 2> error-bath.log >> batch.log
# $ALIEAN
##### for test only #####

rwRatios=(4 0.5 1 9)
rvsis=("2-1-1" "2-2-1" "2-1-2" "2-2-2")
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


# done: rwRatio=4; "1-0-0" "2-0-0" "3-0-0" "1-1-0" "1-2-0" "1-3-0" "1-0-1" "1-0-2"; mpls=(5 10 15 20 25 30)
# 2016-09-30: missing 4-30-"1-3-0" due to unexpected power off
# 2016-10-02: missing 4-"2-2-1"-(15, 20, 25, 30) due to JRMP connection establishment error

# not used: "1-1-1" "2-2-2" (used) "3-3-3"
