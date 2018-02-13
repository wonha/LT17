__Parallelism__<br />
[GitPitch](https://gitpitch.com/wonha/presentation/master?p=concurrency#/)<br />
wonhashin26@gmail.com

---
@title[Agenda]

- Concurrency VS Parallelism
- Tools for Parallel process in Go |
    - goroutine : execution |
    - channel : communication |
    - select : coordination |
- Concurrency & Parallelism pattern |
    - Multi-process |
    - Event-driven + parallelism |
    - Multi-thread |
- Go package for parallism |
  - Go runtime pacakage
  - Go sync package

---
@title[Concurrency VS Parallelism]

[Jenkov](http://tutorials.jenkov.com/java-concurrency/concurrency-vs-parallelism.html)

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
@title[Tools for parallel process in Go]

+++?code=./Go-concurrency-parallelism/asset/goroutine.go&lang=go&title=goroutine

+++
@title[goroutine-anonymous]

- Share information between parent and child
    1. Use function parameter
    1. Use Closure
    1. goroutine

+++?code=./Go-concurrency-parallelism/asset/goroutine-anonymous.go&lang=go&title=goroutine-anonymous
- `sync.WaitGroup` and `CyclicBarrier` in Java

+++?code=./Go-concurrency-parallelism/asset/goroutine-closure.go&lang=go&title=goroutine-closure
- internally creates function parameter and pass reference/pointer of used value
    - this cuases unexpected result as below


+++
@title[channel]

- Queue, FIFO, PubSub model(without callback)

+++
@title[unbuffered channel]

- Both sender and receiver are blocked until for each other

+++
@title[buffered channel]

+++
@title[select]

```c
#include <sys/select.h>
int select(int nfds, fd_set *restrict readfds, fd_set *restrict writefds, fd_set *restrict errorfds, struct timeval *restrict timeout);
```

---
@title[Concurrency & Parallelism pattern]

- I/O bound process
    - Event-driven
        - HIgh efficiency through multiplexing
- CPU bound process
    - Multi-process
        - high safety (no shared memory)
        - context switching
    - Multi-thread
        - user thread : 1KB, goroutine (go compiler perform scheduling)
        - kernal thread : 1~2MB

+++
@title[go runtime]

- M (Machine) : CPU
- P (Process) : Schedular(run queue)
- G (goroutine) : Process

---
@title[Go package for parallelism]

1. runtime
2. sync

+++
@title[runtime package]

- Although goroutine is on user thread, Go have package to operate on OS thread directly (after goroutine user thread has mapped to OS thread)
1. runtime.LockOSThread() / runtime.UnlockOSThread()
2. runtime.Gosched()
3. runtime.GOMAXPROCS(n) / runtime.NumCPU()

+++
@title[Race Detector]

```bash
$ go build -race
$ go run -race
```

+++
@title[sync package]

functionality | PThread API | sync package
--- | --- | ---
Lock | pthread_mutex_* | sync.Mutex
RW Lock | pthread_rwlock_* | sync.RWMutx
wait for group of thread | N/A | sync.WaitGroup
execute once | pthread_once | sync.Once
object pool | N/A | sync.Pool

+++?code=./Go-concurrency-parallelism/asset/mutex.go&lang=go&title=mutex

+++?code=./Go-concurrency-parallelism/asset/pool.go&lang=go&title=sync.Pool
- Shared struct within goroutine