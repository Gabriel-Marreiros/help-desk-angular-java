CREATE TABLE customers (
  id binary(16) NOT NULL,
  cnpj varchar(255) NOT NULL,
  user_id binary(16) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_euat1oase6eqv195jvb71a93s (user_id),
  CONSTRAINT FKrh1g1a20omjmn6kurd35o3eit FOREIGN KEY (user_id) REFERENCES users (id)
)