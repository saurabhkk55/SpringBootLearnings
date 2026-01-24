package PriorityStream;

public class Priority implements Comparable<Priority> {
    String task;
    int priority;

    public Priority(String task, int priority) {
        this.task = task;
        this.priority = priority;
    }

    public String getTask() {
        return task;
    }

    public int getPriority() {
        return priority;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Priority o) {
        // return Integer.valueOf(this.getPriority()).compareTo(Integer.valueOf(o.getPriority()));
        if(this.getPriority() > o.getPriority()) return 1;
        else if (this.getPriority() == o.getPriority()) return this.getTask().compareTo(o.getTask());
        else return -1;
    }
}
