create table items (
  id varchar(50) primary key,
  label varchar(30) not null,
  price DECIMAL not null
);

create table members (
  id bigint primary key auto_increment,
  name varchar(30) not null,
  birthday date,
  created_at timestamp not null
);

create table item_member_relations (
  item_id varchar(50) not null,
  member_id bigint not null
);
