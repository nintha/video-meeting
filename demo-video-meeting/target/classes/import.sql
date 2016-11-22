create table userauth (
	id varchar(36) primary key not null,
	username varchar(20),
	password varchar(20)
);

insert into userauth values ('000','wum','123456');