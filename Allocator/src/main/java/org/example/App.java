package org.example;

public class App {
    public static void main(String[] args) {
        Allocator allocator = new Allocator(20);
        allocator.mem_dump();
        int address = allocator.mem_alloc(5);
        System.out.println(address);
        allocator.mem_dump();
        allocator.mem_free(address);
        allocator.mem_dump();
    }
}
