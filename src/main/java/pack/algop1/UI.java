package pack.algop1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UI {
    private final myArrayList<Task> tasks = new myArrayList<>();
    private final Stage stage;
    private final TableView<Task> tb = new TableView<>();

    public UI(Stage stage) {
        this.stage = stage;
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
            addTask().show();
            refreshPane();
        });
        VBox vb = new VBox(l, b[0], b[1]);
        vb.getStylesheets().add("style.css");
        return vb;
    }

    private Alert addTask() {
        GridPane gp = new GridPane();
        Label[] l = new Label[]{new Label("Task Name:"), new Label("Task Time:"), new Label("Task Productivity:")};
        TextField[] tf = new TextField[3];
        for (int i = 0; i < 3; i++) {
            tf[i] = new TextField();
            gp.add(l[i], 0, i);
            gp.add(tf[i], 1, i);
        }
        gp.getStylesheets().add("style.css");
        gp.setVgap(20);
        gp.setHgap(20);
        Alert s = new Alert(Alert.AlertType.INFORMATION);
        s.setContentText(null);
        s.setHeaderText(" ");
        s.setGraphic(gp);
        s.setTitle("Add Task");
        return s;
    }

    private void refreshPane() {
        if (!tasks.isEmpty())
            stage.setScene(new Scene(tb(), 500, 500));
        else
            stage.setScene(new Scene(startPage(), 500, 500));
    }

    private TableView<Task> tasksTableSetup() {
        tb.getStylesheets().add("style.css");
        tb.setEditable(false);
        tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Task, String> c1 = new TableColumn<>("Name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Task, Integer> c2 = new TableColumn<>("Time");
        c2.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Task, Integer> c3 = new TableColumn<>("Productivity");
        c3.setCellValueFactory(new PropertyValueFactory<>("prodctivity"));

        tb.getColumns().addAll(c1, c2, c3);
        updateTable();
        return tb;
    }

    private void updateTable() {
        ObservableList<Task> data = FXCollections.observableArrayList();
        for (Task task : tasks) {
            data.add(task);
        }
        tb.setItems(data);
    }
}
