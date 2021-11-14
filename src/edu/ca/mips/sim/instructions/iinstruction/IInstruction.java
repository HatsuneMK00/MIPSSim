package edu.ca.mips.sim.instructions.iinstruction;

import edu.ca.mips.sim.instructions.Instruction;

import java.util.List;

public class IInstruction extends Instruction {
    private String rt;
    private String rs;
    private String immediate;

    public IInstruction(int instructionAddress, String mnemonic, String opcode, String binaryCode) {
        super(instructionAddress, mnemonic, opcode, "I");
        this.rs = binaryCode.substring(6, 11);
        this.rt = binaryCode.substring(11,16);
        this.immediate = binaryCode.substring(16, 32);
    }

    public String getRt() {
        return "R" + Integer.parseInt(rt, 2);
    }

    public int getPureRt() {
        return Integer.parseInt(rt, 2);
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getRs() {
        return "R" + Integer.parseInt(rs, 2);
    }

    public int getPureRs() {
        return Integer.parseInt(rs, 2);
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getImmediate() {
        if (getMnemonic().equals("BEQ") ||
                getMnemonic().equals("BGTZ") ||
                getMnemonic().equals("BLTZ")) {
            return String.valueOf(Integer.parseUnsignedInt(immediate, 2) * 4);
        } else {
            return String.valueOf(Integer.parseUnsignedInt(immediate, 2));
        }
    }

    public void setImmediate(String immediate) {
        this.immediate = immediate;
    }

    @Override
    public int execute(List<Integer> registers, List<Integer> memory, int dataStartAddress) {
        switch (getMnemonic()) {
            case "ADDI":
            {
                registers.set(getPureRt(), registers.get(getPureRs()) + Integer.parseInt(getImmediate()));
                break;
            }
            case "ANDI":
            {
                registers.set(getPureRt(), registers.get(getPureRs()) & Integer.parseInt(getImmediate()));
                break;
            }
            case "BEQ":
            {
                if (registers.get(getPureRs()).equals(registers.get(getPureRt()))) {
                    return getInstructionAddress() + 4 + Integer.parseInt(getImmediate());
                }
                break;
            }
            case "BGTZ":
            {
                if (registers.get(getPureRs()) > 0) {
                    return getInstructionAddress() + 4 + Integer.parseInt(getImmediate());
                }
                break;
            }
            case "BLTZ":
            {
                if (registers.get(getPureRs()) < 0) {
                    return getInstructionAddress() + 4 + Integer.parseInt(getImmediate());
                }
                break;
            }
            case "LW":
            {
                registers.set(getPureRt(), memory.get((registers.get(getPureRs()) + Integer.parseInt(getImmediate()) - dataStartAddress) / 4));
                break;
            }
            case "SW":
            {
                memory.set((registers.get(getPureRs()) + Integer.parseInt(getImmediate()) - dataStartAddress) / 4, registers.get(getPureRt()));
                break;
            }
            case "ORI":
            {
                registers.set(getPureRt(), registers.get(getPureRs()) | Integer.parseInt(getImmediate()));
                break;
            }
            case "XORI":
            {
                registers.set(getPureRt(), registers.get(getPureRs()) ^ Integer.parseInt(getImmediate()));
                break;
            }
        }
        return getInstructionAddress() + 4;
    }

    @Override
    public String toString() {
        switch (getMnemonic()) {
            case "LW":
            case "SW":
            {
                return getMnemonic() + " " + getRt() + ", " + getImmediate() + "(" + getRs() + ")";
            }
            case "BEQ":
            {
                return getMnemonic() + " " + getRs() + ", " + getRt() + ", " + "#" + getImmediate();
            }
            case "BGTZ":
            case "BLTZ":
            {
                return getMnemonic() + " " + getRs() + ", " + "#" + getImmediate();
            }
        }
        return getMnemonic() + " " + getRt() + ", " + getRs() + ", " + "#" + getImmediate();
    }
}
