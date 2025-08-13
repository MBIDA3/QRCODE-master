package com.example.qrcode2;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class QRCODEController {




    @FXML
    private Stage  stage;
    public void setStage(Stage stage){

        this.stage = stage;
    }
    @FXML
    private void oneNextPage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("qrcodepage.fxml"));
            Parent secondPage = fxmlLoader.load();
            Scene scene = new Scene(secondPage);
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle("QRCODE GENE");
            newStage.show();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onExit(){
        Stage currentStage = (Stage) stage.getScene().getWindow();
        currentStage.close();
    }
    public static class QRcodeEntry {
        private String url;
        private Date creationTime;

        public QRcodeEntry(String url, Date creationTime) {
            this.url = url;
            this.creationTime = creationTime;
        }

        public String getUrl() {
            return url;
        }

        public Date getCreationTime() {
            return creationTime;
        }

        public StringProperty urlProperty() {
            return new SimpleStringProperty(url);
        }

        public StringProperty dateTimeProperty() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(creationTime);
            return new SimpleStringProperty(formattedDate);
        }
    }

    private static ObservableList<QRcodeEntry> qrCodeHistory = FXCollections.observableArrayList();

    public static ObservableList<QRcodeEntry> getQrCodeHistory() {
        return qrCodeHistory;
    }
}