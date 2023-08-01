-- Create the 'students' table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_students PRIMARY KEY (id)
);

-- Create the 'groups' table
CREATE TABLE IF NOT EXISTS groups (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    student_id BIGINT,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_groups PRIMARY KEY (id),
    CONSTRAINT fk_groups_students FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT uc_groups_name UNIQUE (name)
);

-- Create the 'pairs' table
CREATE TABLE IF NOT EXISTS pairs (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    student_a BIGINT,
    student_b BIGINT,
    CONSTRAINT pk_pairs PRIMARY KEY (id),
    CONSTRAINT fk_pairs_student_a FOREIGN KEY (student_a) REFERENCES students(id),
    CONSTRAINT fk_pairs_student_b FOREIGN KEY (student_b) REFERENCES students(id)
);

-- Create the 'hosts' table
CREATE TABLE IF NOT EXISTS hosts (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_hosts PRIMARY KEY (id)
);

-- Create the 'sessions' table
CREATE TABLE IF NOT EXISTS sessions (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    best_student BIGINT,
    best_group BIGINT,
    status BOOLEAN NOT NULL,
    CONSTRAINT pk_sessions PRIMARY KEY (id),
    CONSTRAINT fk_sessions_best_student FOREIGN KEY (best_student) REFERENCES students(id),
    CONSTRAINT fk_sessions_best_group FOREIGN KEY (best_group) REFERENCES groups(id),
    CONSTRAINT uc_sessions_title UNIQUE (title)
);

-- Create the 'sessionsrecords' table
CREATE TABLE IF NOT EXISTS sessionsrecords (
     session_id BIGINT,
     pair_id BIGINT,
     student_id BIGINT,
     host_id BIGINT,
     score FLOAT NOT NULL,
     host_notes TEXT,
     was_present BOOLEAN NOT NULL,
     PRIMARY KEY (session_id, pair_id, student_id, host_id),
     CONSTRAINT fk_sessionsrecords_session FOREIGN KEY (session_id) REFERENCES sessions(id),
     CONSTRAINT fk_sessionsrecords_pair FOREIGN KEY (pair_id) REFERENCES pairs(id),
     CONSTRAINT fk_sessionsrecords_student FOREIGN KEY (student_id) REFERENCES students(id),
     CONSTRAINT fk_sessionsrecords_host FOREIGN KEY (host_id) REFERENCES hosts(id)
);