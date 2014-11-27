#!/bin/sh
#
# chkconfig: 345 80 20
# description: App
# processname: p
# pidfile: /var/run/p.pid
#
### BEGIN INIT INFO
# Provides:          p
# Required-Start:    $remote_fs $syslog $network
# Required-Stop:     $remote_fs $syslog $network
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start p at boot time
# Description:       Manages the services needed to run p
### END INIT INFO
#
# Startup script for p under *nix systems (it works under NT/cygwin too).
# Adapted from artifactory's start/stop script
# Should be LSB compliant and therefore usable with pacemaker for HA configurations

usage() {
    echo "Usage: $0 {start|stop|restart|force-reload|status}"
    exit 1
}
pid_exists(){
    if [ -f ${PID_FILE} ]; then
       return 0
    else
       return 1
    fi
}
is_running(){
    if pid_exists; then
        if [ "$(ps -p `cat ${PID_FILE}` | wc -l)" -gt 1 ]; then
            return 0
        else
            # not running, but PID file exists
            echo "The PID file exists but the app is not running. Removing old pid file."
            rm ${PID_FILE}
            return 1
        fi
    else
        return 1
    fi
}

# Script starts here
APP_NAME=p
APP_HOME=/opt/p
APP_USER=p
#JAVA_OPTS="-p.home=/opt/p -Dconf.dir=/opt/p -Dlog.dir=/opt/p/logs"
JAVA_OPTS="-Dhttp.port=8456"
JAVA_CMD=/usr/bin/java
PID_FILE=p.pid
MAIN_CLASS=com.mle.play.Starter

if [ -f /etc/default/${APP_NAME} ] ; then
  . /etc/default/${APP_NAME}
fi

case "$1" in
    start)
        if is_running; then
            echo "Already running"
            # LSB says: return 0 when starting an already started service
            exit 0
        fi
        COMMAND="exec ${JAVA_CMD} ${JAVA_OPTS} -cp lib/*:${APP_NAME}.jar ${MAIN_CLASS} 2>&1"
        nohup sh -c "${COMMAND}" 2>&1 &
#        if [ -z "${APP_USER}" ]; then
#            nohup sh -c "${COMMAND}" 2>&1 &
#        else
#            nohup su - ${APP_USER} --shell=/bin/sh -c "${COMMAND}" 2>&1 &
#        fi
        echo $! > ${PID_FILE}
        sleep 1
        if is_running; then
            echo "Started"
        else
            echo "Startup failed"
            exit 1
        fi
        ;;
    stop)
        if pid_exists; then
            # TODO: use rmi call to stop service
            PID=`cat ${PID_FILE} 2>/dev/null`
            kill $PID 2>/dev/null
            rm -f ${PID_FILE}
            # Wait for the service to die; remove if RMI in use
            sleep 2
            echo "Stopped"
        else
            echo "Unable to find PID file; already stopped?"
        fi
        ;;
    restart)
        $0 stop $*
        $0 start $*
        ;;
    force-reload)
        $0 restart
        ;;
    status)
        if is_running; then
            echo "Running"
        else
            echo "Not running"
            # LSB says exit status 3 is "program is not running"
            # http://refspecs.linux-foundation.org/LSB_3.2.0/LSB-Core-generic/LSB-Core-generic/iniscrptact.html
            exit 3
        fi
        ;;
    *)
        usage
        ;;
esac
exit 0



