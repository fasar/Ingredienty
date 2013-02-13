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

create table Unit (
  id                        bigint not null,
  name                      varchar(50),
  name_abrv                 varchar(10),
  plural                    varchar(50),
  plural_abrv               varchar(10),
  cunit_type                integer,
  constraint pk_Unit primary key (id))
;

create sequence ingredient_seq;

create sequence IngredientFamily_seq;

create sequence IngredientProperty_seq;

create sequence Unit_seq;

alter table ingredient add constraint fk_ingredient_family_1 foreign key (ingredientFamily_id) references IngredientFamily (id) on delete restrict on update restrict;
create index ix_ingredient_family_1 on ingredient (ingredientFamily_id);
alter table IngredientProperty add constraint fk_IngredientProperty_unit_2 foreign key (unit_id) references Unit (id) on delete restrict on update restrict;
create index ix_IngredientProperty_unit_2 on IngredientProperty (unit_id);


