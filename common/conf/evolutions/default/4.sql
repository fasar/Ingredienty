# --- !Ups
-- Table des recettes
create table Recipe (
  id                        bigint not null auto_increment,
  name                      varchar(255),
  instructions              LONGTEXT,
  author_email               varchar(255),
  is_public                 boolean,
  description               varchar(255),
  prep_time_sec             integer,
  cook_time_sec             integer,
  constraint pk_Recipes primary key (id)
);

-- Table qui map les recettes et les ingrédients.
-- Elle informe de la quantité de chaque ingrédients.
create table IngredientsRecipeQuantity_Map (
  recipe_id                 bigint not null,
  ingredient_id				bigint not null,
  quantity					double,
  PRIMARY KEY (recipe_id, ingredient_id)
);


create sequence Recipes_seq;

ALTER TABLE Recipe
  ADD FOREIGN KEY (author_email) REFERENCES USER(email) ON DELETE SET null;
ALTER TABLE IngredientsRecipeQuantity_Map
  ADD FOREIGN KEY(recipe_id) REFERENCES Recipe(id) ON DELETE CASCADE;
ALTER TABLE IngredientsRecipeQuantity_Map
  ADD FOREIGN KEY(ingredient_id) REFERENCES Ingredient(id) ON UPDATE CASCADE;
  
INSERT INTO Recipe (id, name, instructions, author_email, is_public, description, prep_time_sec, cook_time_sec) 
 VALUES (1, 'recette1', 'mettre tout', 'sample@sample.com', true, 'un beau mélange', 500, 300);
INSERT INTO Recipe (id, name, instructions, author_email, is_public, description, prep_time_sec, cook_time_sec) 
 VALUES (2, 'recette2', 'mettre tout2', null, true, 'un beau mélange2', 501, 302);
 
INSERT INTO IngredientsRecipeQuantity_Map(recipe_id, ingredient_id, quantity)
 VALUES (1, 2000, 20.0);
INSERT INTO IngredientsRecipeQuantity_Map(recipe_id, ingredient_id, quantity)
 VALUES (1, 2002, 22.0);
INSERT INTO IngredientsRecipeQuantity_Map(recipe_id, ingredient_id, quantity)
 VALUES (1, 2004, 22.0);
 
-- DELETE FROM Recipe WHERE id = 1;

 
# --- !Downs
SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists Recipe;
drop table if exists IngredientsRecipeQuantity_Map;
SET REFERENTIAL_INTEGRITY TRUE;
drop sequence if exists Recipes_seq;
