create table users (
    id bigserial primary key,
    user_name varchar(255) not null unique ,
    password varchar(255) not null
);

create table locations (
    id bigserial primary key,
    user_id serial references users(id),
    location_name varchar(255),
    longitude decimal,
    latitude decimal
);

create table sessions (
    id varchar primary key ,
    user_id serial references users(id),
    expires_at date

);