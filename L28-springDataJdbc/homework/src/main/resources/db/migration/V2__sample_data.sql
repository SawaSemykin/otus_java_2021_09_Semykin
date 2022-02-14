insert into client(id, name) values(1, 'Vasya');
insert into client(id, name) values(2, 'Petya');

insert into address(id, street, client_id) values(1, 'Moscow', 1);
insert into address(id, street, client_id) values(2, 'Saint Petersburg', 2);

insert into phone(id, number, client_id) values(1, '111-11-11', 1);
insert into phone(id, number, client_id) values(2, '222-22-22', 2);
insert into phone(id, number, client_id) values(3, '333-33-33', 2);
