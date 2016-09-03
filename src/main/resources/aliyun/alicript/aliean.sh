#!/bin/bash - 
#===============================================================================
#
#          FILE: aliean.sh
# 
#         USAGE: ./aliean.sh 
# 
#   DESCRIPTION: 
# 
#       OPTIONS: ---
#  REQUIREMENTS: ---
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: YOUR NAME (), 
#  ORGANIZATION: 
#       CREATED: 2016年09月03日 20:13
#      REVISION:  ---
#===============================================================================

# SERVICES=(to-master-ec1 slave-ec2 slave-ec3 slave-nc2 slave-nc3 slave-sc2 slave-sc3 master-ec1 master-nc1 master-sc1 cf-master-ec1 cf-master-nc1 cf-master-sc1)

SERVICES=(slave-nc2 slave-nc3 master-nc1)

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

