module com.mycompany.infs2605 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.baseEmpty;
    requires javafx.base;
    requires javafx.fxmlEmpty;
    requires javafx.controlsEmpty;
    requires javafx.graphicsEmpty;
    requires javafx.graphics;

    opens com.mycompany.infs2605 to javafx.fxml;
    exports com.mycompany.infs2605;
}
