# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table address (
  id                        bigint not null,
  complete_address          varchar(255),
  lat                       double,
  lng                       double,
  place                     varchar(255),
  city                      varchar(255),
  state                     varchar(255),
  country                   varchar(255),
  district                  varchar(255),
  constraint pk_address primary key (id))
;

create table cuisine (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_cuisine primary key (id))
;

create table cuisine_preference (
  id                        bigint not null,
  cuisine_id                bigint,
  client_id                 bigint,
  amount                    integer,
  constraint pk_cuisine_preference primary key (id))
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
  address                   varchar(255),
  discount_id               bigint,
  time_placed               timestamp,
  active                    boolean,
  constraint uq_delivery_order_discount_id unique (discount_id),
  constraint pk_delivery_order primary key (id))
;

create table discount (
  id                        bigint not null,
  code                      varchar(255),
  discount                  integer,
  is_used                   boolean,
  constraint pk_discount primary key (id))
;

create table meal (
  id                        bigint not null,
  name                      varchar(255),
  description               TEXT,
  price                     double,
  photo_id                  bigint,
  is_deleted                boolean,
  restaurant_id             bigint,
  constraint uq_meal_photo_id unique (photo_id),
  constraint pk_meal primary key (id))
;

create table meal_order (
  id                        bigint not null,
  delivery_order_id         bigint not null,
  meal_id                   bigint,
  amount                    integer,
  constraint pk_meal_order primary key (id))
;

create table photo (
  id                        bigint not null,
  name                      varchar(255),
  path                      varchar(255),
  constraint pk_photo primary key (id))
;

create table qualification (
  id                        bigint not null,
  qualification             double,
  client_id                 bigint,
  restaurant_id             bigint,
  constraint pk_qualification primary key (id))
;

create table reservation (
  id                        bigint not null,
  amount                    integer,
  date                      timestamp,
  client_id                 bigint,
  local_id                  bigint,
  discount_id               bigint,
  active                    boolean,
  constraint uq_reservation_discount_id unique (discount_id),
  constraint pk_reservation primary key (id))
;

create table restaurant (
  dtype                     varchar(10) not null,
  id                        bigint not null,
  name                      varchar(255) not null,
  description               TEXT,
  opening_hour              varchar(255),
  closing_hour              varchar(255),
  address_id                bigint not null,
  published                 boolean,
  is_local                  boolean,
  owner_id                  bigint,
  is_deleted                boolean,
  photo_id                  bigint,
  radius                    double,
  response_time             integer,
  capacity                  integer,
  mins_between_turns        integer,
  constraint uq_restaurant_address_id unique (address_id),
  constraint uq_restaurant_photo_id unique (photo_id),
  constraint pk_restaurant primary key (id))
;

create table user (
  dtype                     varchar(10) not null,
  id                        bigint not null,
  first_name                varchar(255) not null,
  last_name                 varchar(255) not null,
  address_id                bigint not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  photo_id                  bigint,
  photo_path                varchar(255),
  active                    boolean,
  constraint uq_user_address_id unique (address_id),
  constraint uq_user_email unique (email),
  constraint uq_user_photo_id unique (photo_id),
  constraint pk_user primary key (id))
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

create table user_cuisine (
  user_id                        bigint not null,
  cuisine_id                     bigint not null,
  constraint pk_user_cuisine primary key (user_id, cuisine_id))
;
create sequence address_seq;

create sequence cuisine_seq;

create sequence cuisine_preference_seq;

create sequence day_seq;

create sequence delivery_order_seq;

create sequence discount_seq;

create sequence meal_seq;

create sequence meal_order_seq;

create sequence photo_seq;

create sequence qualification_seq;

create sequence reservation_seq;

create sequence restaurant_seq;

create sequence user_seq;

alter table delivery_order add constraint fk_delivery_order_client_1 foreign key (client_id) references user (id) on delete restrict on update restrict;
create index ix_delivery_order_client_1 on delivery_order (client_id);
alter table delivery_order add constraint fk_delivery_order_delivery_2 foreign key (delivery_id) references restaurant (id) on delete restrict on update restrict;
create index ix_delivery_order_delivery_2 on delivery_order (delivery_id);
alter table delivery_order add constraint fk_delivery_order_discount_3 foreign key (discount_id) references discount (id) on delete restrict on update restrict;
create index ix_delivery_order_discount_3 on delivery_order (discount_id);
alter table meal add constraint fk_meal_photo_4 foreign key (photo_id) references photo (id) on delete restrict on update restrict;
create index ix_meal_photo_4 on meal (photo_id);
alter table meal add constraint fk_meal_restaurant_5 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;
create index ix_meal_restaurant_5 on meal (restaurant_id);
alter table meal_order add constraint fk_meal_order_delivery_order_6 foreign key (delivery_order_id) references delivery_order (id) on delete restrict on update restrict;
create index ix_meal_order_delivery_order_6 on meal_order (delivery_order_id);
alter table reservation add constraint fk_reservation_client_7 foreign key (client_id) references user (id) on delete restrict on update restrict;
create index ix_reservation_client_7 on reservation (client_id);
alter table reservation add constraint fk_reservation_local_8 foreign key (local_id) references restaurant (id) on delete restrict on update restrict;
create index ix_reservation_local_8 on reservation (local_id);
alter table reservation add constraint fk_reservation_discount_9 foreign key (discount_id) references discount (id) on delete restrict on update restrict;
create index ix_reservation_discount_9 on reservation (discount_id);
alter table restaurant add constraint fk_restaurant_address_10 foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_restaurant_address_10 on restaurant (address_id);
alter table restaurant add constraint fk_restaurant_owner_11 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_restaurant_owner_11 on restaurant (owner_id);
alter table restaurant add constraint fk_restaurant_photo_12 foreign key (photo_id) references photo (id) on delete restrict on update restrict;
create index ix_restaurant_photo_12 on restaurant (photo_id);
alter table user add constraint fk_user_address_13 foreign key (address_id) references address (id) on delete restrict on update restrict;
create index ix_user_address_13 on user (address_id);
alter table user add constraint fk_user_photo_14 foreign key (photo_id) references photo (id) on delete restrict on update restrict;
create index ix_user_photo_14 on user (photo_id);



alter table restaurant_day add constraint fk_restaurant_day_restaurant_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_day add constraint fk_restaurant_day_day_02 foreign key (day_id) references day (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_restaur_01 foreign key (restaurant_id) references restaurant (id) on delete restrict on update restrict;

alter table restaurant_cuisine add constraint fk_restaurant_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

alter table user_cuisine add constraint fk_user_cuisine_user_01 foreign key (user_id) references user (id) on delete restrict on update restrict;

alter table user_cuisine add constraint fk_user_cuisine_cuisine_02 foreign key (cuisine_id) references cuisine (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists address;

drop table if exists cuisine;

drop table if exists cuisine_preference;

drop table if exists day;

drop table if exists delivery_order;

drop table if exists discount;

drop table if exists meal;

drop table if exists meal_order;

drop table if exists photo;

drop table if exists qualification;

drop table if exists reservation;

drop table if exists restaurant;

drop table if exists restaurant_day;

drop table if exists restaurant_cuisine;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists address_seq;

drop sequence if exists cuisine_seq;

drop sequence if exists cuisine_preference_seq;

drop sequence if exists day_seq;

drop sequence if exists delivery_order_seq;

drop sequence if exists discount_seq;

drop sequence if exists meal_seq;

drop sequence if exists meal_order_seq;

drop sequence if exists photo_seq;

drop sequence if exists qualification_seq;

drop sequence if exists reservation_seq;

drop sequence if exists restaurant_seq;

drop sequence if exists user_seq;

