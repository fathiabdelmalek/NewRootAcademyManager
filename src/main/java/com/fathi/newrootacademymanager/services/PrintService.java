package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import jakarta.persistence.EntityManager;
import javafx.print.PrinterJob;
import javafx.scene.web.WebView;
import net.sf.jasperreports.engine.*;
import org.hibernate.Session;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PrintService {
    private static final DBCManager dbcm = DBCManager.getInstance();
    private static final TemplateEngine engine = new TemplateEngine();

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

    public static void printTableBoard(Context context, InputStream template) {
        try (InputStream inputStream = template;
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            String htmlContent = scanner.useDelimiter("\\A").next();
            WebView webView = new WebView();
            webView.getEngine().loadContent(engine.process(htmlContent, context));
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            if (printerJob != null) {
                if (printerJob.showPrintDialog(null)) {
                    printerJob.printPage(webView);
                    printerJob.endJob();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
