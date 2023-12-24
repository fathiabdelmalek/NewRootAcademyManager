SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `teachers`;
DROP TABLE IF EXISTS `lessons`;
DROP TABLE IF EXISTS `rooms`;
DROP TABLE IF EXISTS `grades`;
DROP TABLE IF EXISTS `incomes`;
DROP TABLE IF EXISTS `expenses`;
DROP TABLE IF EXISTS `attendances`;
DROP TABLE IF EXISTS `activities`;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `students` (
    `id` INTEGER AUTOINCREMENT,
    `first name` VARCHAR(25) NOT NULL,
    `last name` VARCHAR(25) NOT NULL,
    `phone number` VARCHAR(10) NOT NULL,
    `sex` VARCHAR(6) NOT NULL,
    `birth date` DATE NOT NULL,
    `grade id` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    CHECK (`sex` IN ('Male', 'Female'))
);

CREATE TABLE `teachers` (
    `id` INTEGER AUTOINCREMENT,
    `first name` VARCHAR(25) NOT NULL,
    `last name` VARCHAR(25) NOT NULL,
    `phone number` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `lessons` (
    `id` INTEGER AUTOINCREMENT,
    `lesson name` VARCHAR(25) NOT NULL,
    `day` VARCHAR(8) NOT NULL,
    `start time` TIME NOT NULL,
    `end time` TIME NOT NULL,
    `price` DECIMAL(38, 2) NOT NULL,
    `classes number` INTEGER NOT NULL DEFAULT (0),
    `teacher dues` DECIMAL(38, 2) NOT NULL DEFAULT (0.00),
    `teacher id` INTEGER NOT NULL,
    `room id` INTEGER NOT NULL,
    `grade id` INTEGER,
    PRIMARY KEY (`id`),
    CHECK (`day` in ('Saturday', 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday'))
);

CREATE TABLE `rooms` (
    `id` INTEGER AUTOINCREMENT,
    `code` VARCHAR(5) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `grades` (
    `id` INTEGER AUTOINCREMENT,
    `level` VARCHAR(10) NOT NULL,
    `year` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    CHECK (
    	(`level` = 'Primary' AND year BETWEEN 1 AND 5) OR
    	(`level` = 'Middle' AND year BETWEEN 1 AND 4) OR
    	(`level` = 'Secondary' AND year BETWEEN 1 AND 3)
    )
);

CREATE TABLE `incomes` (
    `id` INTEGER AUTOINCREMENT,
    `amount` DECIMAL(38, 2) NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `student id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `expenses` (
    `id` INTEGER AUTOINCREMENT,
    `amount` DECIMAL(38, 2) NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    `create time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `teacher id` INTEGER,
    PRIMARY KEY (`id`)
);

CREATE TABLE `attendances` (
    `id` INTEGER AUTOINCREMENT,
    `times present` INTEGER NOT NULL DEFAULT (0),
    `notes` VARCHAR(300),
    `dues` DECIMAL(38, 2) NOT NULL DEFAULT (0.00),
    `lesson id` INTEGER NOT NULL,
    `student id` INTEGER NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE (`lesson id`, `student id`)
);

CREATE TABLE `activities` (
    `id` INTEGER AUTOINCREMENT,
    `timestamp` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `type` VARCHAR(8) NOT NULL,
    `details` VARCHAR(300) NOT NULL,
    PRIMARY KEY (`id`),
    CHECK (`type` IN ('Add', 'Update', 'Delete', 'Enroll', 'Leave', 'Pay', 'Receive', 'Error'))
);

ALTER TABLE `students` ADD "FOREIGN" KEY (`grade id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD "FOREIGN" KEY (`teacher id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD "FOREIGN" KEY (`room id`) REFERENCES `rooms`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `lessons` ADD "FOREIGN" KEY (`grade id`) REFERENCES `grades`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `incomes` ADD "FOREIGN" KEY (`student id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `expenses` ADD "FOREIGN" KEY (`teacher id`) REFERENCES `teachers`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD "FOREIGN" KEY (`lesson id`) REFERENCES `lessons`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `attendances` ADD "FOREIGN" KEY (`student id`) REFERENCES `students`(`id`) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `students` ADD "INDEX" `idx_grade_id` (`grade id`);
ALTER TABLE `lessons` ADD "INDEX" `idx_teacher_id` (`teacher id`);
ALTER TABLE `lessons` ADD "INDEX" `idx_room_id` (`room id`);
ALTER TABLE `lessons` ADD "INDEX" `idx_grade_id` (`grade id`);
ALTER TABLE `incomes` ADD "INDEX" `idx_student_id` (`student id`);
ALTER TABLE `expenses` ADD "INDEX" `idx_teacher_id` (`teacher id`);
ALTER TABLE `attendances` ADD "INDEX" `idx_lesson_id` (`lesson id`);
ALTER TABLE `attendances` ADD "INDEX" `idx_student_id` (`student id`);

CREATE OR REPLACE VIEW `students view` AS
SELECT `students`.`id`, `students`.`first name`, `students`.`last name`, `students`.`phone number`, `students`.`sex`, `students`.`birth date`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade`
FROM `students`
INNER JOIN `grades` ON `grades`.`id` = `students`.`grade id`;

CREATE OR REPLACE VIEW `lessons view` AS
SELECT `lessons`.`id`, `lessons`.`lesson name`, `lessons`.`price`, `lessons`.`classes number`, `lessons`.`day`, `lessons`.`start time`, `lessons`.`end time`, CONCAT(`teachers`.`first name`, ' ', `teachers`.`last name`) AS `teacher name`, `rooms`.`code` AS `room code`, CONCAT(`grades`.`year`, ' ', `grades`.`level`) AS `grade`, ( SELECT COUNT(*) FROM `attendances` WHERE `attendances`.`lesson id` = `lessons`.`id` ) AS `students number`
FROM `lessons`
INNER JOIN `teachers` ON `teachers`.`id` = `lessons`.`teacher id`
INNER JOIN `rooms` ON `rooms`.`id` = `lessons`.`room id`
LEFT JOIN `grades` ON `grades`.`id` = `lessons`.`grade id`;

CREATE OR REPLACE VIEW `incomes view` AS
SELECT `incomes`.`id`, `incomes`.`amount`, `incomes`.`details`, `incomes`.`create time` AS `time`, CONCAT(`students`.`first name`, ' ', `students`.`last name`) AS `student name`
FROM `incomes`
LEFT JOIN `students` ON `students`.`id` = `incomes`.`student id`
ORDER BY `create time` DESC;

CREATE OR REPLACE VIEW `expenses view` AS
SELECT `expenses`.`id`, `expenses`.`amount`, `expenses`.`details`, `expenses`.`create time` AS `time`, CONCAT(`teachers`.`first name`, ' ', `teachers`.`last name`) AS `teacher name`
FROM `expenses`
LEFT JOIN `teachers` ON `teachers`.`id` = `expenses`.`teacher id`
ORDER BY `create time` DESC;

CREATE OR REPLACE VIEW `attendances view` AS
SELECT `attendances`.`id`, `attendances`.`lesson id`, `attendances`.`student id`, `lessons`.`lesson name`, CONCAT(`students`.`first name`, ' ', `students`.`last name`) AS `student name`, `attendances`.`times present`, `attendances`.`notes`, `attendances`.`dues`
FROM `attendances`
INNER JOIN `students` ON `students`.`id` = `attendances`.`student id`
INNER JOIN `lessons` ON `lessons`.`id` = `attendances`.`lesson id`;

INSERT INTO `grades` (`level`, `year`) VALUES ('Primary', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('Primary', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('Primary', 3);
INSERT INTO `grades` (`level`, `year`) VALUES ('Primary', 4);
INSERT INTO `grades` (`level`, `year`) VALUES ('Primary', 5);
INSERT INTO `grades` (`level`, `year`) VALUES ('Middle', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('Middle', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('Middle', 3);
INSERT INTO `grades` (`level`, `year`) VALUES ('Middle', 4);
INSERT INTO `grades` (`level`, `year`) VALUES ('Secondary', 1);
INSERT INTO `grades` (`level`, `year`) VALUES ('Secondary', 2);
INSERT INTO `grades` (`level`, `year`) VALUES ('Secondary', 3);

INSERT INTO `rooms` (`code`) VALUES ('CH101');
INSERT INTO `rooms` (`code`) VALUES ('CH102');
INSERT INTO `rooms` (`code`) VALUES ('CH103');
INSERT INTO `rooms` (`code`) VALUES ('CH104');
INSERT INTO `rooms` (`code`) VALUES ('CH105');
INSERT INTO `rooms` (`code`) VALUES ('CH201');
INSERT INTO `rooms` (`code`) VALUES ('CH202');
INSERT INTO `rooms` (`code`) VALUES ('CH203');
INSERT INTO `rooms` (`code`) VALUES ('CH204');
INSERT INTO `rooms` (`code`) VALUES ('CH205');
INSERT INTO `rooms` (`code`) VALUES ('CH206');
INSERT INTO `rooms` (`code`) VALUES ('BT101');
INSERT INTO `rooms` (`code`) VALUES ('BT103');
INSERT INTO `rooms` (`code`) VALUES ('BT104');
INSERT INTO `rooms` (`code`) VALUES ('BT105');
