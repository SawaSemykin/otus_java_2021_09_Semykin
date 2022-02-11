create table client (
                        id bigserial not null primary key,
                        name varchar(50)
);

create table address (
                        id bigserial not null,
                        street varchar(255),
                        client_id bigint,
                        primary key (id),
                        foreign key (client_id) references client (id)
);

create table phone (
                        id bigserial not null,
                        number varchar(255),
                        client_id bigint not null references client (id),
                        primary key (id)
);
