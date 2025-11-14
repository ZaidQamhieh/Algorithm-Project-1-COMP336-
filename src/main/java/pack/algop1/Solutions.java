package pack.algop1;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;

public class Solutions {

    private final myArrayList<Task> tasks;
    private final ScrollPane dpScroll = new ScrollPane();

    public Solutions(myArrayList<Task> tasks) {
        this.tasks = tasks;
        dpScroll.setPrefViewportWidth(Screen.getPrimary().getVisualBounds().getWidth());
        dpScroll.setPrefViewportHeight(Screen.getPrimary().getVisualBounds().getHeight()/2);
        dpScroll.setFitToHeight(false);
        dpScroll.setFitToWidth(false);
    }

    public Pane tab() {
        return new Pane(dpTable());
    }

    private VBox dpTable() {
        Label totalHoursLabel = new Label("Total Hours");
        TextField tf = new TextField();
        Button calculate = new Button("Calculate");
        Label[] l = new Label[]{new Label("Dynamic Solution"),new Label("Greedy Solution")};
        HBox hbox = new HBox(totalHoursLabel, tf, calculate);
        VBox vbox = new VBox(hbox, dpScroll);

        calculate.setOnAction(e -> {
            int totalHours = 0;
            try { totalHours = Integer.parseInt(tf.getText()); } catch (Exception ignored) {}
            int[][] dp = DynamicSolution(totalHours);
            showDP(dp);
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

        return dp;
    }

    private void showDP(int[][] dp) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(8);
        int rows = dp.length;
        int cols = dp[0].length;
        grid.add(new Label(""), 0, 0);
        for (int w = 0; w < cols; w++)
            grid.add(new Label(w+"h"), w + 1, 0);
        for (int i = 0; i < rows; i++) {
            grid.add(new Label("Task: "+i), 0, i + 1);
            for (int j = 0; j < cols; j++)
                grid.add(new Label(String.valueOf(dp[i][j])), j + 1, i + 1);
        }
        dpScroll.setContent(grid);
    }

    private void greedySolution(int totalHours) {
        Task[] taskArray = new Task[tasks.size()];
        for (int i = 0; i < taskArray.length; i++)
            taskArray[i] = tasks.get(i);

        quickSort(taskArray, 0, taskArray.length - 1);

        int ans = 0;
        for (Task t : taskArray) {
            if (t.getTime() <= totalHours) {
                ans += t.getProdctivity();
                totalHours -= t.getTime();
            }
        }
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
