package edu.ca.mips.sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Project2Entrance {
    public static void main(String[] args) {
        disassemble();
    }

    public static void disassemble() {
        String sampleFilepath = "files/sample.txt";

        File sample = new File(sampleFilepath);

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sample)));
            ArrayList<String> binaryCodes = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                binaryCodes.add(line);
            }

            Disassembler disassembler = new Disassembler();
            disassembler.parseAndPrintResult(binaryCodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
