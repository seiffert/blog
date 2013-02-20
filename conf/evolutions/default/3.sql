# Account schema
 
# --- !Ups

CREATE TABLE account (
    id varchar NOT NULL primary key,
    username varchar(255),
    password varchar(255),
    name varchar(50),
    permission text
);

# --- !Downs
 
DROP TABLE account;
