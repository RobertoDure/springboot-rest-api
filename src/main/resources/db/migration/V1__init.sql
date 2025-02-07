create table student
(
  id    bigint auto_increment
    primary key,
  email varchar(255) not null,
  name  varchar(255) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO student VALUES (1, 1,'john@email.com','John',1);
INSERT INTO student VALUES (2,1,'jane@email.com','Jane',1);
INSERT INTO student VALUES (3,1,'billy@email.com','Billy',1);
INSERT INTO student VALUES (4,'miranda@email.com','Miranda',1);

create table user
(
  id       bigint auto_increment
    primary key,
  admin    bit          not null,
  name     varchar(255) not null,
  password varchar(255) not null,
  username varchar(255) not null,
  constraint UK_sb8bbouer5wak8vyiiy4pf2bx
    unique (username)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO user VALUES (1,1,'admin','$2a$10$VmLuwadDdAaiVawGjqSJyuzYFLg115DQ5QyOJFcfZWZBGFBepJQja','admin');
INSERT INTO user VALUES (3,0,'Alice Penna','$2a$10$VmLuwadDdAaiVawGjqSJyuzYFLg115DQ5QyOJFcfZWZBGFBepJQja','alicepenna');
INSERT INTO teacher VALUES (1,'jhon@school.com','Jhon Mayer');
INSERT INTO teacher VALUES (2,'Jessica@school.com','Jessica Jonnes');
INSERT INTO classroom VALUES (1,35,'Classroom 1');
INSERT INTO classroom VALUES (2,35,'Classroom 2');



