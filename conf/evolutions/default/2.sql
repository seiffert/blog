# BlogPost date column
 
# --- !Ups

ALTER TABLE blog_post ADD COLUMN publish_date timestamp DEFAULT CURRENT_TIMESTAMP;
 
# --- !Downs
 
ALTER TABLE blog_post DROP COLUMN publish_date;