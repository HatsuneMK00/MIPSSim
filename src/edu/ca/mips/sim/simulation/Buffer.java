package edu.ca.mips.sim.simulation;

import edu.ca.mips.sim.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

public class Buffer implements Cloneable {
    String name;
    List<Instruction> queue;
    int maxSize;
    public int size;

    public Buffer(String name, int maxSize) {
        this.name = name;
        this.maxSize = maxSize;
        queue = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            queue.add(null);
        }
        this.size = 0;
    }

    public boolean isFull() {
        return size >= maxSize;
    }

    public void add(Instruction instruction) {
        queue.set(size++, instruction);
    }

    public Instruction poll() {
        if (size == 0) {
            return null;
        }
        Instruction instruction = queue.remove(0);
        queue.add(null);
        size--;
        return instruction;
    }

    public Instruction remove(int index) {
        if (size == 0) {
            return null;
        }
        Instruction instruction = queue.remove(index);
        queue.add(null);
        size--;
        return instruction;
    }

    public Instruction peek() {
        return queue.get(0);
    }

    public Instruction get(int index) {
        return queue.get(index);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Buffer buffer = (Buffer) super.clone();
        buffer.queue = new ArrayList<>(buffer.queue);

        return buffer;
    }
}
