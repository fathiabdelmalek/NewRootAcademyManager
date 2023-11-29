SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `lessons`;
DROP TABLE IF EXISTS `classes`;
DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `incomes`;
DROP TABLE IF EXISTS `expenses`;
DROP TABLE IF EXISTS `attendances`;
DROP TABLE IF EXISTS `lesson_student`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `students` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(25) NOT NULL,
    `last_name` VARCHAR(25) NOT NULL,
    `phone_number` VARCHAR(10) NOT NULL,
    `sex` ENUM('M', 'F') NOT NULL,
    `birth_date` DATE NOT NULL,
    `grade_id` INTEGER NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `teachers` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(25) NOT NULL,
    `last_name` VARCHAR(25) NOT NULL,
    `phone_number` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `lessons` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `lesson_name` VARCHAR(25) NOT NULL,
    `date` ENUM('Satuday', 'Sunday', 'Monday', 'Thuesday', 'Wednesday', 'Thursday', 'Friday') NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `teacher_id` INTEGER NOT NULL,
    `class_id` INTEGER NOT NULL,
    `grade_id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `classes` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(5) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `grades` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `level` ENUM('P', 'M', 'S') NOT NULL,
    `year` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    CHECK (
    	(`level` = 'P' AND year BETWEEN 1 AND 5) OR
    	(`level` = 'M' AND year BETWEEN 1 AND 4) OR
    	(`level` = 'S' AND year BETWEEN 1 AND 3)
    )
);

CREATE TABLE `incomes` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT NOW(),
    `update_time` DATETIME NOT NULL DEFAULT NOW(),
    `student_id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `expenses` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT NOW(),
    `update_time` DATETIME NOT NULL DEFAULT NOW(),
    `teacher_id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `attendances` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `times_present` INTEGER NOT NULL,
    `lesson_id` INTEGER NOT NULL,
    `student_id` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`lesson_id`, `student_id`)
);

CREATE TABLE `lesson_student` (
    `lesson_id` INTEGER NOT NULL,
    `student_id` INTEGER NOT NULL,
    PRIMARY KEY (`lesson_id`, `student_id`)
);

ALTER TABLE `students` ADD FOREIGN KEY (`grade_id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`teacher_id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`class_id`) REFERENCES `classes`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`grade_id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `incomes` ADD FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `expenses` ADD FOREIGN KEY (`teacher_id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD FOREIGN KEY (`lesson_id`) REFERENCES `lessons`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lesson_student` ADD FOREIGN KEY (`lesson_id`) REFERENCES `lessons`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lesson_student` ADD FOREIGN KEY (`student_id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `students` ADD INDEX `idx_grade_id` (`grade_id`);
ALTER TABLE `lessons` ADD INDEX `idx_teacher_id` (`teacher_id`);
ALTER TABLE `lessons` ADD INDEX `idx_class_id` (`class_id`);
ALTER TABLE `lessons` ADD INDEX `idx_grade_id` (`grade_id`);
ALTER TABLE `incomes` ADD INDEX `idx_student_id` (`student_id`);
ALTER TABLE `expenses` ADD INDEX `idx_teacher_id` (`teacher_id`);
ALTER TABLE `attendances` ADD INDEX `idx_lesson_id` (`lesson_id`);
ALTER TABLE `attendances` ADD INDEX `idx_student_id` (`student_id`);
ALTER TABLE `lesson_student` ADD INDEX `idx_lesson_id` (`lesson_id`);
ALTER TABLE `lesson_student` ADD INDEX `idx_student_id` (`student_id`);

CREATE OR REPLACE VIEW `students_view` AS
SELECT `students`.`id`, CONCAT(`students`.`first_name`, `students`.`last_name`) AS `student_name`, `students`.`phone_number`, `students`.`sex`, `students`.`birth_date`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `students`
INNER JOIN `grades` ON `grades`.`id` = `students`.`grade_id`;

CREATE OR REPLACE VIEW `lessons_view` AS
SELECT `lessons`.`id`, `lessons`.`lesson_name`, `lessons`.`date`, `lessons`.`start_time`, `lessons`.`end_time`, CONCAT(`teachers`.`first_name`, ' ', `teachers`.`last_name`) AS `teacher_name`, `classes`.`code`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `lessons`
INNER JOIN `teachers` ON `teachers`.`id` = `lessons`.`teacher_id`
INNER JOIN `classes` ON `classes`.`id` = `lessons`.`class_id`
LEFT JOIN `grades` ON `grades`.`id` = `lessons`.`grade_id`;

CREATE OR REPLACE VIEW `incomes_view` AS
SELECT `incomes`.`id`, `incomes`.`amount`, `incomes`.`details`, `incomes`.`create_time` AS `time`, CONCAT(`students`.`first_name`, ' ', `students`.`last_name`) AS `student_name` FROM `incomes`
LEFT JOIN `students` ON `students`.`id` = `incomes`.`student_id`
ORDER BY `time` DESC;

CREATE OR REPLACE VIEW `expenses_view` AS
SELECT `expenses`.`id`, `expenses`.`amount`, `expenses`.`details`, `expenses`.`create_time` AS `time`, CONCAT(`teachers`.`first_name`, ' ', `teachers`.`last_name`) AS `teacher_name` FROM `expenses`
LEFT JOIN `teachers` ON `teachers`.`id` = `expenses`.`teacher_id`
ORDER BY `time` DESC;

CREATE OR REPLACE VIEW `attendances_view` AS
SELECT `attendances`.`id`, CONCAT(`students`.`first_name`, ' ', `students`.`last_name`) AS `student_name`, `lessons`.`lesson_name`, `attendances`.`times_present` FROM `attendances`
INNER JOIN `students` ON `students`.`id` = `attendances`.`student_id`
INNER JOIN `lessons` ON `lessons`.`id` = `attendances`.`lesson_id`;

INSERT INTO `grades` (`level`, `year`) VALUES ('P', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('P', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('P', 3);
INSERT INTO `grades` (`level`, `year`) VALUES ('P', 4);
INSERT INTO `grades` (`level`, `year`) VALUES ('P', 5);
INSERT INTO `grades` (`level`, `year`) VALUES ('M', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('M', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('M', 3);
INSERT INTO `grades` (`level`, `year`) VALUES ('M', 4);
INSERT INTO `grades` (`level`, `year`) VALUES ('S', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('S', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('S', 3);

INSERT INTO `classes` (`code`) VALUES ('CH101');
INSERT INTO `classes` (`code`) VALUES ('CH102');
INSERT INTO `classes` (`code`) VALUES ('CH103');
INSERT INTO `classes` (`code`) VALUES ('CH104');
INSERT INTO `classes` (`code`) VALUES ('CH105');
INSERT INTO `classes` (`code`) VALUES ('CH201');
INSERT INTO `classes` (`code`) VALUES ('CH202');
INSERT INTO `classes` (`code`) VALUES ('CH203');
INSERT INTO `classes` (`code`) VALUES ('CH204');
INSERT INTO `classes` (`code`) VALUES ('CH205');
INSERT INTO `classes` (`code`) VALUES ('CH206');
INSERT INTO `classes` (`code`) VALUES ('BT101');
INSERT INTO `classes` (`code`) VALUES ('BT103');
INSERT INTO `classes` (`code`) VALUES ('BT104');
INSERT INTO `classes` (`code`) VALUES ('BT105');

INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Hannah', 'Kon', '+60(6)7598', 'M', '2021-07-31', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Gilda', 'Fritsch', '179.530.90', 'M', '2019-02-06', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alia', 'Huels', '(754)955-8', 'F', '2007-09-19', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Donald', 'Rempel', '(699)215-0', 'M', '2021-07-26', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Miles', 'Renner', '1-054-208-', 'M', '1982-09-25', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Sibyl', 'Block', '0501325580', 'M', '1996-10-19', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Nicklaus', 'Kerluke', '574-653-54', 'F', '2000-12-20', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Bud', 'Von', '(657)822-5', 'F', '1985-01-05', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Augustine', 'Durgan', '238.726.13', 'M', '2007-11-03', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Walker', 'Greenholt', '221-935-93', 'M', '2022-05-28', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Mozelle', 'Fay', '1-023-921-', 'F', '2022-04-25', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Moses', 'Gerhold', '(461)735-4', 'F', '1973-08-20', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Bud', 'Reichert', '(484)221-7', 'F', '1976-11-29', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Foster', 'Mertz', '1-518-838-', 'M', '1977-07-25', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Sabrina', 'Schaden', '066.270.78', 'F', '2011-03-16', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Tia', 'Harvey', '446-375-70', 'M', '1994-08-16', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Stanley', 'Stark', '1-399-045-', 'F', '1984-03-02', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Troy', 'Crona', '283-477-87', 'F', '1999-05-06', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Leonel', 'Hoeger', '554-892-48', 'F', '1976-04-16', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Teagan', 'Schultz', '0658526121', 'F', '1980-05-16', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Leslie', 'Hilll', '0492873299', 'F', '1988-08-17', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Oliver', 'Armstrong', '1-233-268-', 'M', '2006-12-15', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Aisha', 'Nolan', '1-490-944-', 'M', '2006-11-20', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Keyon', 'Veum', '534-959-07', 'F', '2002-12-15', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Herminio', 'Hyatt', '+91(2)4544', 'F', '1993-07-16', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Cecilia', 'Upton', '(309)799-0', 'M', '1989-08-06', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Frederique', 'Nader', '1-745-342-', 'F', '2006-02-11', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Kory', 'Bosco', '0010256559', 'M', '2006-04-11', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Johann', 'Casper', '1-647-953-', 'F', '1975-07-06', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Ian', 'Sanford', '(004)449-9', 'M', '1999-08-04', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Dorthy', 'Cartwright', '325.845.18', 'F', '2009-10-23', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Pearlie', 'Abbott', '685-205-35', 'F', '1994-01-12', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Reinhold', 'Osinski', '(647)526-6', 'F', '1987-10-24', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Elmira', 'Beier', '(200)810-4', 'F', '1997-02-24', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Braulio', 'Satterfield', '1-519-769-', 'F', '1999-04-29', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Carlee', 'Kunze', '731-266-98', 'F', '2012-08-27', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Bethel', 'Schinner', '(622)593-8', 'M', '1972-12-22', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Leda', 'Herman', '+89(6)7048', 'F', '1991-12-23', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Eileen', 'Hyatt', '914-158-66', 'F', '2007-06-15', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Shea', 'Rowe', '1-347-808-', 'M', '2010-10-25', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Luella', 'Grant', '348.563.77', 'F', '2020-11-16', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Cindy', 'Kuvalis', '500-281-66', 'F', '2013-08-10', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Jamel', 'Larkin', '056.314.42', 'F', '2006-01-10', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Howell', 'Price', '1-446-091-', 'F', '2008-01-11', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Orin', 'Crooks', '1-483-431-', 'F', '1970-03-01', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Marjorie', 'Kuphal', '694-272-47', 'M', '1974-04-27', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Chadrick', 'O\'Kon', '201.768.53', 'F', '2006-07-21', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Winfield', 'Mitchell', '610.661.29', 'M', '1974-04-14', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Lauretta', 'Frami', '014-339-83', 'F', '1976-12-05', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Tatyana', 'Hermiston', '1-146-841-', 'F', '1992-06-11', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Gunner', 'Jones', '0400791747', 'M', '1981-02-23', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Lonnie', 'Stokes', '246.029.24', 'M', '1994-09-21', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Johnny', 'Rogahn', '(346)304-1', 'F', '1982-10-25', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Ardith', 'Reinger', '1-737-091-', 'M', '2010-09-30', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Devan', 'Walker', '612.493.21', 'M', '1999-02-25', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alek', 'Roberts', '(446)759-7', 'M', '1999-05-09', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Cory', 'Cremin', '1-182-786-', 'M', '2017-10-25', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Gaetano', 'Koss', '419.472.97', 'M', '1982-01-28', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Natalie', 'Powlowski', '(350)550-3', 'M', '1970-08-18', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Sienna', 'Bernier', '1-982-142-', 'M', '2008-06-22', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Effie', 'Feil', '273.759.86', 'F', '2003-02-15', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alessandra', 'Larson', '+11(3)6127', 'M', '1995-10-13', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Keara', 'Klein', '103-084-65', 'M', '2012-07-04', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Dallas', 'O\'Conner', '(695)478-2', 'F', '1979-10-11', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Mozelle', 'Wehner', '762.410.93', 'M', '1982-10-02', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Allie', 'Weber', '+92(7)0901', 'F', '1985-02-06', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Nichole', 'Feil', '1-154-903-', 'F', '1990-04-09', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Jalon', 'Kovacek', '1-075-338-', 'M', '2011-11-16', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Mollie', 'Boyer', '(700)619-9', 'F', '1990-01-02', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Jeff', 'Runolfsdottir', '(563)374-9', 'M', '1994-02-16', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Kendall', 'Volkman', '1-984-746-', 'F', '1997-04-25', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Georgianna', 'Jakubowski', '(888)894-1', 'M', '2009-07-20', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Emmie', 'Larson', '(191)302-0', 'M', '2013-07-24', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Ethelyn', 'Rolfson', '377.859.09', 'F', '2008-10-03', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Cristina', 'Bernier', '1-781-312-', 'M', '1975-01-29', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Miller', 'Abshire', '1-145-624-', 'M', '2017-12-20', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Adelia', 'Schultz', '048-878-71', 'F', '2015-07-12', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Hunter', 'Walter', '767-598-79', 'M', '1977-01-23', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Kelley', 'VonRueden', '(242)728-7', 'F', '1973-10-07', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Isadore', 'Crona', '598.571.09', 'M', '1977-07-25', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alexa', 'Breitenberg', '262-923-66', 'M', '2001-12-13', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Weldon', 'Armstrong', '+57(4)1813', 'F', '1987-09-02', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Matt', 'Stroman', '0533168112', 'F', '2017-09-22', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Spencer', 'Pagac', '692-135-77', 'M', '1973-12-10', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Lindsey', 'Ferry', '777.402.65', 'F', '1980-12-13', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Valerie', 'Hansen', '0553938345', 'M', '1993-03-18', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Annamarie', 'Farrell', '(027)877-9', 'F', '2007-12-30', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Donny', 'Feil', '1-753-384-', 'M', '2018-01-17', 4);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alberta', 'Morissette', '590-271-62', 'M', '2021-10-23', 5);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Peggie', 'O\'Keefe', '625-383-03', 'F', '2004-09-12', 6);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Alanna', 'Moen', '1-283-863-', 'M', '1992-10-15', 7);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Wilber', 'Toy', '0028524302', 'F', '1970-08-05', 8);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Izabella', 'Robel', '187-945-68', 'M', '2007-12-04', 9);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Esta', 'Rath', '1-787-305-', 'F', '2019-08-22', 10);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Orland', 'Davis', '+70(2)0533', 'F', '1990-03-16', 11);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Cecile', 'Cronin', '(370)813-7', 'M', '1998-02-20', 12);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Francisca', 'Lang', '(689)707-0', 'F', '2013-10-11', 1);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Bettie', 'Veum', '1-862-546-', 'M', '2005-07-18', 2);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Immanuel', 'Mohr', '1-278-429-', 'M', '2012-03-24', 3);
INSERT INTO `students` (`first_name`, `last_name`, `phone_number`, `sex`, `birth_date`, `grade_id`) VALUES ('Oswaldo', 'Parker', '659-711-87', 'F', '2006-10-19', 4);

INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Maggie', 'Yundt', '(938)803-8');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Gwendolyn', 'Smith', '119.008.69');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Kirstin', 'Rutherford', '1-272-614-');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Donnell', 'McClure', '382.735.66');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Jody', 'Legros', '701-815-13');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Norene', 'Koelpin', '747.362.88');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Melba', 'Krajcik', '616-353-31');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Casimir', 'Yundt', '+11(4)8586');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Hector', 'Keeling', '1-701-537-');
INSERT INTO `teachers` (`first_name`, `last_name`, `phone_number`) VALUES ('Janelle', 'Schuppe', '087-143-97');

INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math P1', 'Sunday', '10:00:00', '11:00:00', 1, 1, 1);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math P2', 'Thursday', '10:00:00', '11:00:00', 2, 2, 2);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math P3', 'Friday', '10:00:00', '11:00:00', 3, 3, 3);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math P4', 'Thursday', '10:00:00', '11:00:00', 4, 4, 4);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math P5', 'Wednesday', '10:00:00', '11:00:00', 5, 5, 5);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics M1', 'Thuesday', '09:00:00', '10:00:00', 6, 1, 6);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics M2', 'Monday', '09:00:00', '10:00:00', 7, 2, 7);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics M3', 'Thuesday', '09:00:00', '10:00:00', 8, 3, 8);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics M4', 'Thuesday', '09:00:00', '10:00:00', 9, 4, 9);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics S1', 'Satuday', '09:00:00', '10:00:00', 10, 1, 10);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics S2', 'Thuesday', '09:00:00', '10:00:00', 1, 2, 11);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Physics S3', 'Thursday', '08:00:00', '09:00:00', 2, 3, 12);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math M4', 'Friday', '08:00:00', '09:00:00', 3, 1, 9);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math S1', 'Thursday', '08:00:00', '09:00:00', 4, 2, 10);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math S2', 'Sunday', '08:00:00', '09:00:00', 5, 3, 11);
INSERT INTO `lessons` (`lesson_name`, `date`, `start_time`, `end_time`, `teacher_id`, `class_id`, `grade_id`) VALUES ('Math S3', 'Thursday', '08:00:00', '09:00:00', 6, 4, 12);


