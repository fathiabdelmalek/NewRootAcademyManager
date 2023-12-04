module com.fathi.newrootacademymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens com.fathi.newrootacademymanager to javafx.fxml;
    opens com.fathi.newrootacademymanager.models to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.helpers to javafx.fxml, org.hibernate.orm.core;
//    opens com.fathi.newrootacademymanager.actions to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.students to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.teachers to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.lessons to javafx.fxml, org.hibernate.orm.core;
//    opens com.fathi.newrootacademymanager.controllers.incomes to javafx.fxml, org.hibernate.orm.core;
//    opens com.fathi.newrootacademymanager.controllers.expenses to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.grades to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.rooms to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.others to javafx.fxml, org.hibernate.orm.core;

    exports com.fathi.newrootacademymanager;
    exports com.fathi.newrootacademymanager.models;
    exports com.fathi.newrootacademymanager.services;
    exports com.fathi.newrootacademymanager.controllers;
    exports com.fathi.newrootacademymanager.helpers;
    exports com.fathi.newrootacademymanager.helpers.enums;
//    exports com.fathi.newrootacademymanager.actions;
//    exports com.fathi.newrootacademymanager.controllers.students;
//    exports com.fathi.newrootacademymanager.controllers.teachers;
//    exports com.fathi.newrootacademymanager.controllers.incomes;
//    exports com.fathi.newrootacademymanager.controllers.expenses;
//    exports com.fathi.newrootacademymanager.controllers.others;
}