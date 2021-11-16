package edu.ca.mips.sim;

import edu.ca.mips.sim.data.Data;
import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Config;
import edu.ca.mips.sim.simulation.Pipeline;
import edu.ca.mips.sim.utils.FileUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
    final private String resultFilepath = "result/simulation.txt";

    List<Instruction> mProgram;
    List<Data> mProgramData;

    private final List<Integer> registers = new ArrayList<>();
    private final List<Integer> memory = new ArrayList<>();
    private final int dataStartAddress;
    private final int dataEndAddress;
    private final int programStartAddress;

    Pipeline pipeline;
    int cycle = 1;

    public Simulator(List<Instruction> mProgram, List<Data> programData) {
        this.mProgram = mProgram;
        this.mProgramData = programData;
        this.dataStartAddress = mProgramData.get(0).getDataAddress();
        this.dataEndAddress = mProgramData.get(mProgramData.size() - 1).getDataAddress();
        this.programStartAddress = Config.START_PC_VALUE;

        pipeline = new Pipeline();

        initialize();
    }

    public void simulate() throws CloneNotSupportedException, IOException {
        int pc = programStartAddress;
        PrintWriter out = FileUtil.getPrintWriter(resultFilepath);
        if (out == null) {
            return;
        }
        while (pc != -1) {
            pc = pipeline.runCycle(mProgram, registers, memory, dataStartAddress, pc);
            printSystemStatus(out);
            cycle++;
        }
        out.close();
    }

    private void printSystemStatus(PrintWriter out) throws IOException {

        out.println("--------------------");
        out.println("Cycle:" + cycle);
        out.println();
        out.println("IF Unit:");
        out.print("\tWaiting Instruction:");
        Instruction instruction = pipeline.getIfUnit().getWaitingInstruction();
        if (instruction != null) {
            out.println(" [" + instruction.toString() + "]");
        } else {
            out.println();
        }
        out.print("\tExecuted Instruction:");
        instruction = pipeline.getIfUnit().getExecutedInstruction();
        if (instruction != null) {
            out.println(" [" + instruction.toString() + "]");
        } else {
            out.println();
        }
        out.println("Pre-Issue Queue:");
        Buffer buffer = pipeline.getPreIssueBuffer();
        for (int i = 0; i < Config.MAX_PRE_ISSUE_BUFFER_SIZE; i++) {
            out.print("\tEntry " + i + ":");
            instruction = buffer.get(i);
            if (instruction != null) {
                out.println(" [" + instruction.toString() + "]");
            } else {
                out.println();
            }
        }
        out.println("Pre-ALU1 Queue:");
        buffer = pipeline.getPreALU1Buffer();
        for (int i = 0; i < Config.MAX_PRE_ALU_BUFFER_SIZE; i++) {
            out.print("\tEntry " + i + ":");
            instruction = buffer.get(i);
            if (instruction != null) {
                out.println(" [" + instruction.toString() + "]");
            } else {
                out.println();
            }
        }
        out.print("Pre-MEM Queue:");
        buffer = pipeline.getPreMemBuffer();
        instruction = buffer.get(0);
        if (instruction != null) {
            out.println(" [" + instruction.toString() + "]");
        } else {
            out.println();
        }
        out.print("Post-MEM Queue:");
        buffer = pipeline.getPostMemBuffer();
        instruction = buffer.get(0);
        if (instruction != null) {
            out.println(" [" + instruction.toString() + "]");
        } else {
            out.println();
        }
        out.println("Pre-ALU2 Queue:");
        buffer = pipeline.getPreALU2Buffer();
        for (int i = 0; i < Config.MAX_PRE_ALU_BUFFER_SIZE; i++) {
            out.print("\tEntry " + i + ":");
            instruction = buffer.get(i);
            if (instruction != null) {
                out.println(" [" + instruction.toString() + "]");
            } else {
                out.println();
            }
        }
        out.print("Post-ALU2 Queue:");
        buffer = pipeline.getPostALU2Buffer();
        instruction = buffer.get(0);
        if (instruction != null) {
            out.println(" [" + instruction.toString() + "]");
        } else {
            out.println();
        }
        printRegisterAndMemory(out);
    }

    public void initialize() {
        for (Data datum : mProgramData) {
            memory.add(datum.getValue());
        }
        for (int i = 0; i < 32; i++) {
            registers.add(0);
        }
    }

    public void printRegisterAndMemory(PrintWriter out){
        out.println();
        out.println("Registers");
        for (int i = 0; i < registers.size(); i++) {
            if (i % 8 == 0) {
                out.printf("R%02d:\t", i);
            }
            if (i % 8 == 7) {
                out.print(registers.get(i));
                out.println();
            } else {
                out.print(registers.get(i) + "\t");
            }
        }
        out.println();
        out.println("Data");
        for (int i = 0; i < memory.size(); i++) {
            if (i % 8 == 0) {
                out.printf("%03d:\t", dataStartAddress + i * 4);
            }
            if (i % 8 == 7) {
                out.print(memory.get(i));
                out.println();
            } else {
                out.print(memory.get(i) + "\t");
            }
        }
    }
}
