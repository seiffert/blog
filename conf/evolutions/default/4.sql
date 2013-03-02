# Comment schema
 
# --- !Ups

CREATE SEQUENCE comment_id_seq;
CREATE TABLE comment (
    id integer NOT NULL default nextval('comment_id_seq') primary key,
    author varchar(255),
    authorEmail varchar(255),
    body text,
    date timestamp DEFAULT CURRENT_TIMESTAMP,
    blogPostId integer NOT NULL
);
 
# --- !Downs
 
DROP TABLE comment;
DROP SEQUENCE comment_id_seq;