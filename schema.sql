DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS lessons CASCADE;
DROP TABLE IF EXISTS rooms CASCADE;
DROP TABLE IF EXISTS grades CASCADE;
DROP TABLE IF EXISTS incomes CASCADE;
DROP TABLE IF EXISTS expenses CASCADE;
DROP TABLE IF EXISTS attendances CASCADE;
DROP TABLE IF EXISTS activities CASCADE;

CREATE TABLE students (
    id INTEGER AUTO_INCREMENT,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    phone_number VARCHAR(10) NOT NULL,
    sex ENUM('Male', 'Female') NOT NULL,
    birth_date DATE NOT NULL,
    grade_id INTEGER NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE teachers (
    id INTEGER AUTO_INCREMENT,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    phone_number VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE lessons (
    id INTEGER AUTO_INCREMENT,
    lesson_name VARCHAR(25) NOT NULL,
    day_of_week ENUM('Saturday', 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday') NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    price DECIMAL(38, 2) NOT NULL,
    percentage INT NOT NULL DEFAULT 50,
    classes_number INTEGER NOT NULL DEFAULT 0,
    teacher_dues DECIMAL(38, 2) NOT NULL DEFAULT 0.00,
    teacher_id INTEGER NOT NULL,
    room_id INTEGER NOT NULL,
    grade_id INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE rooms (
    id INTEGER AUTO_INCREMENT,
    code VARCHAR(7) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE grades (
    id INTEGER AUTO_INCREMENT,
    level ENUM('Primary', 'Middle', 'Secondary', 'University') NOT NULL,
    year_of_grade INTEGER NOT NULL,
    PRIMARY KEY (id),
    CHECK (
        (level = 'Primary' AND year_of_grade BETWEEN 1 AND 5) OR
        (level = 'Middle' AND year_of_grade BETWEEN 1 AND 4) OR
        (level = 'Secondary' AND year_of_grade BETWEEN 1 AND 3) OR
        (level = 'University' AND year_of_grade BETWEEN 1 AND 5)
        )
);

CREATE TABLE incomes (
    id INTEGER AUTO_INCREMENT,
    amount DECIMAL(38, 2) NOT NULL,
    details VARCHAR(300) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    student_id INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE expenses (
    id INTEGER AUTO_INCREMENT,
    amount DECIMAL(38, 2) NOT NULL,
    details VARCHAR(300) NOT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    teacher_id INTEGER,
    PRIMARY KEY (id)
);

CREATE TABLE attendances (
    id INTEGER AUTO_INCREMENT,
    times_present INTEGER NOT NULL DEFAULT (0),
    notes VARCHAR(300),
    dues DECIMAL(38, 2) NOT NULL DEFAULT (0.00),
    lesson_id INTEGER NOT NULL,
    student_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (lesson_id, student_id)
);

CREATE TABLE activities (
    id INTEGER AUTO_INCREMENT,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    type ENUM('Add', 'Update', 'Delete', 'Enroll', 'Leave', 'Pay', 'Receive') NOT NULL,
    details VARCHAR(300) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE students ADD FOREIGN KEY (grade_id) REFERENCES grades(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE lessons ADD FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE lessons ADD FOREIGN KEY (room_id) REFERENCES rooms(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE lessons ADD FOREIGN KEY (grade_id) REFERENCES grades(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE incomes ADD FOREIGN KEY (student_id) REFERENCES students(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE expenses ADD FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE attendances ADD FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE attendances ADD FOREIGN KEY (student_id) REFERENCES students(id) ON UPDATE CASCADE ON DELETE CASCADE;

CREATE INDEX idx_students_grade_id ON students(grade_id);
CREATE INDEX idx_lessons_teacher_id ON lessons(teacher_id);
CREATE INDEX idx_lessons_room_id ON lessons(room_id);
CREATE INDEX idx_lessons_grade_id ON lessons(grade_id);
CREATE INDEX idx_incomes_student_id ON incomes(student_id);
CREATE INDEX idx_expenses_teacher_id ON expenses(teacher_id);
CREATE INDEX idx_attendances_lesson_id ON attendances(lesson_id);
CREATE INDEX idx_attendances_student_id ON attendances(student_id);


-- INSERT INTO grades (level, year_of_grade) VALUES ('Primary', 1);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Primary', 2);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Primary', 3);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Primary', 4);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Primary', 5);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Middle', 1);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Middle', 2);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Middle', 3);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Middle', 4);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Secondary', 1);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Secondary', 2);
-- INSERT INTO grades (level, year_of_grade) VALUES ('Secondary', 3);
-- INSERT INTO grades (level, year_of_grade) VALUES ('University', 1);
-- INSERT INTO grades (level, year_of_grade) VALUES ('University', 2);
-- INSERT INTO grades (level, year_of_grade) VALUES ('University', 3);
-- INSERT INTO grades (level, year_of_grade) VALUES ('University', 4);
-- INSERT INTO grades (level, year_of_grade) VALUES ('University', 5);
