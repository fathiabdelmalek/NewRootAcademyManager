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
                try (InputStream inputStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml");
                     InputStream logoStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/images/logo.png")){
                    if (inputStream != null) {
                        Map<String, Object> parameters = new HashMap<>();
                        parameters.put("id", id);
                        parameters.put("logoPath", logoStream);
                        JasperReport jr = JasperCompileManager.compileReport(inputStream);
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
