export JAVA_OPTS=" -Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=$JMX_PORT \
-Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT \
-Dcom.sun.management.jmxremote.local.only=false \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
-Djava.rmi.server.hostname=localhost"