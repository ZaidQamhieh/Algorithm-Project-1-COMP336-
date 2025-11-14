package pack.algop1;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.util.Scanner;

public class fileHandler implements EventHandler<ActionEvent> {
    myArrayList<Task> list;

    fileHandler(myArrayList<Task> list) {
        this.list = list;
    }

    @Override
    public void handle(ActionEvent e) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(new Stage());
        if (file == null) return;
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().trim().split(",");
                String name;
                int time;
                int productivity;
                try {
                    name = parts[0];
                    time = Integer.parseInt(parts[1]);
                    productivity = Integer.parseInt(parts[2]);
                } catch (Exception ex) {
                    continue;
                }
                if (name == null || name.isEmpty())
                    continue;

                if (time < 0 || productivity < 0)
                    continue;

                Task task = new Task(name, time, productivity);
                if (list.contains(task))
                    continue;

                list.add(task);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
