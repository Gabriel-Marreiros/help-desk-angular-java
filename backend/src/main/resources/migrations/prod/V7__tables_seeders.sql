INSERT INTO
    roles (title, id)
VALUES
    ('Administrador', UNHEX('69af892472df4a3d9aaec4137a81544a')),
    ('Cliente', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f')),
    ('Técnico', UNHEX('793959ef48a343969a3effd72a80b561'));

INSERT INTO
    priorities (title, id)
VALUES
    ('Alta', UNHEX('dc27957708e74d11afc550ba6287dddb')),
    ('Média', UNHEX('0375630c99ee4bb6bde4a39e4abb7e7a')),
    ('Baixa', UNHEX('1741937b46bb4c588cef1148eb1a95ad'));

INSERT INTO
    users (name, email, password, phone_number, profile_picture, user_status, role_id, id)
VALUES
    ('Administrador 1', 'administrador-1@email.com', '$2a$10$ibrhvSoO06qpvzRs6sSXuOR78UMbzlSD/zYtkaY/Lj4DaiNxXO9Yi', '(11) 97896-5412', 'assets/images/admin-default-profile-avatar.png', 'Ativo', UNHEX('69af892472df4a3d9aaec4137a81544a'), UNHEX('c9c9ac25703842c1b96fb8c1b765be9d')),

    ('Cliente 1', 'cliente-1@email.com', '$2a$10$M/ctOUJMUylYDvY.fPnU6.o0zfwJ8des2qF8CIkFvoG97JAVk737O', '(11) 91235-7462', 'assets/images/customer-default-profile-avatar.png', 'Ativo', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f'), UNHEX('166e6777cb744d0fbcbfc0e87604e556')),
    ('Cliente 2', 'cliente-2@email.com', '$2a$10$uyFTXDXcSgHTs9HgH59WwO4H5bzuBFw3e2nC19Jeyxe4aaKjcgnHS', '(11) 96582-1987', 'assets/images/customer-default-profile-avatar.png', 'Ativo', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f'), UNHEX('9c01911e5be24a1eb5661fb5c2326b35')),
    ('Cliente 3', 'cliente-3@email.com', '$2a$10$XGt8fVHD.i6t/IOaUo1ov..LUzxL9CmLoRIJ.pY9LBYJsLdpgvy/S', '(11) 98283-4901', 'assets/images/customer-default-profile-avatar.png', 'Ativo', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f'), UNHEX('561b3b44011349559e1ce26d1ef6823e')),
    ('Cliente 4', 'cliente-4@email.com', '$2a$10$q7gv5GddZUMinhRrziVJ5O0A91Z6FMIymaNMvyxLEl/IhiscIKHwW', '(11) 97146-9151', 'assets/images/customer-default-profile-avatar.png', 'Ativo', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f'), UNHEX('19da4c953c024224948aa31c90d7be8c')),
    ('Cliente 5', 'cliente-5@email.com', '$2a$10$XnCG5hPAppAqIkZeybmXzutU54NH7RQlhkkZ4WmUU.MIV.26XwxdG', '(11) 92589-6268', 'assets/images/customer-default-profile-avatar.png', 'Ativo', UNHEX('dcfc3598ca7349d1ba8fd4934931b66f'), UNHEX('499997e680f24a948ac9d8ee30aa70dc')),

    ('Técnico 1', 'tecnico-1@email.com', '$2a$10$azEa7sbBi0bSA1zXcW3tTeeVjJ9zGMn4aDoUcawvBnTWeuQECG696', '(11) 99714-0213', 'assets/images/technical-default-profile-avatar.png', 'Ativo', UNHEX('793959ef48a343969a3effd72a80b561'), UNHEX('1a2c47f70c2e4d15aa25e976ddc2f4c9')),
    ('Técnico 2', 'tecnico-2@email.com', '$2a$10$q2LrzdTXt8CbzqseGZEW7OBh.neFXKJZ/dXAh0fQ0tgGPedyVICcm', '(11) 90274-1124', 'assets/images/technical-default-profile-avatar.png', 'Ativo', UNHEX('793959ef48a343969a3effd72a80b561'), UNHEX('83696f9793a94f688ad1c923e9625633')),
    ('Técnico 3', 'tecnico-3@email.com', '$2a$10$BvFppX4FKVMm.6y4JE6wk.XB9nGKebCv6GVxbgE9rd6GSJQQwvhde', '(11) 93087-9908', 'assets/images/technical-default-profile-avatar.png', 'Ativo', UNHEX('793959ef48a343969a3effd72a80b561'), UNHEX('3c6ef84eb2fc4f3392ed2dfea0c9866f')),
    ('Técnico 4', 'tecnico-4@email.com', '$2a$10$/Pt39i8pceGKzSjcteBxxeGR0N9CkLUFWGVHT26ibdr8E8KHD8a6.', '(11) 99020-3697', 'assets/images/technical-default-profile-avatar.png', 'Ativo', UNHEX('793959ef48a343969a3effd72a80b561'), UNHEX('cb0f00f9d9d84d7cb966b174709e9ad3')),
    ('Técnico 5', 'tecnico-5@email.com', '$2a$10$zKlYsJQrXIKvTWiQbnt9M.RDtjG23UMQr614VDmFKI8EJmCNuwIXa', '(11) 97523-9998', 'assets/images/technical-default-profile-avatar.png', 'Ativo', UNHEX('793959ef48a343969a3effd72a80b561'), UNHEX('c8f515b080224bf0a2f27c8d007cf3ac'));

INSERT INTO
    customers (cnpj, user_id, id)
VALUES
    ('33.065.964/0001-77', UNHEX('166e6777cb744d0fbcbfc0e87604e556'), UNHEX('a19ddade5ee440f4a4252b4f468f6fce')),
    ('65.636.158/0001-06', UNHEX('9c01911e5be24a1eb5661fb5c2326b35'), UNHEX('ed93d3aa82574cc8b744c90eb19eaead')),
    ('57.419.065/0001-93', UNHEX('561b3b44011349559e1ce26d1ef6823e'), UNHEX('278094a2b06a41f6973c7799d9aaca57')),
    ('93.736.810/0001-91', UNHEX('19da4c953c024224948aa31c90d7be8c'), UNHEX('d03113dc8b5643eaab8c876848bfb13c')),
    ('67.727.632/0001-22', UNHEX('499997e680f24a948ac9d8ee30aa70dc'), UNHEX('5abbc9836ef54b7884571e2bc06cfb93'));

INSERT INTO
    technicians (date_birth, user_id, id)
VALUES
    ('1993-06-03', UNHEX('1a2c47f70c2e4d15aa25e976ddc2f4c9'), UNHEX('7cac5fa9d13340f0b28f54b63d3c8a5e')),
    ('1995-01-22', UNHEX('83696f9793a94f688ad1c923e9625633'), UNHEX('5eec53009d0e40b7a06acf8d75071eee')),
    ('1990-08-16', UNHEX('3c6ef84eb2fc4f3392ed2dfea0c9866f'), UNHEX('b56e81ad8b874ff78786e19b33144009')),
    ('1989-12-10', UNHEX('cb0f00f9d9d84d7cb966b174709e9ad3'), UNHEX('bdba48a582af45ec9d1246a3abf9fc91')),
    ('1999-07-22', UNHEX('c8f515b080224bf0a2f27c8d007cf3ac'), UNHEX('97e15ace24be4dfa943480b1423dc7b7'));

INSERT INTO
    tickets (id, closed_date, code, description, opening_date, search_term, ticket_status, title, customer_id, priority_id, technical_id)
VALUES
     (0x1A9BCCDA3141447D9F45655AC7D9BA76,NULL,'887','A impressora HP LaserJet do setor de contabilidade não está puxando papel da bandeja principal. Já foi verificado o carregamento do papel e não há atolamento visível.','2024-05-17 00:37:54.953000','887 Impressora Não Puxa Papel A impressora HP LaserJet do setor de contabilidade não está puxando papel da bandeja principal. Já foi verificado o carregamento do papel e não há atolamento visível. Técnico 5 Cliente 3','Resolvido','Impressora Não Puxa Papel',0x278094A2B06A41F6973C7799D9AACA57,0x0375630C99EE4BB6BDE4A39E4ABB7E7A,0x97E15ACE24BE4DFA943480B1423DC7B7),
     (0x22DE82F775D14230961E4A3225464191,NULL,'112','Impressora do setor financeiro não está sendo reconhecida pelo Windows 10. Tentativas de reinstalação dos drivers não resolveram o problema.','2024-05-17 00:34:24.410000','112 Impressora Não Reconhecida pelo Windows Impressora do setor financeiro não está sendo reconhecida pelo Windows 10. Tentativas de reinstalação dos drivers não resolveram o problema. Técnico 1 Cliente 4','Pendente','Impressora Não Reconhecida pelo Windows',0xD03113DC8B5643EAAB8C876848BFB13C,0x0375630C99EE4BB6BDE4A39E4ABB7E7A,0x7CAC5FA9D13340F0B28F54B63D3C8A5E),
     (0x2BE43CEE69B145F0A220C9BCC116DEEE,NULL,'344','Pedido para atualizar o antivírus em todas as estações de trabalho da empresa para a versão mais recente disponível. Inclui agendamento para minimizar impacto no horário de trabalho.','2024-05-17 00:36:49.100000','344 Atualização do Antivírus Corporativo Pedido para atualizar o antivírus em todas as estações de trabalho da empresa para a versão mais recente disponível. Inclui agendamento para minimizar impacto no horário de trabalho. Técnico 3 Cliente 1','Em Progresso','Atualização do Antivírus Corporativo',0xA19DDADE5EE440F4A4252B4F468F6FCE,0x1741937B46BB4C588CEF1148EB1A95AD,0xB56E81AD8B874FF78786E19B33144009),
     (0x3CCD5C9DDB0F4495AD86255EA5B8B04E,NULL,'141','O computador do analista de dados está extremamente lento. Programas simples como Excel e Outlook demoram muito para abrir e operar. A máquina foi recentemente formatada, mas o problema persiste.','2024-05-17 00:39:15.243000','141 Desempenho Lento no Computador O computador do analista de dados está extremamente lento. Programas simples como Excel e Outlook demoram muito para abrir e operar. A máquina foi recentemente formatada, mas o problema persiste. Técnico 3 Cliente 4','Pendente','Desempenho Lento no Computador',0xD03113DC8B5643EAAB8C876848BFB13C,0x1741937B46BB4C588CEF1148EB1A95AD,0xB56E81AD8B874FF78786E19B33144009),
     (0x7076307B5BC04D199BFEDD5FD682EF8A,NULL,'905','Funcionários em home office não conseguem se conectar à VPN da empresa. A mensagem de erro exibida é "Falha na autenticação".','2024-05-17 00:35:35.835000','905 Falha na Conexão VPN Funcionários em home office não conseguem se conectar à VPN da empresa. A mensagem de erro exibida é "Falha na autenticação". Técnico 4 Cliente 2','Em Progresso','Falha na Conexão VPN',0xED93D3AA82574CC8B744C90EB19EAEAD,0xDC27957708E74D11AFC550BA6287DDDB,0xBDBA48A582AF45EC9D1246A3ABF9FC91),
     (0xB28EACFE617342B2B447F1F1DE7D6526,NULL,'355',' O modem de internet do andar 2 está superaquecendo e desligando sozinho. O problema ocorre principalmente durante o período da tarde, quando o tráfego de rede é mais intenso.','2024-05-17 00:40:45.962000','355 Modem Superaquecendo  O modem de internet do andar 2 está superaquecendo e desligando sozinho. O problema ocorre principalmente durante o período da tarde, quando o tráfego de rede é mais intenso. Técnico 1 Cliente 3','Pendente','Modem Superaquecendo',0x278094A2B06A41F6973C7799D9AACA57,0x0375630C99EE4BB6BDE4A39E4ABB7E7A,0x7CAC5FA9D13340F0B28F54B63D3C8A5E),
     (0xC2EC70D3429E4248A1851AE890E491D8,NULL,'568','O modem da sala de reuniões não está se conectando à internet. Todos os cabos foram verificados e o modem já foi reiniciado várias vezes sem sucesso.','2024-05-17 00:39:55.902000','568 Modem de Internet Não Conecta O modem da sala de reuniões não está se conectando à internet. Todos os cabos foram verificados e o modem já foi reiniciado várias vezes sem sucesso. Técnico 4 Cliente 1','Em Progresso','Modem de Internet Não Conecta',0xA19DDADE5EE440F4A4252B4F468F6FCE,0xDC27957708E74D11AFC550BA6287DDDB,0xBDBA48A582AF45EC9D1246A3ABF9FC91),
     (0xD3F1A0EF4C2643C8BB70036F4AF26697,NULL,'898','O computador do funcionário do suporte técnico não está ligando. Já foi verificada a conexão elétrica e trocada a tomada, mas o problema persiste.','2024-05-17 00:38:46.066000','898 Computador Não Liga O computador do funcionário do suporte técnico não está ligando. Já foi verificada a conexão elétrica e trocada a tomada, mas o problema persiste. Técnico 2 Cliente 5','Resolvido','Computador Não Liga',0x5ABBC9836EF54B7884571E2BC06CFB93,0xDC27957708E74D11AFC550BA6287DDDB,0x5EEC53009D0E40B7A06ACF8D75071EEE);