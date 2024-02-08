package com.fathi.newrootacademymanager.services;

import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TableBoardPrinter {

    public static void print(Node node) {
        WebView webView = new WebView();
        System.out.println("=========================================================================================");
        System.out.println(node.getScene().getWindow().getX());
        System.out.println(node.getScene().getWindow().getY());
        System.out.println("=========================================================================================");
        webView.getEngine().loadContent(nodeToHtml(node));
        if (webView.getScene() != null) {
            webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    printWebView(webView);
                }
            });
        } else {
            System.out.println("WebView's scene is null");
        }
    }

    private static String nodeToHtml(Node node) {
        return "<!DOCTYPE html><html><head><style>"
                + "body { font-family: Arial, sans-serif; }"
                + "/* Add your CSS styles here */"
                + "</style></head><body>" + nodeToString(node) + "</body></html>";
    }

    private static String nodeToString(Node node) {
        WritableImage image = node.snapshot(new SnapshotParameters(), null);
        File file = new File("temp.png");
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<img src=\"" + file.toURI() + "\">";
    }

    private static void printWebView(WebView webView) {
        Stage stage = new Stage();
        Scene scene = new Scene(webView);
        stage.setScene(scene);
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean printed = job.showPrintDialog(webView.getScene().getWindow());
            if (printed) {
                webView.getEngine().print(job);
                job.endJob();
            }
            stage.close();
        }
    }
}
