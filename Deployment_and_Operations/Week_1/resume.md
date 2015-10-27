M202

Mongo DB Memory Model

more information -> docs.mongodb.org/manual/faq/storage

Memory mapped files (nmap())
    -> mapped into virtual memory
    -> accessed lazily as needed -> then it gets on the physical memory
    this works until physical memory is full. When this happens, the kernel tries to find room and it does that finding a page to discard and allocate the new one.

Discarding Pages Algorithm
  LRU -> Least Recently Used. That's how the kernel decides which one it will get rid off.

Resident Memory in MongoDB

Working set
  Portion of data that is accessed most often
    - indexes
    - subset of data
  How to figure out working set size?
    good knowledge of your data(and indexes)
      - average document size
      - average size of index bucket, key
  Resident Memory as a Metric
    - for higher than "working set" potentially
    - for lower than "working set" - this is a little more confusing, but it has to do with the journaling

    Journaling:
      data file                                 journal file
          |                                          /\
        nmap()                                       |
          |                                     commits -> syncs every 100ms
         \/                                          |
      shared view--------------remap------------->private view
                                                    /\
                                                     |
                              mongod-----------------
If a crash happens with the above scenario the journal
file replays the changes in the shared view and then persists in the data file.

In writes operations, the resident memory may drop and create a cliff on the graph of resident memory utilization. That's the way Mongo works with journaling, and it doesnt mean the RAM memory is lost, it just needs to be pointed to right place again.

Storage Engine: WiredTiger

* New in MongoDB 3.
* First pluggable storage engine
* Features:
  - Document level locking
  - compression
  - lacks some pitfalls of MMAP v1
  - Performance gains

WiredTiger was built separately from MongoDB and are used by others DBs

To see some options:
  mongod --help -> shows the options of the storageEngine.

  $ mongod --storageEngine wiredTiger (it fails if you have a mongod running before the system and it uses the default storage engine, to fix that you need to rm -rf /data/db/(astherisc)

To see if this worked, you can get inside mongo shell and check using the following:

$ mongo
> use local
> db.startup_log.fin().pretty() - under "storage" : { "engine" : "wiredTiger"}

Then you can start a new collection
> use test
> db.things.save({"test" : 1})

WiredTiger Internals
Store data in B-tree .
Writes are initially separate, incorporate later
Two caches
  - WiredTiger cache     ->  FileSystem cache    -> disk
  it does that every 60s from the end of the last checkpoint.A checkpoint is a valid consistent of view of your data. If you're using Journaling you dont need the checkpoint, you're guarantee to have the most consistent view of the data

Document Level Locking:
  Good concurrency protocols. You're writes should scale according to the threads. With some limitiations -> dont try to update the same file at the same time, and dont try to have more threads than cpus

Compression
  snappy(default) - fast
  zlib - more compression but less fast
  none

Process Restarts, Dropping caches:
What happens is:

Restart:
the data keeps there,

hard page fault - data was read from disk
soft page fault - data already in memory, reassined. This is extremely fast.

Reboot: full operating system is restarted
- all caches invalidated.

How do I invalidated without rebooting?
  -> First approach(not so good) - echo n > /proc/sys/vm/drop_caches, where n is 1, 2 or 3 and 3 deletes all the cache files
  -> Second approach -> sudo sysctl -w vm.drop_caches=n, where n is 1,2 or 3.

Storage and Disk considerations:
  - spinning disks(traditional)
    - not necessarily it will be stored together and so the head will be reading the whole platter.
    - if you optimezed the stores and the docuemnts are in sequence, you will get a better performance.
    - Use cases in MongoDB:
      - capped collections
      - journal
    - best way to use spinning disks is to keep as much info on memory.

  - Network Storage:
    - NFS or similar
    - abstracted services( present as blcok devices). Examples:
      - ebs
      - vmdk(vmware)
    - adds latency because of bandwith

  - solid state disks(ssd)
    - relatively immature
    - significantly faster ( no seeking), but still not fast as memory
    - variable performance( firmware, controller)
    - consumer(mlc) vs enterprise(slc)

RAID
  instead of using one disk, it will use a lot of them.
  - RAID 0
      you save each piece of the document  in each  disk. You make it faster, because you splitted the docuemnt in various disks, but the con is that if one disk fails, all of them will fail to get a document.
  - RAID 1
      mirroing, just provide redundancy. it may  increase some performance, because you can read from the fastest one. the down side is that you're losing half of your disk capacity
  - RAID 10
      mix of the both of them. you have two  cluster of two disk(so you can split the docuemnt in the two cluster and you have redundancy of both of cluster.)

Complications ->
  - backups are hard.
  - snapshot

You can find more info on: info.mongodb.com/rs/mongodb/images/AWS_NoSQL_MongoDB.pdf

MongoDB and NUMA Hardware(System Level Tuning):

- based on docs.mongodb.org/manual/administration/productions-notes/#mongodb-and-numa-hardware

NUMA - Not Unified Memory Architecture

basic principle - multiple cpus in the system, and they will have a better connections for  specific memory . so the idea is to divide the memory in chunks and make each cpu responsible for one chunk.  So there are some penalties, for example if you access the cpu1 to get a information that is stored in the memory allocated for the cpu2, it will make it slower than getting it from the cpu2 itself.

So, NUMA is not enabled for MongoDB
If you do have it, you  can do the following:
  - disable it in BIOS
  - set interleave policy, disable zone reclaim -> numactl --interleave=all /usr/bin/local/mongod && echo 0 > /proc/sys/vm/zone_reclaim_mode


FileSystems and Options:

-ext4 -> allocate support -> ms

-xfs -> allocate support -> ms

  - mount optioni(for ext4 and xfs): noatime(/etc/fstab) it updates on anyaccess(including reads)

  - options exclusively for xfs:
      - alignment 
      - strip width (chunk size)

- ext 3 or an old kernel, instead of using the allocate support, it fills with zeros. - seconds

- NFS - does not play well with journaling.
  - you can have the data on the NFS, but put the JOURNAL somwehere else -> that causes a complicated backups.

Swap

  - allocate some swap for mongodb.
      -if you dont, you can have the OOM(Out of Memory) killer avoidance.
      - and this essentially kills the process with highest memory usage, and usually mongo is the process that has the highest mem usage.

Readahead


