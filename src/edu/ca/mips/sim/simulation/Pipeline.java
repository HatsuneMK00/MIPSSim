package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.unit.*;

import java.util.List;

public class Pipeline {
    Buffer preIssueBuffer = new Buffer("preIssueBuffer", Config.MAX_PRE_ISSUE_BUFFER_SIZE);
    Buffer preALU1Buffer = new Buffer("preALU1Buffer", Config.MAX_PRE_ALU_BUFFER_SIZE);
    Buffer preALU2Buffer = new Buffer("preALU2Buffer", Config.MAX_PRE_ALU_BUFFER_SIZE);
    Buffer preMemBuffer = new Buffer("preMemBuffer", Config.MAX_PRE_MEM_BUFFER_SIZE);
    Buffer postALU2Buffer = new Buffer("postALU2Buffer", Config.MAX_POST_ALU2_BUFFER_SIZE);
    Buffer postMemBuffer = new Buffer("postMemBuffer", Config.MAX_POST_MEM_BUFFER_SIZE);

    IFUnit ifUnit;
    ALU1Unit alu1Unit;
    ALU2Unit alu2Unit;
    IssueUnit issueUnit;
    MemUnit memUnit;
    WriteBackUnit writeBackUnit;
    Scoreboard scoreboard = new Scoreboard();

    public Pipeline() {
        ifUnit = new IFUnit(scoreboard);
        alu1Unit = new ALU1Unit();
        alu2Unit = new ALU2Unit();
        issueUnit = new IssueUnit(scoreboard);
        memUnit = new MemUnit();
        writeBackUnit = new WriteBackUnit();
    }

    public int runCycle(List<Instruction> program, List<Integer> registers, List<Integer> memory, int dataStartAddress, int pc) {
        int newPc = ifUnit.run(program, registers, memory, dataStartAddress, preIssueBuffer, pc);
        if (!ifUnit.isBranchInstruction(program.get((pc - Config.START_PC_VALUE) / 4))) {
            newPc = ifUnit.run(program, registers, memory, dataStartAddress, preIssueBuffer, newPc);
        }
        issueUnit.run(preIssueBuffer, preALU1Buffer, preALU2Buffer);
        alu1Unit.run(preALU1Buffer, preMemBuffer);
        alu2Unit.run(preALU2Buffer, postALU2Buffer);
        memUnit.run(registers, memory, dataStartAddress, preMemBuffer, postMemBuffer);
        writeBackUnit.run(registers, memory, dataStartAddress, postMemBuffer, postALU2Buffer);
        return newPc;
    }

    public IFUnit getIfUnit() {
        return ifUnit;
    }

    public Buffer getPreIssueBuffer() {
        return preIssueBuffer;
    }

    public Buffer getPreALU1Buffer() {
        return preALU1Buffer;
    }

    public Buffer getPreALU2Buffer() {
        return preALU2Buffer;
    }

    public Buffer getPreMemBuffer() {
        return preMemBuffer;
    }

    public Buffer getPostALU2Buffer() {
        return postALU2Buffer;
    }

    public Buffer getPostMemBuffer() {
        return postMemBuffer;
    }
}
