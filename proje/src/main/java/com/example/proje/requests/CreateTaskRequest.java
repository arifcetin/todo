package com.example.proje.requests;

import java.util.Date;

public class CreateTaskRequest {
    private String text;
    private boolean finished;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}