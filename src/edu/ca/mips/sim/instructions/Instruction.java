package edu.ca.mips.sim.instructions;

import java.util.List;

public abstract class Instruction {
    private int instructionAddress;

    private String mnemonic;  // 指令名称
    private String opcode;
    private String type;  // 指令类型

    public Instruction(int instructionAddress, String mnemonic, String opcode, String type) {
        this.instructionAddress = instructionAddress;
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.type = type;
    }

    public Instruction() {
    }

    abstract public int execute(List<Integer> registers, List<Integer> memory, int dataStartAddress);

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public int getInstructionAddress() {
        return instructionAddress;
    }

    public void setInstructionAddress(int instructionAddress) {
        this.instructionAddress = instructionAddress;
    }
}
