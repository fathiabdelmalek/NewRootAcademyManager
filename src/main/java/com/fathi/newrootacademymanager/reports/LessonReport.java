package com.fathi.newrootacademymanager.reports;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.services.CRUDService;
import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LessonReport {

    public static void main(String[] args) {
        DBCManager dbcm = DBCManager.getInstance();
        try (EntityManager em = dbcm.getFactory().createEntityManager()) {
            Session session = em.unwrap(Session.class);
            session.doWork(connection -> {
                try {
                    String path = LessonReport.class.getResource("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml").getFile();

//                    JasperCompileManager.compileReportToFile(LessonReport.class.getResource("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml").getFile());
//                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(CRUDService.readAll(Lesson.class));
//                    Map<String, Object> parameters = new HashMap<>();
//                    parameters.put("title", "My Report");
//                    JasperPrint jasperPrint = JasperFillManager.fillReport(LessonReport.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/LessonDetails.jasper"), parameters, dataSource);
//                    String outputPath = new File(System.getProperty("user.home"), "OneDrive/Desktop/report.pdf").getPath();
//                    File outputFile = new File(outputPath);
//                    if (!outputFile.exists()) {
//                        if (!outputFile.getParentFile().exists()) {
//                            outputFile.getParentFile().mkdirs();
//                        }
//                        outputFile.createNewFile();
//                    }
//                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
//                    System.out.println("Report generated");


//                    JasperReport jasperReport = JasperCompileManager.compileReport(LessonReport.class.getResource("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml").getFile());
//                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(CRUDService.readAll(Lesson.class));
//                    Map<String, Object> parameters = new HashMap<>();
//                    parameters.put("title", "My Report");
//                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
//                    JasperViewer.viewReport(jasperPrint);

//                    JasperPrint jasperPrint = JasperFillManager.fillReport(LessonReport.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/LessonDetails.jasper"), null, connection);
//                    JasperViewer.viewReport(jasperPrint);


                    JasperReport jr = JasperCompileManager.compileReport(path);
                    JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);
                    JasperViewer.viewReport(jp, false);


//                    JasperDesign jd = JRXmlLoader.load(path);
//                    String sql = "SELECT (\"STUDENTS\".\"FIRST_NAME\" || ' ' || \"STUDENTS\".\"LAST_NAME\") AS \"Student Name\", \n" +
//                            "\t\"ATTENDANCES\".\"TIMES_PRESENT\" AS \"Times Present\",\n" +
//                            "\t\"ATTENDANCES\".\"DUES\" AS \"Dues\",\n" +
//                            "\t\"ATTENDANCES\".\"NOTES\" AS \"Notes\",\n" +
//                            "\t\"LESSONS\".\"LESSON_NAME\" AS \"Lesson Name\",\n" +
//                            "\t\"LESSONS\".\"PRICE\" AS \"Price\",\n" +
//                            "\t(\"TEACHERS\".\"FIRST_NAME\" || ' ' || \"TEACHERS\".\"LAST_NAME\") AS \"Teacher Name\",\n" +
//                            "\t\"LESSONS\".\"PERCENTAGE\" AS \"Percentage\",\n" +
//                            "\t\"LESSONS\".\"TEACHER_DUES\" AS \"Teacher Dues\",\n" +
//                            "FROM \"ATTENDANCES\"\n" +
//                            "\tINNER JOIN \"STUDENTS\" ON \n" +
//                            "\t \"STUDENTS\".\"ID\" = \"ATTENDANCES\".\"STUDENT_ID\"\n" +
//                            "\tINNER JOIN \"LESSONS\" ON \n" +
//                            "\t \"LESSONS\".\"ID\" = \"ATTENDANCES\".\"LESSON_ID\"\n" +
//                            "\tINNER JOIN \"TEACHERS\" ON\n" +
//                            "\t \"TEACHERS\".\"ID\" = \"LESSONS\".\"TEACHER_ID\"\n" +
//                            "WHERE \"LESSONS\".\"ID\" = '" + 1 + "'";
//                    JRDesignQuery query = new JRDesignQuery();
//                    query.setText(sql);
//                    jd.setQuery(query);
//                    JasperReport jr = JasperCompileManager.compileReport(jd);
//                    JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);
//                    JasperViewer.viewReport(jp);
                } catch (JRException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
