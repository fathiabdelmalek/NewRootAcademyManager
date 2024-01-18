package com.fathi.newrootacademymanager.services;

import javafx.print.PrinterJob;
import javafx.scene.web.WebView;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PrintService {
    private static final TemplateEngine engine = new TemplateEngine();

    private PrintService() {}

    public static void printContent(Context context, InputStream template) {
        try (InputStream inputStream = template) {
            if (inputStream != null) {
                try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8)) {
                    String htmlContent = scanner.useDelimiter("\\A").next();
                    String processedHtml = engine.process(htmlContent, context);
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
                throw new RuntimeException("Template file not found");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
