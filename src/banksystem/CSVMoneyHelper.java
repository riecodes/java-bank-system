package banksystem;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVMoneyHelper {
    private static final String CSV_FILE_PATH = "money.csv";

    // Read all records
    public static List<String[]> readAllRecords() throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
            return reader.readAll();
        }
    }

    // Update record
    public static void updateRecord(int row, String[] newRecord) throws IOException, CsvException {
        List<String[]> records = readAllRecords();
        if (row >= 0 && row < records.size()) {
            records.set(row, newRecord);
            try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
                writer.writeAll(records);
            }
        }
    }

    // Write all records
    public static void writeAllRecords(List<String[]> records) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(CSV_FILE_PATH))) {
            writer.writeAll(records);
        }
    }
}
