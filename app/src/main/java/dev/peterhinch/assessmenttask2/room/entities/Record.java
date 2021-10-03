package dev.peterhinch.assessmenttask2.room.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "record")
public class Record {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "heading")
    public String heading;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "date")
    public Date date;

    public Record() {}

    // TODO - Will likely need Record functionality to be brought in from models
}
