package pack.algop1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class UI {
    private final Scene scene;
    private final myArrayList<Task> tasks = new myArrayList<>();
    private final TableView<Task> tv = new TableView<>();
    private TabPane tb = null;

    public UI(Scene scene) {
        this.scene = scene;
    }

    public Pane startPage() {
        Label l = new Label("Select An Option");
        Button[] b = new Button[]{new Button("Read File"), new Button("Add Task")};
        b[0].setOnAction(e ->
        {
            new fileHandler(tasks).handle(e);
            refreshPane();
        });
        b[1].setOnAction(e ->
        {
            addTask();
            refreshPane();
        });
        VBox vb = new VBox(l, b[0], b[1]);
        vb.getStylesheets().add("style.css");
        return vb;
    }

    private void tb() {
        tb = new TabPane();
        Tab[] t = new Tab[2];
        String[] names = new String[]{"Tasks View", "Solutions View"};
        for (int i = 0; i < 2; i++) {
            t[i] = new Tab(names[i]);
            t[i].setClosable(false);
        }
        tb.getTabs().addAll(t);
        t[0].setContent(tasksTableSetup());
        t[1].setContent(new Solutions(tasks).p());
    }

    private VBox tasksTableSetup() {
        Rectangle rec = new Rectangle(Screen.getPrimary().getVisualBounds().getWidth(), 80);
        Button[] b = new Button[]
                {
                        new Button("Add Task"),
                        new Button("Edit Task"),
                        new Button("Delete Task")
                };
        HBox hb = new HBox(450, b);
        hb.setAlignment(Pos.CENTER);
        StackPane sp = new StackPane(rec, hb);

        b[0].setOnAction(e -> addTask());
        b[1].setOnAction(e -> editTask(tv.getSelectionModel().getSelectedItem()));
        b[2].setOnAction(e -> deleteTask(tv.getSelectionModel().getSelectedItem()));

        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tv, Priority.ALWAYS);

        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        c1.setEditable(false);

        TableColumn<Task, Integer> c2 = new TableColumn<>("Time");
        c2.setCellValueFactory(new PropertyValueFactory<>("time"));
        c2.setEditable(false);

        TableColumn<Task, Integer> c3 = new TableColumn<>("Productivity");
        c3.setCellValueFactory(new PropertyValueFactory<>("prodctivity"));
        c3.setEditable(false);

        tv.getColumns().clear();
        tv.getColumns().addAll(c1, c2, c3);
        VBox vb = new VBox(sp, tv);

        vb.getStylesheets().add("style.css");
        return vb;
    }


    private Alert taskActionsTemplate(String Action, TextField[] tf) {
        GridPane gp = new GridPane();
        String[] labels = {"Task Name:", "Task Time:", "Task Productivity:"};

        for (int i = 0; i < 3; i++) {
            tf[i] = new TextField();
            gp.add(new Label(labels[i]), 0, i);
            gp.add(tf[i], 1, i);
        }
        gp.getStylesheets().add("style.css");
        gp.setVgap(20);
        gp.setHgap(20);
        Alert s = new Alert(Alert.AlertType.CONFIRMATION);
        s.setContentText(null);
        s.setHeaderText(" ");
        s.setGraphic(gp);
        s.setTitle(Action);
        return s;
    }

    private void addTask() {
        TextField[] textFields = new TextField[3];
        Alert a = taskActionsTemplate("Add Task", textFields);
        a.showAndWait().ifPresent(r -> {
            if (r != ButtonType.OK)
                return;
            if (inputValidation(textFields)) {
                Task task = new Task(textFields[0].getText(), Integer.parseInt(textFields[1].getText()), Integer.parseInt(textFields[2].getText()));
                if (tasks.contains(task)) {
                    showError("Duplicate Found", "The Task Already Exists", "Please Make Sure To Add An Non Duplicated Task", Alert.AlertType.WARNING);
                    return;
                }
                tasks.add(task);
                updateTable();
            }
        });
    }

    private void editTask(Task t) {
        TextField[] textFields = new TextField[3];
        Alert a = taskActionsTemplate("Edit Task", textFields);
        if (t == null) {
            showError("Missing Task", "Try Again", "Please Select A Task From The Table", Alert.AlertType.WARNING);
            return;
        }
        textFields[0].setText(t.getName());
        textFields[1].setText(t.getTime() + "");
        textFields[2].setText(t.getProdctivity() + "");
        a.showAndWait().ifPresent(r -> {
            if (r != ButtonType.OK)
                return;
            if (inputValidation(textFields)) {
                String name = textFields[0].getText().trim();
                int time = Integer.parseInt(textFields[1].getText().trim());
                int prodctivity = Integer.parseInt(textFields[2].getText().trim());
                if (tasks.contains(new Task(name, time, prodctivity))) {
                    showError("Duplicate Found", "The Task Already Exists", "The Edits Would Cause A Duplicate Task To Be Created", Alert.AlertType.WARNING);
                    return;
                }
                t.setName(name);
                t.setTime(time);
                t.setProdctivity(prodctivity);
                updateTable();
            }
        });
    }

    private void deleteTask(Task task) {
        if (task != null) {
            tasks.remove(task);
            updateTable();
        } else
            showError("Missing Task", "Try Again", "Please Select A Task From The Table", Alert.AlertType.WARNING);
    }

    private boolean inputValidation(TextField[] tf) {
        String[] field = {"Task Name", "Task Time", "Task Productivity"};
        for (int i = 0; i < 3; i++) {
            if (tf[i].getText().isEmpty()) {
                showError("Empty Field", "Please Fill The Fields", "The " + field[i] + " Field Shouldn't Be Empty!", Alert.AlertType.WARNING);
                return false;
            }
        }
        String name;
        int time, productivity;
        try {
            name = tf[0].getText().trim();
            time = Integer.parseInt(tf[1].getText().trim());
            productivity = Integer.parseInt(tf[2].getText().trim());
        } catch (Exception e) {
            showError("Invalid Format", "Try Again", "Invalid Input Format\nUse Only Integers In The Time And Productivity Fields", Alert.AlertType.ERROR);
            return false;
        }
        if (name.isEmpty()) {
            showError("Invalid Name", "Try Again", "Invalid Task Name!\nThe Name Shouldn't Be Empty", Alert.AlertType.ERROR);
            return false;
        } else if (time < 0) {
            showError("Invalid Time", "Try Again", "Invalid Task Time!\nThe Time Shouldn't Be Negative", Alert.AlertType.ERROR);
            return false;
        } else if (productivity < 0) {
            showError("Invalid Productivity", "Try Again", "Invalid Task Productivity!\nThe Productivity Shouldn't Be Negative", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void refreshPane() {
        if (!tasks.isEmpty()) {
            if (tb == null) {
                tb();
            }
            scene.setRoot(tb);
            updateTable();
        } else
            scene.setRoot(startPage());
    }

    private void updateTable() {
        tv.getItems().clear();
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : tasks) {
            data.add(task);
        }
        tv.setItems(data);
    }

    private void showError(String title, String header, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.setContentText(msg);
        a.setTitle(title);
        a.showAndWait();
    }
}
