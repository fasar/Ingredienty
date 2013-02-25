# --- Database schema of the domain model

# --- !Ups
create table Unit (
  id                        bigint not null,
  name                      varchar(50),
  name_abrv                 varchar(10),
  plural                    varchar(50),
  plural_abrv               varchar(10),
  cunit_type                integer,
  constraint pk_Unit primary key (id))
;

create sequence Unit_seq;
ALTER SEQUENCE Unit_seq RESTART WITH 1000;



INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(1,'kilo gramme','kg','kilo grammes','kg',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(2,'gramme','g','grammes','g',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(3,'litre','L','litres','L',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(4,'millilitre','mL','millilitres','mL',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(5,'pound','lb','pounds','lbs',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(6,'pint','pt','pints','pt',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(7,'unit','','units','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(8,'cuillère à soupe','cs','cuillières à soupe','cs',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(9,'pincée','','pincées','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(10,'à goût','','à goût','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(11,'ounce liquide','fl oz','ounces liquide','fl oz',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(12,'gousse','','gousses','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(13,'centimètre cube','cm^3','centimètres cube','cm^3',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(14,'botte','','bottes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(15,'coupe','c','coupes','c',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(16,'bouteille','','bouteilles','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(17,'petit','','petit','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(18,'bâton','','bâtons','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(19,'cuillère à café','cc','cuillères à café','cc',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(20,'quart','qt','quarts','qt',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(21,'gallon','gal','gallons','gal',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(22,'ounce','oz','ounces','oz',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(23,'carré','','carrés','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(24,'milligramme','mg','milligrammes','mg',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(25,'boîte','','boîtes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(26,'container','','container','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(27,'large','','large','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(28,'paquet','','paquets','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(29,'tranche','','tranches','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(30,'cube','','cube','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(31,'moyen','','moyen','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(32,'pack','','pack','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(33,'brin','','brins','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(34,'feuille','','feuilles','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(35,'pièce','','pièces','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(36,'paquet','','paquets','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(37,'sac','','sac','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(38,'par service','','par service','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(39,'slices','','slices','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(40,'pincée','','pincées','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(41,'chaque','','chaque','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(42,'goutte','','gouttes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(43,'centilitre','cL','centilitres','cL',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(44,'boîte','','boîtes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(45,'tête','','têtes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(46,'pied','','pieds','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(47,'pot','','pots','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(48,'enveloppes','','enveloppes','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(49,'décilitre','dL','décilitres','dL',2);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(50,'tout','','tous','',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(51,'microgramme','µg','microgrammes','µg',1);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(52,'Calorie','kcal','Calories','kcal',0);
INSERT INTO Unit (id, name, name_abrv, plural, plural_abrv, cunit_type) VALUES(53,'kilojoule','kJ','kilojoules','kJ',0);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;
drop table if exists Unit;
SET REFERENTIAL_INTEGRITY TRUE;
drop sequence if exists Unit_seq;
