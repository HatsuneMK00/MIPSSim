package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Config;
import edu.ca.mips.sim.simulation.Scoreboard;

import java.util.List;

public class IFUnit extends FunctionalUnit{
    Instruction waitingInstruction;
    Instruction executedInstruction;

    public IFUnit() {
        super("IF");
    }

    public int run(List<Instruction> program, List<Integer> registers, List<Integer> memory, int dataStartAddress, Buffer preIssueBufferSnapshot, Buffer preIssueBuffer, int pc, Scoreboard scoreboard) {
        executedInstruction = null;
        if (waitingInstruction != null) {
            IInstruction iInstruction = (IInstruction) waitingInstruction;
            if (scoreboard.isRegisterReady(iInstruction)) {
                executedInstruction = waitingInstruction;
                waitingInstruction = null;
                return iInstruction.execute(registers, memory, dataStartAddress);
            }
            return pc;
        }
        if (preIssueBufferSnapshot.isFull()) {
            return pc;
        }

        int instructionIndex = (pc - Config.START_PC_VALUE) / 4;
        Instruction instruction = program.get(instructionIndex);
        if (instruction.getMnemonic().equals("BREAK")) {
            executedInstruction = instruction;
            return -1;
        }
        if (isBranchInstruction(instruction)) {
            if (instruction.getMnemonic().equals("BEQ") ||
                    instruction.getMnemonic().equals("BLTZ") ||
                    instruction.getMnemonic().equals("BGTZ")) {
                IInstruction iInstruction = (IInstruction) instruction;
                if (scoreboard.isRegisterReady(iInstruction)) {
                    executedInstruction = instruction;
                    return iInstruction.execute(registers, memory, dataStartAddress);
                } else {
                    waitingInstruction = instruction;
                    return pc;
                }
            } else {
                executedInstruction = instruction;
                return instruction.execute(registers, memory, dataStartAddress);
            }
        } else {
            preIssueBuffer.add(instruction);
            preIssueBufferSnapshot.add(instruction);
        }
        return pc + 4;
    }

    public boolean isBranchInstruction(Instruction instruction) {
        return instruction.getMnemonic().equals("J") ||
                instruction.getMnemonic().equals("JR") ||
                instruction.getMnemonic().equals("BEQ") ||
                instruction.getMnemonic().equals("BLTZ") ||
                instruction.getMnemonic().equals("BGTZ");
    }

    public Instruction getWaitingInstruction() {
        return waitingInstruction;
    }

    public Instruction getExecutedInstruction() {
        return executedInstruction;
    }
}
