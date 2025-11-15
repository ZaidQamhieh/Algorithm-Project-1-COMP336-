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
    private final Label maxDP = new Label();
    public Solutions(myArrayList<Task> tasks) {
        this.tasks = tasks;
        dpScroll.setPrefViewportWidth(Screen.getPrimary().getVisualBounds().getWidth());
        dpScroll.setPrefViewportHeight(Screen.getPrimary().getVisualBounds().getHeight() / 3);
        dpScroll.setFitToHeight(false);
        dpScroll.setFitToWidth(false);
        selectedTasksListDP.setPrefHeight(150);
        selectedTasksListGreedy.setPrefHeight(150);
    }

    public VBox p() {
        Label totalHoursLabel = new Label("Total Hours");
        Button calculate = new Button("Calculate");
        TextField tf = new TextField();

        Label dpLabel = new Label("Dynamic Solution");
        Label gLabel = new Label("Greedy Solution");
        Label chosenLabel = new Label("Chosen Tasks (DP)");

        HBox hbox = new HBox(20, totalHoursLabel, tf, calculate);
        VBox vbox = new VBox(25, dpLabel, hbox, dpScroll, chosenLabel, selectedTasksListDP,maxDP, gLabel, greedyTA, new Label("Chosen Tasks (Greedy)"), selectedTasksListGreedy);

        calculate.setOnAction(e -> {
            int totalHours = 0;
            try {
                totalHours = Integer.parseInt(tf.getText());
            } catch (Exception ignored) {
            }
            int[][] dp = DynamicSolution(totalHours);
            showDP(dp);
            showSelectedTasksDP(dp, totalHours);
            greedySolution(totalHours);
        });

        return vbox;
    }

    private int[][] DynamicSolution(int totalHours) {
        int n = tasks.size();
        int[][] dp = new int[n + 1][totalHours + 1];

        for (int i = 1; i <= n; i++) {
            int taskTime = tasks.get(i - 1).getTime();
            int taskValue = tasks.get(i - 1).getProdctivity();

            for (int w = 1; w <= totalHours; w++) {
                if (taskTime <= w)
                    dp[i][w] = Math.max(taskValue + dp[i - 1][w - taskTime], dp[i - 1][w]);
                else
                    dp[i][w] = dp[i - 1][w];
            }
        }
        maxDP.setText("Max Productivity (DP)= "+dp[n][totalHours]);
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
        for (int w = 0; w < cols; w++)
            grid.add(new Label(w + "h"), w + 1, 0);

        for (int i = 0; i < rows; i++) {
            grid.add(new Label("Task: " + i), 0, i + 1);
            for (int j = 0; j < cols; j++)
                grid.add(new Label(String.valueOf(dp[i][j])), j + 1, i + 1);
        }

        grid.setStyle("-fx-background-color: #bca4ec");
        grid.setPrefWidth(dpScroll.getPrefWidth());
        grid.setPrefHeight(dpScroll.getPrefHeight());
        dpScroll.setContent(grid);
    }

    private void greedySolution(int totalHours) {
        Task[] taskArray = new Task[tasks.size()];
        for (int i = 0; i < taskArray.length; i++)
            taskArray[i] = tasks.get(i);

        quickSort(taskArray, 0, taskArray.length - 1);


        int ans = 0;
        int usedHours = 0;
        int tempTotalHours = totalHours;
        int lastSelectedIndex = -1;
        int selected = 0;
        for (int i = 0; i < taskArray.length; i++) {
            if (tempTotalHours == 0)
                break;

            if (taskArray[i].getTime() <= tempTotalHours) {
                ans += taskArray[i].getProdctivity();
                tempTotalHours -= taskArray[i].getTime();
                lastSelectedIndex = i;
                usedHours += taskArray[i].getTime();
                selected++;
                selectedTasksListGreedy.getItems().add(taskArray[i].toString());
            }
        }

        greedyTA.setEditable(false);
        greedyTA.setWrapText(true);
        String s0 = "Total Selected Tasks: " + selected;
        String s1;
        if (lastSelectedIndex == -1)
            s1 = "Last Selected Item: None";
        else
            s1 = "Last Selected Item: " + taskArray[lastSelectedIndex].getName();
        String s3 = "Hours Used : " + usedHours + "/" + totalHours;
        String s4 = "Remaining Hours: " + (totalHours - usedHours);
        String s5 = "Total Productivity: " + ans;
        String s6 = s0 + "\n" + s1 + "\n" + s3 + "\n" + s4 + "\n" + s5;
        greedyTA.setText(s6);
    }

    public <T extends Comparable<T>> void quickSort(T[] a, int l, int r) {
        if (l >= r) return;
        T pivot = a[(l + r) / 2];
        int i = l, j = r;
        while (i <= j) {
            while (a[i].compareTo(pivot) < 0) i++;
            while (a[j].compareTo(pivot) > 0) j--;
            if (i <= j) {
                T tmp = a[i];
                a[i] = a[j];
                a[j] = tmp;
                i++;
                j--;
            }
        }
        quickSort(a, l, j);
        quickSort(a, i, r);
    }
}
