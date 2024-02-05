package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.hibernate.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PrintService {
    private static final DBCManager dbcm = DBCManager.getInstance();

    private PrintService() {}

    public static void printLessonDetailsReport(int id) {
        try (EntityManager em = dbcm.getFactory().createEntityManager();
            Session session = em.unwrap(Session.class)) {
            session.doWork(connection -> {
                String sql = "SELECT (\"STUDENTS\".\"FIRST_NAME\" || ' ' || \"STUDENTS\".\"LAST_NAME\") AS \"Student Name\", \n" +
                        "\t\"ATTENDANCES\".\"TIMES_PRESENT\" AS \"Times Present\",\n" +
                        "\t\"ATTENDANCES\".\"DUES\" AS \"Dues\",\n" +
                        "\t\"ATTENDANCES\".\"NOTES\" AS \"Notes\",\n" +
                        "\t\"LESSONS\".\"LESSON_NAME\" AS \"Lesson Name\",\n" +
                        "\t\"LESSONS\".\"PRICE\" AS \"Price\",\n" +
                        "\t(\"TEACHERS\".\"FIRST_NAME\" || ' ' || \"TEACHERS\".\"LAST_NAME\") AS \"Teacher Name\",\n" +
                        "\t\"LESSONS\".\"PERCENTAGE\" AS \"Percentage\",\n" +
                        "\t\"LESSONS\".\"TEACHER_DUES\" AS \"Teacher Dues\",\n" +
                        "FROM \"ATTENDANCES\"\n" +
                        "\tINNER JOIN \"STUDENTS\" ON \n" +
                        "\t \"STUDENTS\".\"ID\" = \"ATTENDANCES\".\"STUDENT_ID\"\n" +
                        "\tINNER JOIN \"LESSONS\" ON \n" +
                        "\t \"LESSONS\".\"ID\" = \"ATTENDANCES\".\"LESSON_ID\"\n" +
                        "\tINNER JOIN \"TEACHERS\" ON\n" +
                        "\t \"TEACHERS\".\"ID\" = \"LESSONS\".\"TEACHER_ID\"\n" +
                        "WHERE \"LESSONS\".\"ID\" = " + id;
                try (InputStream inputStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml");
                     InputStream logoStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/images/logo.png")){
                    if (inputStream != null) {
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("logoPath", logoStream);
                        JasperDesign jd = JRXmlLoader.load(inputStream);
                        JRDesignQuery query = new JRDesignQuery();
                        query.setText(sql);
                        jd.setQuery(query);
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
                        JasperPrintManager.printReport(jp, true);
                    } else {
                        LoggingService.error("JRXML resource not found");
                    }
                } catch (JRException | IOException e) {
                    DialogsService.showErrorDialog("Error", e.getMessage());
                }
            });
        } catch (Exception e) {
            DialogsService.showErrorDialog("Error", e.getMessage());
        }
    }
}
