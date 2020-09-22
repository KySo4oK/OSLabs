package org.example;

public class App {
    public static void main(String[] args) {
        Allocator allocator = new Allocator(30);
        allocator.mem_dump();
        Integer address = allocator.mem_alloc(9);
        System.out.println(address);
        allocator.mem_dump();
        System.out.println(allocator.mem_realloc(address, 7));
        allocator.mem_dump();
        System.out.println(allocator.mem_alloc(19));
        allocator.mem_dump();
        allocator.mem_free(address);
        allocator.mem_dump();
    }
}
