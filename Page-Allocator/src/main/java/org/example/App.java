package org.example;

public class App {
    public static void main(String[] args) {
        System.out.println("new allocator");
        MemoryAllocator allocator = new Allocator();
        allocator.mem_dump();

        System.out.println("allocate new block");
        Integer address = allocator.mem_alloc(70);
        System.out.println("address - " + address);
        allocator.mem_dump();
//
//        System.out.println("reallocate block");
//        System.out.println("address - " + allocator.mem_realloc(address, 7));
//        allocator.mem_dump();
//
//        System.out.println("make block free");
//        allocator.mem_free(address);
//        allocator.mem_dump();
    }
}
