package edu.ca.mips.sim.utils;

import java.io.*;

public class FileUtil {
    public static PrintWriter getPrintWriter(String resultFilepath) throws IOException {
        File resultFile = new File(resultFilepath);
        if (!resultFile.getParentFile().exists()) {
            if (!resultFile.getParentFile().mkdirs()) {
                return null;
            }
        }
        if (!resultFile.exists()) {
            resultFile.createNewFile();
        }
        return new PrintWriter(new BufferedWriter(new FileWriter(resultFile, false)));
    }
}
