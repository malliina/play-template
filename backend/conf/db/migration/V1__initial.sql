create table my_table
(
    name  varchar(128)                              not null primary key,
    owner varchar(128)                              not null,
    added timestamp(3) default CURRENT_TIMESTAMP(3) not null
);
