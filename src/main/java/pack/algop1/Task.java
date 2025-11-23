package pack.algop1;

public class Task implements Comparable<Task> {
    private String name;
    private float time;
    private int prodctivity;

    public Task(String name, float time, int prodctivity) {
        this.name = name;
        this.time = time;
        this.prodctivity = prodctivity;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Task)) return false;

        Task other = (Task) obj;
        return name.equals(other.name);
    }

    @Override
    public int compareTo(Task o) {
        return Double.compare((double) o.prodctivity / o.time, (double) prodctivity / time);
    }

    @Override
    public String toString() {
        return "[Name: " + name + ", " + "Time: " + time + ", Prodctivity: " + prodctivity + "]";
    }
}
