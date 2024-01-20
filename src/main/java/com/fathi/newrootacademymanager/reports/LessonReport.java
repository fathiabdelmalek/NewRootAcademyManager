package com.fathi.newrootacademymanager.reports;

import com.fathi.newrootacademymanager.models.Lesson;
import com.fathi.newrootacademymanager.services.CRUDService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LessonReport {

    public static void main(String[] args) {
        try {
            JasperCompileManager.compileReportToFile(LessonReport.class.getResource("/com/fathi/newrootacademymanager/jasper/Lesson.jrxml").getFile());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(CRUDService.readAll(Lesson.class));
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", "My Report");
            JasperPrint jasperPrint = JasperFillManager.fillReport(LessonReport.class.getResourceAsStream("/com/fathi/newrootacademymanager/jasper/Lesson.jasper"), parameters, dataSource);
            String outputPath = new File(System.getProperty("user.home"), "OneDrive/Desktop/report.pdf").getPath();
            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                outputFile.createNewFile();
            }
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);
            System.out.println("Report generated");
        } catch (JRException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
