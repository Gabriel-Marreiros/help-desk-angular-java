ALTER TABLE tickets DROP FOREIGN KEY FKi81xre2n3j3as1sp24j440kq1;

ALTER TABLE tickets DROP FOREIGN KEY FK7jtlmjwqkv81k9qb3k81qg97f;


UPDATE
    tickets
JOIN
    customers ON tickets.customer_id = customers.id
SET
    tickets.customer_id = customers.user_id;

UPDATE
    tickets
JOIN
    technicians ON tickets.technical_id = technicians.id
SET
    tickets.technical_id = technicians.user_id;


ALTER TABLE technicians DROP PRIMARY KEY;

ALTER TABLE technicians DROP COLUMN id;

ALTER TABLE technicians ADD PRIMARY KEY (user_id);

ALTER TABLE technicians DROP FOREIGN KEY FKahon685bc8wlxcr7okh2vpvn;

ALTER TABLE technicians
ADD CONSTRAINT FKahon685bc8wlxcr7okh2vpvn
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE technicians DROP INDEX UK_t6mj4qc2wog7bder7nss5ljfh;


ALTER TABLE customers DROP PRIMARY KEY;

ALTER TABLE customers DROP COLUMN id;

ALTER TABLE customers ADD PRIMARY KEY (user_id);

ALTER TABLE customers DROP FOREIGN KEY FKrh1g1a20omjmn6kurd35o3eit;

ALTER TABLE customers
ADD CONSTRAINT FKrh1g1a20omjmn6kurd35o3eit
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE customers DROP INDEX UK_euat1oase6eqv195jvb71a93s;

ALTER TABLE tickets
ADD CONSTRAINT FKi81xre2n3j3as1sp24j440kq1
FOREIGN KEY (customer_id) REFERENCES customers(user_id);

ALTER TABLE tickets
ADD CONSTRAINT FK7jtlmjwqkv81k9qb3k81qg97f
FOREIGN KEY (technical_id) REFERENCES technicians(user_id);