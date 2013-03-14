# Schema for the ingredient cosumed by a user

# --- !Ups

create table Weight (
  id                        bigint not null auto_increment,
  email                     varchar(255) not null,
  date                      timestamp not null,
  weight                    double not null,
  fat                       double not null,
  water                     double not null,
  muscles                   double not null,
  bones                     double not null,
  visceralFat               int not null,
  PRIMARY KEY (id)
);


ALTER TABLE Weight
  ADD FOREIGN KEY (email) REFERENCES User(email) ON DELETE CASCADE;

  
INSERT INTO Weight (id, email, date, weight, fat, water, muscles, bones, visceralFat) VALUES (1, 'sample@sample.com', '2013-02-10 12:32:15', 94, 26.5, 22, 38, 2, 6);
INSERT INTO Weight (id, email, date, weight, fat, water, muscles, bones, visceralFat) VALUES (2, 'sample@sample.com', '2013-02-11 12:32:15', 93, 22.5, 23, 37, 3, 6);
INSERT INTO Weight (id, email, date, weight, fat, water, muscles, bones, visceralFat) VALUES (3, 'sample@sample.com', '2013-02-12 12:32:15', 92, 25.5, 24, 31, 2, 6);
INSERT INTO Weight (id, email, date, weight, fat, water, muscles, bones, visceralFat) VALUES (4, 'sample@sample.com', '2013-02-13 12:32:15', 93, 29.5, 23, 32, 3, 6);
INSERT INTO Weight (id, email, date, weight, fat, water, muscles, bones, visceralFat) VALUES (5, 'sample@sample.com', '2013-02-14 12:32:15', 92, 22.5, 24, 32, 3, 6);





# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists Weight;
SET REFERENTIAL_INTEGRITY TRUE;

