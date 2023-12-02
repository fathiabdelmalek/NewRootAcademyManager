SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `lessons`;
DROP TABLE IF EXISTS `roomes`;
DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `incomes`;
DROP TABLE IF EXISTS `expenses`;
DROP TABLE IF EXISTS `attendances`;
DROP TABLE IF EXISTS `lesson_student`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `students` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `first name` VARCHAR(25) NOT NULL,
    `last name` VARCHAR(25) NOT NULL,
    `phone number` VARCHAR(10) NOT NULL,
    `sex` ENUM('Male', 'Female') NOT NULL,
    `birth date` DATE NOT NULL,
    `grade id` INTEGER NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `teachers` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `first name` VARCHAR(25) NOT NULL,
    `last name` VARCHAR(25) NOT NULL,
    `phone number` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `lessons` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `lesson name` VARCHAR(25) NOT NULL,
    `day` ENUM('Saturday', 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday') NOT NULL,
    `start time` TIME NOT NULL,
    `end time` TIME NOT NULL,
    `price` DECIMAL NOT NULL,
    `teacher id` INTEGER NOT NULL,
    `room id` INTEGER NOT NULL,
    `grade id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `roomes` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(5) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `grades` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `level` ENUM('Primary', 'Middle', 'Secondary') NOT NULL,
    `year` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    CHECK (
    	(`level` = 'Primary' AND year BETWEEN 1 AND 5) OR
    	(`level` = 'Middle' AND year BETWEEN 1 AND 4) OR
    	(`level` = 'Secondary' AND year BETWEEN 1 AND 3)
    )
);

CREATE TABLE `incomes` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create time` DATETIME NOT NULL DEFAULT NOW(),
    `update time` DATETIME NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
    `student id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `expenses` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DECIMAL NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create time` DATETIME NOT NULL DEFAULT NOW(),
    `update time` DATETIME NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP,
    `teacher id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `attendances` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `times present` INTEGER NOT NULL,
    `lesson id` INTEGER NOT NULL,
    `student id` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`lesson id`, `student id`)
);

CREATE TABLE `lesson student` (
    `lesson id` INTEGER NOT NULL,
    `student id` INTEGER NOT NULL,
    PRIMARY KEY (`lesson id`, `student id`)
);

ALTER TABLE `students` ADD FOREIGN KEY (`grade id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`teacher id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`room id`) REFERENCES `roomes`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD FOREIGN KEY (`grade id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `incomes` ADD FOREIGN KEY (`student id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `expenses` ADD FOREIGN KEY (`teacher id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD FOREIGN KEY (`lesson id`) REFERENCES `lessons`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD FOREIGN KEY (`student id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lesson_student` ADD FOREIGN KEY (`lesson id`) REFERENCES `lessons`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lesson_student` ADD FOREIGN KEY (`student id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `students` ADD INDEX `idx_grade id` (`grade id`);
ALTER TABLE `lessons` ADD INDEX `idx_teacher id` (`teacher id`);
ALTER TABLE `lessons` ADD INDEX `idx_room id` (`room id`);
ALTER TABLE `lessons` ADD INDEX `idx_grade id` (`grade id`);
ALTER TABLE `incomes` ADD INDEX `idx_student id` (`student id`);
ALTER TABLE `expenses` ADD INDEX `idx_teacher id` (`teacher id`);
ALTER TABLE `attendances` ADD INDEX `idx_lesson id` (`lesson id`);
ALTER TABLE `attendances` ADD INDEX `idx_student id` (`student id`);
ALTER TABLE `lesson_student` ADD INDEX `idx_lesson id` (`lesson id`);
ALTER TABLE `lesson_student` ADD INDEX `idx_student id` (`student id`);

CREATE OR REPLACE VIEW `students view` AS
SELECT `students`.`id`, CONCAT(`students`.`first name`, ' ', `students`.`last name`) AS `student name`, `students`.`phone number`, `students`.`sex`, `students`.`birth date`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `students`
INNER JOIN `grades` ON `grades`.`id` = `students`.`grade id`;

CREATE OR REPLACE VIEW `lessons view` AS
SELECT `lessons`.`id`, `lessons`.`lesson name`, `lessons`.`date`, `lessons`.`start time`, `lessons`.`end time`, CONCAT(`teachers`.`first name`, ' ', `teachers`.`last name`) AS `teacher name`, `roomes`.`code` AS `room code`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade` FROM `lessons`
INNER JOIN `teachers` ON `teachers`.`id` = `lessons`.`teacher id`
INNER JOIN `roomes` ON `roomes`.`id` = `lessons`.`room id`
LEFT JOIN `grades` ON `grades`.`id` = `lessons`.`grade id`;

CREATE OR REPLACE VIEW `incomes view` AS
SELECT `incomes`.`id`, `incomes`.`amount`, `incomes`.`details`, `incomes`.`create time` AS `time`, CONCAT(`students`.`first name`, ' ', `students`.`last name`) AS `student name` FROM `incomes`
LEFT JOIN `students` ON `students`.`id` = `incomes`.`student id`
ORDER BY `create time` DESC;

CREATE OR REPLACE VIEW `expenses view` AS
SELECT `expenses`.`id`, `expenses`.`amount`, `expenses`.`details`, `expenses`.`create time` AS `time`, CONCAT(`teachers`.`first name`, ' ', `teachers`.`last name`) AS `teacher name` FROM `expenses`
LEFT JOIN `teachers` ON `teachers`.`id` = `expenses`.`teacher id`
ORDER BY `create time` DESC;

CREATE OR REPLACE VIEW `attendances view` AS
SELECT `attendances`.`id`, `lessons`.`lesson name`, CONCAT(`students`.`first name`, ' ', `students`.`last name`) AS `student name`, `attendances`.`times present` FROM `attendances`
INNER JOIN `students` ON `students`.`id` = `attendances`.`student id`
INNER JOIN `lessons` ON `lessons`.`id` = `attendances`.`lesson id`;

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

INSERT INTO `roomes` (`code`) VALUES ('CH101');
INSERT INTO `roomes` (`code`) VALUES ('CH102');
INSERT INTO `roomes` (`code`) VALUES ('CH103');
INSERT INTO `roomes` (`code`) VALUES ('CH104');
INSERT INTO `roomes` (`code`) VALUES ('CH105');
INSERT INTO `roomes` (`code`) VALUES ('CH201');
INSERT INTO `roomes` (`code`) VALUES ('CH202');
INSERT INTO `roomes` (`code`) VALUES ('CH203');
INSERT INTO `roomes` (`code`) VALUES ('CH204');
INSERT INTO `roomes` (`code`) VALUES ('CH205');
INSERT INTO `roomes` (`code`) VALUES ('CH206');
INSERT INTO `roomes` (`code`) VALUES ('BT101');
INSERT INTO `roomes` (`code`) VALUES ('BT103');
INSERT INTO `roomes` (`code`) VALUES ('BT104');
INSERT INTO `roomes` (`code`) VALUES ('BT105');

