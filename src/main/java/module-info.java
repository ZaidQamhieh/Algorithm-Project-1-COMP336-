module pack.algop1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens pack.algop1 to javafx.fxml;
    exports pack.algop1;
}