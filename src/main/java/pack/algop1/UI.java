package pack.algop1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;

public class UI {
    private final TableView<Task> tv = new TableView<>();
    private myArrayList<Task> tasks = new myArrayList<>(5);

    public TabPane p() {
        TabPane tb = new TabPane();
        Tab[] t = new Tab[2];
        String[] names = new String[]{"Tasks View", "Solutions View"};

        for (int i = 0; i < 2; i++) {
            t[i] = new Tab(names[i]);
            t[i].setClosable(false);
        }

        tb.getTabs().addAll(t);
        t[0].setContent(tasksTableSetup());
        t[1].setContent(new Solutions(tasks).p());
        return tb;
    }

    private VBox tasksTableSetup() {

        Button readFileBtn = new Button("Read File");
        Button saveFileBtn = new Button("Save On File");

        HBox hb1 = new HBox(40, readFileBtn, saveFileBtn);
        hb1.setPadding(new Insets(0, 0, 0, 20));
        hb1.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Task");
        Button editBtn = new Button("Edit Task");
        Button deleteBtn = new Button("Delete Task");

        HBox hb2 = new HBox(40, addBtn, editBtn, deleteBtn);
        hb2.setPadding(new Insets(0, 20, 0, 0));
        hb2.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(hb1);
        topBar.setRight(hb2);
        topBar.setPrefHeight(80);
        topBar.setStyle("-fx-background-color: rgb(223,205,255);");

        readFileBtn.setOnAction(e -> {
            new fileHandler(tasks).readFile();
            updateTable();
        });

        saveFileBtn.setOnAction(e ->new  fileHandler(tasks).saveOnFile());

        addBtn.setOnAction(e -> addTask());
        editBtn.setOnAction(e -> editTask(tv.getSelectionModel().getSelectedItem()));
        deleteBtn.setOnAction(e -> deleteTask(tv.getSelectionModel().getSelectedItem()));

        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tv, Priority.ALWAYS);

        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, Integer> c2 = new TableColumn<>("Time");
        c2.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Task, Integer> c3 = new TableColumn<>("Productivity");
        c3.setCellValueFactory(new PropertyValueFactory<>("prodctivity"));

        tv.getColumns().setAll(c1, c2, c3);

        VBox vb = new VBox(topBar, tv);
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
                Task task = new Task(
                        textFields[0].getText(),
                        Integer.parseInt(textFields[1].getText()),
                        Integer.parseInt(textFields[2].getText())
                );

                if (tasks.contains(task)) {
                    showError("Duplicate Found", "The Task Already Exists",
                            "Please Make Sure To Add A Non-Duplicated Task", Alert.AlertType.WARNING);
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
            showError("Missing Task", "Try Again",
                    "Please Select A Task From The Table", Alert.AlertType.WARNING);
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
                    showError("Duplicate Found", "The Task Already Exists",
                            "The Edits Would Create A Duplicate Task", Alert.AlertType.WARNING);
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
        } else {
            showError("Missing Task", "Try Again",
                    "Please Select A Task From The Table", Alert.AlertType.WARNING);
        }
    }

    private boolean inputValidation(TextField[] tf) {
        for (int i = 0; i < 3; i++) {
            if (tf[i].getText().isEmpty()) {
                showError("Empty Field", "Please Fill The Fields",
                        "Field Should Not Be Empty!", Alert.AlertType.WARNING);
                return false;
            }
        }

        try {
            String name = tf[0].getText().trim();
            int time = Integer.parseInt(tf[1].getText().trim());
            int productivity = Integer.parseInt(tf[2].getText().trim());

            if (name.isEmpty()) {
                showError("Invalid Name", "Try Again",
                        "Name Cannot Be Empty", Alert.AlertType.ERROR);
                return false;
            }
            if (time < 0) {
                showError("Invalid Time", "Try Again",
                        "Time Cannot Be Negative", Alert.AlertType.ERROR);
                return false;
            }
            if (productivity < 0) {
                showError("Invalid Productivity", "Try Again",
                        "Productivity Cannot Be Negative", Alert.AlertType.ERROR);
                return false;
            }
        } catch (Exception e) {
            showError("Invalid Format", "Try Again",
                    "Use Only Integers For Time/Productivity", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void updateTable() {
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : tasks)
            data.add(task);

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
