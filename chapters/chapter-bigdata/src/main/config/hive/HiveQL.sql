CREATE TABLE input (line STRING);
LOAD DATA LOCAL INPATH '/FILE_PATH/t8.shakespeare.txt' OVERWRITE INTO TABLE input;
SELECT word, COUNT(*) FROM input LATERAL VIEW explode(split(line, ' ')) lTable as word GROUP BY word;

CREATE TABLE IF NOT EXISTS movies
 (id STRING,
 title STRING,
 releasedate STRING,
 rating INT,
 Publisher STRING)
 ROW FORMAT DELIMITED
 FIELDS TERMINATED BY ','
 STORED AS TEXTFILE;

LOAD DATA local INPATH '/FILE_PATH/movies_data.csv' OVERWRITE INTO TABLE movies;

Select releasedate, count(releasedate) from movies m group by m.releasedate;
Select releasedate, count(releasedate) from movies m where rating >3  group by m.releasedate;

#load data from IGFS
CREATE TABLE igfsinput (line STRING);

LOAD DATA INPATH '/myDir/myFile' OVERWRITE INTO TABLE igfsinput;

SELECT word, COUNT(*) FROM igfsinput LATERAL VIEW explode(split(line, ' ')) lTable as word GROUP BY word;