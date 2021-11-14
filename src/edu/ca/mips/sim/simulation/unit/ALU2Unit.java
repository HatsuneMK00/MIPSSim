package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.Buffer;

public class ALU2Unit extends FunctionalUnit{
    public ALU2Unit() {
        super("ALU2");
    }

    public void run(Buffer preALU2Buffer, Buffer postALU2Buffer) {
        Instruction instruction = preALU2Buffer.poll();
        if (instruction == null) {
            return;
        }
        postALU2Buffer.add(instruction);
    }
}
