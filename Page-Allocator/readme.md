# Lab 2 - Page Allocator
## DESCRIPTION
## About page allocator algorithm

All memory available to the allocator is divided into pages. 
The page size can be from **4 KB** to **several megabytes**. All pages have the same size.

Each page is divided into blocks of fixed size. From the very beginning of the allocator,
none of the pages is marked (divided) into blocks. Page formatting is done while the program is running.
This is because we can't know exactly how to shuffle pages and what size blocks will be used most often.

Each page can be in only three states: **divided** into blocks, **occupied** by a multi-page block, and **free**.

### Algorithm description

#### Allocate function
```
void* mem_alloc(size_t size)
```

First, the size of the requested memory size must be rounded to the first larger degree of 2.
Then we can have 2 variants:
* size <= page size
* size > page size

If the first case: After that we look for a page in our hashmap in which there is a free place. 
Since the hashmap values pages with free blocks, we can simply take the first one from that list. 
If it does not have it, we take an unmarked page and mark it with blocks of the required size 
(not forgetting to create a new descriptor for it) and add it to the hashpam. After selecting a 
page, the address of the first free block is stored separately from its handle (this address will 
be returned to the user) and the counter of free blocks is reduced by -1. After that, update the 
pointer to the first free block in the handle. If there are no more free blocks on the page 
(it is easy to look at the counter), then we delete this page from the hashmap.

If the second case: Let's see if there are free pages in the hashmap. If there are, we take them. 
If not, then take as many free pages as needed, create a descriptor for each of them. 
In the first descriptor we specify the full size of the block (ie the size of these several pages together). 
The rest of the descriptors are just for numbers. We return to the user the pointer to the beginning of the 
first page from taken.

### Reallocate function
```
void* mem_realloc(void* addr, size_t size)
```

This function use memFree and memAlloc functions. First, we must define which type of block we want to reallocate.
If it's free block, we just call memAlloc function. If it's another two types, we just remember current information
,freed this memory and try to allocate. If allocate done successfully it's done. If no, we just rollback
to previous state. 

### Free memory function
```
void mem_free(void* addr)
```

By the given address, define the page number. After that find page's descriptor. And we have 2 cases:
* clean block with size *less* than half a page
* clean block with size *more* than half a page

First case: increment count of free pages in descriptor and refresh pointer to first free block. If the page was 
fully occupied by blocks, we add it to hashmap again.

Second case: just clear our descriptor and add page address to array of free pages.

## DEMONSTRATION