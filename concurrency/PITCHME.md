__Parallelism__<br />
[GitPitch]()<br />
wonhashin26@gmail.com

---
@title[Agenda]

- Concurrency VS Parallelism
- Parallel mechanism in Go
    - goroutine
    - channel
    - select
- Concurrency & Parallelism pattern
    - Multi-process
    - Event-driven
    - Multi-thread
- Thread and goroutine
- Go runtime
- runtime pacakage
- Go Race Detector
- sync package


---
@title[Concurrency VS Parallelism]
[](http://tutorials.jenkov.com/java-concurrency/concurrency-vs-parallelism.html)

+++
@title[Concurrency]

- concurrency is related to how an application handles multiple tasks it works on. An application may process one task at at time (sequentially) or work on multiple tasks at the same time (concurrently).
- Application is making progress on __more than one task__ at the same time
- Application run on __single core CPU__ may not make progress concurrently, but inside the application, it does not completely finish one task before it begins the next

+++
@title[Paralellism]

- Parallelism on the other hand, is related to how an application handles each individual task. An application may process the task serially from start to end, or split the task up into subtasks which can be completed in parallel.
- Application splits its tasks up into smaller subtasks which can be processed in parallel, for instance on multiple CPUs at the exact same time.

+++
@title[Concurrency VS Parallelism]

In programming, concurrency is the composition of independently executing processes, while parallelism is the simultaneous execution of (possibly related) computations. Concurrency is about dealing with lots of things at once. Parallelism is about doing lots of things at once.

- Task requires many computation time : Parallelism
- Task requires waiting from multiple event (I/O blocking) : Concurrency (Parallelism)

+++
@title[Cuncurrent & not Parallel]

- An application processes more than one task at the same time, but the tasks are not broken down into subtasks.
- Some of the asynchronous computation (`goroutine` in Go,`Future` in Java)

+++
@title[not Cuncurrent & Parallel]

An application can also be parallel but not concurrent. This means that the application only works on one task at a time, and this task is broken down into subtasks which can be processed in parallel.

+++
@title[not Cuncurrent & not Parallel]

Additionally, an application can be neither concurrent nor parallel. This means that it works on only one task at a time, and the task is never broken down into subtasks for parallel execution.

+++
@title[Cuncurrent & Parallel]

Finally, an application can also be both concurrent and parallel, in that it both works on multiple tasks at the same time, and also breaks each task down into subtasks for parallel execution. However, some of the benefits of concurrency and parallelism may be lost in this scenario, as the CPUs in the computer are already kept reasonably busy with either concurrency or parallelism alone. Combining it may lead to only a small performance gain or even performance loss. Make sure you analyze and measure before you adopt a concurrent parallel model blindly.

---
@title[File System in UNIX]

---
@title[File System in Windows]

---
@title[File Operation with POSIX System Call]

---
@title[File Operation with C]

---
@title[File Operation with Perl]

---
@title[File Operation with Go]

---
@title[Notify File Modification]

---
@title[File Lock]

---
@title[Memory Mapped File]

---
@title[Multiplexing I/O]

---
@title[Sync/Async and Block/Non-block I/O]
