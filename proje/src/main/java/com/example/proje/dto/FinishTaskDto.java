package com.example.proje.dto;

import java.util.Date;

public class FinishTaskDto {
    private Date finishDate;
    private String message;
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
