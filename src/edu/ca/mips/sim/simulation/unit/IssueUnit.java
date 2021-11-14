package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Scoreboard;

public class IssueUnit extends FunctionalUnit{
    Scoreboard scoreboard;
    public IssueUnit(Scoreboard scoreboard) {
        super("Issue");
        this.scoreboard = scoreboard;
    }

    public void run(Buffer preIssueBuffer, Buffer preALU1Buffer, Buffer preALU2Buffer) {
        Instruction instruction = preIssueBuffer.peek();
        if (instruction == null) return;

        if (instruction.getMnemonic().equals("LW") || instruction.getMnemonic().equals("SW")) {
            if (preALU1Buffer.isFull()) {
                return;
            }

            IInstruction iInstruction = (IInstruction) instruction;
            if (scoreboard.isRegisterReady(iInstruction)) {
                preALU1Buffer.add(instruction);
            } else {
                return;
            }
        } else {
            if (preALU2Buffer.isFull()) {
                return;
            }

            if (scoreboard.isRegisterReady(instruction)) {
                preALU2Buffer.add(instruction);
            } else {
                return;
            }
        }
        preIssueBuffer.poll();
    }
}
