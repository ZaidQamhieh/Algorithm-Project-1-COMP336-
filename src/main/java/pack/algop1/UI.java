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

    public UI(Scene scene) {
        this.scene = scene;
    }

    public TabPane tb() {
        TabPane tb = new TabPane();
        Tab[] t = new Tab[4];
        String[] names = new String[]{"Tasks View", "Dynamic Solution", "Greedy Solution", "Comparison"};
        for (int i = 0; i < 4; i++) {
            t[i] = new Tab(names[i]);
            t[i].setClosable(false);
        }
        tb.getTabs().addAll(t);
        t[0].setContent(tasksTableSetup());
        return tb;
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
            addEditTask("Add",false, null);
            refreshPane();
        });
        VBox vb = new VBox(l, b[0], b[1]);
        vb.getStylesheets().add("style.css");
        return vb;
    }

    private Alert taskActionsTemplate(String Action, TextField[] tf) {
        GridPane gp = new GridPane();
        Label[] l = new Label[]
                {
                        new Label("Task Name:"),
                        new Label("Task Time:"),
                        new Label("Task Productivity:")
                };
        if (tf != null) {
            for (int i = 0; i < 3; i++) {
                tf[i] = new TextField();
                gp.add(l[i], 0, i);
                gp.add(tf[i], 1, i);
            }
        }
        gp.getStylesheets().add("style.css");
        gp.setVgap(20);
        gp.setHgap(20);
        Alert s = new Alert(Alert.AlertType.INFORMATION);
        s.setContentText(null);
        s.setHeaderText(" ");
        s.setGraphic(gp);
        s.setTitle(Action);
        return s;
    }

    private void addEditTask(String action,boolean edit, Task t) {
        TextField[] textFields = new TextField[3];
        Alert a = taskActionsTemplate(action, textFields);
        if (edit) {
            if (t == null) {
                Alert taskAlert = new Alert(Alert.AlertType.WARNING, "Please Select A Task");
                taskAlert.setContentText("Please Select A Task First");
                taskAlert.setTitle("Invalid Task");
                taskAlert.setHeaderText(null);
                taskAlert.showAndWait();
                return;
            }
            textFields[0].setText(t.getName());
            textFields[1].setText(t.getTime() + "");
            textFields[2].setText(t.getProdctivity() + "");
        }
        a.showAndWait().ifPresent(r -> {
            if (r != ButtonType.OK)
                return;
            String name = textFields[0].getText();
            int time, productivity;

            try {
                time = Integer.parseInt(textFields[1].getText());
                productivity = Integer.parseInt(textFields[2].getText());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Enter Valid Numbers!", ButtonType.OK).showAndWait();
                addEditTask(action,edit,t);
                return;
            }

            Task task = new Task(name, time, productivity);
            if (!taskInputValidation(task)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Task Data!", ButtonType.OK).showAndWait();
                addEditTask(action,edit,t);
                return;
            }

            tasks.add(task);
            refreshPane();
        });
    }


    private void deleteTask() {
        tasks.remove(tv.getSelectionModel().getSelectedItem());
        refreshPane();
    }

    private boolean taskInputValidation(Task task) {
        if (task == null)
            return false;
        if (task.getName() == null || task.getName().isEmpty())
            return false;
        if (task.getTime() < 0 || task.getProdctivity() < 0)
            return false;
        return true;
    }

    private void refreshPane() {
        scene.setRoot(new Pane());
        if (!tasks.isEmpty())
            scene.setRoot(tb());
        else
            scene.setRoot(startPage());
        updateTable();
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

        b[0].setOnAction(e -> addEditTask("Add",false, null));
        b[1].setOnAction(e -> addEditTask("Edit",true, tv.getSelectionModel().getSelectedItem()));
        b[2].setOnAction(e -> deleteTask());

        tv.setEditable(false);
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tv, Priority.ALWAYS);

        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, Integer> c2 = new TableColumn<>("Time");
        c2.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Task, Integer> c3 = new TableColumn<>("Productivity");
        c3.setCellValueFactory(new PropertyValueFactory<>("prodctivity"));

        tv.getColumns().clear();
        tv.getColumns().addAll(c1, c2, c3);
        VBox vb = new VBox(sp, tv);

        vb.getStylesheets().add("style.css");
        return vb;
    }

    private void updateTable() {
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : tasks) {
            data.add(task);
        }
        tv.setItems(data);
    }
}
