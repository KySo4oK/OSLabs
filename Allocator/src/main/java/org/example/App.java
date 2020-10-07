package org.example;

import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        //new allocator
        System.out.println("new allocator");
        Allocator allocator = new Allocator(30);
        allocator.mem_dump();

        //allocate new block
        System.out.println("allocate new block");
        Integer address = allocator.mem_alloc(9);
        System.out.println(address);
        allocator.mem_dump();

        //read data from empty block
        System.out.println("read data from empty block");
        System.out.println(Arrays.toString(allocator.readDataByAddress(address)));
        allocator.mem_dump();

        //reallocate block
        System.out.println("reallocate block");
        System.out.println(allocator.mem_realloc(address, 7));
        allocator.mem_dump();

        //write data to block
        System.out.println("write data to block");
        allocator.writeDataByAddress(address, new byte[]{
                Byte.parseByte("1"),
                Byte.parseByte("2")}
        );
        allocator.mem_dump();

        //read data from not empty block
        System.out.println("read data from not empty block");
        System.out.println(Arrays.toString(allocator.readDataByAddress(address)));
        allocator.mem_dump();

        //complicated allocate
        System.out.println("complicated allocate");
        System.out.println(allocator.mem_alloc(19));
        allocator.mem_dump();

        //set block is free
        System.out.println("set block is free");
        allocator.mem_free(address);
        allocator.mem_dump();
    }
}
