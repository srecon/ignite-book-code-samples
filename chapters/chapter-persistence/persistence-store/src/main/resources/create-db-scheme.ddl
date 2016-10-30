DROP TABLE IF EXISTS POSTS;
CREATE TABLE POSTS (
  id           VARCHAR(150),
  title        VARCHAR(255),
  description  TEXT,
  creationDate DATE,
  author       VARCHAR(150)
);
DROP INDEX IF EXISTS posts_id_uindex;
CREATE UNIQUE INDEX posts_id_uindex ON POSTS (id);
ALTER TABLE POSTS
  ADD CONSTRAINT posts_id_pk PRIMARY KEY (id);
ALTER TABLE POSTS
  ALTER COLUMN id SET NOT NULL;
