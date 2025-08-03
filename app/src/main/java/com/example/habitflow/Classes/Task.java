package com.example.habitflow.Classes;

public class Task {
    private String id;
    private String title;
    private boolean isDone;

    public Task() {
    }

    public Task(String id, String title, boolean isDone) {
        this.id = id;
        this.title = title;
        this.isDone = isDone;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public boolean isDone() { return isDone; }
    public void setTitle(String title) { this.title = title; }
    public void setId(String id) { this.id = id; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    public void setDone(boolean done) { isDone = done; }
}

