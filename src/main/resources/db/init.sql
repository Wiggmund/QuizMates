DROP TABLE if EXISTS sessionsrecords CASCADE;
DROP TABLE if EXISTS sessions CASCADE;
DROP TABLE if EXISTS hosts CASCADE;
DROP TABLE if EXISTS pairs CASCADE;
DROP TABLE if EXISTS groups CASCADE;
DROP TABLE if EXISTS students CASCADE;

-- Create the 'groups' table
CREATE TABLE IF NOT EXISTS groups
(
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name            VARCHAR(255)                            NOT NULL,
    students_amount INTEGER                                 NOT NULL DEFAULT 0,
    teamlead_id     BIGINT                                  NOT NULL DEFAULT 0,
    CONSTRAINT pk_groups PRIMARY KEY (id),
    CONSTRAINT uc_groups_name UNIQUE (name)
);
-- Create the 'students' table
CREATE TABLE IF NOT EXISTS students
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(255)                            NOT NULL,
    last_name  VARCHAR(255)                            NOT NULL,
    group_id   BIGINT                                  REFERENCES groups (id) ON DELETE SET NULL,
    CONSTRAINT pk_students PRIMARY KEY (id)
);

-- Create the 'pairs' table
CREATE TABLE IF NOT EXISTS pairs
(
    id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    student_a BIGINT REFERENCES students (id) ON DELETE CASCADE,
    student_b BIGINT REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT pk_pairs PRIMARY KEY (id)
);

-- Create the 'hosts' table
CREATE TABLE IF NOT EXISTS hosts
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(255)                            NOT NULL,
    last_name  VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_hosts PRIMARY KEY (id)
);

-- Create the 'sessions' table
CREATE TABLE IF NOT EXISTS sessions (
    id              BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title           VARCHAR(255) NOT NULL,
    description     TEXT NOT NULL,
    date            TIMESTAMP NOT NULL,
    best_student    BIGINT REFERENCES students(id) ON DELETE SET NULL,
    best_group      BIGINT REFERENCES students(id) ON DELETE SET NULL,
    status          VARCHAR(255) NOT NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (id),
    CONSTRAINT uc_sessions_title UNIQUE (title)
);

-- Create the 'sessionsrecords' table
CREATE TABLE IF NOT EXISTS sessionsrecords (
     id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
     session_id     BIGINT REFERENCES sessions(id) ON DELETE SET NULL,
     pair_id        BIGINT REFERENCES pairs(id) ON DELETE SET NULL,
     student_id     BIGINT REFERENCES students(id) ON DELETE SET NULL,
     host_id        BIGINT REFERENCES hosts(id) ON DELETE SET NULL,
     score          FLOAT NOT NULL,
     host_notes     VARCHAR(255),
     was_present    BOOLEAN NOT NULL,
     action         VARCHAR(255) NOT NULL,
     question       VARCHAR(255),
     CONSTRAINT pk_sessions_records PRIMARY KEY (id)
=======
CREATE TABLE IF NOT EXISTS sessionsrecords
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    session_id  BIGINT                                  REFERENCES sessions (id) ON DELETE SET NULL,
    pair_id     BIGINT                                  REFERENCES pairs (id) ON DELETE SET NULL,
    student_id  BIGINT                                  REFERENCES students (id) ON DELETE SET NULL,
    host_id     BIGINT                                  REFERENCES hosts (id) ON DELETE SET NULL,
    score       FLOAT                                   NOT NULL,
    host_notes  TEXT,
    was_present BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_sessions_records PRIMARY KEY (id)
>>>>>>> changing-group
);
