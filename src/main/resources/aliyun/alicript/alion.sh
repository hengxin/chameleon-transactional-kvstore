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

ALIPLOY="./aliploy.sh"
ALIRUN="./alirun.sh"
ALIYUN_SERVICE_FULL_DIR="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/target/aliyun"
JAR_EXTENSION=".jar"

#################### For Test #################### 
# SERVICES=(to-master-ec1) 
# SERVICES=(cf-master-ec1 cf-slave-nc1 cf-slave-sc1)
# SERVICES=(slave-ec2 slave-ec3 master-ec1)
# SERVICES=(slave-ec2 slave-ec3 master-nc2 slave-nc3 slave-sc2 slave-sc3 master-ec1 slave-nc1 slave-sc1 cf-master-ec1 cf-slave-nc1 cf-slave-sc1)
#################### For Test #################### 

SERVICES=(to-master-ec1 slave-ec2 slave-ec3 slave-nc2 slave-nc3 slave-sc2 slave-sc3 master-ec1 master-nc1 master-sc1 cf-master-ec1 cf-master-nc1 cf-master-sc1)

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
