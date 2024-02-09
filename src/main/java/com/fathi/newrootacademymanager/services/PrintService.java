package com.fathi.newrootacademymanager.services;

import com.fathi.newrootacademymanager.helpers.DBCManager;
import jakarta.persistence.EntityManager;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import net.sf.jasperreports.engine.*;
import org.hibernate.Session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class PrintService {
    private static PrintService instance;
    private static final DBCManager dbcm = DBCManager.getInstance();
    private static PrinterJob printerJob;

    private PrintService() {
        printerJob = PrinterJob.createPrinterJob();
    }

    public static PrintService getInstance() {
        if (instance == null) instance = new PrintService();
        return instance;
    }

    public static void printLessonDetailsReport(int id) {
        try (EntityManager em = dbcm.getFactory().createEntityManager();
            Session session = em.unwrap(Session.class)) {
            session.doWork(connection -> {
                try (InputStream inputStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/LessonDetails.jrxml");
                     InputStream logoStream = PrintService.class.getResourceAsStream("/com/fathi/newrootacademymanager/images/logo.png")) {
                    if (inputStream != null) {
                        Map<String, Object> parameters = new HashMap<>();
                        System.out.println(logoStream.read());
                        parameters.put("id", id);
//                        parameters.put("logoPath", logoStream);
                        JasperReport jr = JasperCompileManager.compileReport(inputStream);
                        JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
                        JasperPrintManager.printReport(jp, true);
                    } else {
                        DialogsService.showErrorDialog("Error", "JRXML resource not found");
                        LoggingService.error("JRXML resource not found");
                    }
                } catch (JRException | IOException e) {
                    DialogsService.showErrorDialog("Error", e.getMessage());
                    LoggingService.error(e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            DialogsService.showErrorDialog("Error", e.getMessage());
            LoggingService.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printTableBoard(Node node) {
        WritableImage snapshot = node.snapshot(new SnapshotParameters(), null);
        ImageView imageView = new ImageView(snapshot);
        imageView.setFitWidth(Paper.A4.getHeight());
        imageView.setFitHeight(Paper.A4.getWidth());
        if (printerJob != null) {
            PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
            printerJob.getJobSettings().setPageLayout(pageLayout);
            printerJob.printPage(pageLayout, imageView);
            printerJob.endJob();
        }
    }
}
