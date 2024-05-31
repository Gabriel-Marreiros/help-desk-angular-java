CREATE TABLE technicians (
  id binary(16) NOT NULL,
  date_birth date NOT NULL,
  user_id binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY UK_t6mj4qc2wog7bder7nss5ljfh (user_id),
  CONSTRAINT FKahon685bc8wlxcr7okh2vpvn FOREIGN KEY (user_id) REFERENCES users (id)
)