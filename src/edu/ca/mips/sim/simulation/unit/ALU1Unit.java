package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.simulation.Buffer;

import java.util.List;

public class ALU1Unit extends FunctionalUnit{
    public ALU1Unit() {
        super("ALU1");
    }

    public void run(Buffer preALU1Buffer, Buffer preMemBuffer) {
        Instruction instruction = preALU1Buffer.poll();
        if (instruction == null) {
            return;
        }
        preMemBuffer.add(instruction);
    }
}
