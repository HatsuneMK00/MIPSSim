package edu.ca.mips.sim.data;

public class Data {
    private int dataAddress;

    private String binaryCode;
    private int value;

    public Data(int dataAddress, String binaryCode) {
        this.dataAddress = dataAddress;
        this.binaryCode = binaryCode;
        this.value = Integer.parseUnsignedInt(binaryCode, 2);
    }

    public int getValue() {
        return value;
    }

    public int getDataAddress() {
        return dataAddress;
    }

    public void setDataAddress(int dataAddress) {
        this.dataAddress = dataAddress;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
