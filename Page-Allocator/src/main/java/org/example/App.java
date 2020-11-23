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

        System.out.println("allocate small block");
        Integer address12 = allocator.mem_alloc(10);
        System.out.println("address - " + address12);
        allocator.mem_dump();

        System.out.println("reallocate small block");
        System.out.println("address - " + allocator.mem_realloc(address12, 7));
        allocator.mem_dump();

        System.out.println("reallocate big block");
        System.out.println("address - " + allocator.mem_realloc(address, 130));
        allocator.mem_dump();

        System.out.println("make big block free");
        allocator.mem_free(address);
        allocator.mem_dump();

        System.out.println("make small block free");
        allocator.mem_free(address12);
        allocator.mem_dump();
    }
}
