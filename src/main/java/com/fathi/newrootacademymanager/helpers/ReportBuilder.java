package com.fathi.newrootacademymanager.helpers;

import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

public class ReportBuilder {
    private static JasperReport report;
    private static JasperViewer viewer;
    private static JasperPrint print;

    public static void createReport(Connection connection, Map<String, Object> map, InputStream by) {
        try {
            report = (JasperReport) JRLoader.loadObject(by);
            print = JasperFillManager.fillReport(report, map, connection);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    public static void showReport() {
        viewer = new JasperViewer(print);
        viewer.setVisible(true);
    }
}
