package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.instructions.rinstruction.RInstruction;
import edu.ca.mips.sim.simulation.unit.FunctionalUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard implements Cloneable {
    List<FunctionalUnit> registerResultStatus = new ArrayList<>();
    Map<Instruction, FunctionalUnitStatus> functionalUnitStatusMap = new HashMap<>();

    public Scoreboard() {
        for (int i = 0; i < 32; i++) {
            registerResultStatus.add(null);
        }
    }

    public void setFunctionalUnitStatus(Instruction instruction) { // WAR hazard
        if (instruction instanceof IInstruction) {
            FunctionalUnitStatus functionalUnitStatus = new FunctionalUnitStatus();
            functionalUnitStatus.busy = true;
            functionalUnitStatus.fi = ((IInstruction) instruction).getPureRt();
            functionalUnitStatus.fj = ((IInstruction) instruction).getPureRs();
            functionalUnitStatus.rj = registerResultStatus.get(functionalUnitStatus.fj) == null;
            functionalUnitStatusMap.put(instruction, functionalUnitStatus);
        } else if (instruction instanceof RInstruction) {
            FunctionalUnitStatus functionalUnitStatus = new FunctionalUnitStatus();
            functionalUnitStatus.busy = true;
            functionalUnitStatus.fi = ((RInstruction) instruction).getPureRd();
            functionalUnitStatus.fj = ((RInstruction) instruction).getPureRt();
            functionalUnitStatus.fk = ((RInstruction) instruction).getPureRs();
            functionalUnitStatus.rj = registerResultStatus.get(functionalUnitStatus.fj) == null;
            functionalUnitStatus.rk = registerResultStatus.get(functionalUnitStatus.fk) == null;
            functionalUnitStatusMap.put(instruction, functionalUnitStatus);
        }
    }

    public void removeFunctionalUnitStatus(Instruction instruction) {
        functionalUnitStatusMap.remove(instruction);
    }

    public boolean isRegisterReady(Instruction instruction) {
        if (instruction instanceof IInstruction) {
            boolean ready;
            ready = registerResultStatus.get(((IInstruction) instruction).getPureRt()) == null
                    && registerResultStatus.get(((IInstruction) instruction).getPureRs()) == null;
            for (FunctionalUnitStatus functionalUnitStatus : functionalUnitStatusMap.values()) {
                if (((IInstruction) instruction).getPureRt() == functionalUnitStatus.fj) {
                    ready = ready && !functionalUnitStatus.rj;
                }
            }
            return ready;
        } else if (instruction instanceof RInstruction) {
            boolean ready;
            ready = registerResultStatus.get(((RInstruction) instruction).getPureRt()) == null &&
                    registerResultStatus.get(((RInstruction) instruction).getPureRs()) == null &&
                    registerResultStatus.get(((RInstruction) instruction).getPureRd()) == null;
            for (FunctionalUnitStatus functionalUnitStatus : functionalUnitStatusMap.values()) {
                if (((RInstruction) instruction).getPureRd() == functionalUnitStatus.fj) {
                    ready = ready && !functionalUnitStatus.rj;
                }
                if (((RInstruction) instruction).getPureRd() == functionalUnitStatus.fk) {
                    ready = ready && !functionalUnitStatus.rk;
                }
            }
            return ready;
        }
        return false;
    }

    public void setRegisterResultStatus(FunctionalUnit functionalUnit, int register) {
        registerResultStatus.set(register, functionalUnit);
    }

    public void removeRegisterResultStatus(int register) {
        registerResultStatus.set(register, null);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Scoreboard scoreboard = (Scoreboard) super.clone();
        scoreboard.registerResultStatus = new ArrayList<>(scoreboard.registerResultStatus);
        scoreboard.functionalUnitStatusMap = new HashMap<>(scoreboard.functionalUnitStatusMap);
        return scoreboard;
    }
}
