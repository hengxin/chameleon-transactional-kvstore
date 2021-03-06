#!/bin/bash - 
#===============================================================================
#
#          FILE: aliean.sh
# 
#         USAGE: ./aliean.sh 
# 
#   DESCRIPTION: Clean Chameleon-related processes on Aliyun.
# 
#       OPTIONS: not implemented yet.
#  REQUIREMENTS: ---
# 		   TODO: to provide options
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Hengfeng Wei (ant-hengxin), 
#  ORGANIZATION: ICS, NJU
#       CREATED: 09-03-2016 20:13
#      REVISION:  ---
#===============================================================================

SERVICES=(to-master-ec1 slave-ec2 slave-ec3 slave-nc1 slave-nc3 slave-sc1 slave-sc2 master-ec1 master-nc2 master-sc3 cf-master-ec1 cf-master-nc2 cf-master-sc3)

ALISH="/usr/local/bin/alish"

for service in ${SERVICES[@]}; do
	IFS='- ' read -r -a serviceInfo <<< "$service"	# split string using '- '  
	host=${serviceInfo[-1]}		# the last component is the host
	echo "#################### Clean Chameleon $service on $host ####################"
	# procs=$(ps aux | grep $service	| grep -v grep | awk '{print $2}')
	# `ps aux | grep "$service" | grep -v grep | awk "{print $2}"`
	$ALISH -r="kill -s 9 \$(ps aux | grep $service | grep -v grep | awk '{print \$2}')" -t="$host"
done

wait
echo "#################### DONE! ####################"

