#Setup password less or passphrase less ssh
$ ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
$ cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys

#Format the HDFS file system
$bin/hdfs namenode -format

#Start namenode/datanode daemon
$ sbin/start-dfs.sh

#Create new directories into HDFS
$bin/hdfs dfs -mkdir /user
$bin/hdfs dfs -mkdir /input

#Insert some files in directory /input
$bin/hdfs dfs -put $HADOOP_HOME/etc/hadoop /input

#Run the map reduce worcount example
$bin/hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.2.jar wordcount /input/hadoop output

#View the output from the HDFS
$bin/hdfs dfs -cat output/*

#Add shakespeare.txt into HDFS directory wc-input
$bin/hdfs dfs -put /YOUR_PATH_TO_THE_FILE /t8.shakespeare.txt /wc-input

# run Hadoop wordcount example for Shakespeare work of art
time hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.7.2.jar wordcount /wc-input/ output6
