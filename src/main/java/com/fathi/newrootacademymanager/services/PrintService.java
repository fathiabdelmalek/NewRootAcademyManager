package com.fathi.newrootacademymanager.services;

import javafx.print.*;
import javafx.scene.Node;

public class PrintService {
    public static void printContent(Node node) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            if (printerJob.showPrintDialog(null)) {
                printerJob.printPage(node);
                printerJob.endJob();
            }
        }
    }
}
