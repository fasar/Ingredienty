# --- Database schema of the website model

# --- !Ups

-- Table des utilisateurs
create table user (
  email                     varchar(255) not null primary key,
  name                      varchar(255) not null,
  password                  varchar(255) not null,
  role						int not null
);

INSERT INTO user (email, name, password, role) VALUES ('sample@sample.com', 'Sample', 'secret', 100);
INSERT INTO user (email, name, password, role) VALUES ('admin@sample.com', 'Admin', 'secret', 300);


# --- !Downs
SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists user;
SET REFERENTIAL_INTEGRITY TRUE;
