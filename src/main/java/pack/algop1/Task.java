package pack.algop1;

public class Task implements Comparable<Task> {
    // Task Attributes
    private String name;
    private float time;
    private int prodctivity;

    public Task(String name, float time, int prodctivity) {
        this.name = name;
        this.time = time;
        this.prodctivity = prodctivity;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getProdctivity() {
        return prodctivity;
    }

    public void setProdctivity(int prodctivity) {
        this.prodctivity = prodctivity;
    }
    // Checks if The Object is Equal to Another Object
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;

        Task other = (Task) obj;
        return name.equals(other.name);
    }
    // Compares Two Objects By Their productivity / time Ratio
    @Override
    public int compareTo(Task o) {
        return Double.compare((double) o.prodctivity / o.time, (double) prodctivity / time);
    }
    // To Display Object Information When Printing
    @Override
    public String toString() {
        return "[Name: " + name + ", " + "Time: " + time + ", Prodctivity: " + prodctivity + "]";
    }
}
