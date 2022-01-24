create table address (
                         id bigint not null,
                         street varchar(255),
                         primary key (id)
);

create table phone (
                       id bigint not null,
                       number varchar(255),
                       client_id bigint not null,
                       primary key (id)
);

alter table client
    add address_id bigint,
    add constraint FKjxmuvgb1uw5k6hwp4pjltoojw
        foreign key (address_id)
            references address;

alter table phone
    add constraint FK3o48ec26lujl3kf01hwqplhn2
        foreign key (client_id)
            references client;



