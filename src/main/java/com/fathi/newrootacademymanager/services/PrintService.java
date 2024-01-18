package com.fathi.newrootacademymanager.services;

import javafx.print.*;
import javafx.scene.web.WebView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PrintService {
    public static void printContent(Context context, InputStream template) {
        TemplateEngine templateEngine = new TemplateEngine();
        try (InputStream inputStream = template) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
                    String htmlContent = scanner.useDelimiter("\\A").next();
                    String processedHtml = templateEngine.process(htmlContent, context);
                    WebView webView = new WebView();
                    webView.getEngine().loadContent(processedHtml);
                    PrinterJob printerJob = PrinterJob.createPrinterJob();
                    if (printerJob != null) {
                        if (printerJob.showPrintDialog(null)) {
                            printerJob.printPage(webView);
                            printerJob.endJob();
                        }
                    }
                }
            } else {
                throw new RuntimeException("Template file not found: printing-template.html");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
