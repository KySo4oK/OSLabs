package org.example;

public class Allocator {
    public static final int ALIGNMENT_SIZE = 4;
    private final int size;
    private final Object[] memory;

    public Allocator(int size) {
        this.size = size;
        memory = new Object[size];
        initMemory();
    }

    private void initMemory() {
        memory[0] = new Header(size - 1, 1);
        for (int i = 1; i < size; i++) {
            memory[i] = Byte.valueOf("0");
        }
    }

    public Integer mem_alloc(int size) {
        for (int i = 0; i < this.size; i++) {
            Object current = memory[i];
            if (current instanceof Byte) {
                continue;
            }
            Header header = ((Header) current);
            if (!header.isFree()) {
                i = getIndexOfNextHeaderStartAddress(header) - 1;
                continue;
            }
            if (header.getSize() >= size) {
                header.setFree(false);
                header.setSize(getSizeWithAlignment(size));
                createNewHeaderAfterNewBlock(getIndexOfNextHeaderStartAddress(header));
                return header.getAddressStart();
            }
            if (isPossibleToExtendCurrentBlock(size, header)) return header.getAddressStart();
        }
        return null;
    }

    private int getSizeWithAlignment(int size) {
        return (int) (Math.ceil((size + 0.0) / ALIGNMENT_SIZE) * ALIGNMENT_SIZE);
    }

    private void createNewHeaderAfterNewBlock(int headerIndex) {
        Header nextHeaderAfterNew = null;
        Header newHeader = new Header();
        newHeader.setAddressStart(headerIndex + 1);
        for (int i = headerIndex; i < size; i++) {
            Object current = memory[i];
            if (current instanceof Header) {
                nextHeaderAfterNew = ((Header) current);
                break;
            }
        }
        if (null == nextHeaderAfterNew) {
            newHeader.setSize(size - newHeader.getAddressStart());
        } else {
            newHeader.setSize(nextHeaderAfterNew.getAddressStart() - 1 - newHeader.getAddressStart());
        }
        if (headerIndex != memory.length) {
            memory[headerIndex] = newHeader;
        }
    }

    private int getIndexOfNextHeaderStartAddress(Header header) {
        return header.getSize() + header.getAddressStart();
    }

    private int getNewSizeOfBlock(Header header, Header nextHeader) {
        return getIndexOfNextHeaderStartAddress(nextHeader) - 1 - header.getAddressStart();
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

    private Header getNextHeader(Header header) {
        return (Header) memory[getIndexOfNextHeaderStartAddress(header)];
    }

    public Integer mem_realloc(int address, int size) {
        Header header = ((Header) memory[address - 1]);
        int realSize = header.getSize();
        if (realSize == size) {
            return address;
        } else if (realSize < size) {
            if (isPossibleToExtendCurrentBlock(size, header)) {
                return header.getAddressStart();
            } else {
                int newAddress = mem_alloc(size);
                copyBlockToNewAddress(header, newAddress);
                header.setFree(true);
                return newAddress;
            }
        } else {
            int alignmentBlocksThatCanBeRemoved = (realSize - size) / ALIGNMENT_SIZE;
            if (alignmentBlocksThatCanBeRemoved > 0) {
                header.setSize(header.getSize() - alignmentBlocksThatCanBeRemoved * ALIGNMENT_SIZE);
                createNewHeaderAfterNewBlock(header.getSize() + header.getAddressStart());
                return address;
            }
        }
        return null;
    }

    private void copyBlockToNewAddress(Header header, int newAddress) {
        for (int i = 0; i < header.getSize(); i++) {
            memory[newAddress + i] = memory[header.getAddressStart() + i];
        }
    }

    private boolean isPossibleToExtendCurrentBlock(int size, Header header) {
        Header nextHeader = getNextHeader(header);
        while (nextHeader.isFree()) {
            if (size <= getNewSizeOfBlock(header, nextHeader)) {
                occupyAllMemoryBetweenHeaders(header, nextHeader);
                header.setSize(getSizeWithAlignment(size));
                createNewHeaderAfterNewBlock(getIndexOfNextHeaderStartAddress(header));
                return true;
            } else {
                nextHeader = getNextHeader(nextHeader);
            }
        }
        return false;
    }

    public void mem_free(int address) {
        Header header = ((Header) memory[address - 1]);
        header.setFree(true);
    }

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

    public byte[] readDataByAddress(int address){
        Header header = (Header) memory[address - 1];
        int blockSize = header.getSize();
        int blockStart = header.getAddressStart();
        byte[] bytes = new byte[blockSize];
        for (int i = 0; i < blockSize; i++) {
            bytes[i] = (byte) memory[blockStart + i];
        }
        return bytes;
    }
    public void writeDataByAddress(int address, byte[] data){
        for (int i = 0; i < data.length; i++) {
            memory[address + i] = data[i];
        }
    }
}