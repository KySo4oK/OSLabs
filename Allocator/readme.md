# Memory Allocator imitation on Java
## Explanation
* int ALIGNMENT_SIZE
##### min size of block and all blocks are aliquot of it
* Integer mem_alloc(int size)
##### function, which allocate memory by some size
* Integer mem_realloc(int address, int newSizeOfExistingBlock)
##### function that reallocate an existed block of memory to new or extend current
* void mem_free(int address)
##### function that takes address of an existed block and mark him as free
* void mem_dump()
##### function which show state of our memory
## Work examples
* Integer mem_alloc(int size)
#### before
![1](images/1.png)
#### after
![2](images/2.png)
* Integer mem_realloc(int address, int newSizeOfExistingBlock)
#### before
![3](images/2.png)
#### after
![4](images/3.png)
* void mem_free(int address)
#### before
![5](images/3.png)
#### after
![6](images/4.png)
