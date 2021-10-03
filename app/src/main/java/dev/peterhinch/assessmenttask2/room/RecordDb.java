package dev.peterhinch.assessmenttask2.room;

import java.util.ArrayList;
import java.util.Date;

import dev.peterhinch.assessmenttask2.models.Record;

public class RecordDb {
    // TODO - Implement room functionality

    // Static variable single_instance of type Singleton.
    private static RecordDb db_instance = null;

    private final ArrayList<Record> records;

    // Private constructor restricted to this class itself.
    private RecordDb() {
        records = new ArrayList<>();

        // Add sample data.
        addContact(new Record("Amon", "Description 01", "0123 456 789", new Date(2021/1/1)));
        addContact(new Record("Dominik", "Description 02", "0456 789 123", new Date(2021/2/2)));
        addContact(new Record("Marlowe", "Description 03", "0789 123 456", new Date(2021/3/3)));
        addContact(new Record("Max", "Description 04", "0123 456 789", new Date(2021/4/4)));
        addContact(new Record("Luke", "Description 05", "0456 789 123", new Date(2021/5/5)));
        addContact(new Record("Jon", "Description 06", "0789 123 456", new Date(2021/6/6)));
        addContact(new Record("Keith", "Description 07", "0123 456 789", new Date(2021/7/7)));
        addContact(new Record("Zef", "Description 08", "0456 789 123", new Date(2021/8/8)));
        addContact(new Record("Azeem", "Description 09", "0789 123 456", new Date(2021/9/9)));
        addContact(new Record("Rob", "Description 10", "0123 456 789", new Date(2021/10/10)));
        addContact(new Record("Steve", "Description 11", "0456 789 123", new Date(2021/11/11)));
        addContact(new Record("Martyn", "Description 12", "0789 123 456", new Date(2021/12/12)));
        addContact(new Record("Don", "Description 13", "0123 456 789", new Date(2021/1/1)));
        addContact(new Record("Pablo", "Description 14", "0456 789 123", new Date(2021/2/2)));
        addContact(new Record("Zackey", "Description 15", "0789 123 456", new Date(2021/3/3)));
        addContact(new Record("Mike", "Description 16", "0123 456 789", new Date(2021/4/4)));
        addContact(new Record("Edan", "Description 17", "0456 789 123", new Date(2021/5/5)));
        addContact(new Record("Stacey", "Description 18", "0789 123 456", new Date(2021/6/6)));
        addContact(new Record("Danny", "Description 19", "0123 456 789", new Date(2021/7/7)));
        addContact(new Record("David", "Description 20", "0456 789 123", new Date(2021/8/8)));
        addContact(new Record("Jojo", "Description 21", "0789 123 456", new Date(2021/9/9)));
        addContact(new Record("Jay", "Description 22", "0123 456 789", new Date(2021/10/10)));
        addContact(new Record("Isaiah", "Description 23", "0456 789 123", new Date(2021/11/11)));
        addContact(new Record("Hugh", "Description 24", "0789 123 456", new Date(2021/12/12)));
        addContact(new Record("Talib", "Description 25", "0123 456 789", new Date(2021/1/1)));
        addContact(new Record("Anderson", "Description 26", "0456 789 123", new Date(2021/2/2)));
        addContact(new Record("Iggy", "Description 27", "0789 123 456", new Date(2021/3/3)));
        addContact(new Record("Emily", "Description 28", "0123 456 789", new Date(2021/4/4)));
        addContact(new Record("Kanye", "Description 29", "0456 789 123", new Date(2021/5/5)));
        addContact(new Record("Marlena", "Description 30", "0789 123 456", new Date(2021/6/6)));
    }

    // Static method to create instance of Singleton class.
    public static RecordDb getInstance() {
        if (db_instance == null)
            db_instance = new RecordDb();
        return db_instance;
    }

    // Add a record.
    public void addContact(Record newRecord) {
        records.add(newRecord);
    }

    // List records.
    public ArrayList<Record> readRecords() {
        return records;
    }

    // Dump DB data to console for debug purpose
    public void dumpToConsole() {
        System.out.println("Dump records data ");
        System.out.println("Number of records: " + records.size());
        for (int i = 0; i < records.size(); i++){
            Record c = records.get(i);
            System.out.println("(" + i + ") Heading: " + c.getHeading() +
                    "; Description: " + c.getDescription() + "; Phone: " +
                    c.getPhone() + "; Date: " + c.getDate());
        }
    }
}
