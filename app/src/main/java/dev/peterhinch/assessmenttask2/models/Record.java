package dev.peterhinch.assessmenttask2.models;

import java.util.Date;

public class Record {
    private final String heading;
    private final String description;
    private final String phone;
    private final Date date;

    public Record(String heading, String description, String phone, Date date) {
        this.heading = heading;
        this.description = description;
        this.phone = phone;
        this.date = date;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public Date getDate() {
        return date;
    }
}
