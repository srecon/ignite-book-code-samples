drop table POSTS;
create table POSTS(
  id VARCHAR(150),
  title VARCHAR(255),
  description TEXT,
  creationDate DATE,
  author VARCHAR(150)
);
DROP INDEX posts_id_uindex;
CREATE UNIQUE INDEX posts_id_uindex ON posts (id);
ALTER TABLE posts ADD CONSTRAINT posts_id_pk PRIMARY KEY (id);
ALTER TABLE posts ALTER COLUMN id SET NOT NULL;
