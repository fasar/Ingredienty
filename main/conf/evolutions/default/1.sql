# --- Database schema of the domain model

# --- !Ups


create table ingredient (
  id                        bigint not null,
  name                      varchar(120),
  ingredientFamily_id       bigint,
  constraint pk_ingredient primary key (id))
;

create table IngredientFamily (
  id                        bigint not null,
  name                      varchar(50),
  constraint pk_IngredientFamily primary key (id))
;

create table IngredientProperty (
  id                        bigint not null,
  name                      varchar(50),
  description               varchar(500),
  unit_id                   bigint,
  constraint pk_IngredientProperty primary key (id))
;

create table ingredients_list (
  id                        bigint not null,
  quantity                  double,
  ingredient_id             bigint,
  recipe_id                 bigint,
  unit_id                   bigint,
  constraint pk_ingredients_list primary key (id))
;

create table Recipes (
  id                        bigint not null,
  name                      varchar(255),
  instructions              LONGTEXT,
  author                    varchar(255),
  public                    boolean,
  description               varchar(255),
  prep_time_sec             integer,
  cook_time_sec             integer,
  yields                    integer,
  constraint pk_Recipes primary key (id))
;

create table Unit (
  id                        bigint not null,
  name                      varchar(50),
  name_abrv                 varchar(10),
  plural                    varchar(50),
  plural_abrv               varchar(10),
  cunit_type                integer,
  constraint pk_Unit primary key (id))
;

--Table qui map les ingrédients et les propriétés.
CREATE TABLE Ingredient_IngredientProperty_Map (
  Ingredient_id bigint NOT NULL,
  ingredientProperties varchar(25) DEFAULT NULL,
  ingredientProperties_KEY bigint NOT NULL,
  PRIMARY KEY (Ingredient_id, ingredientProperties_KEY)
);

create sequence ingredient_seq;

create sequence IngredientFamily_seq;

create sequence IngredientProperty_seq;

create sequence ingredients_list_seq;

create sequence Recipes_seq;

create sequence Unit_seq;

alter table ingredient add constraint fk_ingredient_family_1 foreign key (ingredientFamily_id) references IngredientFamily (id) on delete restrict on update restrict;
create index ix_ingredient_family_1 on ingredient (ingredientFamily_id);
alter table IngredientProperty add constraint fk_IngredientProperty_unit_2 foreign key (unit_id) references Unit (id) on delete restrict on update restrict;
create index ix_IngredientProperty_unit_2 on IngredientProperty (unit_id);
alter table ingredients_list add constraint fk_ingredients_list_ingredient_3 foreign key (ingredient_id) references ingredient (id) on delete restrict on update restrict;
create index ix_ingredients_list_ingredient_3 on ingredients_list (ingredient_id);
alter table ingredients_list add constraint fk_ingredients_list_recipe_4 foreign key (recipe_id) references Recipes (id) on delete restrict on update restrict;
create index ix_ingredients_list_recipe_4 on ingredients_list (recipe_id);
alter table ingredients_list add constraint fk_ingredients_list_unit_5 foreign key (unit_id) references Unit (id) on delete restrict on update restrict;
create index ix_ingredients_list_unit_5 on ingredients_list (unit_id);


# --- !Downs

-- SET REFERENTIAL_INTEGRITY FALSE;
-- drop table if exists ingredient;
-- drop table if exists IngredientFamily;
-- drop table if exists IngredientProperty;
-- drop table if exists ingredients_list;
-- drop table if exists Recipes;
-- drop table if exists Unit
-- DROP TABLE if exists Ingredient_IngredientProperty_Map;;
-- SET REFERENTIAL_INTEGRITY TRUE;
-- drop sequence if exists ingredient_seq;
-- drop sequence if exists IngredientFamily_seq;
-- drop sequence if exists IngredientProperty_seq;
-- drop sequence if exists ingredients_list_seq;
-- drop sequence if exists Recipes_seq;
-- drop sequence if exists Unit_seq;
-- drop sequence if exists Ingredient_IngredientProperty_Map_seq