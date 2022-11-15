#!/usr/bin/env bash
SERVER=/app/baoliBestCopy
#export PATH=/app/java/jdk/jdk1.8.0_121/bin:$PATH
export LC_ALL=en_US.UTF-8
cd $SERVER
JAR=*.jar

#java虚拟机启动参数
JAVA_OPTS="-Xmx1G -Xms512m -Xss4m -XX:MaxNewSize=256m"

if [ "$2" = "debug" ]; then
DEBUG_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,address=9000,server=y,suspend=n"
fi

start(){
    echo "start.."
    LOG=$SERVER/logs/all.log
    cd $SERVER
    mkdir -p logs
    echo "start project begin..." >> $LOG
    classPath="."
    nohup java $JAVA_OPTS $DEBUG_OPTS -jar $SERVER/$JAR >/dev/null 2>&1 &
    echo $! > $SERVER/server.pid
    echo "start project end..." >> $LOG
    status
}

stop(){
    echo "stop project..."
    sec=10
    kill `cat $SERVER/server.pid`
    #开始一个循环，以判断进程是否关闭
    for var in 1 2 3 4 5
    do
      #打印出当前的java进程
      count=`ps -ef|grep $SERVER |grep -v grep |grep -v $0 | wc -l`
      if [ $count -gt 0 ]; then
        #若进程还未关闭，则脚本sleep几秒
        echo sleep $sec second the $var time, the JAVA thread is still alive. [$count]
        sleep $sec
      else
        #若进程已经关闭，则跳出循环
        echo "break"
        break
      fi
    done

    rm -rf $SERVER/server.pid
    echo "stop project end..."
}


restart(){
    stop
    start
}

status() {
	count=`ps -ef|grep $SERVER |grep -v $0 |grep -v grep | wc -l`
	if [ $count -ge 1 ];
	then
		echo "[SUCCESS] The JAVA threads are alive!"
		ps -ef|grep $SERVER | grep -v $0|grep -v grep
		RETVAL=0
	else
		echo "[ERROR] The JAVA Process does not exist!"
		#echo "deploy_status_error"
		RETVAL=1
	fi
	return $RETVAL
}

if [ "$1" = "start" ]; then
start
elif [ "$1" = "stop" ]; then
stop
elif [ "$1" = "restart" ]; then
restart
elif [ "$1" = "status" ]; then
status
else
restart
fi
