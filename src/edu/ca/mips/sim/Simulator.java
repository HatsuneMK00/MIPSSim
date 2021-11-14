package edu.ca.mips.sim;

import edu.ca.mips.sim.data.Data;
import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Config;
import edu.ca.mips.sim.simulation.Pipeline;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Simulator {
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

    public void simulate() {
        int pc = programStartAddress;
        while (pc != -1) {
            pc = pipeline.runCycle(mProgram, registers, memory, dataStartAddress, pc);
            printSystemStatus();
            cycle++;
        }
    }

    private void printSystemStatus() {
        System.out.println("--------------------");
        System.out.println("Cycle:" + cycle);
        System.out.println();
        System.out.println("IF Unit:");
        System.out.print("\tWaiting Instruction: ");
        Instruction instruction = pipeline.getIfUnit().getWaitingInstruction();
        if (instruction != null) {
            System.out.println("[" + instruction.toString() + "]");
        } else {
            System.out.println();
        }
        System.out.print("\tExecuted Instruction: ");
        instruction = pipeline.getIfUnit().getExecutedInstruction();
        if (instruction != null) {
            System.out.println("[" + instruction.toString() + "]");
        } else {
            System.out.println();
        }
        System.out.println("Pre-Issue Queue:");
        Buffer buffer = pipeline.getPreIssueBuffer();
        for (int i = 0; i < Config.MAX_PRE_ISSUE_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println("Pre-ALU1 Queue:");
        buffer = pipeline.getPreALU1Buffer();
        for (int i = 0; i < Config.MAX_PRE_ALU_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println("Pre-MEM Queue:");
        buffer = pipeline.getPreMemBuffer();
        for (int i = 0; i < Config.MAX_PRE_MEM_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println("Post-MEM Queue:");
        buffer = pipeline.getPostMemBuffer();
        for (int i = 0; i < Config.MAX_POST_MEM_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println("Pre-ALU2 Queue:");
        buffer = pipeline.getPreALU2Buffer();
        for (int i = 0; i < Config.MAX_PRE_ALU_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println("Post-ALU2 Queue:");
        buffer = pipeline.getPostALU2Buffer();
        for (int i = 0; i < Config.MAX_POST_ALU2_BUFFER_SIZE; i++) {
            System.out.print("\tEntry " + i + ": ");
            instruction = buffer.get(i);
            if (instruction != null) {
                System.out.println("[" + instruction.toString() + "]");
            } else {
                System.out.println();
            }
        }
        System.out.println();
        printRegisterAndMemory();
    }

    public void initialize() {
        for (Data datum : mProgramData) {
            memory.add(datum.getValue());
        }
        for (int i = 0; i < 32; i++) {
            registers.add(0);
        }
    }

    public void printRegisterAndMemory(){
        System.out.println();
        System.out.println("Registers");
        for (int i = 0; i < registers.size(); i++) {
            if (i % 8 == 0) {
                System.out.printf("R%02d:\t", i);
            }
            if (i % 8 == 7) {
                System.out.print(registers.get(i));
                System.out.println();
            } else {
                System.out.print(registers.get(i) + "\t");
            }
        }
        System.out.println();
        System.out.println("Data");
        for (int i = 0; i < memory.size(); i++) {
            if (i % 8 == 0) {
                System.out.printf("%03d:\t", dataStartAddress + i * 4);
            }
            if (i % 8 == 7) {
                System.out.print(memory.get(i));
                System.out.println();
            } else {
                System.out.print(memory.get(i) + "\t");
            }
        }
        System.out.println();
    }
}
