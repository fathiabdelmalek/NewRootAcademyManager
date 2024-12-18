module com.fathi.newrootacademymanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires java.sql;
    requires com.h2database;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;

    opens com.fathi.newrootacademymanager to javafx.fxml;
    opens com.fathi.newrootacademymanager.models to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.helpers to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.students to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.teachers to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.lessons to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.incomes to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.expenses to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.grades to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.rooms to javafx.fxml, org.hibernate.orm.core;
    opens com.fathi.newrootacademymanager.controllers.others to javafx.fxml, org.hibernate.orm.core;

    exports com.fathi.newrootacademymanager;
    exports com.fathi.newrootacademymanager.models;
    opens com.fathi.newrootacademymanager.actions to javafx.fxml, org.hibernate.orm.core;
}
