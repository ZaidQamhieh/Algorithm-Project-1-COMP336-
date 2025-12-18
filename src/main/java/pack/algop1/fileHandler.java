package pack.algop1;

import javafx.scene.control.Alert;
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
        // Opens a Window to Choose The Wanted File From
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(new Stage());
        if (file == null) return;
        try {
            Scanner sc = new Scanner(file);

            int capacity = sc.nextInt();
            if (capacity <= 0) {
                new Alert(Alert.AlertType.INFORMATION, "Capacity Is 0 Or Less\nNo Tasks To Be Read").showAndWait();
                return;
            }

            float totalHours;
            if (!sc.hasNextFloat()) {
                new Alert(Alert.AlertType.ERROR, "File Missing Total Time").showAndWait();
                return;
            }

            totalHours = sc.nextFloat();
            if (totalHours < 0) {
                new Alert(Alert.AlertType.WARNING, "Total Time is Negative\nTotal Time Will Be Set to 0 ").showAndWait();
                return;
            }
            if (list.isEmpty())
                list.updateCapacity(capacity);
            else
                // If List Wasn't Empty It Will Get Old Size + new Capacity
                list.updateCapacity(capacity + list.size());
            while (sc.hasNextLine()) {
                String[] parts = sc.nextLine().trim().split(",");
                String name;
                float time;
                int productivity;
                // Input Validations
                try {
                    name = parts[0];
                    time = Float.parseFloat(parts[1]);
                    productivity = Integer.parseInt(parts[2]);
                } catch (Exception ex) {
                    continue;
                }
                if (name == null || name.isEmpty())
                    continue;

                if (time < 0 || productivity < 0)
                    continue;

                Task task = new Task(name, time, productivity);
                // Duplicates Check
                if (list.contains(task))
                    continue;

                list.add(task);
            }
            // Updates The Textfield and Value of The Solutions UI Total Hours
            ui.setTotalHours(totalHours);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveOnFile() {
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(new Stage());
        if (file == null) return;

        try (FileOutputStream out = new FileOutputStream(file)) {
            StringBuilder sb = new StringBuilder();
            sb.append(list.size() + 1).append("\n");
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
