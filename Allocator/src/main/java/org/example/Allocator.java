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
            Header nextHeader = getNextHeader(header);
            while (nextHeader.isFree()) {
                if (size < getNewSizeOfBlock(header, nextHeader)) {
                    occupyAllMemoryBetweenHeaders(header, nextHeader);
                    header.setSize(getSizeWithAlignment(size));
                    createNewHeaderAfterNewBlock(getIndexOfNextHeaderStartAddress(header));
                    return header.getAddressStart();
                } else {
                    nextHeader = getNextHeader(nextHeader);
                }
            }
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

        memory[headerIndex] = newHeader;
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

//    public Integer mem_realloc(int address, int size) {
//        Header header = ((Header) memory.get(address));
//        int realSize = header.getSize();
//        if (realSize == size) {
//            return address;
//        } else if (realSize < size) {
//            int headersToOccupy = (int) Math.ceil((size - realSize + 0.0) / ALIGNMENT_SIZE);
//            Header nexHeader = getNextHeader(header);
//            for (int i = 0; i < headersToOccupy; i++) {
//                if (!nexHeader.isFree()) {
//                    return mem_alloc(size);
//                }
//                nexHeader = getNextHeader(nexHeader);
//            }
//
//        } else {
//            int headersToFree = (realSize - size) / ALIGNMENT_SIZE;
//            for (int i = 0; i < headersToFree; i++) {
//                int index = getIndexOfLastHeader(getIndexOfEndOfBlock(header), i + 1);
//                Object current = memory.get(index + 1);
//                if (current instanceof Byte) {
//                    System.out.println(index);
//                    throw new RuntimeException();
//                }
//                ((Header) current).setFree(true);
//            }
//            header.setSize(header.getSize() - headersToFree * ALIGNMENT_SIZE);
//            return address;
//        }
//        return null;
//    }
//
//    private int getIndexOfLastHeader(int indexOfEndOfBlock, int i) {
//        return indexOfEndOfBlock - (i) * ALIGNMENT_SIZE;
//    }
//
//    private int getIndexOfEndOfBlock(Header header) {
//        return header.getAddressStart() + header.getSize();
//    }
//
//    public void mem_free(int address) {
//        Header header = ((Header) memory.get(address));
//        for (int i = address; i < getIndexOfNextHeader(header.getSize(), address, header); i++) {
//            Object current = memory.get(i);
//            if (current instanceof Byte) {
//                continue;
//            }
//            ((Header) current).setFree(true);
//        }
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
}