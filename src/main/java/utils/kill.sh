#!/bin/bash -

SERVICES=(slave.jar master.jar cf.jar to.jar)

for service in ${SERVICES[@]}; do
	kill -s 9 $(ps aux | grep $service | grep -v grep | awk '{print $2}')
done

wait
echo "########## Kill Chameleon Done!!! ##########"
