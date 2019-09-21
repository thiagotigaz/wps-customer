CREATE TABLE IF NOT EXISTS customer (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  age int(11) DEFAULT NULL,
  date_created datetime DEFAULT NULL,
  first_name varchar(255) DEFAULT NULL,
  last_name varchar(255) DEFAULT NULL,
  profession varchar(255) DEFAULT NULL,
  CONSTRAINT customers_pk PRIMARY KEY (id)
);
