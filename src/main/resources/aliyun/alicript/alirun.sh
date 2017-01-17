#!/bin/bash - 
#===============================================================================
#
#          FILE: alirun.sh
# 
#         USAGE: ./alirun.sh -j='jar file'
# 
#   DESCRIPTION: Run the jar file on the appropriate aliyun host.
# 
#       OPTIONS: 
#			-j | --jar: the jar file to run
#			-h | --help: help
#  REQUIREMENTS: The jar file should following the naming convention.
#		   TODO: full path or relative path of the jar file
#          BUGS: ---
#         NOTES: ---
#        AUTHOR: Hengfeng Wei (ant-hengxin)
#  ORGANIZATION: ICS, NJU
#       CREATED: 09-17-2016 15:17
#      REVISION:  ---
#===============================================================================

JAR=""

function usage() {
	echo ""
    echo "alirun -j='jar file'"
    echo ""
    echo "This script runs the jar file on appropriate site in Aliyun ECS."
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
########## run the jar file on the remote host ########## 
JAR_FILE="$CHAMELEON_DIR/$jarBase"
echo "The jar file on the remote host is in: $JAR_FILE."

# select command line arguments
ALIYUN_DIR="aliyun"
PROPERTIES_DIR="$ALIYUN_DIR/$jar"
ALISH="/usr/local/bin/alish"
declare -a ARGS

case $service in
	client)
		ARGS=(site cf to)
		;;

	master)
		ARGS=(site sa)
		;;

	slave)
		ARGS=(site sp)
		;;

	cf)
		ARGS=(cf to)
		;;

	to)
		ARGS=(to)
		;;

	*)
		echo "No such service: $service."
		usage
		exit 1
		;;
esac    # --- end of case ---

echo "The parameters in order for ($jarBase) is (${ARGS[*]})."

args=""
SPACE=" "

for arg in "${ARGS[@]}"; do
	args+=$PROPERTIES_DIR/$arg-$jar.properties$SPACE 
done

echo "The parameters for ($jarBase) is ($args)."

$ALISH -r="cd $CHAMELEON_DIR && java -jar $jarBase $args" -t="$host"
