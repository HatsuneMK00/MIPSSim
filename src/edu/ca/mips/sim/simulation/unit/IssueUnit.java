package edu.ca.mips.sim.simulation.unit;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.instructions.rinstruction.RInstruction;
import edu.ca.mips.sim.simulation.Buffer;
import edu.ca.mips.sim.simulation.Scoreboard;

public class IssueUnit extends FunctionalUnit{
    public IssueUnit() {
        super("Issue");
    }

    public void run(Buffer preIssueBufferSnapshot, Buffer preIssueBuffer, Buffer preALU1Buffer, Buffer preALU2Buffer, Scoreboard scoreboard) throws CloneNotSupportedException {
        Scoreboard scoreboardSnapshot = (Scoreboard) scoreboard.clone();
        for (int i = 0; i < preIssueBufferSnapshot.size; i++) {
            Instruction instruction = preIssueBufferSnapshot.get(i);
            if (instruction == null) return;

            if (instruction.getMnemonic().equals("LW") || instruction.getMnemonic().equals("SW")) {
                if (preALU1Buffer.isFull()) {
                    return;
                }

                IInstruction iInstruction = (IInstruction) instruction;
                if (scoreboardSnapshot.isRegisterReady(iInstruction)) {
                    preALU1Buffer.add(instruction);
                    preIssueBuffer.remove(i);
                    scoreboard.setRegisterResultStatus(this, iInstruction.getPureRt());
//                    scoreboard.setFunctionalUnitStatus(instruction);
                    break;
                } else {
                    scoreboardSnapshot.setRegisterResultStatus(this, iInstruction.getPureRt());
                    scoreboardSnapshot.setFunctionalUnitStatus(instruction);
                }
            } else {
                if (preALU2Buffer.isFull()) {
                    return;
                }

                if (scoreboardSnapshot.isRegisterReady(instruction)) {
                    preALU2Buffer.add(instruction);
                    preIssueBuffer.remove(i);
                    if (instruction instanceof IInstruction) {
                        scoreboard.setRegisterResultStatus(this, ((IInstruction) instruction).getPureRt());
                    } else if (instruction instanceof RInstruction) {
                        scoreboard.setRegisterResultStatus(this, ((RInstruction) instruction).getPureRd());
                    }
//                    scoreboard.setFunctionalUnitStatus(instruction);
                    break;
                } else {
                    if (instruction instanceof IInstruction) {
                        scoreboardSnapshot.setRegisterResultStatus(this, ((IInstruction) instruction).getPureRt());
                    } else if (instruction instanceof RInstruction) {
                        scoreboardSnapshot.setRegisterResultStatus(this, ((RInstruction) instruction).getPureRd());
                    }
                    scoreboardSnapshot.setFunctionalUnitStatus(instruction);
                }
            }
        }
    }
}
