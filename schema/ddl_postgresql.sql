
    alter table category 
        drop constraint FK_81thrbnb8c08gua7tvqj7xdqk;

    alter table category_food_item 
        drop constraint FK_pjc47hvpkirbrjg8fv5hj6i53;

    alter table category_food_item 
        drop constraint FK_57cujwjie5pbw98s9ub9ibmr3;

    alter table food_item 
        drop constraint FK_g2t49cbwowqs6wppdtbuceuhr;

    alter table recipe 
        drop constraint FK_5erw19wu4k1mu350u80omax3s;

    drop table if exists category cascade;

    drop table if exists category_food_item cascade;

    drop table if exists food_item cascade;

    drop table if exists recipe cascade;

    drop table if exists user cascade;

    drop sequence category_seq;

    drop sequence food_item_seq;

    drop sequence recipe_seq;

    drop sequence user_seq;

    create table category (
        id bigint not null,
        title varchar(255) not null,
        parent_id bigint,
        primary key (id)
    );

    create table category_food_item (
        category_id bigint not null,
        food_item_id bigint not null
    );

    create table food_item (
        id bigint not null,
        title varchar(255) not null,
        vegetarian boolean not null,
        top_recipe_id bigint,
        primary key (id)
    );

    create table recipe (
        id bigint not null,
        flash_video varchar(255),
        image varchar(255),
        source_url varchar(255),
        title varchar(255) not null,
        you_tube_video varchar(255),
        food_item_id bigint,
        primary key (id)
    );

    create table user (
        id bigint not null,
        disabled boolean not null,
        email varchar(255) not null,
        passwordHash varchar(255) not null,
        primary key (id)
    );

    alter table category 
        add constraint category_u1 unique (parent_id, title);

    alter table food_item 
        add constraint food_item_u1 unique (title);

    alter table user 
        add constraint user_u1 unique (email);

    alter table category 
        add constraint FK_81thrbnb8c08gua7tvqj7xdqk 
        foreign key (parent_id) 
        references category;

    alter table category_food_item 
        add constraint FK_pjc47hvpkirbrjg8fv5hj6i53 
        foreign key (food_item_id) 
        references food_item;

    alter table category_food_item 
        add constraint FK_57cujwjie5pbw98s9ub9ibmr3 
        foreign key (category_id) 
        references category;

    alter table food_item 
        add constraint FK_g2t49cbwowqs6wppdtbuceuhr 
        foreign key (top_recipe_id) 
        references recipe;

    alter table recipe 
        add constraint FK_5erw19wu4k1mu350u80omax3s 
        foreign key (food_item_id) 
        references food_item;

    create sequence category_seq;

    create sequence food_item_seq;

    create sequence recipe_seq;

    create sequence user_seq;
