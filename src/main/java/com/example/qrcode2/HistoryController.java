// HistoryController.java
package com.example.qrcode2;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import com.example.qrcode2.QRCODEController.QRcodeEntry;

import java.util.List;

public class HistoryController {

    @FXML
    private TableView<QRcodeEntry> historyTableView;
    @FXML
    private TableColumn<QRcodeEntry, String> urlColumn;
    @FXML
    private TableColumn<QRcodeEntry, String> dateTimeColumn;

    @FXML
    public void initialize(List<String> historyData) {
        // Configurer les cellules de la table
        urlColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        dateTimeColumn.setCellValueFactory(cellData -> cellData.getValue().dateTimeProperty());

        // Assigner les données d'historique à la table
        historyTableView.setItems(QRCODEController.getQrCodeHistory());
    }
}
