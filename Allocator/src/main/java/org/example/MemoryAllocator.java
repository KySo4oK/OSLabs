package org.example;

public interface MemoryAllocator {
    int ALIGNMENT_SIZE = 4;

    Integer mem_alloc(int size);

    Integer mem_realloc(int address, int newSizeOfExistingBlock);

    void mem_free(int address);

    void mem_dump(int... address);
}
