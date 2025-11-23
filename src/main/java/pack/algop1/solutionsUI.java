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

    private final String color1 = "#3C2E7A", color2 = "#5644A8",
            color3 = "#EFEFFF", color5 = "#9C9FE6", color6 = "#D3D4FF";

    public solutionsUI(UI ui) {
        this.ui = ui;
        tasks = ui.getTasks();
        maxX = Screen.getPrimary().getVisualBounds().getMaxX();
        maxY = Screen.getPrimary().getVisualBounds().getMaxY();
        for (int i = 0; i < selectedTasks.length; i++) {
            selectedTasks[i] = new ListView<>();
            selectedTasks[i].setPrefHeight(150);
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
        dpTable.setPrefViewportHeight(maxY / 3);
        dpTable.setFitToHeight(false);
        dpTable.setFitToWidth(false);
        dpTable.setMaxWidth(maxX / 2 - 200);
        dpTable.setMaxHeight(maxY / 5);
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

        for (int i = 0; i < lines.length; i++) {
            lines[i].setStrokeWidth(5);
        }
        HBox hb = new HBox(20, labels[0], timeTF, calculate);

        VBox subDpGroup = new VBox(40, dpTable, selectedTasks[0]);
        VBox dpGroup = new VBox(10, subDpGroup, labels[7], labels[8]);
        VBox greedyGroup = new VBox(40, ta[0], selectedTasks[1]);

        setXY(dpGroup, maxX, 7, maxX / 24, -100);
        setXY(greedyGroup, 2, 7, maxX / 20, 0);
        setXY(ta[1], 3, 1, -70, -(maxY / 2) + 250);
        setXY(hb, 2, maxY, -155, 50);

        labelSettings(labels);
        controlSettings(calculate, 70, 30, false);
        controlSettings(timeTF, 120, 30, false);
        hb.setAlignment(Pos.CENTER);
        dpGroup.setPadding(new Insets(100, 10, 10, 10));

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
        setXY(l[2], maxX, 3, (maxX / 5) + 30, 20);
        setXY(l[3], 2, 10, (maxX / 5) + 30, 20);
        setXY(l[4], 2, 3, (maxX / 5) + 30, 20);
        setXY(l[5], 2, 1, -50, -(maxY / 2) + 200);
        setXY(l[6], 2, maxY, -50, 5);
        l[6].setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        l[8].setStyle("-fx-font-size: 16px;");
        l[8].setText("if (Task Time ≤ j):\n dp[i][j] = max( Task Productivity + dp[i−1][j−Task Time] , dp[i−1][j] )\nelse:\ndp[i][j] = dp[i−1][j]");
    }

    private void controlSettings(Control b, int width, int height, boolean bottom) {
        b.setPrefWidth(width);
        b.setPrefHeight(height);
        b.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-background-color:" + color2 +
                        ";-fx-border-color: white;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;"
        );
        if (b instanceof Button button) {
            if (bottom)
                button.setContentDisplay(ContentDisplay.TOP);
        }
    }

    private void calculate() {
        if (timeTF.getText().isEmpty())
            return;
        float totalHours;
        try {
            totalHours = Float.parseFloat(timeTF.getText());
            if (totalHours <= 0)
                return;

            ui.setTotalHours(totalHours);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Total Hours Accepts Numbers Only").show();
            return;
        }

        int[][] dp = dpSolution(totalHours);
        showDP(dp);
        showSelectedTasksDP(dp, totalHours);
        int greedyValue = greedySolution(totalHours);
        int dpValue = dp[tasks.size()][Math.round(totalHours * 2)];
        compareDPGreedy(dpValue, greedyValue);
    }

    private void showDP(int[][] dp) {
        GridPane grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);

        int rows = dp.length;
        int cols = dp[0].length;

        int width = 80;
        int height = 30;

        Label empty = new Label("");
        empty.setPrefSize(2 * width, height);
        empty.setStyle("-fx-border-color: black; -fx-background-color:" + color2 + ";-fx-alignment: center; -fx-padding: 5;");
        grid.add(empty, 0, 0);

        Label noTask = new Label("No Task");
        noTask.setPrefSize(2 * width, height);
        noTask.setStyle("-fx-text-fill: white;-fx-border-color: black; -fx-alignment: center; -fx-background-color:" + color1 + ";-fx-font-weight: bold;");
        grid.add(noTask, 0, 1);

        for (int i = 0; i < cols; i++) {
            Label l = new Label(i / 2.0f + "h");
            l.setPrefSize(width, height);
            l.setStyle("-fx-text-fill: white;-fx-border-color: black; -fx-alignment: center; -fx-background-color:" + color2 + ";-fx-font-weight: bold;");
            grid.add(l, i + 1, 0);
        }

        for (int i = 1; i <= tasks.size(); i++) {
            Label l = new Label(tasks.get(i - 1).getName());
            l.setPrefSize(2 * width, height);
            l.setStyle("-fx-text-fill: white;-fx-border-color: black; -fx-alignment: center; -fx-background-color:" + color1 + ";-fx-font-weight: bold;");
            grid.add(l, 0, i + 1);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Label l = new Label(String.valueOf(dp[i][j]));
                l.setPrefSize(width, height);
                l.setStyle("-fx-border-color: gray;-fx-text-fill: black; -fx-alignment: center; -fx-background-color:" + color3);
                grid.add(l, j + 1, i + 1);
            }
        }
        grid.setStyle("-fx-background-color: " + color2 + ";");
        dpTable.setContent(grid);
    }


    private void showSelectedTasksDP(int[][] dp, float totalHours) {
        selectedTasks[0].getItems().clear();

        int i = tasks.size();
        int j = Math.round(totalHours * 2);

        while (i > 0 && j > 0) {
            if (dp[i][j] != dp[i - 1][j]) {
                Task t = tasks.get(i - 1);
                selectedTasks[0].getItems().add(t.toString());
                j -= Math.round(t.getTime() * 2);
            }
            i--;
        }
    }

    private int[][] dpSolution(float totalHoursF) {
        long start = System.nanoTime();

        int n = tasks.size();
        int totalHours = Math.round(totalHoursF * 2);

        int[][] dp = new int[n + 1][totalHours + 1];

        for (int i = 1; i <= n; i++) {
            float timeF = tasks.get(i - 1).getTime();
            int time = Math.round(timeF * 2);
            int prod = tasks.get(i - 1).getProdctivity();

            for (int j = 0; j <= totalHours; j++) {
                if (time <= j)
                    dp[i][j] = Math.max(prod + dp[i - 1][j - time], dp[i - 1][j]);
                else
                    dp[i][j] = dp[i - 1][j];
            }
        }
        long end = System.nanoTime();
        timeDP = end - start;
        return dp;
    }

    private int greedySolution(float totalHoursF) {
        myArrayList<String> selectedTasks = new myArrayList<>(tasks.size());
        this.selectedTasks[1].getItems().clear();

        long start = System.nanoTime();
        Task[] arr = new Task[tasks.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = tasks.get(i);

        sortTasks(arr, 0, arr.length - 1);

        int ans = 0, used = 0, temp = Math.round(totalHoursF * 2);
        int last = -1, selected = 0;

        for (int i = 0; i < arr.length; i++) {
            if (temp == 0) break;

            int time = Math.round(arr[i].getTime() * 2);
            if (time <= temp) {
                ans += arr[i].getProdctivity();
                temp -= time;
                used += time;
                last = i;
                selected++;

                selectedTasks.add(arr[i].toString());
            }
        }

        long end = System.nanoTime();
        timeGreedy = end - start;

        for (int i = 0; i < selectedTasks.size(); i++)
            this.selectedTasks[1].getItems().add(selectedTasks.get(i));

        String s0 = "Total Selected Tasks: " + selected;
        String s1;
        if (last == -1)
            s1 = "Last Selected Item: None";
        else
            s1 = "Last Selected Item: " + arr[last].getName();
        String s2 = "Hours Used: " + used / 2.0f + "/" + totalHoursF;
        String s3 = "Remaining Hours: " + ((Math.round(totalHoursF * 2) - used) / 2.0f);
        String s4 = "Total Productivity: " + ans;

        ta[0].setText(s0 + "\n\n" + s1 + "\n\n" + s2 + "\n\n" + s3 + "\n\n" + s4);

        return ans;
    }

    private void compareDPGreedy(int dpValue, int greedyValue) {
        StringBuilder sb = new StringBuilder();

        sb.append("DP Productivity: ").append(dpValue).append("\n");
        sb.append("Greedy Productivity: ").append(greedyValue).append("\n\n");

        sb.append("DP Time: ").append(timeDP).append(" ns\n");
        sb.append("Greedy Time: ").append(timeGreedy).append(" ns\n\n");

        if (dpValue > greedyValue)
            sb.append("DP Gives A Better Solution in Terms Of Productivity.\n");
        else if (greedyValue > dpValue)
            sb.append("Greedy Gives A Better Solution in Terms Of  Productivity.\n");
        else
            sb.append("Both Give The Same Productivity.\n");

        if (timeDP > timeGreedy)
            sb.append("Greedy Gives A Better Solution in Terms Of Time.\n");
        else if (timeGreedy > timeDP)
            sb.append("DP Gives A Better Solution in Terms Of Time.\n");
        else
            sb.append("Both Give The Same Time.\n");

        ta[1].setText(sb.toString());
    }

    private void sortTasks(Task[] a, int l, int r) {
        if (l >= r) return;
        Task p = a[(l + r) / 2];
        int i = l, j = r;

        while (i <= j) {
            while (a[i].compareTo(p) < 0) i++;
            while (a[j].compareTo(p) > 0) j--;
            if (i <= j) {
                Task t = a[i];
                a[i] = a[j];
                a[j] = t;
                i++;
                j--;
            }
        }

        sortTasks(a, l, j);
        sortTasks(a, i, r);
    }

    private void setXY(Node n, double x, double y, double offsetX, double offsetY) {
        n.setLayoutX((maxX / x) + offsetX);
        n.setLayoutY((maxY / y) + offsetY);
    }

    public void runCalculating() {
        calculate.fire();
    }

    public void updateTime(float time) {
        timeTF.setText(String.valueOf(time));
        calculate.fire();
    }
}
