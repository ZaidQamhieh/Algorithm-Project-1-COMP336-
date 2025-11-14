module pack.algop1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.xml.dom;


    opens pack.algop1 to javafx.fxml;
    exports pack.algop1;
}