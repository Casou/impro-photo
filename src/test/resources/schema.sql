DROP TABLE imp_categorie IF EXISTS;
CREATE TABLE imp_categorie(
  id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  nom varchar(50) NOT NULL,
  type varchar(15) NOT NULL,
  path_folder varchar(100) NOT NULL,
  termine tinyint(1) NOT NULL DEFAULT '0',
  ordre int(11) NOT NULL DEFAULT '99',
);
