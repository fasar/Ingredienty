# --- Database schema of the website model

# --- !Ups

create table user (
  email                     varchar(255) not null primary key,
  name                      varchar(255) not null,
  password                  varchar(255) not null
);

INSERT INTO user (email, name, password) VALUES ('sample@sample.com', 'Sample', 'secret');


# --- !Downs

drop table if exists user;