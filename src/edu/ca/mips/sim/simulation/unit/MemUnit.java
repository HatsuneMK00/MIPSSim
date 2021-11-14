package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.Buffer;

import java.util.List;

public class MemUnit extends FunctionalUnit{
    public MemUnit() {
        super("Mem");
    }

    public void run(List<Integer> registers, List<Integer> memory, int dataStartAddress, Buffer preMemBuffer, Buffer postMemBuffer) {
        Instruction instruction = preMemBuffer.poll();
        if (instruction == null) {
            return;
        }
        if (instruction.getMnemonic().equals("SW")) {
            instruction.execute(registers, memory, dataStartAddress);
        } else {
            postMemBuffer.add(instruction);
        }
    }
}
