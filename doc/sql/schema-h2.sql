DROP TABLE IF EXISTS bgrigar_document;
CREATE TABLE bgrigar_document (
  id int AUTO_INCREMENT PRIMARY KEY,
  name varchar(255) NOT NULL,
  title varchar(1024),
  extention varchar(32),
  path varchar(2000) NOT NULL,
  last_modified timestamp DEFAULT CURRENT_TIMESTAMP,
  content clob
);