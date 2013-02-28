# Schema for the ingredient cosumed by a user

# --- !Ups

create table ConsumedIngredient (
  ingredient_id             bigint not null,
  email                     varchar(255) not null,
  cdate                     timestamp not null,
  recipe_id                 bigint,
  quantity                  double not null,
  PRIMARY KEY (ingredient_id, email, cdate)
);


ALTER TABLE ConsumedIngredient
  ADD FOREIGN KEY (email) REFERENCES User(email) ON DELETE CASCADE;
ALTER TABLE ConsumedIngredient
  ADD FOREIGN KEY (ingredient_id) REFERENCES Ingredient(id) ON UPDATE CASCADE;  

  
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2000, 'sample@sample.com', '2013-02-10 12:32:15', 1, 10.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2002, 'sample@sample.com', '2013-02-10 12:32:15', 1, 11.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2004, 'sample@sample.com', '2013-02-10 12:32:15', 1, 12.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2006, 'sample@sample.com', '2013-02-10 12:45:00', null, 14.0);
INSERT INTO ConsumedIngredient (ingredient_id, email, cdate, recipe_id, quantity) VALUES (2002, 'sample@sample.com', '2013-02-10 12:50:22', null, 15.0);




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists ConsumedIngredient;
SET REFERENTIAL_INTEGRITY TRUE;

