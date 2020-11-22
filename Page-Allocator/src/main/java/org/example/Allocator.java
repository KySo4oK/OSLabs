package org.example;

import java.nio.ByteBuffer;
import java.util.*;

public class Allocator implements MemoryAllocator {
    public static final int NEXT_PAGE_DESCRIPTOR_SIZE = 4;
    private final int size;
    private final byte[] memory;
    private final int pages = 4;
    private final int pageSize;
    private final int pageDescriptorSize = 2;
    private final int blockHeaderSize = 5;
    private final List<Integer> freePages = new ArrayList<>();
    private final Map<Integer, List<Integer>> freeBlocksMap = new HashMap<>();

    public Allocator() {
        this(256);
    }

    public Allocator(int size) {
        this.size = size;
        this.pageSize = size / pages;
        memory = new byte[size];
        initMemory();
    }

    private void initMemory() {
        memory[0] = getFalseInByte();
        for (int i = 0; i < 4; i++) {
            int pageIndex = i * pageSize;
            memory[pageIndex] = getIndexOfEnumInByte(PageState.FREE);
            this.freePages.add(pageIndex);
        }
    }

    private byte getIndexOfEnumInByte(PageState pageState) {
        return (byte) pageState.ordinal();
    }

    private byte getFalseInByte() {
        return (byte) (0);
    }

    @Override
    public Integer mem_alloc(int size) {
        if (isMoreThanHalfOfPage(size)) {
            int numberOfPages = getNumberOfPages(size);
            List<Integer> pageList = getFreePagesForCall(numberOfPages);
            for (Integer pageIndex : pageList) {
                memory[pageIndex] = getIndexOfEnumInByte(PageState.OCCUPIED);
            }
            Integer firstPage = pageList.get(0);
            memory[firstPage + 1] = ((byte) (pageList.size() - 1));
            for (int i = 1; i < pageList.size(); i++) {
                byte[] nextPageIndex = getByteArrayOfInt(pageList.get(i));
                for (int j = 0; j < nextPageIndex.length; j++) {
                    int nextPageIndexIterationIndex = firstPage + pageDescriptorSize + (i - 1) * 4 + j;
                    memory[nextPageIndexIterationIndex] = nextPageIndex[j];
                }
            }
            return firstPage;
        } else {
            //block in page
            return null;
        }
    }

    private List<Integer> getFreePagesForCall(int numberOfPages) {
        List<Integer> pageList = new ArrayList<>();
        for (int i = 0; i < numberOfPages; i++) {
            pageList.add(freePages.remove(0));
        }
        return pageList;
    }

    private int getNumberOfPages(int size) {
        int predefinedPages = (int) (Math.ceil((size + 0.0) / (pageSize - pageDescriptorSize)));
        if (predefinedPages * (pageSize - pageDescriptorSize) - size >
                NEXT_PAGE_DESCRIPTOR_SIZE * (predefinedPages - 1)) {
            return predefinedPages;
        }
        return predefinedPages + 1;
    }

    private boolean isMoreThanHalfOfPage(int size) {
        return size > pageSize - 2 - 10;
    }

    private boolean isNextHeaderFree(int nextHeaderIndex) {
        return convertByteToBoolean(memory[nextHeaderIndex]);
    }

    private int getNextHeaderIndex(int lengthOfBlock, int i) {
        return i + 5 + lengthOfBlock;
    }

    private void setNewSize(int i, int sizeWithAlignment) {
        byte[] array = getByteArrayOfInt(sizeWithAlignment);
        for (int j = 0; j < 4; j++) {
            memory[j + i + 1] = array[j];
        }
    }

    private byte[] getByteArrayOfInt(int sizeWithAlignment) {
        return ByteBuffer.allocate(4).putInt(sizeWithAlignment).array();
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

    @Override
    public Integer mem_realloc(int address, int newSizeOfExistingBlock) {
        int realSize = getLengthOfBlock(address);
        newSizeOfExistingBlock = getSizeWithAlignment(newSizeOfExistingBlock);
        if (realSize == newSizeOfExistingBlock) {
            return address;
        } else if (realSize < newSizeOfExistingBlock) {
            int nextHeaderIndex = getNextHeaderIndex(realSize, address);
            while (isNextHeaderFree(nextHeaderIndex)) {
                int newSize = getLengthOfBlock(nextHeaderIndex) + nextHeaderIndex - (address + 4);
                if (newSize > newSizeOfExistingBlock) {
                    setNewSize(address, newSizeOfExistingBlock);
                    createNewHeaderAfterNewBlock(nextHeaderIndex, newSize - newSizeOfExistingBlock);
                    return address;
                }
                nextHeaderIndex = getNextHeaderIndex(getLengthOfBlock(nextHeaderIndex), nextHeaderIndex);
            }
            int newAddress = mem_alloc(newSizeOfExistingBlock);
            copyBlockToNewAddress(address, newAddress);
            memory[address] = getFalseInByte();
            return newAddress;
        } else {
            int alignmentBlocksThatCanBeRemoved = (realSize - newSizeOfExistingBlock) / ALIGNMENT_SIZE;
            if (alignmentBlocksThatCanBeRemoved > 0) {
                int sizeOfFreeMemory = alignmentBlocksThatCanBeRemoved * ALIGNMENT_SIZE;
                setNewSize(address, getLengthOfBlock(address) - sizeOfFreeMemory);
                createNewHeaderAfterNewBlock(getNextHeaderIndex(getLengthOfBlock(address), address), sizeOfFreeMemory);
                return address;
            }
        }
        return null;
    }

    private void copyBlockToNewAddress(int header, int newAddress) {
        int lengthOfBlock = getLengthOfBlock(header);
        for (int i = 0; i < lengthOfBlock; i++) {
            memory[newAddress + 5 + i] = memory[header + 5 + i];
        }
    }


    @Override
    public void mem_free(int address) {
        memory[address] = getFalseInByte();
    }

    @Override
    public void mem_dump() {
        printSeparatorLine();
        System.out.println();
        for (byte b : memory) {
            System.out.print(b + " ");
        }
        System.out.println();
        printSeparatorLine();
    }

    private void printSeparatorLine() {
        System.out.println("=================================================================");
    }
}