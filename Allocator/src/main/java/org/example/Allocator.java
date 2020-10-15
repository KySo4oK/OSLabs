package org.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Allocator {
    public static final int ALIGNMENT_SIZE = 4;
    private final int size;
    private final byte[] memory;

    public Allocator(int size) {
        this.size = size;
        memory = new byte[size];
        initMemory();
    }

    private void initMemory() {
        memory[0] = getFalseInByte();
        setNewSize(0, this.size - 5);
    }

    private byte getFalseInByte() {
        return (byte) (0);
    }

    public Integer mem_alloc(int size) {
        for (int i = 0; i < this.size; i++) {
            int lengthOfBlock = getLengthOfBlock(i);

            if (convertByteToBoolean(memory[i])) {
                i = i + 5 + lengthOfBlock;
                continue;
            }

            int sizeWithAlignment = getSizeWithAlignment(size);
            int nextHeaderIndex = getNextHeaderIndex(lengthOfBlock, i);
            if (lengthOfBlock >= size) {
                memory[i] = getTrueInByte();
                int sizeOfNewBlock = lengthOfBlock - size - 5;
                setNewSize(i, sizeWithAlignment);
                createNewHeaderAfterNewBlock(getNextHeaderIndex(getLengthOfBlock(i), i), sizeOfNewBlock);
                return i;
            }
            while (isNextHeaderFree(nextHeaderIndex)) {
                int newSize = getLengthOfBlock(nextHeaderIndex) + nextHeaderIndex - (i + 4);
                if (newSize > sizeWithAlignment) {
                    setNewSize(i, sizeWithAlignment);
                    createNewHeaderAfterNewBlock(nextHeaderIndex, newSize - sizeWithAlignment);
                    return i;
                }
                nextHeaderIndex = getNextHeaderIndex(getLengthOfBlock(nextHeaderIndex), nextHeaderIndex);
            }
        }
        return null;
    }

    private boolean isNextHeaderFree(int nextHeaderIndex) {
        return convertByteToBoolean(memory[nextHeaderIndex]);
    }

    private int getNextHeaderIndex(int lengthOfBlock, int i) {
        return i + 5 + lengthOfBlock;
    }

    private void setNewSize(int i, int sizeWithAlignment) {
        byte[] array = ByteBuffer.allocate(4).putInt(sizeWithAlignment).array();
        for (int j = 0; j < 4; j++) {
            memory[j + i + 1] = array[j];
        }
    }

    private byte getTrueInByte() {
        return (byte) 1;
    }

    private int getLengthOfBlock(int i) {
        return convertByteToInt(Arrays.copyOfRange(memory, i + 1, i + 5));
    }

    private int convertByteToInt(byte[] array) {
        return ByteBuffer.wrap(array).getInt();
    }

    private boolean convertByteToBoolean(byte b) {
        return b != 0;
    }

    private int getSizeWithAlignment(int size) {
        return (int) (Math.ceil((size + 0.0) / ALIGNMENT_SIZE) * ALIGNMENT_SIZE);
    }

    private void createNewHeaderAfterNewBlock(int headerIndex, int sizeOfNewBlock) {
        memory[headerIndex] = getFalseInByte();
        setNewSize(headerIndex, sizeOfNewBlock - 5);
    }

    private int getIndexOfNextHeaderStartAddress(Header header) {
        return header.getSize() + header.getAddressStart();
    }

    private void occupyAllMemoryBetweenHeaders(Header header, Header lastHeader) {
        header.setFree(false);
        int endOfNewBlock = getIndexOfNextHeaderStartAddress(lastHeader);
        for (int i = header.getAddressStart(); i < endOfNewBlock; i++) {
            Object current = memory[i];
            if (current instanceof Header) {
                memory[i] = Byte.valueOf("0");
            }
        }
    }

//    public Integer mem_realloc(int address, int size) {
//        Header header = ((Header) memory[address - 1]);
//        int realSize = header.getSize();
//        if (realSize == size) {
//            return address;
//        } else if (realSize < size) {
//            if (isPossibleToExtendCurrentBlock(size, header)) {
//                return header.getAddressStart();
//            } else {
//                int newAddress = mem_alloc(size);
//                copyBlockToNewAddress(header, newAddress);
//                header.setFree(true);
//                return newAddress;
//            }
//        } else {
//            int alignmentBlocksThatCanBeRemoved = (realSize - size) / ALIGNMENT_SIZE;
//            if (alignmentBlocksThatCanBeRemoved > 0) {
//                header.setSize(header.getSize() - alignmentBlocksThatCanBeRemoved * ALIGNMENT_SIZE);
//                createNewHeaderAfterNewBlock(header.getSize() + header.getAddressStart(), sizeOfNewBlock);
//                return address;
//            }
//        }
//        return null;
//    }

    private void copyBlockToNewAddress(Header header, int newAddress) {
        for (int i = 0; i < header.getSize(); i++) {
            memory[newAddress + i] = memory[header.getAddressStart() + i];
        }
    }


//    public void mem_free(int address) {
//        Header header = ((Header) memory[address - 1]);
//        header.setFree(true);
//    }

    public void mem_dump(int... address) {
        printSeparatorLine();
        if (address.length == 0) {
            for (Object o : memory) {
                System.out.println(o);
            }
        } else {
            for (int i = address[0]; i < size; i++) {
                System.out.println(memory[i]);
            }
        }
        printSeparatorLine();
    }

    private void printSeparatorLine() {
        System.out.println("=========================================================");
    }
//
//    public byte[] readDataByAddress(int address){
//        Header header = (Header) memory[address - 1];
//        int blockSize = header.getSize();
//        int blockStart = header.getAddressStart();
//        byte[] bytes = new byte[blockSize];
//        for (int i = 0; i < blockSize; i++) {
//            bytes[i] = (byte) memory[blockStart + i];
//        }
//        return bytes;
//    }
//    public void writeDataByAddress(int address, byte[] data){
//        for (int i = 0; i < data.length; i++) {
//            memory[address + i] = data[i];
//        }
//    }
}