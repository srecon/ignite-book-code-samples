#Word count example
A = load '/wc-input/t8.shakespeare.txt';
B = foreach A generate flatten(TOKENIZE((chararray)$0)) as word;
C = group B by word;
D = foreach C generate COUNT(B), group;
E = Limit D 10;
Dump E;
# movie
movies = LOAD '/input/hadoop/movies_data.csv' USING PigStorage(',') as (id:int,name:chararray,year:int,rating:double,duration:int);
movies_rating_greater_than_four = FILTER movies BY (float)rating > 4.0;
DUMP movies_rating_greater_than_four;
movie_mummy = FILTER movies by (name matches '.*Mummy.*');
grouped_by_year = group movies by year;
count_by_year = FOREACH grouped_by_year GENERATE group, COUNT(movies);
group_all = GROUP count_by_year ALL;
sum_all = FOREACH group_all GENERATE SUM(count_by_year.$1);
DUMP sum_all;