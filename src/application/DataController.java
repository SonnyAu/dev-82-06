package application;

import java.io.File;
import java.io.FileWriter;

public class DataController<T> {

    private String[] ACCOUNT_HEADERS;
    private final Class<T> type;
    private String fileName;
    private String filePath;
    private String DATA_DIR = "src/data/";

    public DataController (Class<T> type) {
        this.type = type;
        this.fileName = getDirLoc(type);
        this.filePath = DATA_DIR + fileName;
        createCSV();
    }

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

    private void createCSV() {
        try {
            File f = new File(filePath);
            if (f.createNewFile()) {
                createHeaders(filePath, ACCOUNT_HEADERS);
                System.out.println("File created at: " + filePath);
            } else {
                System.out.println("File edited at: " + filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void writeToCSV(String dirPath, String[] data) {
        try (FileWriter fw = new FileWriter(dirPath, true)) {
            for (int i = 0; i < data.length; i++) {
                fw.append(data[i]);
                if (i != data.length - 1) {
                    fw.append(",");
                }
            }
            fw.append("\n");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public String getFilePath() {
        return filePath;
    }
}
