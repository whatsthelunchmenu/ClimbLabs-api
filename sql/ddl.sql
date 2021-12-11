create table advantage
(
    advantage_id bigint not null auto_increment,
    item         varchar(255),
    post_id      bigint,
    primary key (advantage_id)
) engine=InnoDB;

create table dis_advantage
(
    id      bigint not null auto_increment,
    item    varchar(255),
    post_id bigint,
    primary key (id)
) engine=InnoDB;

create table image
(
    id      bigint not null auto_increment,
    name    varchar(255),
    url     varchar(255),
    post_id bigint,
    primary key (id)
) engine=InnoDB;

create table member
(
    member_id  bigint not null auto_increment,
    created_at datetime(6),
    name       varchar(255),
    updated_at datetime(6),
    user_id    varchar(255),
    primary key (member_id)
) engine=InnoDB;

create table post
(
    post_id        bigint  not null auto_increment,
    city           varchar(255),
    detail_street  varchar(255),
    street         varchar(255),
    zip_code       varchar(255),
    climbing_title varchar(255),
    created_at     datetime(6),
    feature        varchar(255),
    level          integer not null,
    scale_type     varchar(255),
    title          varchar(255),
    updated_at     datetime(6),
    member_id      bigint,
    primary key (post_id)
) engine=InnoDB;