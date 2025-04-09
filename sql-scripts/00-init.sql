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
    email    varchar(255) not null,
    constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username)
);

INSERT INTO user
VALUES (1, 1, 'admin', '$2a$10$VmLuwadDdAaiVawGjqSJyuzYFLg115DQ5QyOJFcfZWZBGFBepJQja', 'admin', 'admin@admin.com');

-- Populate classroom table
INSERT INTO classroom (capacity, classroom_name)
VALUES (30, 'Room A');
INSERT INTO classroom (capacity, classroom_name)
VALUES (25, 'Room B');

-- Populate teacher table
INSERT INTO teacher (address, date_of_birth, email, gender, id_classroom, name, nationality, highest_degree,
                     phone_number, subject, years_of_experience)
VALUES ('123 Maple St, Springfield', '1980-04-15', 'j.smith@example.com', 'Male', 1, 'John Smith', 'American',
        'MSc Mathematics', '555-1234', 'Mathematics', '10');
INSERT INTO teacher (address, date_of_birth, email, gender, id_classroom, name, nationality, highest_degree,
                     phone_number, subject, years_of_experience)
VALUES ('456 Oak Ave, Springfield', '1975-09-20', 'l.jones@example.com', 'Female', 2, 'Linda Jones', 'American',
        'PhD Physics', '555-5678', 'Physics', '12');
INSERT INTO teacher (address, date_of_birth, email, gender, id_classroom, name, nationality, highest_degree,
                     phone_number, subject, years_of_experience)
VALUES ('789 Pine Rd, Springfield', '1988-02-10', 'a.kim@example.com', 'Female', 1, 'Alice Kim', 'American',
        'MA History', '555-9012', 'History', '8');

-- Populate lecture table
INSERT INTO lecture (description, end_date, lecture_name, start_date)
VALUES ('Introduction to calculus', '2023-09-01 12:00:00', 'Calculus 101', '2023-09-01 09:00:00');
INSERT INTO lecture (description, end_date, lecture_name, start_date)
VALUES ('Fundamentals of classical mechanics', '2023-09-02 13:00:00', 'Physics Basics', '2023-09-02 10:00:00');
INSERT INTO lecture (description, end_date, lecture_name, start_date)
VALUES ('Overview of social history', '2023-09-03 11:00:00', 'World History', '2023-09-03 08:30:00');

-- Populate grade table
INSERT INTO grade (grade, grade_comment, grade_date, lecture_id, teacher_id)
VALUES (8.5, 'Good progress', '2023-09-10 10:00:00', 1, 1);
INSERT INTO grade (grade, grade_comment, grade_date, lecture_id, teacher_id)
VALUES (9.0, 'Excellent performance', '2023-09-11 11:00:00', 2, 2);
INSERT INTO grade (grade, grade_comment, grade_date, lecture_id, teacher_id)
VALUES (7.0, 'Needs improvement', '2023-09-12 09:30:00', 3, 3);

-- Populate student table
INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('12 Elm St, Springfield', '2005-03-15', 'm.brown@example.com', 'Male', 1, 'Michael Brown', 'American',
        '555-1122', 'Sarah Brown', 1);
INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('34 Cedar Ave, Springfield', '2006-07-22', 'e.davis@example.com', 'Female', 2, 'Emma Davis', 'American',
        '555-3344', 'John Davis', 2);
INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('56 Birch Blvd, Springfield', '2005-11-30', 'r.miller@example.com', 'Male', 1, 'Robert Miller', 'American',
        '555-5566', 'Linda Miller', 3);
INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('100 First Ave, Springfield', '2006-05-15', 'john.doe@example.com', 'Male', 1, 'John Doe', 'American',
        '555-0100', 'Jane Doe', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('200 Second St, Springfield', '2005-11-03', 'maria.smith@example.com', 'Female', 2, 'Maria Smith', 'American',
        '555-0101', 'Ann Smith', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('300 Third Blvd, Springfield', '2006-03-22', 'david.jones@example.com', 'Male', 1, 'David Jones', 'American',
        '555-0102', 'Karen Jones', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('400 Fourth Rd, Springfield', '2007-09-12', 'linda.taylor@example.com', 'Female', 2, 'Linda Taylor', 'American',
        '555-0103', 'Susan Taylor', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('500 Fifth Ave, Springfield', '2006-12-25', 'robert.brown@example.com', 'Male', 1, 'Robert Brown', 'American',
        '555-0104', 'Deborah Brown', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('600 Sixth Ln, Springfield', '2005-07-19', 'patricia.wilson@example.com', 'Female', 2, 'Patricia Wilson',
        'American', '555-0105', 'Nancy Wilson', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('700 Seventh Dr, Springfield', '2006-08-08', 'james.martin@example.com', 'Male', 1, 'James Martin', 'American',
        '555-0106', 'Barbara Martin', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('800 Eighth St, Springfield', '2007-04-21', 'barbara.thomas@example.com', 'Female', 2, 'Barbara Thomas',
        'American', '555-0107', 'Margaret Thomas', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('900 Ninth Blvd, Springfield', '2005-09-30', 'michael.jackson@example.com', 'Male', 1, 'Michael Jackson',
        'American', '555-0108', 'Janet Jackson', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1000 Tenth Rd, Springfield', '2006-11-11', 'sarah.lee@example.com', 'Female', 2, 'Sarah Lee', 'American',
        '555-0109', 'Alice Lee', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('101 Alpha St, Springfield', '2007-01-15', 'alice.johnson@example.com', 'Female', 1, 'Alice Johnson',
        'American', '555-1101', 'Mary Johnson', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('202 Beta Ave, Springfield', '2006-02-20', 'brian.clark@example.com', 'Male', 2, 'Brian Clark', 'American',
        '555-1102', 'Robert Clark', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('303 Gamma Rd, Springfield', '2005-03-21', 'carla.morris@example.com', 'Female', 1, 'Carla Morris', 'American',
        '555-1103', 'Lisa Morris', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('404 Delta Blvd, Springfield', '2006-04-10', 'derek.adams@example.com', 'Male', 2, 'Derek Adams', 'American',
        '555-1104', 'Sarah Adams', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('505 Epsilon Ln, Springfield', '2007-05-25', 'elena.sanchez@example.com', 'Female', 1, 'Elena Sanchez',
        'American', '555-1105', 'Carlos Sanchez', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('606 Zeta Park, Springfield', '2006-06-30', 'frank.hall@example.com', 'Male', 2, 'Frank Hall', 'American',
        '555-1106', 'Nancy Hall', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('707 Eta Dr, Springfield', '2007-07-15', 'grace.lopez@example.com', 'Female', 1, 'Grace Lopez', 'American',
        '555-1107', 'Juan Lopez', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('808 Theta Ct, Springfield', '2005-08-12', 'henry.harris@example.com', 'Male', 2, 'Henry Harris', 'American',
        '555-1108', 'Alice Harris', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('909 Iota Way, Springfield', '2006-09-05', 'irene.patel@example.com', 'Female', 1, 'Irene Patel', 'American',
        '555-1109', 'Raj Patel', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1010 Kappa St, Springfield', '2007-10-20', 'jack.miller@example.com', 'Male', 2, 'Jack Miller', 'American',
        '555-1110', 'Laura Miller', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1111 Lambda Ave, Springfield', '2005-11-11', 'karen.wright@example.com', 'Female', 1, 'Karen Wright',
        'American', '555-1111', 'Mark Wright', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1212 Mu Blvd, Springfield', '2006-12-12', 'leo.baker@example.com', 'Male', 2, 'Leo Baker', 'American',
        '555-1112', 'Donna Baker', 3);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1313 Nu Ln, Springfield', '2007-01-31', 'mia.king@example.com', 'Female', 1, 'Mia King', 'American',
        '555-1113', 'John King', 1);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1414 Xi Rd, Springfield', '2006-02-28', 'nathan.kingston@example.com', 'Male', 2, 'Nathan Kingston',
        'American', '555-1114', 'Pam Kingston', 2);

INSERT INTO student (address, date_of_birth, email, gender, id_classroom, name, nationality, guardian_contact,
                     guardian_name, id_teacher)
VALUES ('1515 Omicron Dr, Springfield', '2007-03-17', 'olivia.moore@example.com', 'Female', 1, 'Olivia Moore',
        'American', '555-1115', 'Diane Moore', 3);

-- Populate student_grades join table
-- Assume Michael Brown received the Calculus grade (grade id = 1),
-- Emma Davis received the Physics grade (grade id = 2),
-- and Robert Miller received the History grade (grade id = 3)
INSERT INTO student_grades (student_id, grades_id)
VALUES (1, 1);
INSERT INTO student_grades (student_id, grades_id)
VALUES (2, 2);
INSERT INTO student_grades (student_id, grades_id)
VALUES (3, 3);

-- Populate student_lectures join table
-- Map each student to one lecture:
INSERT INTO student_lectures (student_id, lectures_id)
VALUES (1, 1);
INSERT INTO student_lectures (student_id, lectures_id)
VALUES (2, 2);
INSERT INTO student_lectures (student_id, lectures_id)
VALUES (3, 3);

-- Populate teacher_lectures join table
-- Map each teacher to the lecture they deliver:
INSERT INTO teacher_lectures (teacher_id, lectures_id)
VALUES (1, 1);
INSERT INTO teacher_lectures (teacher_id, lectures_id)
VALUES (2, 2);
INSERT INTO teacher_lectures (teacher_id, lectures_id)
VALUES (3, 3);


