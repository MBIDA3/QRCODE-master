module com.example.qrcode2 {
    requires javafx.controls;
    requires javafx.fxml;
            
                        requires org.kordamp.bootstrapfx.core;
    requires com.google.zxing;
    requires java.desktop;

    opens com.example.qrcode2 to javafx.fxml;
    exports com.example.qrcode2;
}