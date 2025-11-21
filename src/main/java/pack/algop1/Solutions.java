package pack.algop1;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Screen;

public class Solutions {

    private final myArrayList<Task> tasks;

    private final ScrollPane dpScroll = new ScrollPane();
    private final ListView<String> selectedTasksListDP = new ListView<>();
    private final ListView<String> selectedTasksListGreedy = new ListView<>();
    private final TextArea greedyTA = new TextArea();
    private final TextArea comparisonTA = new TextArea();
    private final UI ui;
    private final TextField timeTF = new TextField();
    private final Button calculate = new Button("Calculate");
    private final double maxX, maxY;
    private long timeDP, timeGreedy;

    public Solutions(UI ui) {
        this.ui = ui;
        tasks = ui.getTasks();

        maxX = Screen.getPrimary().getVisualBounds().getMaxX();
        maxY = Screen.getPrimary().getVisualBounds().getMaxY();

        dpScroll.setPrefViewportWidth(maxX / 2);
        dpScroll.setPrefViewportHeight(maxY / 3);
        dpScroll.setFitToHeight(false);
        dpScroll.setFitToWidth(false);
        dpScroll.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 200);
        dpScroll.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight() / 5);

        selectedTasksListDP.setPrefHeight(150);
        selectedTasksListGreedy.setPrefHeight(150);

        greedyTA.setEditable(false);
        greedyTA.setWrapText(true);
        greedyTA.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 200);
        greedyTA.setPrefHeight(dpScroll.getMaxHeight());

        comparisonTA.setEditable(false);
        comparisonTA.setWrapText(true);
        comparisonTA.setPrefHeight(145);
        comparisonTA.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2 - 200);
    }

    public Pane p() {
        Label[] labels = new Label[]{
                new Label("Total Hours:"), new Label("Dynamic Solution"), new Label("Chosen Tasks"),
                new Label("Greedy Solution"), new Label("Chosen Tasks"),
                new Label("DP vs Greedy Comparison"), new Label("Dynamic Programming vs Greedy"), new Label()};

        Line[] lines = new Line[]{
                new Line(0, maxY / 1.5, maxX, maxY / 1.5),
                new Line(maxX / 2, maxY / 12, maxX / 2, maxY / 1.5),
                new Line(0, maxY / 12, maxX, maxY / 12)};

        HBox hb = new HBox(20, labels[0], timeTF, calculate);

        VBox subDpGroup = new VBox(40, dpScroll, selectedTasksListDP);
        VBox dpGroup = new VBox(5, subDpGroup, labels[7]);
        VBox greedyGroup = new VBox(40, greedyTA, selectedTasksListGreedy);

        setXY(dpGroup, maxX, 7, maxX / 24, -100);
        setXY(greedyGroup, 2, 7, maxX / 20, 0);
        setXY(comparisonTA, 3, 1, -70, -(maxY / 2) + 250);
        setXY(hb, 2, maxY, -155, 60);

        labelSettings(labels);
        hb.setAlignment(Pos.CENTER);
        dpGroup.setPadding(new Insets(100, 10, 10, 10));

        calculate.setOnAction(e -> calculate());

        Pane p = new Pane(hb, dpGroup, greedyGroup, comparisonTA);
        p.getChildren().addAll(lines);

        for (int i = 1; i < labels.length - 1; i++)
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
        setXY(l[5], 2, 1, -100, -(maxY / 2) + 200);
        setXY(l[6], 2, maxY, -160, 5);
        l[6].setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
        l[7].setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        l[7].setText("DP Relation:\nif (Task Time ≤ j):\n dp[i][j] = max( Task Productivity + dp[i−1][j−Task Time] , dp[i−1][j] )\n\nelse:\ndp[i][j] = dp[i−1][j]");
    }

    private void calculate() {
        int totalHours;
        try {
            totalHours = Integer.parseInt(timeTF.getText());
            ui.setTotalHours(totalHours);
        } catch (Exception ex) {
            totalHours = 0;
        }

        int[][] dp = dpSolution(totalHours);
        showDP(dp);
        showSelectedTasksDP(dp, totalHours);
        int greedyValue = greedySolution(totalHours);
        int dpValue = dp[tasks.size()][totalHours];
        compareDPGreedy(dpValue, greedyValue);
    }

    private int[][] dpSolution(int totalHours) {
        if (totalHours <= 0)
            throw new IllegalArgumentException("Total Hours Must Be Greater Than 0");

        long n1 = System.nanoTime();
        int n = tasks.size();
        int[][] dp = new int[n + 1][totalHours + 1];

        for (int i = 1; i <= n; i++) {
            int time = tasks.get(i - 1).getTime();
            int prod = tasks.get(i - 1).getProdctivity();

            for (int j = 1; j <= totalHours; j++) {
                if (time <= j)
                    dp[i][j] = Math.max(prod + dp[i - 1][j - time], dp[i - 1][j]);
                else
                    dp[i][j] = dp[i - 1][j];
            }
        }
        long n2 = System.nanoTime();
        timeDP = n2 - n1;
        return dp;
    }

    private void showSelectedTasksDP(int[][] dp, int totalHours) {
        selectedTasksListDP.getItems().clear();

        int i = tasks.size();
        int j = totalHours;

        while (i > 0 && j > 0) {
            if (dp[i][j] != dp[i - 1][j]) {
                Task t = tasks.get(i - 1);
                selectedTasksListDP.getItems().add(t.toString());
                j -= t.getTime();
            }
            i--;
        }
    }

    private void showDP(int[][] dp) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(8);

        int rows = dp.length;
        int cols = dp[0].length;

        grid.add(new Label(""), 0, 0);
        for (int w = 0; w < cols; w++) {
            grid.add(new Label(w + "h"), w + 1, 0);
        }

        grid.add(new Label("No Task"), 0, 1);
        for (int i = 1; i <= tasks.size(); i++) {
            grid.add(new Label(tasks.get(i - 1).getName()), 0, i + 1);
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid.add(new Label(String.valueOf(dp[i][j])), j + 1, i + 1);
            }
        }

        grid.setStyle("-fx-background-color: #bca4ec");
        dpScroll.setContent(grid);
    }

    private int greedySolution(int totalHours) {
        myArrayList<String> selectedItems = new myArrayList<>(tasks.size());
        selectedTasksListGreedy.getItems().clear();

        long n1 = System.nanoTime();
        Task[] arr = new Task[tasks.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = tasks.get(i);

        sortTasks(arr, 0, arr.length - 1);

        int ans = 0, used = 0, temp = totalHours;
        int last = -1, selected = 0;

        for (int i = 0; i < arr.length; i++) {
            if (temp == 0) break;

            if (arr[i].getTime() <= temp) {

                ans += arr[i].getProdctivity();
                temp -= arr[i].getTime();
                used += arr[i].getTime();
                last = i;
                selected++;

                selectedItems.add(arr[i].toString());
            }
        }

        long n2 = System.nanoTime();
        timeGreedy = n2 - n1;

        for (int i = 0; i < selectedItems.size(); i++)
            selectedTasksListGreedy.getItems().add(selectedItems.get(i));

        String s0 = "Total Selected Tasks: " + selected;
        String s1;
        if (last == -1)
            s1 = "Last Selected Item: None";
        else
            s1 = "Last Selected Item: " + arr[last].getName();
        String s2 = "Hours Used: " + used + "/" + totalHours;
        String s3 = "Remaining Hours: " + (totalHours - used);
        String s4 = "Total Productivity: " + ans;

        greedyTA.setText(s0 + "\n" + s1 + "\n" + s2 + "\n" + s3 + "\n" + s4);

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

        comparisonTA.setText(sb.toString());
    }

    private void sortTasks(Task[] a, int l, int r) {
        if (l >= r) return;
        Task p = a[(l + r) / 2];
        int i = l, j = r;

        while (i <= j) {
            while (a[i].getProdctivity() > p.getProdctivity()) i++;
            while (a[j].getProdctivity() < p.getProdctivity()) j--;
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

    public void setXY(Node n, double x, double y, double offsetX, double offsetY) {
        n.setLayoutX((maxX / x) + offsetX);
        n.setLayoutY((maxY / y) + offsetY);
    }

    public void updateTime(int time) {
        timeTF.setText(String.valueOf(time));
        calculate.fire();
    }
}
