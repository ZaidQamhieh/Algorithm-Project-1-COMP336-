package pack.algop1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Driver extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(new UI(stage).startPage(),500,500));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}