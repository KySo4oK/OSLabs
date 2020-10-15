#include <iostream>

int main() {
    std::cout << "Hello, World!" << std::endl;
    return 0;
}

void *mem_alloc(size_t size);

void *mem_realloc(void *addr, size_t size);

void mem_free(void *addr);

void mem_dump();
