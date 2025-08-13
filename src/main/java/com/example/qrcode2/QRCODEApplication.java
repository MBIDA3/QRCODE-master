package com.example.qrcode2;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.QRCodeReader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.example.qrcode2.QRCODEController.QRcodeEntry;

import javax.imageio.ImageIO;
import java.awt.TextArea;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Hashtable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.net.URL;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;


public class QRCODEApplication implements Initializable {

    @FXML
    private TextField urlTextField;
    @FXML
    private TextField urlTextField2;
    @FXML
    private VBox qrCode;
    @FXML
    private Label helptext;
    @FXML
    private Button saveButton;
    @FXML
    private Button historiqueButton;
    private List<QRCODEController.QRcodeEntry> qrcodehistory;
    private  List<QRcodeEntry> qrCodeHistory = new ArrayList<>();
    @FXML
    private Button customizeButton;

    @FXML
    private ColorPicker backgroundColorPicker;
    private Color backgroundColor;
    private Color qrCodeColor;


    @FXML
    private void GeneratedButton(ActionEvent event) {
        String url = urlTextField.getText();
        if (qrcodehistory == null) {
            qrcodehistory = new ArrayList<QRCODEController.QRcodeEntry>();
        }

        // Vérifier si l'URL est vide
        if (url.isEmpty()) {
            // Afficher une erreur
            showPopup2("Veuillez saisir une URL avant de générer le QR Code.", 500, 200);
            return; // Ne continuez pas l'exécution de la méthode si l'URL est vide
        }

        // Si l'URL n'est pas vide, continuer avec la génération du QR Code
        Image qrCodeImage = generateQRCodeImage2(url, qrCodeColor, backgroundColor);
        if (qrCodeImage != null) {
            ImageView qrCodeImageView = new ImageView(qrCodeImage);
            qrCode.getChildren().clear();
            qrCode.getChildren().add(qrCodeImageView);
            // Spécifier la position (x, y) où vous voulez que le popup apparaisse
            double popupX = 680;
            double popupY = 150;

            // Afficher le popup avec le message à la position spécifiée
            showPopup("QR Code généré avec succès!", popupX, popupY);

            // Ajouter l'entrée à l'historique
            QRCODEController.QRcodeEntry entry = new QRCODEController.QRcodeEntry(url, new Date());
            qrcodehistory.add(entry);

            // Afficher l'historique dans la console (à des fins de test)
            System.out.println("Historique des QR code :");
            for (QRCODEController.QRcodeEntry qRcodeEntry : qrcodehistory) {
                System.out.println("URL : " + qRcodeEntry.getUrl());
                System.out.println("Date & heure de création : " + qRcodeEntry.getCreationTime());
                System.out.println();
            }
        }
    }


    private Image generateQRCodeImage(String url, Color qrCodeColor,Color backgroundColor) {
        final int QRCODE_SIZE = 250;
        final int MARGIN = 10;
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    url,
                    BarcodeFormat.QR_CODE,
                    QRCODE_SIZE,
                    QRCODE_SIZE,
                    hints
            );

            BufferedImage bufferedImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE, BufferedImage.TYPE_INT_ARGB);

            for (int x = 0; x < QRCODE_SIZE; x++) {
                for (int y = 0; y < QRCODE_SIZE; y++) {
                    int rgbColor = bitMatrix.get(x, y) ? convertColorToRGB(qrCodeColor): convertColorToRGB(backgroundColor);
                    bufferedImage.setRGB(x, y, rgbColor);
                }
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return new Image(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Image generateQRCodeImage2 (String url, Color qrCodeColor,Color backgroundColor) {
        final int QRCODE_SIZE = 250;
        final int MARGIN = 10;

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedImage bufferedImage = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE, BufferedImage.TYPE_INT_ARGB);

            for (int x = 0; x < QRCODE_SIZE; x++) {
                for (int y = 0; y < QRCODE_SIZE; y++) {
                    int rgbColor = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                    bufferedImage.setRGB(x, y, rgbColor);
                }
            }
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    private int convertColorToRGB(Color color){
        int red = (int) (color.getRed() * 255);
        int green = (int) (color.getGreen() * 255);
        int blue = (int) (color.getBlue() * 255);
        int alpha = (int) (color.getOpacity() * 255);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    public static class QRCodeDecoder {

        public static String decodeQRCode(String filePath) {
            try {
                BufferedImage image = ImageIO.read(new File(filePath));
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Result result = new QRCodeReader().decode(bitmap);
                return result.getText();
            } catch (IOException | NotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (ChecksumException e) {
                throw new RuntimeException(e);
            } catch (FormatException e) {
                throw new RuntimeException(e);
            }
        }



        private static class BufferedImageLuminanceSource extends LuminanceSource {
            private final BufferedImage image;
            private final int[] pixels;

            BufferedImageLuminanceSource(BufferedImage image) {
                super(image.getWidth(), image.getHeight());
                this.image = image;
                this.pixels = new int[image.getWidth() * image.getHeight()];
            }

            @Override
            public byte[] getRow(int y, byte[] row) {
                int width = getWidth();
                if (row == null || row.length < width) {
                    row = new byte[width];
                }

                image.getRGB(0, y, width, 1, pixels, 0, width);
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[x];
                    int luminance = (int) ((0.2126 * ((pixel >> 16) & 0xFF)) +
                            (0.7152 * ((pixel >> 8) & 0xFF)) +
                            (0.0722 * (pixel & 0xFF)));
                    row[x] = (byte) luminance;
                }

                return row;
            }

            @Override
            public byte[] getMatrix() {
                int width = getWidth();
                int height = getHeight();
                byte[] matrix = new byte[width * height];
                image.getRGB(0, 0, width, height, pixels, 0, width);
                for (int y = 0; y < height; y++) {
                    int offset = y * width;
                    for (int x = 0; x < width; x++) {
                        int pixel = pixels[offset + x];
                        int luminance = (int) ((0.2126 * ((pixel >> 16) & 0xFF)) +
                                (0.7152 * ((pixel >> 8) & 0xFF)) +
                                (0.0722 * (pixel & 0xFF)));
                        matrix[offset + x] = (byte) luminance;
                    }
                }
                return matrix;
            }
        }
    }
    @FXML
    private void decodeQRCode(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png"));
        File file = fileChooser.showOpenDialog(null);
        String url = null;
        if (file != null) {
            String qrCodeImagePath = file.getAbsolutePath();
            url = QRCodeDecoder.decodeQRCode(qrCodeImagePath);
            if (url != null) {
                // Faites quelque chose avec l'URL extraite, par exemple, l'afficher dans un label
                urlTextField2.setText(url);
            } else {
                // Gestion des erreurs si le QR code ne peut pas être décodé
                urlTextField2.setText("");
                System.out.println("Impossible de décoder le QR code.");
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveButton.setOnAction(this:: saveQrCode);

    }
    @FXML
    protected void onHelp(){
        Stage helpStage = new Stage();
        VBox helpLayout = new VBox();
        helpLayout.getStyleClass().add("help-vbox");
        TextArea helpText = new TextArea();
        helpText.setEditable(false);
        helptext.setText("Bienvenu sur notre Générateur de QR CODE ! Veuillez saisir une information \n" +
                "dans la barre de texte (URL,Numéro de téléphone,...) \n" +
                "puis appuiez sur le bouton Générer afin d'avoir le QR CODE correspondant \n" +
                ". Appuiez le bouton Importer pour converitr une image QR CODE en url \n" +
                "le bouton Enregistrer pour sauvegarder votre QRCODE sous forme d'image sur votre PC.\n"+
                "Vous diposez égamelement d'un bouton Historique pour voir les qr codes génér et du bouton \n" +
                "personnaliser pour changer la couleur des qr codes. ");

        helpLayout.getChildren().add(helptext);
        Scene helpScene = new Scene(helpLayout,550, 150);
        helpStage.setScene(helpScene);
        helpScene.getStylesheets().add(getClass().getResource("fenetre.css").toExternalForm());
        helpStage.setTitle("Aide - Générateur de QR Code");
        helpStage.show();
    }


    @FXML
    public void saveQrCode(ActionEvent event) {
        Image qrCodeImage = qrCode.getChildren().get(0).snapshot(null, null);
        if (qrCodeImage != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png"));
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                try {
                    BufferedImage bufferedImage = SwingFXUtils.fromFXImage(qrCodeImage, null);
                    ImageIO.write(bufferedImage, "png", file);
                    showAlert("Sauvegarde réusie","l'image QR code a été enregistrée avec succès.");
                    System.out.println("QR code saved successfully.");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static class QRcodeEntry{
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
    }

    @FXML
    private void customizeButton(ActionEvent event){
        ColorPicker QRCodeColorPicker = new ColorPicker();
        QRCodeColorPicker.setValue(qrCodeColor);
        ColorPicker backgroundColorPicker = new ColorPicker();
        backgroundColorPicker.setValue(backgroundColor);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Personnaliser votre QR Code");
        dialog.setHeaderText("Choisissez les couleurs du Qr Code et du fond");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.add(new Label("Couleur du QR Code:"), 0, 0);
        grid.add(QRCodeColorPicker, 1, 0);
        grid.add(new Label("Couleur de fond:"), 0, 1);
        grid.add(backgroundColorPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);
        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            qrCodeColor = QRCodeColorPicker.getValue();
            backgroundColor = backgroundColorPicker.getValue();
            generateAndDisplayQRCode();
            double popupX = 680;
            double popupY = 360;
            showPopup("QR Code personnalisé !", popupX, popupY);

            String url = urlTextField.getText();
            Image qrCodeImage = generateQRCodeImage(url, qrCodeColor, backgroundColor);
            if(qrCodeImage != null){
                ImageView qrCodeImageView = new ImageView(qrCodeImage);
                qrCode.getChildren().clear();
                qrCode.getChildren().add(qrCodeImageView);
            }

        }
    }
    private void generateAndDisplayQRCode() {
        String url = urlTextField.getText();
        Image qrCodeImage = generateQRCodeImage(url, qrCodeColor, backgroundColor);
        if (qrCodeImage != null) {
            ImageView qrCodeImageView = new ImageView(qrCodeImage);
            qrCode.getChildren().clear();
            qrCode.getChildren().add(qrCodeImageView);
        }
    }
    private void showPopup(String message, double x,double y) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: #336633; -fx-text-fill: #ECF0F1; -fx-padding: 10px;");
        popup.getContent().add(label);
        // Afficher le popup
        popup.show(qrCode.getScene().getWindow(), x, y);
        // Fermer le popup après 3 secondes
        Duration duration = Duration.seconds(4);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> popup.hide()));
        timeline.play();
    }
    private void showPopup2 (String message, double x,double y) {
        Popup popup = new Popup();
        Label label = new Label(message);
        label.setStyle("-fx-background-color: #950606; -fx-text-fill: #ECF0F1; -fx-padding: 10px;");
        popup.getContent().add(label);
        // Afficher le popup
        popup.show(qrCode.getScene().getWindow(), x, y);
        // Fermer le popup après 3 secondes
        Duration duration = Duration.seconds(4);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> popup.hide()));
        timeline.play();
    }
    @FXML
    private void onHistoryButton(ActionEvent event) {
        try {
            // Charger la vue de l'historique depuis le fichier FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("history-view.fxml"));
            Parent historyPage = fxmlLoader.load();

            // Créer un nouveau stage pour la vue de l'historique
            Stage historyStage = new Stage();
            historyStage.setScene(new Scene(historyPage));

            // Récupérer le contrôleur de la vue de l'historique
            HistoryController historyController = fxmlLoader.getController();

            // Charger et initialiser l'historique (ajuster cela en fonction de votre modèle d'historique)
            List<String> historyData = loadHistoryData(); // Remplacez par votre méthode pour charger l'historique
            historyController.initialize(historyData);

            // Afficher la vue de l'historique
            historyStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode fictive pour charger des données d'historique (adapter selon votre modèle)
    private List<String> loadHistoryData() {
        // ... (chargement des données d'historique depuis votre modèle)
        return List.of("QR Code 1", "QR Code 2", "QR Code 3"); // Exemple, remplacez avec vos données réelles
    }


}