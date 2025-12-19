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

import java.util.Optional;

public class UI {

    private final TableView<Task> tv;
    private final myArrayList<Task> tasks;
    private final solutionsUI solutionsUI;
    private final TextField[] tf = new TextField[3];
    private final double maxX, maxY;
    private float totalHours;

    // Colors of The UI
    private final String color1 = "#3C2E7A", color2 = "#5644A8", color3 = "#D3D4FF";

    public UI() {
        tasks = new myArrayList<>(5);
        tv = new TableView<>();
        solutionsUI = new solutionsUI(this);

        // Get Screen Resolution of The User
        maxX = Screen.getPrimary().getVisualBounds().getMaxX();
        maxY = Screen.getPrimary().getVisualBounds().getMaxY();

        // Initialize Text Fields Array
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
        tab[1].setContent(solutionsUI.p());

        // Fire Calculations When Switching to Solutions View
        tab[1].setOnSelectionChanged(e -> {
            if (!tasks.isEmpty())
                solutionsUI.runCalculating();
        });

        return tp;
    }

    public Pane viewTasks() {

        // Initialize Action Buttons with Their Icons
        ImageView[] mv = new ImageView[]{
                new ImageView("add_task.png"), new ImageView("edit_task.png"), new ImageView("delete_task.png"),
                new ImageView("read_file.png"), new ImageView("save_file.png"),
                new ImageView("confirm.png"), new ImageView("cancel.png")
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

        HBox hb1 = new HBox(10, searchLabel, searchField);
        GridPane gp = addEditSection();

        Button confirm = (Button) gp.getChildren().getLast();
        Button cancel = (Button) gp.getChildren().get(gp.getChildren().size() - 2);
        confirm.setGraphic(mv[5]);
        cancel.setGraphic(mv[6]);

        // Background Rectangle for UI Styling
        Rectangle rec = new Rectangle();
        rectangleSizing(rec);

        hb1.setAlignment(Pos.CENTER_LEFT);

        // Apply Settings to All Control Elements
        controlSettings(taskActions, 130, 110, 5.8, 8, 170, true);
        controlSettings(fileActions, 130, 40, 1.41, 8, 50, false);
        controlSettings(new Control[]{tf[0], tf[1], tf[2], searchField},
                130, 40, 0, 0, 0, false);

        setSizeImages(60, 60, new ImageView[]{mv[0], mv[1], mv[2]});
        setSizeImages(20, 20, new ImageView[]{mv[3], mv[4], mv[5], mv[6]});

        fileActions[0].setOnAction(e -> readFile());
        fileActions[1].setOnAction(e -> {

            // Confirm Before Saving when Task List is Empty
            if (tasks.isEmpty()) {
                Alert save = new Alert(Alert.AlertType.CONFIRMATION);
                save.setTitle("Empty List");
                save.setHeaderText("Tasks List Is Empty");
                save.setContentText("Are You Sure You Want To Save An Empty List To The File?");
                Optional<ButtonType> result = save.showAndWait();
                if (result.isEmpty() || result.get() != ButtonType.OK)
                    return;
            }

            new fileHandler(this).saveOnFile();
        });

        taskActions[0].setOnAction(e -> addTask(gp));
        taskActions[1].setOnAction(e -> editTask(tv.getSelectionModel().getSelectedItem(), gp));
        taskActions[2].setOnAction(e -> deleteTask(tv.getSelectionModel().getSelectedItem()));
        searchField.setOnKeyTyped(e -> searchTask(searchField.getText()));

        // Position All Elements on The Pane
        setXY(p, 160, 7.8, 0, 0);
        setXY(hb1, 4, 10.8, 0, -20);
        setXY(tv, 4, 8, 0, 0);
        setXY(gp, 1.41, 3.5, 0, 0);

        // Add All Components to The Pane
        p.getChildren().addAll(rec, tasksTableSetup(), hb1, gp);
        p.getChildren().addAll(taskActions);
        p.getChildren().addAll(fileActions);

        // Create Layout Pane with Background Color
        Pane lP = new Pane(p);
        lP.setStyle("-fx-background-color: " + color1);
        return lP;
    }

    // Resize Icons while Preserving Aspect Ratio
    private void setSizeImages(double width, double height, ImageView[] mv) {
        for (ImageView iv : mv) {
            iv.setFitWidth(width);
            iv.setFitHeight(height);
            iv.setPreserveRatio(true);
        }
    }

    // Position Node Relative to Screen Size
    public void setXY(Node n, double x, double y, double offsetX, double offsetY) {
        n.setLayoutX((maxX / x) + offsetX);
        n.setLayoutY((maxY / y) + offsetY);
    }

    // Read Tasks from File and Refresh UI
    private void readFile() {
        new fileHandler(this).readFile();
        updateTable(tasks);
        solutionsUI.updateTime(totalHours);
    }

    // Configure Rectangle Background Styling
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

    // Apply Common Settings to Control Elements
    private void controlSettings(Control[] b, int width, int height,
                                 double divX, double divY, int increase, boolean bottom) {

        for (int i = 0; i < b.length; i++) {

            // Set Size for Each Control
            b[i].setPrefWidth(width);
            b[i].setPrefHeight(height);

            // Apply Styling to Control
            b[i].setStyle(
                    "-fx-text-fill: black;" +
                            "-fx-background-color: " + color3 + ";" +
                            "-fx-border-color: " + color1 + ";" +
                            "-fx-border-radius: 8;" +
                            "-fx-background-radius: 8;"
            );

            // Position Buttons and Set Text Position (if bottom boolean is true)
            if (b[i] instanceof Button button) {
                setXY(b[i], divX, divY, 0, i * increase);
                if (bottom)
                    button.setContentDisplay(ContentDisplay.TOP);
            }
        }
    }

    // Apply Common Settings to Labels
    private void labelSettings(Label l) {
        l.setStyle("-fx-text-fill: " + color3 +
                "; -fx-font-weight: bold; -fx-font-size: 16px;");
    }

    // Setup and Configure Tasks Table View
    private TableView<Task> tasksTableSetup() {
        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, Float> c2 = new TableColumn<>("Time");
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

    // Search and Filter Tasks by Name
    private void searchTask(String text) {
        myArrayList<Task> res = new myArrayList<>(tasks.size());

        // Display All Tasks if Search is Empty
        if (text == null || text.isEmpty()) {
            for (Task t : tasks)
                res.add(t);
        } else {
            // Filter Tasks by Search Text
            String s = text.toLowerCase();
            for (Task t : tasks)
                if (t.getName().toLowerCase().contains(s))
                    res.add(t);
        }

        // Update Table with Filtered Results
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task t : res)
            data.add(t);

        tv.setItems(data);
    }

    // Add New Task to The List
    private void addTask(GridPane gp) {
        gp.setVisible(true);

        Button confirm = (Button) gp.getChildren().getLast();
        Button cancel = (Button) gp.getChildren().get(gp.getChildren().size() - 2);

        Label l = (Label) gp.getChildren().getFirst();
        l.setText("Adding Task");

        // Clear All Input Fields
        for (TextField x : tf) x.clear();

        // Handle Confirm Button Action
        confirm.setOnAction(e -> {
            if (inputValidation(tf)) {

                // Create New Task from Input
                Task task = new Task(
                        tf[0].getText(),
                        Float.parseFloat(tf[1].getText()),
                        Integer.parseInt(tf[2].getText())
                );

                // Check for Duplicate Task
                if (tasks.contains(task)) {
                    showAlert("Duplicate Found", "The Task Already Exists",
                            "Please Make Sure To Add A Non-Duplicated Task",
                            Alert.AlertType.WARNING);
                    return;
                }

                tasks.add(task);
                updateTable(tasks);
                gp.setVisible(false);
                showAlert("Task Added", null,
                        "Task Successfully Added",
                        Alert.AlertType.INFORMATION);
            }

            // Clear Input Fields after Action
            for (TextField x : tf) x.clear();
        });

        // Handle Cancel Button Action
        cancel.setOnAction(e -> {
            for (TextField x : tf) x.clear();
            gp.setVisible(false);
        });
    }

    // Edit Selected Task from The Table
    private void editTask(Task task, GridPane gp) {

        // Check if Task is Selected
        if (task == null) {
            showAlert("Missing Task", "Try Again",
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

        // Handle Confirm Button Action
        b.setOnAction(e -> {
            if (inputValidation(tf)) {

                // Check if Task Still Exists
                if (!tasks.contains(task)) {
                    showAlert("Missing Task", "Try Again",
                            "The Task Seems To Be Deleted Or Missing",
                            Alert.AlertType.WARNING);
                    return;
                }

                // Get Updated Values from Input Fields
                String name = tf[0].getText().trim();
                float time = Float.parseFloat(tf[1].getText().trim());
                int prod = Integer.parseInt(tf[2].getText().trim());

                // Check if Edits Create a Duplicate Task
                if (!name.equalsIgnoreCase(task.getName())
                        && tasks.contains(new Task(name, time, prod))) {

                    showAlert("Duplicate Found", "The Task Already Exists",
                            "The Edits Would Create A Duplicate Task",
                            Alert.AlertType.WARNING);
                    return;
                }

                // Update Task with New Values
                task.setName(name);
                task.setTime(time);
                task.setProdctivity(prod);
                updateTable(tasks);
                gp.setVisible(false);
                showAlert("Task Edit", null,
                        "Task Edited Successfully",
                        Alert.AlertType.INFORMATION);
            }
        });

        // Handle Cancel Button Action
        cancel.setOnAction(e -> {
            for (TextField x : tf) x.clear();
            gp.setVisible(false);
        });
    }

    // Create Add and Edit Task Form Section
    private GridPane addEditSection() {
        Button[] b = new Button[]{new Button("Confirm"), new Button("Cancel")};
        GridPane gp = new GridPane();

        String[] labels = {"Task Name:", "Task Time:", "Task Productivity:"};
        Label l = new Label();
        labelSettings(l);

        gp.add(l, 1, 0);
        gp.setVisible(false);

        // Add Labels and Text Fields to Grid
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

    // Delete Selected Task from The List
    private void deleteTask(Task task) {

        // Check if Task is Selected
        if (task == null) {
            showAlert("Missing Task", "Try Again",
                    "Please Select A Task From The Table",
                    Alert.AlertType.WARNING);
            return;
        }

        // Show Confirmation Dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Task");
        confirm.setHeaderText("Are You Sure You Want To Delete This Task?");
        confirm.setContentText("This Action Cannot Be Undone");

        // Delete Task if Confirmed
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tasks.remove(task);
            updateTable(tasks);
        }
    }

    // Validate Input Fields before Adding or Editing Task
    private boolean inputValidation(TextField[] tf) {

        // Check for Empty Fields
        for (TextField x : tf) {
            if (x.getText().isEmpty()) {
                showAlert("Empty Field", "Please Fill The Fields",
                        "Field Should Not Be Empty!",
                        Alert.AlertType.WARNING);
                return false;
            }
        }

        try {
            // Parse and Validate Input Values
            String name = tf[0].getText().trim();
            float time = Float.parseFloat(tf[1].getText().trim());
            int prod = Integer.parseInt(tf[2].getText().trim());

            // Validate Name is Not Empty
            if (name.isEmpty()) {
                showAlert("Invalid Name", "Try Again",
                        "Name Cannot Be Empty",
                        Alert.AlertType.ERROR);
                return false;
            }

            // Validate Time Moves in 0.5 Steps
            float validStep = (float) (time - Math.floor(time));
            if (!(validStep == 0.5 || validStep == 0)) {
                showAlert("Invalid Time", "Try Again",
                        "Time Should Move In 0.5 Steps",
                        Alert.AlertType.WARNING);
            }

            // Validate Time is Not Negative
            if (time < 0) {
                showAlert("Invalid Time", "Try Again",
                        "Time Cannot Be Negative",
                        Alert.AlertType.ERROR);
                return false;
            }

            // Validate Productivity is Not Negative
            if (prod < 0) {
                showAlert("Invalid Productivity", "Try Again",
                        "Productivity Cannot Be Negative",
                        Alert.AlertType.ERROR);
                return false;
            }

        } catch (Exception e) {
            // Handle Invalid Format Exceptions
            showAlert("Invalid Format", "Try Again",
                    "Time Must Be Float (0.5 Steps) | Productivity Must Be Integer",
                    Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    // Update Table View with Current Task List
    private void updateTable(myArrayList<Task> list) {
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : list)
            data.add(task);
        tv.setItems(data);
        tv.refresh();
    }

    // Display Alert Dialog with Custom Message
    private void showAlert(String title, String header, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.setContentText(msg);
        a.setTitle(title);
        a.showAndWait();
    }

    // Getters
    public float getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(float totalHours) {
        this.totalHours = totalHours;
    }

    public myArrayList<Task> getTasks() {
        return tasks;
    }
}