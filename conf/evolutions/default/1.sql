# BlogPost schema
 
# --- !Ups

CREATE SEQUENCE blog_post_id_seq;
CREATE TABLE blog_post (
    id integer NOT NULL default nextval('blog_post_id_seq') primary key,
    title varchar(255),
    body text
);
 
# --- !Downs
 
DROP TABLE blog_post;
DROP SEQUENCE blog_post_id_seq;