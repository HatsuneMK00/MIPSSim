package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Scoreboard;

import java.util.List;

public class MemUnit extends FunctionalUnit{
    public MemUnit() {
        super("Mem");
    }

    public void run(List<Integer> registers, List<Integer> memory, int dataStartAddress, Buffer preMemBufferSnapshot, Buffer preMemBuffer, Buffer postMemBuffer, Scoreboard scoreboard) {
        Instruction instruction = preMemBufferSnapshot.peek();
        if (instruction == null) {
            return;
        }
        preMemBuffer.poll();
        if (instruction.getMnemonic().equals("SW")) {
            instruction.execute(registers, memory, dataStartAddress);
            scoreboard.removeRegisterResultStatus(((IInstruction) instruction).getPureRt());
        } else {
            postMemBuffer.add(instruction);
        }
    }
}
