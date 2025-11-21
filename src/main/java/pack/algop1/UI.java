package pack.algop1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class UI {
    private final TableView<Task> tv;
    private final myArrayList<Task> tasks;
    private final Solutions solutions;
    private final TextField[] tf = new TextField[3];
    private final double maxX, maxY;
    private int totalHours;

    private final String color1 = "#3C2E7A", color2 = "#5644A8", color3 = "#D3D4FF";


    public UI() {
        tasks = new myArrayList<>(5);
        tv = new TableView<>();
        solutions = new Solutions(this);
        maxX = Screen.getPrimary().getVisualBounds().getMaxX();
        maxY = Screen.getPrimary().getVisualBounds().getMaxY();
        for (int i = 0; i < tf.length; i++)
            tf[i] = new TextField();
    }

    public TabPane p() {
        TabPane tp = new TabPane();
        Tab[] tab = new Tab[2];
        String[] names = new String[]{"Tasks View", "Solutions View"};

        for (int i = 0; i < 2; i++) {
            tab[i] = new Tab(names[i]);
            tab[i].setClosable(false);
            tab[i].setStyle("-fx-background-color: " + color2 + ";" +
                    "-fx-text-base-color: white;" +
                    "-fx-font-weight: bold;");
        }

        tp.setStyle(
                "-fx-tab-min-width: 150;" +
                        "-fx-tab-max-width: 150;" +
                        "-fx-tab-min-height: 40;" +
                        "-fx-background-color: " + color1 + ";"
        );

        tp.getTabs().addAll(tab);
        tab[0].setContent(viewTasks());
        tab[1].setContent(solutions.p());
        return tp;
    }

    public Pane viewTasks() {
        ImageView[] mv = new ImageView[]{
                new ImageView("add_task.png"), new ImageView("edit_task.png"), new ImageView("delete_task.png"),
                new ImageView("read_file.png"), new ImageView("save_file.png"), new ImageView("confirm.png"), new ImageView("cancel.png")
        };

        Button[] taskActions = new Button[]{
                new Button("Add Task", mv[0]),
                new Button("Edit Task", mv[1]),
                new Button("Delete Task", mv[2])
        };

        Button[] fileActions = new Button[]{
                new Button("Read File", mv[3]),
                new Button("Save On File", mv[4])
        };

        Pane p = new Pane();

        Label searchLabel = new Label("Search:");
        labelSettings(searchLabel);
        TextField searchField = new TextField();

        Label sortLabel = new Label("Sort:");
        labelSettings(sortLabel);
        ComboBox<String> sortCB =
                new ComboBox<>(FXCollections.observableArrayList("By Name", "By Time", "By Productivity"));
        sortCB.setOnAction(e -> sortTasks(sortCB.getValue()));

        HBox hb1 = new HBox(10, searchLabel, searchField);
        HBox hb2 = new HBox(10, sortLabel, sortCB);
        GridPane gp = addEditSection();

        Button confirm = (Button) gp.getChildren().getLast();
        Button cancel = (Button) gp.getChildren().get(gp.getChildren().size() - 2);
        confirm.setGraphic(mv[5]);
        cancel.setGraphic(mv[6]);

        Rectangle rec = new Rectangle();
        rectangleSizing(rec);

        hb1.setAlignment(Pos.CENTER_LEFT);
        hb2.setAlignment(Pos.CENTER_LEFT);

        controlSettings(taskActions, 130, 130, 5.8, 8, 180, true);
        controlSettings(fileActions, 130, 40, 1.41, 8, 50, false);
        controlSettings(new Control[]{tf[0], tf[1], tf[2], searchField, sortCB},
                130, 40, 0, 0, 0, false);

        setSizeImages(60, 60, new ImageView[]{mv[0], mv[1], mv[2]});
        setSizeImages(20, 20, new ImageView[]{mv[3], mv[4], mv[5], mv[6]});

        fileActions[0].setOnAction(e -> readFile());
        fileActions[1].setOnAction(e -> new fileHandler(this).saveOnFile());
        taskActions[0].setOnAction(e -> addTask(gp));
        taskActions[1].setOnAction(e -> editTask(tv.getSelectionModel().getSelectedItem(), gp));
        taskActions[2].setOnAction(e -> deleteTask(tv.getSelectionModel().getSelectedItem()));
        searchField.setOnKeyTyped(e -> searchTask(searchField.getText()));

        setXY(p, 160, 7.8, 0, 0);
        setXY(hb1, 4, 10.8, 0, -20);
        setXY(hb2, 1.64, 10.8, 0, -20);
        setXY(tv, 4, 8, 0, 0);
        setXY(gp, 1.41, 3.5, 0, 0);

        p.getChildren().addAll(rec, tasksTableSetup(), hb1, hb2, gp);
        p.getChildren().addAll(taskActions);
        p.getChildren().addAll(fileActions);

        Pane lP = new Pane(p);
        lP.setStyle("-fx-background-color: " + color1);
        return lP;
    }

    private void setSizeImages(double width, double height, ImageView[] mv) {
        for (ImageView iv : mv) {
            iv.setFitWidth(width);
            iv.setFitHeight(height);
            iv.setPreserveRatio(true);
        }
    }

    public void setXY(Node n, double x, double y, double offsetX, double offsetY) {
        n.setLayoutX((maxX / x) + offsetX);
        n.setLayoutY((maxY / y) + offsetY);
    }

    private void readFile() {
        new fileHandler(this).readFile();
        updateTable(tasks);
        solutions.updateTime(totalHours);
    }

    private void rectangleSizing(Rectangle rec) {
        rec.setStroke(Color.TRANSPARENT);
        rec.setFill(Color.web(color2));
        rec.setWidth(maxX / 1.3);
        rec.setHeight(maxY / 1.3);
        rec.setArcHeight(500);
        rec.setArcWidth(500);
        rec.setStrokeWidth(300);
        setXY(rec, 9.235, -27, 0, 0);
    }

    private void controlSettings(Control[] b, int width, int height,
                                 double divX, double divY, int increase, boolean bottom) {

        for (int i = 0; i < b.length; i++) {

            b[i].setPrefWidth(width);
            b[i].setPrefHeight(height);

            b[i].setStyle(
                    "-fx-text-fill: black;" +
                            "-fx-background-color: " + color3 + ";" +
                            "-fx-border-color: " + color1 + ";" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;"
            );

            if (b[i] instanceof Button button) {
                setXY(b[i], divX, divY, 0, i * increase);
                if (bottom)
                    button.setContentDisplay(ContentDisplay.TOP);
            }
        }
    }

    private void labelSettings(Label l) {
        l.setStyle("-fx-text-fill: " + color3 + "; -fx-font-weight: bold; -fx-font-size: 16px;");
    }

    private TableView<Task> tasksTableSetup() {
        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Task, Integer> c2 = new TableColumn<>("Time");
        c2.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<Task, Integer> c3 = new TableColumn<>("Productivity");
        c3.setCellValueFactory(new PropertyValueFactory<>("prodctivity"));

        tv.getColumns().setAll(c1, c2, c3);
        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tv.setPrefSize(maxX / 2.24, maxY / 2.24);
        VBox.setVgrow(tv, Priority.ALWAYS);

        tv.getStylesheets().add("style.css");
        return tv;
    }

    private void searchTask(String text) {
        myArrayList<Task> res = new myArrayList<>(tasks.size());

        if (text == null || text.isEmpty()) {
            for (Task t : tasks)
                res.add(t);
        } else {
            String s = text.toLowerCase();
            for (Task t : tasks)
                if (t.getName().toLowerCase().contains(s))
                    res.add(t);
        }

        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task t : res)
            data.add(t);

        tv.setItems(data);
    }

    private void sortTasks(String type) {
        Task[] tempArr = new Task[tasks.size()];
        for (int i = 0; i < tasks.size(); i++)
            tempArr[i] = tasks.get(i);

        sortTasks(tempArr, 0, tempArr.length - 1, type);

        myArrayList<Task> res = new myArrayList<>(tempArr.length);
        for (Task t : tempArr)
            res.add(t);

        updateTable(res);
    }

    private void sortTasks(Task[] a, int l, int r, String type) {
        if (l >= r) return;
        Task p = a[(l + r) / 2];
        int i = l, j = r;

        while (i <= j) {
            while (compare(a[i], p, type) > 0) i++;
            while (compare(a[j], p, type) < 0) j--;
            if (i <= j) {
                Task t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
                j--;
            }
        }

        sortTasks(a, l, j, type);
        sortTasks(a, i, r, type);
    }

    private int compare(Task a, Task b, String type) {
        if (type.equals("By Name"))
            return a.getName().compareToIgnoreCase(b.getName());
        if (type.equals("By Time"))
            return Integer.compare(a.getTime(), b.getTime());
        return Integer.compare(a.getProdctivity(), b.getProdctivity());
    }

    private void addTask(GridPane gp) {
        gp.setVisible(true);

        Button confirm = (Button) gp.getChildren().getLast();
        Button cancel = (Button) gp.getChildren().get(gp.getChildren().size() - 2);

        Label l = (Label) gp.getChildren().getFirst();
        l.setText("Adding Task");

        confirm.setOnAction(e -> {
            if (inputValidation(tf)) {
                Task task = new Task(tf[0].getText(),
                        Integer.parseInt(tf[1].getText()),
                        Integer.parseInt(tf[2].getText()));

                if (tasks.contains(task)) {
                    showError("Duplicate Found", "The Task Already Exists",
                            "Please Make Sure To Add A Non-Duplicated Task",
                            Alert.AlertType.WARNING);
                    return;
                }

                tasks.add(task);
                updateTable(tasks);
            }
            gp.setVisible(false);
        });

        cancel.setOnAction(e -> {
            for (TextField x : tf) x.clear();
            gp.setVisible(false);
        });
    }

    private void editTask(Task task, GridPane gp) {
        if (task == null) {
            showError("Missing Task", "Try Again",
                    "Please Select A Task From The Table",
                    Alert.AlertType.WARNING);
            return;
        }

        tf[0].setText(task.getName());
        tf[1].setText(task.getTime() + "");
        tf[2].setText(task.getProdctivity() + "");

        gp.setVisible(true);

        Button b = (Button) gp.getChildren().getLast();
        Button cancel = (Button) gp.getChildren().get(gp.getChildren().size() - 2);
        Label l = (Label) gp.getChildren().getFirst();

        l.setText("Editing Task");

        b.setOnAction(e -> {
            if (inputValidation(tf)) {
                String name = tf[0].getText().trim();
                int time = Integer.parseInt(tf[1].getText().trim());
                int prod = Integer.parseInt(tf[2].getText().trim());

                if (tasks.contains(new Task(name, time, prod))) {
                    showError("Duplicate Found", "The Task Already Exists",
                            "The Edits Would Create A Duplicate Task",
                            Alert.AlertType.WARNING);
                    return;
                }

                task.setName(name);
                task.setTime(time);
                task.setProdctivity(prod);
                updateTable(tasks);
            }
            gp.setVisible(false);
        });

        cancel.setOnAction(e -> {
            for (TextField x : tf) x.clear();
            gp.setVisible(false);
        });
    }

    private GridPane addEditSection() {
        Button[] b = new Button[]{new Button("Confirm"), new Button("Cancel")};
        GridPane gp = new GridPane();

        String[] labels = {"Task Name:", "Task Time:", "Task Productivity:"};
        Label l = new Label();
        labelSettings(l);
        gp.add(l, 1, 0);
        gp.setVisible(false);

        for (int i = 0; i < 3; i++) {
            Label l1 = new Label(labels[i]);
            labelSettings(l1);
            gp.add(l1, 0, i + 1);
            gp.add(tf[i], 1, i + 1);
        }

        controlSettings(b, 100, 40, 1, 1, 0, true);

        gp.add(b[1], 0, 4);
        gp.add(b[0], 1, 4);

        gp.setHgap(10);
        gp.setVgap(20);

        return gp;
    }

    private void deleteTask(Task task) {
        if (task != null) {
            tasks.remove(task);
            updateTable(tasks);
        } else {
            showError("Missing Task", "Try Again",
                    "Please Select A Task From The Table",
                    Alert.AlertType.WARNING);
        }
    }

    private boolean inputValidation(TextField[] tf) {
        for (TextField x : tf) {
            if (x.getText().isEmpty()) {
                showError("Empty Field", "Please Fill The Fields",
                        "Field Should Not Be Empty!",
                        Alert.AlertType.WARNING);
                return false;
            }
        }

        try {
            String name = tf[0].getText().trim();
            int time = Integer.parseInt(tf[1].getText().trim());
            int prod = Integer.parseInt(tf[2].getText().trim());

            if (name.isEmpty()) {
                showError("Invalid Name", "Try Again",
                        "Name Cannot Be Empty",
                        Alert.AlertType.ERROR);
                return false;
            }
            if (time < 0) {
                showError("Invalid Time", "Try Again",
                        "Time Cannot Be Negative",
                        Alert.AlertType.ERROR);
                return false;
            }
            if (prod < 0) {
                showError("Invalid Productivity", "Try Again",
                        "Productivity Cannot Be Negative",
                        Alert.AlertType.ERROR);
                return false;
            }
        } catch (Exception e) {
            showError("Invalid Format", "Try Again",
                    "Use Only Integers For Time/Productivity",
                    Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void updateTable(myArrayList<Task> list) {
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : list)
            data.add(task);
        tv.setItems(data);
        tv.refresh();
    }

    private void showError(String title, String header, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.setContentText(msg);
        a.setTitle(title);
        a.showAndWait();
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public myArrayList<Task> getTasks() {
        return tasks;
    }
}
