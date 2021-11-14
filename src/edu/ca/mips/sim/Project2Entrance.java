package edu.ca.mips.sim;

import edu.ca.mips.sim.data.Data;
import edu.ca.mips.sim.instructions.Instruction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static void simulate() {
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
            Map<String, Object> result = disassembler.parse(binaryCodes);
            List<Instruction> assembleProgram = disassembler.getAssembleProgram(result);
            List<Data> programData = disassembler.getProgramData(result);

            Simulator simulator = new Simulator(assembleProgram, programData);
            simulator.simulate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
