//
// Created by rp on 15.10.20.
//

#ifndef ALLOCATOR_C___ALLOCATOR_H
#define ALLOCATOR_C___ALLOCATOR_H


#include <cstddef>

class Allocator {

    void *mem_alloc(size_t size);

    void *mem_realloc(void *addr, size_t size);

    void mem_free(void *addr);

    void mem_dump();
};


#endif //ALLOCATOR_C___ALLOCATOR_H
