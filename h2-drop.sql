SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists ingredient;

drop table if exists IngredientFamily;

drop table if exists IngredientProperty;

drop table if exists Unit;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists ingredient_seq;

drop sequence if exists IngredientFamily_seq;

drop sequence if exists IngredientProperty_seq;

drop sequence if exists Unit_seq;

