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
ALIYUN_SERVICE_FULL_DIR="/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/target/aliyun"
JAR_EXTENSION=".jar"

#################### For Test #################### 
# SERVICES=(to-master-ec1) 
# SERVICES=(cf-master-ec1 cf-master-nc1 cf-master-sc1)
# SERVICES=(slave-ec2 slave-ec3 master-ec1)
# SERVICES=(slave-ec2 slave-ec3 slave-nc2 slave-nc3 slave-sc2 slave-sc3 master-ec1 master-nc1 master-sc1 cf-master-ec1 cf-master-nc1 cf-master-sc1)
#################### For Test #################### 

SERVICES=(to-master-ec1 slave-ec2 slave-ec3 slave-nc2 slave-nc3 slave-sc2 slave-sc3 master-ec1 master-nc1 master-sc1 cf-master-ec1 cf-master-nc1 cf-master-sc1)

for service in "${SERVICES[@]}"; do
	echo "############################################################"
	echo "########## Deploy and Run $service ##########"
	$ALIPLOY -j="$ALIYUN_SERVICE_FULL_DIR/$service$JAR_EXTENSION" &
	echo "########## Wait for one minute. ##########"
	sleep 60s
done

echo "#################### DONE! ####################"
