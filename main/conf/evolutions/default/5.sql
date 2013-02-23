# Schema for the ingredient cosumed by a user

# --- !Ups

create table ConsumedIngredient (
  ingredient_id             bigint not null,
  email                     varchar(255) not null,
  cdate                     timestamp not null,
  recipe_id                 bigint,
  quantity                  double,
  PRIMARY KEY (ingredient_id, email, cdate)
);


INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2000, 'sample@sample.com', '2013-02-23 12:32:15', null, 10.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2002, 'sample@sample.com', '2013-02-23 12:32:15', null, 11.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2004, 'sample@sample.com', '2013-02-23 12:32:15', null, 12.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2006, 'sample@sample.com', '2013-02-23 12:45:00', 1, 14.0);




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists ConsumedIngredient;
SET REFERENTIAL_INTEGRITY TRUE;

