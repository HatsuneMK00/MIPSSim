package edu.ca.mips.sim.instructions.jinstruction;

import edu.ca.mips.sim.instructions.Instruction;

import java.util.List;

public class JInstruction extends Instruction {
    private String address;

    public JInstruction(int instructionAddress, String mnemonic, String opcode, String binaryCode) {
        super(instructionAddress, mnemonic, opcode, "J");
        this.address = binaryCode.substring(6, 32);
    }

    @Override
    public int execute(List<Integer> registers, List<Integer> memory, int dataStartAddress) {
        return Integer.parseInt(getAddress());
    }

    public String getAddress() {
        return String.valueOf(Integer.parseInt(address, 2) * 4);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return getMnemonic() + " #" + getAddress();
    }
}
