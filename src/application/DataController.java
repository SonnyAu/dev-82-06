package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

// this class handles all the data reading, writing, accessing
public class DataController<T> {

    private String[] ACCOUNT_HEADERS;
    private final Class<T> type;
    private String fileName;
    private String filePath;
    private String DATA_DIR = "src/data/";

    // this constructor is mostly used when creating a new file
    public DataController (Class<T> type) {
        this.type = type;
        this.fileName = getDirLoc(type);
        this.filePath = DATA_DIR + fileName;
        createCSV();
    }

    // this constructor is mostly used when accessing a file by provinding the filename
    public DataController(String fileName) {
        this.filePath = DATA_DIR + fileName;
        this.type = null;
    }

    // this method sets the headers when creating csv files and also denotes which file we work with
    public String getDirLoc(Class<T> type) {
        String typeName = type.getSimpleName();
        // we can add more cases later for other data types we save (transactions etc...)
        switch (typeName) {
            case "AccountModel":
                ACCOUNT_HEADERS = new String[]{"name", "balance", "date"};
                return "accounts.csv";
            default:
                return "";
        }
    }

    // this method creates a csv and adds headers to it
    private void createCSV() {
        try {
            File f = new File(filePath);
            if (f.createNewFile()) {
//                createHeaders(filePath, ACCOUNT_HEADERS);
                System.out.println("File created at: " + filePath);
            } else {
                System.out.println("File edited at: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this method writes headers to blank csvs
    private void createHeaders(String dirPath, String[] headers) {
        try (FileWriter fw = new FileWriter(dirPath, true)){
            for (int i = 0; i < headers.length; i++) {
                fw.append(headers[i]);
                if (i != headers.length - 1) {
                    fw.append(",");
                }
            }
            fw.append("\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this method writes to a csv ensuring no duplicate entries
// DATA INPUT IS AN ARRAY OF STRINGS ex: ["name", "100", "2024-10-24"]
    public void writeToCSV(String dirPath, String[] data) {
        try {
            // Read existing data to check for duplicates
            ArrayList<String[]> existingData = readFromCSV();

            // Check for duplicate entry
            boolean isDuplicate = existingData.stream().anyMatch(
                    row -> compareData(row, data) // Compare each row with new data
            );

            if (!isDuplicate) {
                try (FileWriter fw = new FileWriter(dirPath, true)) {
                    for (int i = 0; i < data.length; i++) {
                        fw.append(data[i]);
                        if (i != data.length - 1) {
                            fw.append(",");
                        }
                    }
                    fw.append("\n");
                }
            } else {
                System.out.println("Duplicate entry. Data not written to: " + dirPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to compare two data entries
    private boolean compareData(String[] existingRow, String[] newRow) {
        if (existingRow.length != newRow.length) return false;
        for (int i = 0; i < existingRow.length; i++) {
            if (!existingRow[i].equals(newRow[i])) {
                return false;
            }
        }
        return true;
    }

    // returns the filepath
    public String getFilePath() {
        return filePath;
    }

    // reads the csv and returns an arraylist of string arrays for each line
    // ex: [ ["name", "100", "2024-10-24"], ["name", "100", "2024-10-24"] ]
    public ArrayList<String[]> readFromCSV() {
        ArrayList<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip header line
//            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                data.add(values);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return data;
    }
}
