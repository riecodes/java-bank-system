/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package banksystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
/**
 *
 * @author eirmo
 */
public class CSVUserHelper {
    private static final String CSV_FILE_PATH = "user.csv";

    // Create
    public static void addRecord(String[] record, String[] columnNames) throws IOException, CsvException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH, true))) {
            String[] formattedRecord = new String[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                int index = getColumnIndex(columnNames[i]);
                if (index != -1 && index < record.length) {
                    formattedRecord[i] = record[index];
                } else {
                    formattedRecord[i] = ""; // Placeholder for missing data
                }
            }
            // Set default balance value to "0" if the balance field is empty
            if (getColumnIndex("balance") != -1 && record.length > getColumnIndex("balance") && formattedRecord[getColumnIndex("balance")].isEmpty()) {
                formattedRecord[getColumnIndex("balance")] = "0";
            }
            writer.writeNext(formattedRecord);
        }
    }
    
    public static void addRecordWithDefault(String[] record, String[] columnNames) throws IOException, CsvException {
        // Create CSVWriter object with FileWriter
        CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH, true));

        // Check if the record length matches the columnNames length
        if (record.length != columnNames.length) {
            throw new IllegalArgumentException("Record length must match columnNames length.");
        }

        // Iterate through the record array and replace empty strings with "*"
        for (int i = 0; i < record.length; i++) {
            if (record[i].isEmpty()) {
                record[i] = "*"; // Replace empty strings with "*"
            }
        }

        // Write record to CSV file
        writer.writeNext(record);

        // Close the writer
        writer.close();
    }


    // Read
    public static List<String[]> readAllRecords() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            return reader.readAll();
        }
    }

    // Update
    public static void updateRecord(int row, String[] newRecord) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        if (row >= 0 && row < records.size()) {
            records.set(row, newRecord);
            try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
                writer.writeAll(records);
            }
        }
    }

    // Delete
    public static void deleteRecord(int row) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        if (row >= 0 && row < records.size()) {
            records.remove(row);
            try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
                writer.writeAll(records);
            }
        }
    }

    // Authenticate user
    public static boolean authenticateUser(String userID, String password) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        int userIDIndex = getColumnIndex("userID");
        int passwordIndex = getColumnIndex("password");
        if (userIDIndex != -1 && passwordIndex != -1) {
            for (String[] record : records) {
                if (record.length > userIDIndex && record.length > passwordIndex &&
                        record[userIDIndex].equals(userID) && record[passwordIndex].equals(password)) {
                    return true; // User authenticated
                }
            }
        }
        return false; // User not found or password does not match
    }
    
    public static int getColumnIndex(String columnName) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        if (!records.isEmpty()) {
            String[] headers = records.get(0);
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase(columnName)) {
                    return i;
                }
            }
        }
        return -1; // Column not found
    }
    
    public static void updateRecordByUserID(String userID, String[] newRecord) throws IOException, CsvException {
        List<String[]> records = readAllRecords(); // Read all records from the CSV file
        for (int i = 0; i < records.size(); i++) {
            String[] record = records.get(i);
            if (record.length > 0 && record[0].equals(userID)) { // Check if the user ID matches
                records.set(i, newRecord); // Update the record
                break; // Stop searching after updating the record
            }
        }
        // Write the updated records back to the CSV file
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.writeAll(records);
        }
    }

    public static boolean userExists(String userID) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        for (String[] record : records) {
            if (record[0].equals(userID)) {
                return true;
            }
        }
        return false;
    }
    
    public static double getUserBalance(String userID) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        for (String[] record : records) {
            if (record[0].equals(userID)) {
                if (record.length > 4) {
                    return Double.parseDouble(record[4]);
                } else {
                    // Handle the case when the balance field is not available
                    return 0;
                }
            }
        }
        return 0; // Return 0 if user is not found
    }



    public static void updateUserBalance(String userID, double newBalance) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        for (String[] record : records) {
            if (record[0].equals(userID)) {
                record[4] = String.valueOf(newBalance); // Update the balance field
                updateRecordByUserID(userID, record); // Update the record in the CSV file
                return;
            }
        }
    }

}
