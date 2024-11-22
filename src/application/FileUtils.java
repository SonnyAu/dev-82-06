package application;

import java.io.File;

public class FileUtils {

    /**
     * Checks if a file is empty or does not exist.
     *
     * @param filePath The path to the file.
     * @return true if the file is empty or does not exist, false otherwise.
     */
    public static boolean isFileEmpty(String filePath) {
        File file = new File(filePath);
        return !file.exists() || file.length() == 0;
    }
}
