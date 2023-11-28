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
    `time` DATETIME NOT NULL,
    `student_id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `expenses` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `time` DATETIME NOT NULL,
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
SELECT CONCAT(`students`.`first_name`, `students`.`last_name`) AS `student_name`, `students`.`phone_number`, `students`.`sex`, `students`.`birth_date`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `students`
INNER JOIN `grades` ON `grades`.`id` = `students`.`grade_id`;

CREATE OR REPLACE VIEW `lessons_view` AS
SELECT `lessons`.`lesson_name`, `lessons`.`date`, `lessons`.`start_time`, `lessons`.`end_time`, CONCAT(`teachers`.`first_name`, ' ', `teachers`.`last_name`) AS `teacher_name`, `classes`.`code`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `lessons`
INNER JOIN `teachers` ON `teachers`.`id` = `lessons`.`teacher_id`
INNER JOIN `classes` ON `classes`.`id` = `lessons`.`class_id`
INNER JOIN `grades` ON `grades`.`id` = `lessons`.`grade_id`;

CREATE OR REPLACE VIEW `incomes_view` AS
SELECT `incomes`.`amount`, `incomes`.`details`, `incomes`.`time`, CONCAT(`students`.`first_name`, ' ', `students`.`last_name`) AS `student_name` FROM `incomes`
INNER JOIN `students` ON `students`.`id` = `incomes`.`student_id`;

CREATE OR REPLACE VIEW `expenses_view` AS
SELECT `expenses`.`amount`, `expenses`.`details`, `expenses`.`time`, CONCAT(`teachers`.`first_name`, ' ', `teachers`.`last_name`) AS `teacher_name` FROM `expenses`
INNER JOIN `teachers` ON `teachers`.`id` = `expenses`.`teacher_id`;

CREATE OR REPLACE VIEW `attendances_view` AS
SELECT CONCAT(`students`.`first_name`, ' ', `students`.`last_name`) AS `student_name`, `lessons`.`lesson_name`, `attendances`.`times_present` FROM `attendances`
INNER JOIN `students` ON `students`.`id` = `attendances`.`student_id`
INNER JOIN `lessons` ON `lessons`.`id` = `attendances`.`lesson_id`;

