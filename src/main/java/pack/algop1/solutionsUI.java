package pack.algop1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class solutionsUI {

    private final UI ui;
    private final myArrayList<Task> tasks;
    private final double maxX, maxY;
    private long timeDP, timeGreedy;

    private final TextArea[] ta = new TextArea[2];
    private final TextField timeTF = new TextField();
    private final Button calculate = new Button("Calculate");
    private final ScrollPane dpTable = new ScrollPane();
    private final ListView<String>[] selectedTasks = new ListView[2];
    private final GridPane dpGrid = new GridPane();

    private final String color1 = "#3C2E7A", color2 = "#5644A8",
            color3 = "#EFEFFF", color5 = "#9C9FE6", color6 = "#D3D4FF";

    public solutionsUI(UI ui) {
        this.ui = ui;
        tasks = ui.getTasks();
        // Get Screen Resolution
        maxX = Screen.getPrimary().getVisualBounds().getMaxX();
        maxY = Screen.getPrimary().getVisualBounds().getMaxY();

        // Initialize Selected Tasks List Views
        for (int i = 0; i < selectedTasks.length; i++) {
            selectedTasks[i] = new ListView<>();
            selectedTasks[i].setPrefHeight(270);
            selectedTasks[i].setStyle(
                    "-fx-control-inner-background: " + color2 +
                            ";-fx-text-fill: " + color3 +
                            ";-fx-border-color: " + color1 +
                            ";-fx-border-width: 2;" +
                            "-fx-background-radius: 6;" +
                            "-fx-border-radius: 6;" +
                            "-fx-font-weight: bold;" +
                            "-fx-cell-border-color: " + color1
            );
        }

        // Initialize Text Areas for Algorithm Results
        for (int i = 0; i < ta.length; i++) {
            ta[i] = new TextArea();
            ta[i].setEditable(false);
            ta[i].setWrapText(true);
            ta[i].setPrefWidth(maxX / 2 - 200);
            ta[i].setPrefHeight(dpTable.getMaxHeight());
            ta[i].setStyle(
                    "-fx-control-inner-background: " + color2 +
                            ";-fx-text-fill: " + color3 +
                            ";-fx-border-color: " + color1 +
                            ";-fx-border-radius: 6;" +
                            ";-fx-background-radius: 6;" +
                            ";-fx-font-size: 15px;" +
                            "-fx-font-weight: bold;"
            );
        }

        dpTable.setPrefViewportWidth(maxX / 2);
        dpTable.setPrefViewportHeight(maxY / 8);
        dpTable.setFitToHeight(false);
        dpTable.setFitToWidth(false);
        dpTable.setMaxWidth(maxX / 2 - 150);
        dpTable.setMaxHeight(maxY / 12);
        dpTable.setStyle(
                "-fx-background: " + color2 + ";" +
                        "-fx-background-color: " + color2 + ";" +
                        "-fx-border-color: " + color1 + ";" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );
    }

    public Pane p() {
        Label[] labels = new Label[]{
                new Label("Total Hours:"), new Label("Dynamic Solution"), new Label("Chosen Tasks"),
                new Label("Greedy Solution"), new Label("Chosen Tasks"),
                new Label("Comparison"), new Label("DP vs Greedy"), new Label("DP Relation:"), new Label()};

        Line[] lines = new Line[]{
                new Line(50, maxY / 1.5, maxX - 50, maxY / 1.5),
                new Line(maxX / 2, maxY / 12, maxX / 2, maxY / 1.5),
                new Line(50, maxY / 12, maxX - 50, maxY / 12),
                new Line(50, maxY / 1.5, 50, maxY / 12),
                new Line(maxX - 50, maxY / 1.5, maxX - 50, maxY / 12),};

        for (Line line : lines)
            line.setStrokeWidth(5);

        HBox hb = new HBox(20, labels[0], timeTF, calculate);

        VBox subDpGroup = new VBox(40, dpTable, selectedTasks[0]);
        VBox dpGroup = new VBox(10, subDpGroup, labels[7], labels[8]);
        VBox greedyGroup = new VBox(40, ta[0], selectedTasks[1]);

        setXY(dpGroup, maxX, 7, maxX / 24, -100);
        setXY(greedyGroup, 2, 7, maxX / 20, 0);
        setXY(ta[1], 3, 1, -70, -(maxY / 2) + 250);
        setXY(hb, 2, maxY, -155, 50);

        labelSettings(labels);
        controlSettings(calculate, 70);
        controlSettings(timeTF, 120);
        hb.setAlignment(Pos.CENTER);
        dpGroup.setPadding(new Insets(100, 10, 10, 10));

        // Fire Calculation Action
        calculate.setOnAction(e -> calculate());

        Rectangle[] r = new Rectangle[2];
        for (int i = 0; i < r.length; i++) {
            r[i] = new Rectangle();
        }
        r[0].setStyle("-fx-fill: " + color5);
        r[0].setHeight(maxY);
        r[0].setWidth(maxX);
        r[1].setStyle("-fx-fill: " + color6);
        r[1].setHeight(maxY / 1.715);
        r[1].setWidth(maxX - 100);
        setXY(r[1], maxX, 12, 50, 0);

        Pane p = new Pane();
        p.getChildren().addAll(r);
        p.getChildren().addAll(lines);
        p.getChildren().addAll(hb, dpGroup, greedyGroup, ta[1]);

        for (int i = 1; i < labels.length - 2; i++)
            p.getChildren().add(labels[i]);
        return p;
    }

    private void labelSettings(Label[] l) {
        for (Label label : l)
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        setXY(l[1], maxX, 10, (maxX / 5) + 30, 20);
        setXY(l[2], maxX, 5, (maxX / 5) + 30, 35);
        setXY(l[3], 2, 10, (maxX / 5) + 30, 20);
        setXY(l[4], 2, 3, (maxX / 5) + 30, 20);
        setXY(l[5], 2, 1, -50, -(maxY / 2) + 200);
        setXY(l[6], 2, maxY, -50, 5);
        l[6].setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        l[8].setStyle("-fx-font-size: 16px;");
        l[8].setText("if (j ≥ Task Time):\n dp[j] = max( dp[j] , dp[j−Task Time] + Task Productivity )\nelse:\ndp[j] = dp[j]");
    }

    private void controlSettings(Control b, int width) {
        b.setPrefWidth(width);
        b.setPrefHeight(30);
        b.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color:" + color2 +
                        ";-fx-border-color: white;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
    }

    // Main Calculation Method that Runs Both Algorithms
    private void calculate() {
        // Default to 0 if Input is Empty
        if (timeTF.getText().isEmpty()) {
            timeTF.setText("0");
            calculate.fire();
            return;
        }
        float totalHours;
        try {
            // Parse and Validate Total Hours Input
            totalHours = Float.parseFloat(timeTF.getText());
            if (totalHours < 0)
                return;

            // Validate Time Moves in 0.5 Steps
            float validStep = (float) (totalHours - Math.floor(totalHours));
            if (!(validStep == 0.5 || validStep == 0)) {
                showAlert("Invalid Total Time", "Try Again",
                        "Total Time Should Move In 0.5 Steps", Alert.AlertType.WARNING);
                return;
            }
            ui.setTotalHours(totalHours);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Total Hours Accepts Numbers Only").show();
            return;
        }

        int dpValue = dpSolution(totalHours);
        int greedyValue = greedySolution(totalHours);
        compareDPGreedy(dpValue, greedyValue);
    }

    private int dpSolution(float totalHours) {
        int n = tasks.size();
        int time = Math.round(totalHours * 2);
        long start = System.nanoTime();
        int[] dp = new int[time + 1];
        boolean[][] take = new boolean[n][time + 1];

        // Initialize The DP to The First Task
        int firstTaskT = Math.round(tasks.get(0).getTime() * 2);
        int firstTaskP = tasks.get(0).getProdctivity();
        for (int j = firstTaskT; j <= time; j++) {
            dp[j] = firstTaskP;
            take[0][j] = true;
        }

        for (int i = 1; i < n; i++) {
            int taskTime = Math.round(tasks.get(i).getTime() * 2);
            int taskProductivity = tasks.get(i).getProdctivity();
            for (int j = time; j >= taskTime; j--) {
                int select = dp[j - taskTime] + taskProductivity;
                if (select > dp[j]) {
                    dp[j] = select;
                    take[i][j] = true;
                }
            }
        }

        timeDP = System.nanoTime() - start;
        showSelectedTasksDP(take, time);
        buildDPGridHeader(time);
        fillTable(dp);
        dpTable.setContent(dpGrid);
        return dp[time];
    }

    private void showSelectedTasksDP(boolean[][] take, int time) {
        selectedTasks[0].getItems().clear();

        int j = time;
        for (int i = tasks.size() - 1; i >= 0; i--) {
            if (take[i][j]) {
                Task task = tasks.get(i);
                selectedTasks[0].getItems().add(task.toString());
                int taskTime = Math.round(task.getTime() * 2);
                j -= taskTime;
            }
        }
    }

    private void buildDPGridHeader(int time) {
        dpGrid.getChildren().clear();
        dpGrid.setHgap(0);
        dpGrid.setVgap(0);

        int width = 80;
        int height = 30;

        Label corner = new Label("");
        corner.setPrefSize(160, height);
        corner.setStyle("-fx-background-color:" + color2 + ";-fx-border-color:black;");
        dpGrid.add(corner, 0, 0);

        for (int j = 0; j <= time; j++) {
            float hours = j / 2.0f;
            Label l = new Label(hours + " h");
            l.setPrefSize(width, height);
            l.setStyle("-fx-text-fill:white;" +
                    "-fx-background-color:" + color2 + ";" +
                    "-fx-border-color:black;" +
                    "-fx-alignment:center;");
            dpGrid.add(l, j + 1, 0);
        }
    }

    private void fillTable(int[] dp) {
        int width = 80;
        int height = 30;

        Label rowLabel = new Label("DP Value");
        rowLabel.setPrefSize(160, height);
        rowLabel.setStyle("-fx-background-color:" + color1 + ";-fx-text-fill:white;-fx-alignment:center;");
        dpGrid.add(rowLabel, 0, 1);

        for (int j = 0; j < dp.length; j++) {
            Label l = new Label(String.valueOf(dp[j]));
            l.setPrefSize(width, height);
            l.setStyle("-fx-background-color:" + color3 + ";-fx-border-color:gray;-fx-alignment:center;-fx-text-fill: black");
            dpGrid.add(l, j + 1, 1);
        }
    }

    // Greedy Solution
    private int greedySolution(float totalHoursF) {
        this.selectedTasks[1].getItems().clear();

        // Convert Hours to Half Hours
        int totalTime = Math.round(totalHoursF * 2);
        int productivity = 0;
        int used = 0;
        int last = -1;

        myArrayList<Task> selected = new myArrayList<>(tasks.size());
        long start = System.nanoTime();

        for (int i = 0; i < tasks.size() && totalTime > 0; i++) {
            int t = Math.round(tasks.get(i).getTime() * 2);
            // If Task Fits in Remaining Time Add It
            if (t <= totalTime) {
                productivity += tasks.get(i).getProdctivity();
                totalTime -= t;
                used += t;
                last = i;
                selected.add(tasks.get(i));
            }
        }
        // Time it Took The Greedy Solution to Execute
        long end = System.nanoTime();
        timeGreedy = end - start;

        // Display Selected Tasks
        for (int i = 0; i < selected.size(); i++)
            this.selectedTasks[1].getItems().add(selected.get(i).toString());

        // Display Greedy Solution Statistics
        ta[0].setText(
                "Total Selected Tasks: " + selected.size() +
                        "\n\nLast Selected Item: " + (last == -1 ? "None" : tasks.get(last).getName()) +
                        "\n\nHours Used: " + (used / 2.0f) + " / " + totalHoursF +
                        "\n\nRemaining Hours: " + ((Math.round(totalHoursF * 2) - used) / 2.0f) +
                        "\n\nTotal Productivity: " + productivity
        );

        return productivity;
    }


    // Compare Dynamic Programming and Greedy Solutions
    private void compareDPGreedy(int dpValue, int greedyValue) {
        StringBuilder sb = new StringBuilder();

        // Compare Productivity Results
        sb.append("Productivity Results:\n");
        sb.append("DP Productivity: ").append(dpValue).append("\n");
        sb.append("Greedy Productivity: ").append(greedyValue).append("\n\n");

        if (dpValue > greedyValue) sb.append("DP Gives A Better Productivity\n");
        else if (greedyValue > dpValue) sb.append("Greedy Gives A Better Productivity\n");
        else sb.append("Both Give The Same Productivity\n");

        sb.append("-".repeat((int) (ta[1].getWidth() / 8))).append("\n");

        // Compare Time Results
        sb.append("Time Results:\n");
        sb.append("DP Time: ").append(timeDP).append(" ns\n");
        sb.append("Greedy Time: ").append(timeGreedy).append(" ns\n\n");

        if (timeDP > timeGreedy) sb.append("Greedy Is Faster\n");
        else if (timeGreedy > timeDP) sb.append("DP Is Faster\n");
        else sb.append("Both Give The Same Time\n");

        sb.append("-".repeat((int) (ta[1].getWidth() / 8))).append("\n");

        // List Advantages and Disadvantages of DP
        sb.append("\nDP Advantages:\n");
        sb.append("-Always Optimal\n");
        sb.append("-Checks All Combinations\n");

        sb.append("\nDP Disadvantages:\n");
        sb.append("-Slower Than Greedy\n");
        sb.append("-Harder To Track\n");

        sb.append("-".repeat((int) (ta[1].getWidth() / 8))).append("\n");

        // List Advantages and Disadvantages of Greedy
        sb.append("\nGreedy Advantages:\n");
        sb.append("-Very fast\n");
        sb.append("-Easy to Implement\n");

        sb.append("\nGreedy Disadvantages:\n");
        sb.append("-Not Always Optimal\n");

        ta[1].setText(sb.toString());
    }

    // Position Node Relative to Screen Size
    private void setXY(Node n, double x, double y, double offsetX, double offsetY) {
        n.setLayoutX((maxX / x) + offsetX);
        n.setLayoutY((maxY / y) + offsetY);
    }

    // To Fire Calculation from Other Classes
    public void runCalculating() {
        calculate.fire();
    }

    // Update Time Field and Recalculate
    public void updateTime(float time) {
        timeTF.setText(String.valueOf(time));
        calculate.fire();
    }

    // Display Alert Dialog with Custom Message
    private void showAlert(String title, String header, String msg, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(header);
        a.setContentText(msg);
        a.setTitle(title);
        a.showAndWait();
    }
}