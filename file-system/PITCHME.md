__File System__<br />
[GitPitch]()<br />
wonhashin26@gmail.com

---
@title[Agenda]

- File System in UNIX |
- File System In Windows |
- File operation with POSIX system call |
- File operation with C |
- File operation with Perl |
- File operation with Go |
    - notify file modification |
    - File Lock |
- Memory Mapped File |
- Multiplexing I/O |
    - Multiplexing on Network |
    - Multiplexing on File I/O |
- Sync/Async and Block/Non-block I/O |

---
@title[File System]

- Power on -- BIOS -- MBR -- Boot Block -- Os run
- Disk : MBR, partition table, partition
- each partition : boot block, super block, i-node()

+++
@title[File indication]

- Linux : inode
- Windows NTFS file system : master file table
- Mac HFS+ file system : catalog node

OS using inode put file attribute to inode, so directory entry get shorter

+++
@title[hard]

System recognize Storage as sector (Array of byte size of 512B to 4KiB), so actual file contents are stored through sector
sector : physical unit of device
block : locigal unit in Filesystem
page : logical unit in memory

+++
@title[Page Cache]

- file contents written in sector are brought to page cache
- page : basic unit of memory block
    - temporal locality
    - sequential locality
- Same paging mechanism is used to manage process
- (To manage this caching directly, refer __mamory mapped file__)

- swapping swap file (paging file) VS context switching
- swapping swap file : (mainly) memory - disk
- context switching : register - CPU cache - memory - disk
- (sometimes, this two terms are just used as same)

---
@title[File System in UNIX]

- __file = file contents + other information (inode)__

```bash
ls -i
ls -ail
```

+++
@title[directory]

filename | inode number
--- | ---
. | 910
.. | 8002
README.md | 11003

- information on inode
    1. inode number
    1. address of physical location where actual file contents exist
    1. link count ($ of true name, # of hard link)
    1. etc

+++
@title[directory2]

- __directory entry(a.k.a, dirent) = [file name] + [inode number]__

- directory alco has file name and it's inode
    - for example, inode number of root directory is always 2

+++
@title[link]

- Hard link : [file name] n .. 1 [inode number]
- symlink is created to link directory or file in other mounted device
- Symlink : 
    - ino is meaningless in outside of Filesystem
    - ino of synlink contains absolute/relative path for the file    
- inode : structure which holds information about file attribute (-, d, c, b, l, p, s, ... : only -, d, l create inode)

+++
@title[VFS]

- One stroage can have multiple FS. Or, VFS
    - FS on the top of other FS is called __mounted volume__, such as disk partition, ssd, CD-ROM
    - Virtual FS
    - Journaling FS : FS which keep consistency between inode information and actual file contents in sector even at system error
    - Docker : By using `chroot` command, it makes container recognize a part of storage is entire Storage of machine
- There are lots of Filesystem, and kernal should handle with them all
- VFS : Filesystem abstraction mechanism for kernal
- VFS provides common file model (inode, super block, directory entry, etc)
- Because of VFS, we can use same syscall on different Filesystems

+++
@title[Optimization using VFS]
- In Linux, just write to buffer when file write operation comes, by using VFS (same for read operation)
- So, when programmer try to I/O to FS, it is just I/O to buffer actually

---
@title[File System in Windows]

Special memory block -- Kernal Object
File, Process, Thread, Mutex, ... are represented by 'Kernal Object'
(although these have different implementation of kernal object)

Object handle for kernal object

- __directory entry(a.k.a, dirent) = [file name] + [number of the first block] (MS-DOS)__

+++
@title[kernal object]

- __Kernal Object is owned by OS, not by process__
    - Thus, multiple process can access to the same Kernal Object
- __Object handle is owned by process, not by OS__
    - Whenever process is created, handle table for those process also be created
- Process handle (object handle) and Process ID
    - distinguish kernal object
    - distinguish process
- Usage count
    - When some of the process call 'closeHandle()', than usage count decremented
    - Usage count for process is always more than 2
    - Usage count of kernal object representing Folder starts from 2

+++
@title[struct]

```c
typedef struct _PROCESS_INFORMATION {
  HANDLE hProcess;
  HANDLE hThread;
  DWORD  dwProcessId;
  DWORD  dwThreadId;
} PROCESS_INFORMATION, *LPPROCESS_INFORMATION;
```

```c
typedef struct _FILE_BASIC_INFO {
  LARGE_INTEGER CreationTime;
  LARGE_INTEGER LastAccessTime;
  LARGE_INTEGER LastWriteTime;
  LARGE_INTEGER ChangeTime;
  DWORD         FileAttributes;
} FILE_BASIC_INFO, *PFILE_BASIC_INFO;
```
- lots of other information about file
- https://msdn.microsoft.com/en-us/library/windows/desktop/aa364217(v=vs.85).aspx


---
@title[File Operation with POSIX System Call]

```c
#include <fcntl.h>
int open(const char *path, int oflag, ...);
```
- file struct (a.k.a, file table, file table entry or open file description, kernal object) is create with open(2), and file descriptor(object handle) for this structure is returned
close(2), read(2), write(2), lseek(2) is operated on this file descriptor

+++
@title[stat]
```c
#include <sys/stat.h>
int stat(const char *restrict path, struct stat *restrict buf);
```
- used to get __file attribute__
- returns information about file

```
$ man 2 stat
$ stat memo.md 
15738392 2573920489 -rw-r--r-- 1 wonha staff 0 5642 "Jan 29 20:05:58 2018" "Jan 29 00:07:07 2018" "Jan 29 00:07:07 2018" "Jan 27 20:52:44 2018" 3371638 16 0 memo.md
```

---
@title[File Operation with C]

```c
#include <stdio.h>
FILE *fopen(const char *pathname, const char *rwmode);
```
File struct
FileInfo struct

---
@title[File Operation with Perl]

Perl has many file test operators
```perl
print "A file $filename already exist\n" if -e $filename;
print "This directory is readable, writable, and executable\n" if (-r -w -x -o -d $filename);
```

+++
@title[perl]

File test | Meaning
-r | readable (mask) ?
-w | writable ?
-x | executable ?
-s | size ?
-d | is directory ?
-S | is Socket ?
-p | is named bibe ?

+++
@title[perl2]

```perl
my ($dev, $ino, $mode, $nlink, $uid, $gid, $rdev, $sie, $attime, $mtime, $ctime, $blksize, $blocks) = stat($filename);
# $def    : device number
# $ino    : inode number
# $mode   : file permission (e.g. 0755)
# $nlink  : number of (hard) links to this file (# of true name, always more than 2 for dir, 1 for file)
# $uid    : user ID
# $gid    : group ID
# $size   : file size
# 
```

+++
@title[perl3]

```perl
if (-r $filename and -w _) {
    # ...
}
```

---
@title[File Operation with Go]

- os.Stat() -> file existence
- os.path.exists() of Python : internally call method os.stat() which internally call syscall
- C stat(), access()
- os.File.Stat() returns os.FileInfo, and there is inode information and other information which depends on OS
- In Linux, you can't get information about file creation date for each file, since there is no API to get this although OS has thin information

---
@title[Notify File Modification]

- When working on one file by using two different text editor, one should be recognize when the other perform write operation on file buffer or disk

+++
@title[how to recognize modification?]

1. Go to see perodically
    - we can use `stat(2)` for this
1. Get notification (event) -- we will see this

+++
@title[imple for notification linux]

- inotify in Linux uses event
```c
$include <inotify.h>
int inotify_init(void);
```

+++
@title[imple for notification Windows]

-  Windows has its own API
```c
ULONG SHChangeNotifyRegister(
  _In_       HWND                hwnd,
             int                 fSources,
             LONG                fEvents,
             UINT                wMsg,
             int                 cEntries,
  _In_ const SHChangeNotifyEntry *pshcne
);
```

+++
@title[others]

- Many languages has wrapper for this mechanism, let's see Go
```
$ go get gopkg.in/fsnotify.v1 
```
This 3rd party package calls inotify for Linux, kqueue for BSD and ReadDirectoryChangesW for Windows

---
@title[File Lock]

```c
#include <sys/file.h>

#define   LOCK_SH   1    /* shared lock */
#define   LOCK_EX   2    /* exclusive lock */
#define   LOCK_NB   4    /* don't block when locking */
#define   LOCK_UN   8    /* unlock */

int flock(int fd, int operation);
```

+++
@title[locking]
- exclusive lock
    - If an object is exclusively locked, shared locks cannot be obtained.
    - If an object is exclusively locked, other exclusive locks cannot be obtained.
- shared lock: 
    - Multiple shared locks can co-exist.
    - If one or more shared locks already exist, exclusive locks cannot be obtained.
- Advisory locking VS Mandatory locking
    - https://gavv.github.io/blog/file-locks/#mandatory-locking

---
@title[Memory Mapped File]

- Advantages
    1. Read/Write on memory-mapped file, thus can avoid frequent syscall
    1. Aside from any page fault (cache miss), there will be no syscall calling or context switching(swapping file)
    1. Multiple process can share this memory space
    1. COW
- Disadvange
    1. MMAP is available with unit of page. For example, when you set page size to 4 KiB, a 7 byte mapping wastes 4,089 bytes. 
    1. memory space fragment can be occured, when various size of mapping happens frequently
    1. When just reading file with sequential access from front to end, performance will not increased

+++
@title[mmap]

- POSIX syscall
```c
#include <sys/mman.h>
void * mmap(void *addr, size_t len, int prot, int flags, int fd, off_t offset);
```

+++
@title[mmap2]

- Windows also provide this mechanism
```c
HANDLE WINAPI CreateFileMapping(
  _In_     HANDLE                hFile,
  _In_opt_ LPSECURITY_ATTRIBUTES lpAttributes,
  // ...
);
```

+++
@title[mmap3]

```
$ go get github.com/edsrzf/mmap-go ⏎
```

---
@title[Effective I/O]

- Network I/O
- File I/O

+++
@title[summary]
- how to handle thread blocking caused by I/O ?
    1. Thread-based way
        1. Multi process (fork) -- out of scope for this presentation
            1. Inefficient memory usage
        1. Multi thread -- out of scope for this presentation
            1. shared memory
            2. smaller memory
            - Lots of Web framework are implementing multi-threaded server architecture with synchronous-blocking I/O operations within it (thread-per-connection model).

+++
@title[summary2]

- how to handle thread blocking caused by I/O ?
    1. Thread-based way
    1. Event Driven way (Multiplexing)
        -  Instead of process/thread-per-connection, using a single thread to multiple connections.
            - Event .. Event Queue .. Event Loop (dequeing event one by one) .. Event handler
        - less context switching, less memory
        - Event driven way is historically depended on the __asynchronou + non-blocking I/O operations__ and __event notification interfaces__ such as epoll or kqueue.
            - Sync/Async/Blocking/Non-blocking
                - shown below
            - Event Notification interface
                1. select
                1. poll
                1. epoll
                1. kqueue
    1. Combined way
        - [refer this](http://berb.github.io/diploma-thesis/original/042_serverarch.html)

+++
@title[wiki]

- What is Multiplexing ?
> Multiplexing (sometimes contracted to muxing) is a method by which multiple analog or digital signals are combined into one signal over a shared medium. The aim is to share a scarce resource - Wikipedia

---
@title[Sync/Async and Block/Non-block I/O]

> In multithreaded computer programming, asynchronous method invocation (AMI), also known as asynchronous method calls or the asynchronous pattern is a design pattern in which the call site is not blocked while waiting for the called code to finish. - Wikipedia
- Wikipedia thinks if a function is asynchronous, then it is non-block. Really ?

+++
@title[synchronized]

- synchronized VS synchoronous
-https://www.safaribooksonline.com/library/view/linux-system-programming/9781449341527/ch04.html

+++
@title[concern]

- Distinguish by concern
    - Synchronous/Asynchronous : Wheter caller have to care the response of the called API
    - B
    - locking/Non-blocking : Whether the caller can perform other task
        - Return right after write/read to/from a buffer
        - Even with blocking function, if `transfered data <= buffer size`, than the function sometimes does not blocked, and returned right after write data to write buffer

+++
@title[behavior]

- Distinguish by behavior
    - Synchronous/Asynchronous : Asynchronous call run from other thread, mostly with callback
    - Blocking/Non-blocking : Whether the called function immediately returns or not (after operate on buffer)

+++
@title[characteristic]
- Distinguish by characteristic
    - Synchronous/Asynchronous : How the data be processed ? 
    - Blocking/Non-blocking : characteristic that caller cares
- Nonblock한 함수를 호출한 후에도 busy-loop을 통해 계속 변화를 체크하는 동작의 경우 sync,
Nonblock한 함수를 호출한 후 idle한 상태에 있다가 이벤트의 발생을 감지하여 Callback 같은 방식으로 처리하는 경우 async.

+++
@title[diff1]

- [IBM](https://www.ibm.com/developerworks/linux/library/l-async/)
    - This material is old, Multiplexing can be a Non-blocking now
        - Muxing I/O can be a Sync/Non-blocking
- Different perspective
    - https://www.slipp.net/questions/367
    - https://www.slideshare.net/unitimes/sync-asyncblockingnonblockingio
        - I don't think so... (page 5 out of 8, it says about Async/Blocking, but can't explain current JDBC with non-blocking code example)

+++
@title[]

### Async + Non-blocking
- Proactor Pattern provided by POSIX AIO interface and completition events instead of blocking event notification interface
http://berb.github.io/diploma-thesis/original/042_serverarch.html

+++
@title[]

### Sync + Non-blocking
- Reactor Pattern + event notification interface
http://berb.github.io/diploma-thesis/original/042_serverarch.html
```java
// This is just sample code :)
Future future = asyncFileChannel.read(...);
while(!future.isDone()) {
    // do something ...
}
```

+++
@title[]

### Async + Blocking
- Can't think any benefitcial point of this combination.
- Sometimes this happend unintentionally
    - e.g, Current JDBC : While using Aync + Non-blocking, one of the function works as blocking, then the procudure can be a blocked asyncronous


+++
@title[]

### Async + Non-blocking(Blocking) APIs

+++
@title[]

#### kqueue (BSD)

+++
@title[]

#### APC, Overlapped I/O, Completion Routine, IOCP (I/O Completion Port) (Windows)
Overlapped I/OAsync, Non-blocking I/O

+++
@title[]

```c
typedef struct _OVERLAPPED {
  ULONG_PTR Internal;
  ULONG_PTR InternalHigh;
  union {
    struct {
      DWORD Offset;
      DWORD OffsetHigh;
    };
    PVOID  Pointer;
  };
  HANDLE    hEvent;
} OVERLAPPED, *LPOVERLAPPED;
```

+++
@title[]

Completion Routine
```c
BOOL WINAPI WriteFileEx(
  _In_     HANDLE                          hFile,
  _In_opt_ LPCVOID                         lpBuffer,
  _In_     DWORD                           nNumberOfBytesToWrite,
  _Inout_  LPOVERLAPPED                    lpOverlapped,
  _In_     LPOVERLAPPED_COMPLETION_ROUTINE lpCompletionRoutine
);

VOID CALLBACK FileIOCompletionRoutine(
  _In_    DWORD        dwErrorCode,
  _In_    DWORD        dwNumberOfBytesTransfered,
  _Inout_ LPOVERLAPPED lpOverlapped
);

typedef VOID (WINAPI *LPOVERLAPPED_COMPLETION_ROUTINE)(
    _In_    DWORD        dwErrorCode,
    _In_    DWORD        dwNumberOfBytesTransfered,
    _Inout_ LPOVERLAPPED lpOverlapped
);
```

+++
@title[]

- Asyncchronous Procedure Call
    - User-mode APC
    - kernel-mode APC
        1. Normal kernal-mode APC
            - Completion Routine is implemented by this
            - every thread has it's own APC Queue, and all the async function call and it's arguments are saved in this queue
        1. Special kernal-mode APC
```c
DWORD QueueUserAPC (
    PAPCFUNC pfnAPC,
    HANDLE hThread,
    ULONG_PTR dwData
);
```
Even with blocking function, if `transfered data <= buffer size`, than the function sometimes does not blocked, and returned right after write data to write buffer

+++
@title[]

I/O Completion Port (IOCP)
IOCP VS epoll ?
```c
HANDLE WINAPI CreateIoCompletionPort(
  _In_     HANDLE    FileHandle,
  _In_opt_ HANDLE    ExistingCompletionPort,
  _In_     ULONG_PTR CompletionKey,
  _In_     DWORD     NumberOfConcurrentThreads
);
```

+++
@title[]

#### POSIX AIO (Linux)
- [](https://homoefficio.github.io/2017/02/19/Blocking-NonBlocking-Synchronous-Asynchronous/)


+++
@title[]

### Event Notification Interface(Multiplexing) - select
- Sync/Blocking, Sync/Non-blocking, Async/Blocking
```c
#include <sys/select.h>
int select(int nfds, fd_set *restrict readfds, fd_set *restrict writefds, fd_set *restrict errorfds, struct timeval *restrict timeout);
// when socket received data
// when socket can send data
// when exception occur on socekt
```

Good : Many OS supports select()
Bad : Need to send data(set of file descriptor) to kernal often

+++
@title[]

### Event Notification Interface(Multiplexing) - poll
- Sync/Blocking, Sync/Non-blocking, Async/Blocking
```c
#include <poll.h>
int poll(struct pollfd fds[], nfds_t nfds, int timeout);
```
almost same with select
Good : Call system call less than select()


+++
@title[]

select and poll is __level trigger__
    - Level trigger : Register event again and again whenever there still remained data in read/write buffer(event is accumulated)
epoll supports both __edge trigger and level trigger__
    - Edge trigger : Register event onlhy once when data reached to read/write buffer

+++
@title[]

### Event Notification Interface (Multiplexing) - epoll (Linux)
- Sync/Blocking , Sync/Non-blocking
Good : select() called multiple times, hence application need to hand over same data to OS again and again

```c
#include <sys/select.h>
int epoll_create(int size);
int epoll_ctl(int epfd, ...);
int epoll_wait(int epfd, ...)
```

+++
@title[]

### Event Notification Interface (Multiplexing) - WSAAsyncSelect (Windows)
- Async/Non-blocking