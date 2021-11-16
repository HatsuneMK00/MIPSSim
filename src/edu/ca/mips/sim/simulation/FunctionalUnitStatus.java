package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;
import edu.ca.mips.sim.simulation.unit.FunctionalUnit;

public class FunctionalUnitStatus {
    boolean busy = false;
    Instruction op;
    int fi;
    int fj, fk;
    FunctionalUnit qj, qk;
    boolean rj = false, rk = false;

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Instruction getOp() {
        return op;
    }

    public void setOp(Instruction op) {
        this.op = op;
    }

    public int getFi() {
        return fi;
    }

    public void setFi(int fi) {
        this.fi = fi;
    }

    public int getFj() {
        return fj;
    }

    public void setFj(int fj) {
        this.fj = fj;
    }

    public int getFk() {
        return fk;
    }

    public void setFk(int fk) {
        this.fk = fk;
    }

    public FunctionalUnit getQj() {
        return qj;
    }

    public void setQj(FunctionalUnit qj) {
        this.qj = qj;
    }

    public FunctionalUnit getQk() {
        return qk;
    }

    public void setQk(FunctionalUnit qk) {
        this.qk = qk;
    }

    public boolean isRj() {
        return rj;
    }

    public void setRj(boolean rj) {
        this.rj = rj;
    }

    public boolean isRk() {
        return rk;
    }

    public void setRk(boolean rk) {
        this.rk = rk;
    }
}
