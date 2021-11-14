package edu.ca.mips.sim;

import edu.ca.mips.sim.data.Data;
import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.instructions.jinstruction.JInstruction;
import edu.ca.mips.sim.instructions.rinstruction.RInstruction;
import edu.ca.mips.sim.utils.FileUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disassembler {

    HashMap<String, String> opcode2Instruction = new HashMap<>();
    final int startingAddress = 256;

    final String resultFilepath = "result/disassembly.txt";

    public Disassembler() {
        opcode2Instruction.put("010000", "J");
        opcode2Instruction.put("010001", "JR");
        opcode2Instruction.put("010010", "BEQ");
        opcode2Instruction.put("010011", "BLTZ");
        opcode2Instruction.put("010100", "BGTZ");
        opcode2Instruction.put("010101", "BREAK");
        opcode2Instruction.put("010110", "SW");
        opcode2Instruction.put("010111", "LW");
        opcode2Instruction.put("011000", "SLL");
        opcode2Instruction.put("011001", "SRL");
        opcode2Instruction.put("011010", "SRA");
        opcode2Instruction.put("011011", "NOP");

        opcode2Instruction.put("110000", "ADD");
        opcode2Instruction.put("110001", "SUB");
        opcode2Instruction.put("110010", "MUL");
        opcode2Instruction.put("110011", "AND");
        opcode2Instruction.put("110100", "OR");
        opcode2Instruction.put("110101", "XOR");
        opcode2Instruction.put("110110", "NOR");
        opcode2Instruction.put("110111", "SLT");
        opcode2Instruction.put("111000", "ADDI");
        opcode2Instruction.put("111001", "ANDI");
        opcode2Instruction.put("111010", "ORI");
        opcode2Instruction.put("111011", "XORI");
    }

    public Map<String, Object> parse(List<String> binaryCodes) {
        ArrayList<Instruction> assembleProgram = new ArrayList<>();
        ArrayList<Data> programData = new ArrayList<>();
        HashMap<String, Object> resultObj = new HashMap<>();
        resultObj.put("assembleProgram", assembleProgram);
        resultObj.put("programData", programData);
        int i;
        int programAddress;
        for (i = 0, programAddress = startingAddress; i < binaryCodes.size(); i++, programAddress+=4) {
            String binaryCode = binaryCodes.get(i);
            String opcode = binaryCode.substring(0, 6);
            String mnemonic = opcode2Instruction.get(opcode);

            switch (mnemonic) {
                case "ADD":
                case "AND":
                case "BREAK":
                case "JR":
                case "MUL":
                case "NOP":
                case "NOR":
                case "OR":
                case "SLL":
                case "SLT":
                case "SRA":
                case "SRL":
                case "SUB":
                case "XOR":
                {
                    assembleProgram.add(new RInstruction(programAddress, mnemonic, opcode, binaryCode));
                    break;
                }
                case "J":
                {
                    assembleProgram.add(new JInstruction(programAddress, mnemonic, opcode, binaryCode));
                    break;
                }
                case "ADDI":
                case "ANDI":
                case "BEQ":
                case "BGTZ":
                case "BLTZ":
                case "LW":
                case "ORI":
                case "SW":
                case "XORI":
                {
                    assembleProgram.add(new IInstruction(programAddress, mnemonic, opcode, binaryCode));
                    break;
                }
            }
            if (mnemonic.equals("BREAK")) {
                break;
            }
        }
        for (i = i + 1, programAddress+=4; i < binaryCodes.size(); i++, programAddress+=4) {
            String binaryCode = binaryCodes.get(i);
            programData.add(new Data(programAddress, binaryCode));
        }
        return resultObj;
    }

    public void parseAndPrintResult(List<String> binaryCodes) throws IOException {
        Map<String, Object> parseResult = parse(binaryCodes);
        List<Instruction> assembleProgram = getAssembleProgram(parseResult);
        List<Data> programData = getProgramData(parseResult);

        PrintWriter out = FileUtil.getPrintWriter(resultFilepath);
        if (out == null) {
            return;
        }

        for (Instruction instruction : assembleProgram) {
            out.print(instruction.getInstructionAddress() + "\t");
            out.println(instruction.toString());
        }
        for (Data programDatum : programData) {
            out.print(programDatum.getDataAddress() + "\t");
            out.println(programDatum.toString());
        }
        out.close();
    }

    public List<Instruction> getAssembleProgram(Map<String, Object> result) {
        return (List<Instruction>) result.get("assembleProgram");
    }

    public List<Data> getProgramData(Map<String, Object> result) {
        return (List<Data>) result.get("programData");
    }
}
