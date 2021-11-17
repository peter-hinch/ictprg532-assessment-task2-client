package dev.peterhinch.assessmenttask2.room.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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
  @TypeConverters({Converters.class})
  public Date date;

  public Record() { }

  @Ignore
  public Record(String heading, String description, String phone, Date date) {
    this.heading = heading;
    this.description = description;
    this.phone = phone;
    this.date = date;
  }

  @Ignore
  public Record(int id, String heading, String description, String phone, Date date) {
    this.id = id;
    this.heading = heading;
    this.description = description;
    this.phone = phone;
    this.date = date;
  }

  @Ignore
  public int getId() {
    return id;
  }

  @Ignore
  public void setId(int id) {
    this.id = id;
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

  @NonNull
  @Override
  public String toString() {
    return "id: " + this.id +
        "\nheading: " + this.heading +
        "\ndescription: " + this.description +
        "\nphone: " + this.phone +
        "\ndate: " + this.date;
  }
}
