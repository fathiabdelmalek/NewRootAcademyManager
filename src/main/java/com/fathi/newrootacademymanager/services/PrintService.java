package com.fathi.newrootacademymanager.services;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
