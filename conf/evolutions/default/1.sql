# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table favorites_list (
  id                            bigint auto_increment not null,
  name                          varchar(1024) not null,
  user_id                       bigint,
  constraint uq_favorites_list_name unique (name),
  constraint pk_favorites_list primary key (id)
);

create table movie_list (
  list_id                       bigint not null,
  movie_id                      bigint not null,
  constraint pk_movie_list primary key (list_id,movie_id)
);

create table movie (
  id                            bigint auto_increment not null,
  title                         varchar(1024) not null,
  thumbnail_url                 varchar(255),
  themoviedb_id                 bigint not null,
  constraint uq_movie_themoviedb_id unique (themoviedb_id),
  constraint pk_movie primary key (id)
);

create table todo (
  id                            bigint auto_increment not null,
  value                         varchar(1024) not null,
  user_id                       bigint,
  constraint pk_todo primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  auth_token                    varchar(255),
  email_address                 varchar(256) not null,
  sha_password                  varbinary(64) not null,
  full_name                     varchar(256) not null,
  creation_date                 timestamp not null,
  constraint uq_user_email_address unique (email_address),
  constraint pk_user primary key (id)
);

alter table favorites_list add constraint fk_favorites_list_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_favorites_list_user_id on favorites_list (user_id);

alter table movie_list add constraint fk_movie_list_favorites_list foreign key (list_id) references favorites_list (id) on delete restrict on update restrict;
create index ix_movie_list_favorites_list on movie_list (list_id);

alter table movie_list add constraint fk_movie_list_movie foreign key (movie_id) references movie (id) on delete restrict on update restrict;
create index ix_movie_list_movie on movie_list (movie_id);

alter table todo add constraint fk_todo_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_todo_user_id on todo (user_id);


# --- !Downs

alter table favorites_list drop constraint if exists fk_favorites_list_user_id;
drop index if exists ix_favorites_list_user_id;

alter table movie_list drop constraint if exists fk_movie_list_favorites_list;
drop index if exists ix_movie_list_favorites_list;

alter table movie_list drop constraint if exists fk_movie_list_movie;
drop index if exists ix_movie_list_movie;

alter table todo drop constraint if exists fk_todo_user_id;
drop index if exists ix_todo_user_id;

drop table if exists favorites_list;

drop table if exists movie_list;

drop table if exists movie;

drop table if exists todo;

drop table if exists user;

