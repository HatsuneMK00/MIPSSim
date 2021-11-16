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
        ifUnit = new IFUnit();
        alu1Unit = new ALU1Unit();
        alu2Unit = new ALU2Unit();
        issueUnit = new IssueUnit();
        memUnit = new MemUnit();
        writeBackUnit = new WriteBackUnit();
    }

    public int runCycle(List<Instruction> program, List<Integer> registers, List<Integer> memory, int dataStartAddress, int pc) throws CloneNotSupportedException {
        Buffer preIssueBufferSnapshot = (Buffer) preIssueBuffer.clone();
        Buffer preALU1BufferSnapshot = (Buffer) preALU1Buffer.clone();
        Buffer preALU2BufferSnapshot = (Buffer) preALU2Buffer.clone();
        Buffer preMemBufferSnapshot = (Buffer) preMemBuffer.clone();
        Buffer postMemBufferSnapshot = (Buffer) postMemBuffer.clone();
        Buffer postALU2BufferSnapshot = (Buffer) postALU2Buffer.clone();

        issueUnit.run(preIssueBufferSnapshot, preIssueBuffer, preALU1Buffer, preALU2Buffer, scoreboard);
        Scoreboard scoreboardSnapshot = (Scoreboard) scoreboard.clone();
        writeBackUnit.run(registers, memory, dataStartAddress, postMemBuffer, postALU2Buffer, scoreboard);
        memUnit.run(registers, memory, dataStartAddress, preMemBufferSnapshot, preMemBuffer, postMemBuffer, scoreboard);
        alu1Unit.run(preALU1BufferSnapshot, preALU1Buffer, preMemBuffer);
        alu2Unit.run(preALU2BufferSnapshot, preALU2Buffer, postALU2Buffer);
        int newPc = ifUnit.run(program, registers, memory, dataStartAddress, preIssueBufferSnapshot, preIssueBuffer, pc, scoreboardSnapshot);
        if (!ifUnit.isBranchInstruction(program.get((pc - Config.START_PC_VALUE) / 4)) && newPc != -1) {
            newPc = ifUnit.run(program, registers, memory, dataStartAddress, preIssueBufferSnapshot, preIssueBuffer, newPc, scoreboardSnapshot);
        }
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
