package dev.peterhinch.assessmenttask2.models;

import java.util.Comparator;

public class CustomComparator implements Comparator<Record> {
    @Override
    public int compare(Record record1, Record record2) {
        return record1.getHeading().compareTo(record2.getHeading());
    }
}
