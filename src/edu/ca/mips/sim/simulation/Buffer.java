package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
    String name;
    List<Instruction> queue;
    int maxSize;

    public Buffer(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
        queue = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            queue.add(null);
        }
    }

    public boolean isFull() {
        return queue.size() >= maxSize;
    }

    public boolean add(Instruction instruction) {
        return queue.add(instruction);
    }

    public Instruction poll() {
        Instruction instruction = queue.remove(0);
        queue.add(null);
        return instruction;
    }

    public Instruction peek() {
        return queue.get(0);
    }

    public Instruction get(int index) {
        return queue.get(index);
    }

}
