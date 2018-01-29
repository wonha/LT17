## File System
Power on -- BIOS -- MBR -- Boot Block -- Os run
Disk : MBR, partition table, partition
each partition : boot block, super block, i-node()

Linux : inode
Windows NTFS file system : master file table
Mac HFS+ file system : catalog node

OS using inode put file attribute to inode, so directory entry get shorter

System recognize Storage as sector (Array of byte size of 512B to 4KiB), so actual file contents are stored through sector
sector : physical unit of device
block : locigal unit in Filesystem
page : logical unit in memory

### Page Cache
file contents written in sector are brought to page cache
page : basic unit of memory block
    - temporal locality
    - sequential locality
Same paging mechanism is used to manage process
(To manage this caching directly, refer __mamory mapped file__)

- swapping swap file (paging file) VS context switching
swapping swap file : (mainly) memory - disk
context switching : register - CPU cache - memory - disk
(sometimes, this two terms are just used as same)


## File System in UNIX

- __file = file contents + other information (inode)__

```bash
ls -i
ls -ail
```

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

- __directory entry(a.k.a, dirent) = [file name] + [inode number]__

- directory alco has file name and it's inode
    - for example, inode number of root directory is always 2

### Link
Hard link : [file name] n .. 1 [inode number]
- symlink is created to link directory or file in other mounted device

Symlink : 
    ino is meaningless in outside of Filesystem
    ino of synlink contains absolute/relative path for the file    


Process struct
File struct
Type : UFS, NFS
inode : structure which holds information about file attribute (-, d, c, b, l, p, s, ... : only -, d, l create inode)


### VFS (Virtual File System)
- One stroage can have multiple FS. Or, VFS
    - FS on the top of other FS is called __mounted volume__, such as disk partition, ssd, CD-ROM

    - Virtual FS
    - Journaling FS : FS which keep consistency between inode information and actual file contents in sector even at system error
    - Docker : By using `chroot` command, it makes container recognize a part of storage is entire Storage of machine

There are lots of Filesystem, and kernal should handle with them all
VFS : Filesystem abstraction mechanism for kernal
VFS provides common file model (inode, super block, directory entry, etc)
Because of VFS, we can use same syscall on different Filesystems

#### Optimization of file operation
In Linux, just write to buffer when file write operation comes, by using VFS (same for read operation)
So, when programmer try to I/O to FS, it is just I/O to buffer actually

Application
--
VFS (maybe, inode + file contents)
--
FS (inode + file contents)
--
Storage

## File System concept in Windows
Special memory block -- Kernal Object
File, Process, Thread, Mutex, ... are represented by 'Kernal Object'
(although these have different implementation of kernal object)

Object handle for kernal object

- __directory entry(a.k.a, dirent) = [file name] + [number of the first block] (MS-DOS)__

__Kernal Object is owned by OS, not by process__
    - Thus, multiple process can access to the same Kernal Object
__Object handle is owned by process, not by OS__
    - Whenever process is created, handle table for those process also be created

```c
typedef struct _PROCESS_INFORMATION {
  HANDLE hProcess;
  HANDLE hThread;
  DWORD  dwProcessId;
  DWORD  dwThreadId;
} PROCESS_INFORMATION, *LPPROCESS_INFORMATION;
```
https://msdn.microsoft.com/en-us/library/windows/desktop/ms684873(v=vs.85).aspx

```c
typedef struct _FILE_BASIC_INFO {
  LARGE_INTEGER CreationTime;
  LARGE_INTEGER LastAccessTime;
  LARGE_INTEGER LastWriteTime;
  LARGE_INTEGER ChangeTime;
  DWORD         FileAttributes;
} FILE_BASIC_INFO, *PFILE_BASIC_INFO;
```
lots of other information about file
https://msdn.microsoft.com/en-us/library/windows/desktop/aa364217(v=vs.85).aspx

Process handle (object handle) and Process ID
    - distinguish kernal object
    - distinguish process

Usage count
    When some of the process call 'closeHandle()', than usage count decremented
    Usage count for process is always more than 2
    Usage count of kernal object representing Folder starts from 2

## File Access Mechanism in POSIX syscall
```c
#include <fcntl.h>
int open(const char *path, int oflag, ...);
```
- file struct (a.k.a, file table, file table entry or open file description, kernal object) is create with open(2), and file descriptor(object handle) for this structure is returned
close(2), read(2), write(2), lseek(2) is operated on this file descriptor


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

## File Access Mechanism in C
```c
#include <stdio.h>
FILE *fopen(const char *pathname, const char *rwmode);
```
File struct
FileInfo struct

## File Access Mechanism Perl
Perl has many file test operators
```perl
print "A file $filename already exist\n" if -e $filename;
print "This directory is readable, writable, and executable\n" if (-r -w -x -o -d $filename);
```

File test | Meaning
-r | readable (mask) ?
-w | writable ?
-x | executable ?
-s | size ?
-d | is directory ?
-S | is Socket ?
-p | is named bibe ?

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

```perl
if (-r $filename and -w _) {
    # ...
}
```

## File Access Mechanism Go
os.File struct of Go VS file struct in C VS file struct that Sinoda thought
what about Perl and Haskell ?
os.Rename() can't move file between Drive in Widnows, since Win32 API does not support it

Read learning perl file system, and it's implementation

os.Stat() -> file existence
os.path.exists() of Python : internally call method os.stat() which internally call syscall
Perl ? 
Haskell ?
C stat(), access()

os.File.Stat() returns os.FileInfo, and there is inode information and other information which depends on OS


In Linux, you can't get information about file creation date for each file, since there is no API to get this although OS has thin information

### nofity file modification
When working on one file by using two different text editor, one should be recognize when the other perform write operation on file buffer or disk

#### how to recognize modification ?
1. Go to see perodically
    - we can use `stat(2)` for this
1. Get notification (event) -- we will see this

#### implementations to get notification
- inotify in Linux uses event
```c
$include <inotify.h>
int inotify_init(void);
```
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
- Many languages has wrapper for this mechanism, let's see Go
```
$ go get gopkg.in/fsnotify.v1 
```
This 3rd party package calls inotify for Linux, kqueue for BSD and ReadDirectoryChangesW for Windows

### File Lock

```c
#include <sys/file.h>

#define   LOCK_SH   1    /* shared lock */
#define   LOCK_EX   2    /* exclusive lock */
#define   LOCK_NB   4    /* don't block when locking */
#define   LOCK_UN   8    /* unlock */

int flock(int fd, int operation);
```
- exclusive lock
    - If an object is exclusively locked, shared locks cannot be obtained.
    - If an object is exclusively locked, other exclusive locks cannot be obtained.
- shared lock: 
    - Multiple shared locks can co-exist.
    - If one or more shared locks already exist, exclusive locks cannot be obtained.



## File MMAP

- Advantages
    1. Read/Write on memory-mapped file, thus can avoid frequent syscall
    1. Aside from any page fault (cache miss), there will be no syscall calling or context switching(swapping file)
    1. Multiple process can share this memory space
    1. COW
- Disadvange
    1. MMAP is available with unit of page. For example, when you set page size to 4 KiB, a 7 byte mapping wastes 4,089 bytes. 
    1. memory space fragment can be occured, when various size of mapping happens frequently
    1. When just reading file with sequential access from front to end, performance will not increased

- POSIX syscall
```c
#include <sys/mman.h>
void * mmap(void *addr, size_t len, int prot, int flags, int fd, off_t offset);
```

- Windows also provide this mechanism
```c
HANDLE WINAPI CreateFileMapping(
  _In_     HANDLE                hFile,
  _In_opt_ LPSECURITY_ATTRIBUTES lpAttributes,
  // ...
);
```

```
$ go get github.com/edsrzf/mmap-go ⏎
```

## Multiplexing

- Network I/O
- File I/O

- how to handle thread blocking caused by I/O ?
    1. Multi process (fork) -- out of scope for this presentation
    1. Multi thread -- out of scope for this presentation
    1. I/O Multiplexing + event/signal (less context switching, less memory) -- let's talk about this now
        1. select
        1. poll
        1. epoll
        1. kqueue
    1. Asynchronous + callback/event/signal -- will talk about this later

- What is Multiplexing ?
> Multiplexing (sometimes contracted to muxing) is a method by which multiple analog or digital signals are combined into one signal over a shared medium. The aim is to share a scarce resource - Wikipedia


### select
```c
#include <sys/select.h>
int select(int nfds, fd_set *restrict readfds, fd_set *restrict writefds, fd_set *restrict errorfds, struct timeval *restrict timeout);
// when socket received data
// when socket can send data
// when exception occur on socekt
```

Good : Many OS supports select()
Bad : Need to send data(set of file descriptor) to kernal often

### poll
```c
#include <poll.h>
int poll(struct pollfd fds[], nfds_t nfds, int timeout);
```
almost same with select
Good : Call system call less than select()

select and poll is __level trigger__
    - Level trigger : Register event again and again whenever there still remained data in read/write buffer(event is accumulated)
epoll supports both __edge trigger and level trigger__
    - Edge trigger : Register event onlhy once when data reached to read/write buffer

### epoll (Linux)
Good : select() called multiple times, hence application need to hand over same data to OS again and again

```c
#include <sys/select.h>
int epoll_create(int size);
int epoll_ctl(int epfd, ...);
int epoll_wait(int epfd, ...)
```

### kqueue
epoll in BSD

### overlapped IO, IOCP (Windows)

## Sync/Async Block/Non-block
### For General
- IBM
    - This material is old, Multiplexing can be a Non-blocking now
#### Sync/Async
#### Block/Non-block
### For Network
#### Sync/Async
Async
#### Block/Non-block
### For I/O
#### Sync/Async
- return send/read function immediately
#### Block/Non-block
