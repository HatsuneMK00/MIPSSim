package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.Buffer;

import java.util.List;

public class WriteBackUnit extends FunctionalUnit{
    public WriteBackUnit() {
        super("WriteBack");
    }

    public void run(List<Integer> registers, List<Integer> memory, int dataStartAddress, Buffer postMemBuffer, Buffer postALU2Buffer
    ) {
        Instruction instruction = postMemBuffer.poll();
        Instruction instruction1 = postALU2Buffer.poll();

        instruction.execute(registers, memory, dataStartAddress);
        instruction1.execute(registers, memory, dataStartAddress);
    }
}
