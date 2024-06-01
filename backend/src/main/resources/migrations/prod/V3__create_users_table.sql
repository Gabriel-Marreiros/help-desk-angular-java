CREATE TABLE users (
  id binary(16) NOT NULL,
  email varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  phone_number varchar(255) DEFAULT NULL,
  profile_picture longtext,
  user_status varchar(255) NOT NULL,
  role_id binary(16) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_6dotkott2kjsp8vw4d0m25fb7 (email),
  UNIQUE KEY UK_9q63snka3mdh91as4io72espi (phone_number),
  KEY FKp56c1712k691lhsyewcssf40f (role_id),
  CONSTRAINT FKp56c1712k691lhsyewcssf40f FOREIGN KEY (role_id) REFERENCES roles (id)
)