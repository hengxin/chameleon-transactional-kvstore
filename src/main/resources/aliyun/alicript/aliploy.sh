#!/bin/bash - 
#===============================================================================
#
#          FILE: aliploy.sh
# 
#         USAGE: ./aliploy.sh -j='jar file'
# 
#   DESCRIPTION: Deploy the jar file onto an aliyun host.
# 
#       OPTIONS: 
#			-j | --jar: the jar file to deploy
#			-h | --help: help
#  REQUIREMENTS: The jar file should following the naming convention.
#		   TODO: full path or relative path of the jar file
#          BUGS: ---
#         NOTES: --- 
#        AUTHOR: Hengfeng Wei (ant-hengxin)
#  ORGANIZATION: ICS, NJU
#       CREATED: 09-03-2016
#      REVISION:  ---
#===============================================================================

JAR=""

function usage() {
	echo ""
    echo "aliploy -j='jar file'"
    echo ""
    echo "This script deploys the jar file on appropriate site in Aliyun ECS."
    echo "It requires that the name of the jar file follows the naming convention."
	echo ""
    echo "-h --help"
    echo "-j --jar. No default value."
    echo ""
}

# parse the arguments: -h, -j
while [ "$1" != "" ]; do
    PARAM=`echo $1 | awk -F= '{print $1}'`
    VALUE=`echo $1 | awk -F= '{print $2}'`
    case $PARAM in
        -h | --help)
            usage
            exit
            ;;
        -j | --jar)
            JAR=$VALUE
            ;;
        *)
            echo "ERROR: unknown parameter \"$PARAM\""
            usage
            exit 1
            ;;
    esac
    shift
done

########## The Main Body ##########

CHAMELEON_DIR="~/chameleon-aliyun"	# chameleon directory on aliyun

########## parse the name of the input jar file ########## 
echo "The jar file is '$JAR'."
jarBase=$(basename $JAR)
echo "The base name of the jar file is '$jarBase'."
jar="${jarBase%.*}"	# remove the ".jar" extension
echo "The jar file without the extension is '$jar'."
IFS='- ' read -r -a  jarInfo <<< "$jar"	# split using `-`

service="${jarInfo[0]}"	# the first is the service type: client, master, slave, cf, or to.
host="${jarInfo[-1]}"	# the last is the host

echo "The host on aliyun involved is '$host'."

########## deploy the jar file onto the remote host ########## 
/usr/local/bin/alicp.sh -l="$JAR" -r="$CHAMELEON_DIR" -t="$host"

echo "Wait for deploying $jarBase onto the remote host $host."
wait
echo "Deployed! You can run $jarBase on the remote host $host by executing [./alirun.sh  $JAR]."
