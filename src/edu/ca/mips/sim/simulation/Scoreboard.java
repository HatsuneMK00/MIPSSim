package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.instructions.iinstruction.IInstruction;
import edu.ca.mips.sim.instructions.rinstruction.RInstruction;
import edu.ca.mips.sim.simulation.unit.FunctionalUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scoreboard {
    List<FunctionalUnit> registerResultStatus = new ArrayList<>();
    Map<String, FunctionalUnitStatus> functionalUnitStatusMap = new HashMap<>();

    public Scoreboard() {
        for (int i = 0; i < 32; i++) {
            registerResultStatus.add(null);
        }
    }

    public boolean isRegisterReady(Instruction instruction) {
        Instruction castedInstruction = instruction;
        switch (instruction.getMnemonic()) {
            case "ADD":
            case "AND":
            case "BREAK":
            case "JR":
            case "MUL":
            case "NOP":
            case "NOR":
            case "OR":
            case "SLL":
            case "SLT":
            case "SRA":
            case "SRL":
            case "SUB":
            case "XOR":
            {
                castedInstruction = (RInstruction) instruction;
                break;
            }
            case "ADDI":
            case "ANDI":
            case "BEQ":
            case "BGTZ":
            case "BLTZ":
            case "LW":
            case "ORI":
            case "SW":
            case "XORI":
            {
                castedInstruction = (IInstruction) instruction;
                break;
            }
        }
        if (castedInstruction instanceof IInstruction) {
            return registerResultStatus.get(((IInstruction) castedInstruction).getPureRs()) == null &&
                    registerResultStatus.get(((IInstruction) castedInstruction).getPureRt()) == null;
        } else if (castedInstruction instanceof RInstruction) {
            return registerResultStatus.get(((RInstruction) castedInstruction).getPureRt()) == null &&
                    registerResultStatus.get(((RInstruction) castedInstruction).getPureRs()) == null &&
                    registerResultStatus.get(((RInstruction) castedInstruction).getPureRd()) == null;
        }
        return false;
    }
}
