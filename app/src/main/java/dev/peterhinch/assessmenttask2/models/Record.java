package dev.peterhinch.assessmenttask2.models;

import java.util.Date;

public class Record {
    // TODO - Will likely need Record functionality moved across to room entities

    private String heading;
    private String description;
    private String phone;
    private Date date;

    public Record(String heading, String description, String phone, Date date) {
        this.heading = heading;
        this.description = description;
        this.phone = phone;
        this.date = date;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
