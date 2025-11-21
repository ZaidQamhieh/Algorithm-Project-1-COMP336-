package pack.algop1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class Driver extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Pane());
        UI ui = new UI();
        scene.setRoot(ui.p());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("An Intelligent Daily Task Scheduling System");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}