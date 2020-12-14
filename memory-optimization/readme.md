# Memory optimization in Java
### Measure with original code(partly rewritten to Java)
![1](images/origin.png)
### Measure original code with difference and with bigger bounds(10 vs 100)
![2](images/origin-1.png)
### Measure after replacing column order
![3](images/origin-2.png)
### Measure after changing int type to byte
![4](images/or-3.png)
### Measure after replacing increment order(asc to desc)
![5](images/or-4.png)
## As you can see execution time was reduced by 3 times - from 32109764 to 10670416.
## This result for memory optimization show that it make sense
