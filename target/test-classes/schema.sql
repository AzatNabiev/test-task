drop table if exists account cascade;
drop table if exists event cascade;

create table account(
	id serial primary key,
	email varchar(255),
	name varchar(255)
);

create table event(
    id serial primary key,
    added_time timestamp,
    event_starts timestamp,
    event_ends timestamp,
    event_name varchar (255),
    user_id bigint references account(id)
);
