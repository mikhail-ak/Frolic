create table game_info (
    id bigint primary key,
    description varchar(1023),
    price_per_day decimal(19,2) not null,
    ratings_count int not null,
    ratings_sum int not null,
    release_date date,
    genre varchar(255),
    title varchar(255) unique not null
);

create table game_file (
    installation_file blob not null,
    last_updated_on timestamp,
    info_id bigint primary key,
    foreign key (info_id) references game_info(id)
);

create table picture (
    picture blob not null;
    mime_type varchar(63) not null;
    description varchar(255);
    picture_id bigint primary key;
    foreign key (info_id) references game_info(id)
);

create table users (
    id bigint primary key,
    account_status varchar(255) not null,
    email varchar(255) unique not null,
    name varchar(255) unique not null,
    password varchar(255) not null
);

create table subscription (
    id bigint primary key,
    activation_time timestamp not null,
    expiration_time timestamp not null,
    status varchar(255) not null,
    user_id bigint not null,
    info_id bigint not null,
    foreign key (user_id) references users(id),
    foreign key (info_id) references game_info(id)
);