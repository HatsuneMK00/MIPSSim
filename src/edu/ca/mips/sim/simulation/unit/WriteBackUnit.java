package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.instructions.rinstruction.RInstruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Scoreboard;

import java.util.List;
import java.util.SortedMap;

public class WriteBackUnit extends FunctionalUnit{

    public WriteBackUnit() {
        super("WriteBack");
    }

    public void run(List<Integer> registers, List<Integer> memory, int dataStartAddress, Buffer postMemBuffer, Buffer postALU2Buffer, Scoreboard scoreboard
    ) {
        Instruction instruction = postMemBuffer.poll();
        Instruction instruction1 = postALU2Buffer.poll();

        if (instruction != null) {
            instruction.execute(registers, memory, dataStartAddress);
            scoreboard.removeRegisterResultStatus(((IInstruction) instruction).getPureRt());
            scoreboard.removeFunctionalUnitStatus(instruction);
        }
        if (instruction1 != null) {
            instruction1.execute(registers, memory, dataStartAddress);
            if (instruction1 instanceof IInstruction) {
                scoreboard.removeRegisterResultStatus(((IInstruction) instruction1).getPureRt());
            } else if (instruction1 instanceof RInstruction) {
                scoreboard.removeRegisterResultStatus(((RInstruction) instruction1).getPureRd());
            }
            scoreboard.removeFunctionalUnitStatus(instruction1);
        }
    }
}
