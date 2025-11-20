package pack.algop1;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class fileHandler {
    private final UI ui;
    private final myArrayList<Task> list;
    fileHandler(UI ui) {
        this.ui = ui;
        list = ui.getTasks();
    }

    public void readFile() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(new Stage());
        if (file == null) return;
        try {
            Scanner sc = new Scanner(file);
            int capacity = sc.nextInt();
            int totalHours = sc.nextInt();
            if (list.isEmpty())
                list.updateCapacity(capacity);
            else
                list.updateCapacity(capacity + list.size());
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
            ui.setTotalHours(totalHours);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveOnFile() {
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(new Stage());
        if (file == null) return;

        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Save File");
        a.setHeaderText("Are You Sure? This Will Overwrite The Existing File?");
        a.setContentText("Saving To The File Would Cause All Current Data In The File To Be Deleted");

        if (a.showAndWait().get() != ButtonType.OK) return;

        try (FileOutputStream out = new FileOutputStream(file)) {
            StringBuilder sb = new StringBuilder();
            sb.append(list.size()+1).append("\n");
            sb.append(ui.getTotalHours()).append("\n");
            for (int i = 0; i < list.size(); i++) {
                Task t = list.get(i);
                sb.append(t.getName()).append(",")
                        .append(t.getTime()).append(",")
                        .append(t.getProdctivity()).append("\n");
            }
            out.write(sb.toString().getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
