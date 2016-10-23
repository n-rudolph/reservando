# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table client (
  id                        bigint not null,
  constraint pk_client primary key (id))
;

create table cuisine (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_cuisine primary key (id))
;

create table delivery (
  id                        bigint not null,
  radius                    double,
  image                     blob,
  constraint pk_delivery primary key (id))
;

create table local (
  id                        bigint not null,
  capacity                  integer,
  constraint pk_local primary key (id))
;

create table meal (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  price                     double,
  image                     blob,
  constraint pk_meal primary key (id))
;

create table order (
  id                        bigint not null,
  client_id                 bigint,
  delivery_id               bigint,
  address                   varchar(255),
  discount                  double,
  constraint uq_order_client_id unique (client_id),
  constraint uq_order_delivery_id unique (delivery_id),
  constraint pk_order primary key (id))
;

create table owner (
  id                        bigint not null,
  constraint pk_owner primary key (id))
;

create table reservation (
  id                        bigint not null,
  amount                    integer,
  date                      timestamp,
  client_id                 bigint,
  local_id                  bigint,
  constraint uq_reservation_client_id unique (client_id),
  constraint uq_reservation_local_id unique (local_id),
  constraint pk_reservation primary key (id))
;

create table restaurant (
  id                        bigint not null,
  owner_id                  bigint not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_restaurant primary key (id))
;

create table user (
  id                        bigint not null,
  firstname                 varchar(255),
  lastname                  varchar(255),
  birthday                  timestamp,
  address                   varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  profile_picture           blob,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;


create table client_cuisine (
  client_id                      bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_client_cuisine primary key (client_id, cuisine_id))
;

create table delivery_meal (
  delivery_id                    bigint not null,
  meal_id                        bigint not null,
  constraint pk_delivery_meal primary key (delivery_id, meal_id))
;

create table local_cuisine (
  local_id                       bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_local_cuisine primary key (local_id, cuisine_id))
;

create table local_meal (
  local_id                       bigint not null,
  meal_id                        bigint not null,
  constraint pk_local_meal primary key (local_id, meal_id))
;

create table order_meal (
  order_id                       bigint not null,
  meal_id                        bigint not null,
  constraint pk_order_meal primary key (order_id, meal_id))
;

create table restaurant_cuisine (
  restaurant_id                  bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_restaurant_cuisine primary key (restaurant_id, cuisine_id))
;
create sequence client_seq;

create sequence cuisine_seq;

create sequence delivery_seq;

create sequence local_seq;

create sequence meal_seq;

create sequence order_seq;

create sequence owner_seq;

create sequence reservation_seq;

create sequence restaurant_seq;

create sequence user_seq;

alter table order add constraint fk_order_client_1 foreign key (client_id) references client (id) on delete restrict on update restrict;
create index ix_order_client_1 on order (client_id);
alter table order add constraint fk_order_delivery_2 foreign key (delivery_id) references delivery (id) on delete restrict on update restrict;
create index ix_order_delivery_2 on order (delivery_id);
alter table reservation add constraint fk_reservation_client_3 foreign key (client_id) references client (id) on delete restrict on update restrict;
create index ix_reservation_client_3 on reservation (client_id);
alter table reservation add constraint fk_reservation_local_4 foreign key (local_id) references local (id) on delete restrict on update restrict;
create index ix_reservation_local_4 on reservation (local_id);
alter table restaurant add constraint fk_restaurant_owner_5 foreign key (owner_id) references owner (id) on delete restrict on update restrict;
create index ix_restaurant_owner_5 on restaurant (owner_id);



alter table client_cuisine add constraint fk_client_cuisine_client_01 foreign key (client_id) references client (id) on delete restrict on update restrict;

alter table client_cuisine add constraint fk_client_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

alter table delivery_meal add constraint fk_delivery_meal_delivery_01 foreign key (delivery_id) references delivery (id) on delete restrict on update restrict;

alter table delivery_meal add constraint fk_delivery_meal_meal_02 foreign key (meal_id) references meal (id) on delete restrict on update restrict;

alter table local_cuisine add constraint fk_local_cuisine_local_01 foreign key (local_id) references local (id) on delete restrict on update restrict;

alter table local_cuisine add constraint fk_local_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

alter table local_meal add constraint fk_local_meal_local_01 foreign key (local_id) references local (id) on delete restrict on update restrict;

alter table local_meal add constraint fk_local_meal_meal_02 foreign key (meal_id) references meal (id) on delete restrict on update restrict;

alter table order_meal add constraint fk_order_meal_order_01 foreign key (order_id) references order (id) on delete restrict on update restrict;

alter table order_meal add constraint fk_order_meal_meal_02 foreign key (meal_id) references meal (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_restaur_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists client;

drop table if exists client_cuisine;

drop table if exists cuisine;

drop table if exists delivery;

drop table if exists delivery_meal;

drop table if exists local;

drop table if exists local_cuisine;

drop table if exists local_meal;

drop table if exists meal;

drop table if exists order;

drop table if exists order_meal;

drop table if exists owner;

drop table if exists reservation;

drop table if exists restaurant;

drop table if exists restaurant_cuisine;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists client_seq;

drop sequence if exists cuisine_seq;

drop sequence if exists delivery_seq;

drop sequence if exists local_seq;

drop sequence if exists meal_seq;

drop sequence if exists order_seq;

drop sequence if exists owner_seq;

drop sequence if exists reservation_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists user_seq;

