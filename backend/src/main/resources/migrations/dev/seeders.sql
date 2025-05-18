INSERT INTO
    roles (title, id)
VALUES
    ('Administrador', '19596b7c-2f3d-4cb2-9213-c37e71e7e51c'),
    ('Cliente', '0bdf9727-723c-4257-9927-74acb16054d0'),
    ('Técnico', 'b64c59c2-994b-4cf6-b68f-b82597a37e21');

INSERT INTO
    priorities (title, id)
VALUES
    ('Alta', '365fd7c2-4d25-4407-a376-55a7b7aeec43'),
    ('Média', '0892aa67-ae77-47a8-8f64-64a963314450'),
    ('Baixa', '152cb1e5-a7b7-411c-ba29-19886798dfbf');

INSERT INTO
    users (name, email, password, phone_number, profile_picture, user_status, role_id, id)
VALUES
    ('Administrador teste 1', 'administrador-1@email.com', '$2a$10$ibrhvSoO06qpvzRs6sSXuOR78UMbzlSD/zYtkaY/Lj4DaiNxXO9Yi', '(11) 97896-5412', 'assets/images/admin-default-profile-avatar.png', 'Ativo', '19596b7c-2f3d-4cb2-9213-c37e71e7e51c', 'a008268c-a0df-4efd-9c08-5035881bbdf3'),

    ('Cliente teste 1', 'cliente-teste1@email.com', '$2a$10$M/ctOUJMUylYDvY.fPnU6.o0zfwJ8des2qF8CIkFvoG97JAVk737O', '(11) 91235-7462', 'assets/images/customer-default-profile-avatar.png', 'Ativo', '0bdf9727-723c-4257-9927-74acb16054d0', '8f069839-6943-4c4a-ae8f-6581d9dcf42c'),
    ('Cliente teste 2', 'cliente-teste2@email.com', '$2a$10$uyFTXDXcSgHTs9HgH59WwO4H5bzuBFw3e2nC19Jeyxe4aaKjcgnHS', '(11) 96582-1987', 'assets/images/customer-default-profile-avatar.png', 'Inativo', '0bdf9727-723c-4257-9927-74acb16054d0', '6c2071db-3ebe-49a1-84a8-f728c7d9f45d'),
    ('Cliente teste 3', 'cliente-teste3@email.com', '$2a$10$XGt8fVHD.i6t/IOaUo1ov..LUzxL9CmLoRIJ.pY9LBYJsLdpgvy/S', '(11) 98283-4901', 'assets/images/customer-default-profile-avatar.png', 'Ativo', '0bdf9727-723c-4257-9927-74acb16054d0', '99e959f1-067b-4239-b14d-1806abd45458'),

    ('Técnico teste 1', 'tecnico-teste1@email.com', '$2a$10$azEa7sbBi0bSA1zXcW3tTeeVjJ9zGMn4aDoUcawvBnTWeuQECG696', '(11) 99714-0213', 'assets/images/technical-default-profile-avatar.png', 'Ativo', 'b64c59c2-994b-4cf6-b68f-b82597a37e21', '16f2a0b5-73bd-4634-90d2-a6f051c8f0e5'),
    ('Técnico teste 2', 'tecnico-teste2@email.com', '$2a$10$q2LrzdTXt8CbzqseGZEW7OBh.neFXKJZ/dXAh0fQ0tgGPedyVICcm', '(11) 90274-1124', 'assets/images/technical-default-profile-avatar.png', 'Inativo', 'b64c59c2-994b-4cf6-b68f-b82597a37e21', '8146338a-c0a2-43bb-8aac-a8702e884b08'),
    ('Técnico teste 3', 'tecnico-teste3@email.com', '$2a$10$BvFppX4FKVMm.6y4JE6wk.XB9nGKebCv6GVxbgE9rd6GSJQQwvhde', '(11) 93087-9908', 'assets/images/technical-default-profile-avatar.png', 'Ativo', 'b64c59c2-994b-4cf6-b68f-b82597a37e21', '91e03a80-45fa-4eb8-b34d-b7ab5902d5d5');

INSERT INTO
    customers (cnpj, user_id)
VALUES
    ('33.065.964/0001-77', '8f069839-6943-4c4a-ae8f-6581d9dcf42c'),
    ('65.636.158/0001-06', '6c2071db-3ebe-49a1-84a8-f728c7d9f45d'),
    ('57.419.065/0001-93', '99e959f1-067b-4239-b14d-1806abd45458');

INSERT INTO
    technicians (date_birth, user_id)
VALUES
    ('1993-06-03', '16f2a0b5-73bd-4634-90d2-a6f051c8f0e5'),
    ('1995-01-22', '8146338a-c0a2-43bb-8aac-a8702e884b08'),
    ('1990-08-16', '91e03a80-45fa-4eb8-b34d-b7ab5902d5d5');

INSERT INTO
    tickets (id, closed_date, code, description, opening_date, search_term, ticket_status, title, customer_id, priority_id, technical_id)
VALUES
     ('733aefa2-22df-48d5-a16e-9242cc4156b9',NULL,'129','Chamado teste 1','2024-05-17 00:37:54.953000','129','Pendente','Chamado teste 1','8f069839-6943-4c4a-ae8f-6581d9dcf42c','365fd7c2-4d25-4407-a376-55a7b7aeec43','16f2a0b5-73bd-4634-90d2-a6f051c8f0e5'),
     ('11d7959a-dcda-4777-8e2b-4b2693e0cde2',NULL,'951','Chamado teste 2','2024-05-17 00:34:24.410000','951','Em Progresso','Chamado teste 2','8f069839-6943-4c4a-ae8f-6581d9dcf42c','0892aa67-ae77-47a8-8f64-64a963314450','8146338a-c0a2-43bb-8aac-a8702e884b08'),
     ('749fbbe2-2965-4cef-b45a-082f4f4c4ab4',NULL,'753','Chamado teste 3','2024-05-17 00:36:49.100000','753','Resolvido','Chamado teste 3','8f069839-6943-4c4a-ae8f-6581d9dcf42c','152cb1e5-a7b7-411c-ba29-19886798dfbf','8146338a-c0a2-43bb-8aac-a8702e884b08'),
     ('dd796734-82e6-4b3f-a58a-0acbff4896fd',NULL,'357','Chamado teste 4','2024-05-17 00:39:15.243000','357','Pendente','Chamado teste 4','6c2071db-3ebe-49a1-84a8-f728c7d9f45d','365fd7c2-4d25-4407-a376-55a7b7aeec43','16f2a0b5-73bd-4634-90d2-a6f051c8f0e5'),
     ('7beea182-8f09-4c7e-b550-abcb4dab6304',NULL,'654','Chamado teste 5','2024-05-17 00:35:35.835000','654','Em Progresso','Chamado teste 5','6c2071db-3ebe-49a1-84a8-f728c7d9f45d','0892aa67-ae77-47a8-8f64-64a963314450','16f2a0b5-73bd-4634-90d2-a6f051c8f0e5'),
     ('eb98ec1a-33f6-4f17-b3a8-22fe91035fca',NULL,'458','Chamado teste 6','2024-05-17 00:40:45.962000','458','Resolvido','Chamado teste 6','6c2071db-3ebe-49a1-84a8-f728c7d9f45d','152cb1e5-a7b7-411c-ba29-19886798dfbf','8146338a-c0a2-43bb-8aac-a8702e884b08');