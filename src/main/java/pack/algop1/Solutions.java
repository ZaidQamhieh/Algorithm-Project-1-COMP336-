package pack.algop1;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class Solutions {

    private final myArrayList<Task> tasks;

    private final ScrollPane dpScroll = new ScrollPane();
    private final ListView<String> selectedTasksListDP = new ListView<>();
    private final ListView<String> selectedTasksListGreedy = new ListView<>();
    private final TextArea greedyTA = new TextArea();
    private final TextArea comparisonTA = new TextArea();
    private final Label maxDP = new Label();

    public Solutions(myArrayList<Task> tasks) {
        this.tasks = tasks;

        double w = Screen.getPrimary().getVisualBounds().getWidth();
        double h = Screen.getPrimary().getVisualBounds().getHeight();

        dpScroll.setPrefViewportWidth(w);
        dpScroll.setPrefViewportHeight(h / 3);
        dpScroll.setFitToHeight(false);
        dpScroll.setFitToWidth(false);

        selectedTasksListDP.setPrefHeight(150);
        selectedTasksListGreedy.setPrefHeight(150);

        greedyTA.setEditable(false);
        greedyTA.setWrapText(true);

        comparisonTA.setEditable(false);
        comparisonTA.setWrapText(true);
        comparisonTA.setPrefHeight(150);
    }

    public VBox p() {

        Label totalHoursLabel = new Label("Total Hours");
        TextField tf = new TextField();
        Button calculate = new Button("Calculate");

        HBox hbox = new HBox(20, totalHoursLabel, tf, calculate);

        VBox vbox = new VBox(
                20,
                new Label("Dynamic Solution"),
                hbox,
                dpScroll,
                new Label("Chosen Tasks (DP)"),
                selectedTasksListDP,
                maxDP,
                new Label("Greedy Solution"),
                greedyTA,
                new Label("Chosen Tasks (Greedy)"),
                selectedTasksListGreedy,
                new Label("DP vs Greedy Comparison"),
                comparisonTA
        );

        calculate.setOnAction(e -> {
            int totalHours;
            try {
                totalHours = Integer.parseInt(tf.getText());
            } catch (Exception ex) {
                totalHours = 0;
            }

            int[][] dp = DynamicSolution(totalHours);
            showDP(dp);
            showSelectedTasksDP(dp, totalHours);

            int greedyValue = greedySolution(totalHours);
            int dpValue = dp[tasks.size()][totalHours];

            compareDPGreedy(dpValue, greedyValue);
        });

        return vbox;
    }

    private int[][] DynamicSolution(int totalHours) {
        int n = tasks.size();
        int[][] dp = new int[n + 1][Math.max(1, totalHours + 1)];

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

        maxDP.setText("Max Productivity (DP) = " + dp[n][totalHours]);
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

        for (int i = 0; i < rows; i++) {
            grid.add(new Label("Task: " + i), 0, i + 1);
            for (int j = 0; j < cols; j++) {
                grid.add(new Label(String.valueOf(dp[i][j])), j + 1, i + 1);
            }
        }

        grid.setStyle("-fx-background-color: #bca4ec");
        dpScroll.setContent(grid);
    }

    private int greedySolution(int totalHours) {

        selectedTasksListGreedy.getItems().clear();

        Task[] arr = new Task[tasks.size()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = tasks.get(i);

        quickSort(arr, 0, arr.length - 1);

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

                selectedTasksListGreedy.getItems().add(arr[i].toString());
            }
        }

        String s0 = "Total Selected Tasks: " + selected;
        String s1 = (last == -1 ? "Last Selected Item: None" :
                "Last Selected Item: " + arr[last].getName());
        String s2 = "Hours Used: " + used + "/" + totalHours;
        String s3 = "Remaining Hours: " + (totalHours - used);
        String s4 = "Total Productivity: " + ans;

        greedyTA.setText(s0 + "\n" + s1 + "\n" + s2 + "\n" + s3 + "\n" + s4);

        return ans;
    }

    private void compareDPGreedy(int dpValue, int greedyValue) {

        StringBuilder sb = new StringBuilder();

        sb.append("Dynamic Programming Productivity: ").append(dpValue).append("\n");
        sb.append("Greedy Productivity: ").append(greedyValue).append("\n\n");

        if (dpValue > greedyValue) {
            sb.append("DP gives a better solution.\n");
            sb.append("DP is higher by ").append(dpValue - greedyValue).append(" productivity points.\n\n");
            sb.append("Explanation: DP checks every possible combination of tasks, ");
            sb.append("while Greedy only selects based on sorted order, which can miss better combinations.\n");
        }
        else if (greedyValue > dpValue) {
            sb.append("Greedy gives a better solution (this is unusual).\n");
        }
        else {
            sb.append("Both methods produced the same total productivity.\n");
        }

        comparisonTA.setText(sb.toString());
    }

    public <T extends Comparable<T>> void quickSort(T[] a, int l, int r) {
        if (l >= r) return;

        T pivot = a[(l + r) / 2];
        int i = l, j = r;

        while (i <= j) {
            while (a[i].compareTo(pivot) < 0) i++;
            while (a[j].compareTo(pivot) > 0) j--;

            if (i <= j) {
                T temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                i++;
                j--;
            }
        }

        quickSort(a, l, j);
        quickSort(a, i, r);
    }
}
