#!/bin/bash - 
#===============================================================================
#
#          FILE: alion.sh
# 
#         USAGE: ./alion.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 2016年09月03日 15:57
#      REVISION:  ---
#===============================================================================

ALIPLOY="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/aliploy.sh"
ALIRUN="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/alirun.sh"
ALIYUN_SERVICE_FULL_DIR="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/target/aliyun"
JAR_EXTENSION=".jar"

# SERVICES=(to-master-ec1)
SERVICES=(to-master-ec1 slave-ec2 slave-ec3 slave-nc1 slave-nc3 slave-sc1 slave-sc2 master-ec1 master-nc2 master-sc3 cf-master-ec1 cf-master-nc2 cf-master-sc3)

echo "#################### Deploy and Run: Begin! ####################"

for service in "${SERVICES[@]}"; do
	echo "############### Deploy $service ###############"
	$ALIPLOY -j="$ALIYUN_SERVICE_FULL_DIR/$service$JAR_EXTENSION" &
done
wait

for service in "${SERVICES[@]}"; do
	echo "############### Run $service ###############"
	$ALIRUN -j="$ALIYUN_SERVICE_FULL_DIR/$service$JAR_EXTENSION" &
	sleep 5s
done

echo "#################### Deploy and Run: DONE! ####################"
