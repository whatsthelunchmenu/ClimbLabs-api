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

create table post (
    post_id bigint not null auto_increment,
    city varchar(255),
    detail_street varchar(255),
    sido varchar(255),
    street varchar(255),
    zip_code varchar(255),
    created_at datetime,
    feature varchar(255),
    level integer not null,
    scale integer,
    scale_type varchar(255),
    thumb_nail_name varchar(255),
    thumb_nail_url varchar(255),
    title varchar(255),
    updated_at datetime,
    member_id bigint,
    primary key (post_id)
) engine=InnoDB;


alter table advantage
    add constraint FKc7a2rfe5m10aks8qdqj3qc7ej
        foreign key (post_id)
            references post (post_id);

alter table dis_advantage
    add constraint FKqo6ym272lph8q6yymsj4kljqd
        foreign key (post_id)
            references post (post_id);

alter table image
    add constraint FKe2l07hc93u2bbjnl80meu3rn4
        foreign key (post_id)
            references post (post_id);

alter table post
    add constraint FK83s99f4kx8oiqm3ro0sasmpww
        foreign key (member_id)
            references member (member_id);
