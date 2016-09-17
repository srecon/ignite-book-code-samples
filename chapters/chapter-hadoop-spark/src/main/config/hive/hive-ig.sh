#!/usr/bin/env bash
# Specify Hive home directory:
export HIVE_HOME=<Hive installation directory>

# If you did not set hadoop executable in PATH, specify Hadoop home explicitly:
export HADOOP_HOME=<Hadoop installation folder>

# Specify configuration files location:
export HIVE_CONF_DIR=$HADOOP_HOME/etc/hadoop-hive

# Avoid problem with different 'jline' library in Hadoop:
export HADOOP_USER_CLASSPATH_FIRST=true

${HIVE_HOME}/bin/hive "${@}"