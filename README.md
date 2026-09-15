1) Implement the producer consumer problem using threads in the Java framework
Approach:
The program implements the Producer-Consumer problem using two threads that share a common `Buffer` object. The `Producer` thread generates values from 1 to 5 and calls `produce()`, while the `Consumer` thread calls `consume()` to read them. In the `Buffer` class, `synchronized` ensures that only one thread accesses the buffer at a time. The `available` variable indicates whether the buffer contains data; if the buffer is full, the producer waits using `wait()`, and if it is empty, the consumer waits. After producing or consuming a value, `notify()` wakes the waiting thread. Thus, the code ensures that every produced value is consumed correctly without accessing the buffer at the wrong time.


Logic: 
PRODUCER
   ↓
Creates data
   ↓
BUFFER
   ↓
Stores data temporarily
   ↓
CONSUMER
   ↓
Uses data


Basic Rules:
Buffer full  → Producer waits
Buffer empty → Consumer waits
Producer adds data → Consumer can proceed
Consumer removes data → Producer can proceed




2) Implement Matrix multiplication of 2 matrices using Threads. Minimum 100 rows & 100 columns. Every multiplication operation must be on a thread .Use any framework - TensorFlow specified. Along with this, demonstrate the working with an animation
Approach:
The C++ code solves 100×100 matrix multiplication by combining core matrix-multiplication logic with several threading concepts: first, it relies on the mathematical fact that each output element C[i][j] is simply the dot product of row i of matrix A and column j of matrix B (the sum of A[i][k]*B[k][j] for k from 0 to 99), and since this computation for any given cell never depends on or touches any other cell, the problem is "embarrassingly parallel" and safely splittable across independent workers; building on this, the code uses the POSIX threads (pthreads) library, where pthread_create() is called once for every single one of the 10,000 output cells, passing each new thread a small heap-allocated struct (ThreadData) containing just that thread's assigned row and column so it knows which single element it's responsible for computing, and the same worker function (multiplyOneElement) runs on every thread, independently looping through all 100 terms of its dot product and storing the final sum directly into its own unique cell of the shared C array; because every thread writes to a distinct memory address and only reads from the shared A and B arrays without ever modifying them, there is no data race between threads, which means no mutexes, locks, or other synchronization primitives are required for correctness — a concept sometimes called "embarrassingly parallel, lock-free" execution; the code also uses pthread_attr_setstacksize() to shrink each thread's stack to smaller data (much smaller than the default), a practical concept needed specifically because spawning 10,000 real OS threads at once would otherwise risk exhausting the process's available memory or hitting system thread limits; finally, the main thread calls pthread_join() in a loop for every one of the 10,000 threads before reading or printing matrix C, which enforces the necessary synchronization barrier ensuring all threads have completed their individual computations and written their results before the program treats C as the finished, correct product of A and B.


How it works:
Matrices A (100×100) and B (100×100), result C (100×100) — meets the "minimum 100 rows/cols" requirement.
For every single output element C[i][j], one dedicated pthread is spawned that computes just that one dot product (sum of A[i][k]*B[k][j] for k in 0..99) and writes it to C[i][j].
Thread creation happens in row-major order, so thread 0 → C[0][0], thread 1 → C[0][1], ... thread 99 → C[0][99], thread 100 → C[1][0], and so on — exactly as you described.
Total threads spawned = 100 × 100 = 10,000.
Each thread only reads shared A/B and writes to its own unique C cell, so there's no data race — no mutex needed.
Per-thread stack size is reduced to 256 KB (via pthread_attr_setstacksize) so that creating 10,000 threads doesn't blow past typical OS limits on virtual memory/thread count.
Main thread joins all 10,000 threads before printing results, and the program ends with a serial sanity-check recompute of C[0][0] and C[99][99] to confirm correctness.
