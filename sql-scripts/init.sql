create table classroom
(
    id             bigint auto_increment
        primary key,
    capacity       int          not null,
    classroom_name varchar(255) not null
);

create table grade
(
    id            bigint auto_increment
        primary key,
    grade         float        not null,
    grade_comment varchar(255) not null,
    grade_date    datetime     not null,
    lecture_id    bigint       not null,
    teacher_id    bigint       not null
);

create table lecture
(
    id           bigint auto_increment
        primary key,
    description  varchar(255) not null,
    end_date     datetime     not null,
    lecture_name varchar(255) not null,
    start_date   datetime     not null
);

create table schema_version
(
    version_rank   int                                 not null,
    installed_rank int                                 not null,
    version        varchar(50)                         not null
        primary key,
    description    varchar(200)                        not null,
    type           varchar(20)                         not null,
    script         varchar(1000)                       not null,
    checksum       int                                 null,
    installed_by   varchar(100)                        not null,
    installed_on   timestamp default CURRENT_TIMESTAMP not null,
    execution_time int                                 not null,
    success        tinyint(1)                          not null
);

create index schema_version_ir_idx
    on schema_version (installed_rank);

create index schema_version_s_idx
    on schema_version (success);

create index schema_version_vr_idx
    on schema_version (version_rank);

create table student
(
    id               bigint auto_increment
        primary key,
    address          varchar(255) not null,
    date_of_birth    datetime     not null,
    email            varchar(255) not null,
    gender           varchar(255) not null,
    id_classroom     int          not null,
    name             varchar(255) not null,
    nationality      varchar(255) not null,
    guardian_contact varchar(255) not null,
    guardian_name    varchar(255) not null,
    id_teacher       bigint       not null
);

create table student_grades
(
    student_id bigint not null,
    grades_id  bigint not null,
    constraint UK_3vo68ol3kaecejj24sjk1dwmx
        unique (grades_id),
    constraint FK61clm4w3vln89ttmp2c3s6n4r
        foreign key (grades_id) references grade (id),
    constraint FKqxhto571qgves967x0medsrn
        foreign key (student_id) references student (id)
);

create table student_lectures
(
    student_id  bigint not null,
    lectures_id bigint not null,
    constraint UK_efic2fk7umjfb2mjq3tcavnc3
        unique (lectures_id),
    constraint FKa05kniate65ru2bytpuw6vg4c
        foreign key (lectures_id) references lecture (id),
    constraint FKjrt2ee0ng7ewok39kivqmtjdp
        foreign key (student_id) references student (id)
);

create table teacher
(
    id                  bigint auto_increment
        primary key,
    address             varchar(255) not null,
    date_of_birth       datetime     not null,
    email               varchar(255) not null,
    gender              varchar(255) not null,
    id_classroom        int          not null,
    name                varchar(255) not null,
    nationality         varchar(255) not null,
    highest_degree      varchar(255) not null,
    phone_number        varchar(255) not null,
    subject             varchar(255) not null,
    years_of_experience varchar(255) not null
);

create table teacher_lectures
(
    teacher_id  bigint not null,
    lectures_id bigint not null,
    constraint UK_bjcp97c4sw1i5nivg0cy0l05i
        unique (lectures_id),
    constraint FK5763owksyh7eef4ap35p7sa98
        foreign key (lectures_id) references lecture (id),
    constraint FK9u6gvos8ucetbp0l54p6g1vsk
        foreign key (teacher_id) references teacher (id)
);

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
);

