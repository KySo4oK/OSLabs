package org.example;

import java.util.ArrayList;
import java.util.List;

public class Allocator {
    public static final int blockSize = 4;
    private int size;
    private List<Object> memory;

    public Allocator(int size) {
        this.size = size;
        memory = new ArrayList<>(size);
        divideMemoryIntoBlocks();
    }

    private void divideMemoryIntoBlocks() {
        int blocksNumber = size / blockSize;
        for (int i = 0; i < blocksNumber; i++) {
            memory.add(new Header(blockSize - 1, memory.size()));
            for (int j = 0; j < blockSize - 1; j++) {
                memory.add(Byte.valueOf("0"));
            }
        }
    }

    public Integer mem_alloc(int size) {
        for (int i = 0; i < memory.size(); i++) {
            Object current = memory.get(i);
            if (current instanceof Byte) {
                continue;
            }
            Header header = ((Header) current);
            if (!header.isFree()) {
                i += header.getSizeOfBlock();
                continue;
            }
            if (header.getSizeOfBlock() >= size) {
                header.setFree(false);
                return header.getAddressStart();
            }
            Header nextHeader = getNextHeader(header);
            while (nextHeader.isFree()) {
                if (size < header.getSizeOfBlock() + nextHeader.getSizeOfBlock()) {
                    occupyAllBlocksBetween(header, nextHeader);
                    setNewSizeOfBlock(header, nextHeader);
                    return header.getAddressStart();
                } else {
                    nextHeader = getNextHeader(nextHeader);
                }
            }
        }
        return null;
    }

    private void setNewSizeOfBlock(Header header, Header nextHeader) {
        header.setSizeOfBlock(header.getAddressStart() + nextHeader.getAddressStart() + nextHeader.getSizeOfBlock());
    }

    private void occupyAllBlocksBetween(Header header, Header lastHeader) {
        for (int i = header.getAddressStart(); i <= lastHeader.getAddressStart(); i++) {
            Object current = memory.get(i);
            if (current instanceof Header) {
                ((Header) current).setFree(false);
            }
        }
    }

    private Header getNextHeader(Header nextHeader) {
        return (Header) memory.get(nextHeader.getAddressStart() + nextHeader.getSizeOfBlock() + 1);
    }

    public Integer mem_realloc(int address, int size) {
//        Header header = ((Header) memory.get(address));
//        if (header.getSizeOfBlock() < size) {
//            checkNextHeader();
//        } else {
//
//        }
        return null;
    }

    public void mem_free(int address) {
        Header header = ((Header) memory.get(address));
        for (int i = address; i < header.getSizeOfBlock() + address; i++) {
            Object current = memory.get(i);
            if (current instanceof Byte) {
                continue;
            }
            ((Header) current).setFree(true);
        }
    }

    public void mem_dump(int... address) {
        printSeparatorLine();
        if (address.length == 0) {
            for (Object o : memory) {
                System.out.println(o);
            }
        } else {
            for (int i = address[0]; i < size; i++) {
                System.out.println(memory.get(i));
            }
        }
        printSeparatorLine();
    }

    private void printSeparatorLine() {
        System.out.println("=========================================================");
    }
}