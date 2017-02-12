# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cuisine (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_cuisine primary key (id))
;

create table day (
  id                        bigint not null,
  day                       varchar(255),
  constraint pk_day primary key (id))
;

create table delivery_order (
  id                        bigint not null,
  client_id                 bigint,
  delivery_id               bigint,
  discount                  double,
  constraint pk_delivery_order primary key (id))
;

create table meal (
  id                        bigint not null,
  name                      varchar(255),
  description               varchar(255),
  price                     double,
  constraint pk_meal primary key (id))
;

create table reservation (
  id                        bigint not null,
  amount                    integer,
  date                      timestamp,
  client_id                 bigint,
  local_id                  bigint,
  constraint pk_reservation primary key (id))
;

create table restaurant (
  dtype                     varchar(10) not null,
  id                        bigint not null,
  owner_id                  bigint not null,
  name                      varchar(255) not null,
  description               varchar(255),
  opening_hour              varchar(255),
  closing_hour              varchar(255),
  address                   varchar(255) not null,
  published                 boolean,
  is_local                  boolean,
  radius                    double,
  capacity                  integer,
  constraint pk_restaurant primary key (id))
;

create table user (
  dtype                     varchar(10) not null,
  id                        bigint not null,
  first_name                varchar(255) not null,
  last_name                 varchar(255) not null,
  address                   varchar(255) not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;


create table delivery_order_meal (
  delivery_order_id              bigint not null,
  meal_id                        bigint not null,
  constraint pk_delivery_order_meal primary key (delivery_order_id, meal_id))
;

create table restaurant_day (
  restaurant_id                  bigint not null,
  day_id                         bigint not null,
  constraint pk_restaurant_day primary key (restaurant_id, day_id))
;

create table restaurant_cuisine (
  restaurant_id                  bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_restaurant_cuisine primary key (restaurant_id, cuisine_id))
;

create table restaurant_meal (
  restaurant_id                  bigint not null,
  meal_id                        bigint not null,
  constraint pk_restaurant_meal primary key (restaurant_id, meal_id))
;

create table user_cuisine (
  user_id                        bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_user_cuisine primary key (user_id, cuisine_id))
;
create sequence cuisine_seq;

create sequence day_seq;

create sequence delivery_order_seq;

create sequence meal_seq;

create sequence reservation_seq;

create sequence restaurant_seq;

create sequence user_seq;

alter table delivery_order add constraint fk_delivery_order_client_1 foreign key (client_id) references user (id) on delete restrict on update restrict;
create index ix_delivery_order_client_1 on delivery_order (client_id);
alter table delivery_order add constraint fk_delivery_order_delivery_2 foreign key (delivery_id) references restaurant (id) on delete restrict on update restrict;
create index ix_delivery_order_delivery_2 on delivery_order (delivery_id);
alter table reservation add constraint fk_reservation_client_3 foreign key (client_id) references user (id) on delete restrict on update restrict;
create index ix_reservation_client_3 on reservation (client_id);
alter table reservation add constraint fk_reservation_local_4 foreign key (local_id) references restaurant (id) on delete restrict on update restrict;
create index ix_reservation_local_4 on reservation (local_id);
alter table restaurant add constraint fk_restaurant_user_5 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_restaurant_user_5 on restaurant (owner_id);



alter table delivery_order_meal add constraint fk_delivery_order_meal_delive_01 foreign key (delivery_order_id) references delivery_order (id) on delete restrict on update restrict;

alter table delivery_order_meal add constraint fk_delivery_order_meal_meal_02 foreign key (meal_id) references meal (id) on delete restrict on update restrict;

alter table restaurant_day add constraint fk_restaurant_day_restaurant_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_day add constraint fk_restaurant_day_day_02 foreign key (day_id) references day (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_restaur_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

alter table restaurant_meal add constraint fk_restaurant_meal_restaurant_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_meal add constraint fk_restaurant_meal_meal_02 foreign key (meal_id) references meal (id) on delete restrict on update restrict;

alter table user_cuisine add constraint fk_user_cuisine_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_cuisine add constraint fk_user_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists cuisine;

drop table if exists day;

drop table if exists delivery_order;

drop table if exists delivery_order_meal;

drop table if exists meal;

drop table if exists reservation;

drop table if exists restaurant;

drop table if exists restaurant_day;

drop table if exists restaurant_cuisine;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cuisine_seq;

drop sequence if exists day_seq;

drop sequence if exists delivery_order_seq;

drop sequence if exists meal_seq;

drop sequence if exists reservation_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists user_seq;

