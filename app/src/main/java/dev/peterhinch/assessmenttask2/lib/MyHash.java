package dev.peterhinch.assessmenttask2.lib;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import dev.peterhinch.assessmenttask2.models.CustomComparator;
import dev.peterhinch.assessmenttask2.models.Record;

public class MyHash {
    // Declare an array of ArrayLists to contain Contact objects.
    private static ArrayList<Record>[] hashTable;

    public MyHash() {
        hashTable = new ArrayList[27];
        for(int i = 0; i < hashTable.length; i++){
            hashTable[i] = new ArrayList<>();
        }
    }

    // Re-link all the elements to a linked list.
    // Note: - Elements are not copied.
    //       - The hashtable links are still there.
    //
    // Example:
    // ***Hash table view***
    // A => Alan, Alex, Amahli
    // B => Bob
    // C => Cali, Cindy
    // ***Linked list view***
    // Alan, Alex, Amahli, Bob, Cali, Cindy
    public ArrayList<Record> toList(boolean reverse) {
        ArrayList<Record> c = new ArrayList<>();
        for (ArrayList<Record> contacts : hashTable) {
            c.addAll(contacts);
        }
        if(reverse) {
            Collections.reverse(c);
        }
        return c;
    }

    // For a specific key, this function calculates the offset of the element
    // with a first letter starting with the "key" in the ArrayList.
    // Example: In the following list entering a key of 3 (for letter 'C')
    // will return 4, as the first name in the list starting with 'C' is at
    // index 4.
    // A => Alan, Alex, Amahli
    // B => Bob
    // C => Cali, Cindy
    public int calcOffsetByKey(int k) {
        int offset = 0;
        if (k >= 0 && k < hashTable.length) {
            for (int i = 0; i < k; i++) {
                // Offset is the sum of the size of all preceding lists.
                offset = offset + hashTable[i].size();
            }
        }
        return offset;
    }

    // Build a hash table. Use an ArrayList to resolve collision.
    // Sort all the individual ArrayLists.
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buildHashTable(ArrayList<Record> list) {
        if(list == null) {
            return;
        }

        for (Record c : list) {
            int hashTableIndex = hash(c.getHeading());
            hashTable[hashTableIndex].add(c);
        }

        for (ArrayList<Record> contacts : hashTable) {
            contacts.sort(new CustomComparator());
        }
    }

    // Hash the first char of a String.
    // First char: # A B C D E F G H I J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z
    // Hash key:   0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26
    // Input: String
    // Output: Hash key
    public static int hash(String s) {
        // Get the first char and convert to uppercase.
        char c = s.toUpperCase().charAt(0);
        // Convert the char to it's ASCII value.
        int asciiValue = (int)c;
        if(asciiValue >= 65 && asciiValue <= 90) {
            asciiValue = asciiValue - 64;
        } else {
            asciiValue = 0;
        }
        return asciiValue;
    }

    // Additional functionality to allow searching of the hash table.
    public ArrayList<Record> filter(String query) {
        ArrayList<Record> results = new ArrayList<>();
        for (ArrayList<Record> contacts : hashTable) {
            for (int j = 0; j < contacts.size(); j++) {
                if (Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                        .matcher(contacts
                                .get(j)
                                .getHeading())
                        .find()) {
                    results.add(contacts.get(j));
                }
            }
        }
        return results;
    }
}
