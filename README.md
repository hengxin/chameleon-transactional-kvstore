# chameleon-transactional-kvstore [![Build Status](https://travis-ci.org/hengxin/chameleon-transactional-kvstore.svg?branch=2pc)](https://travis-ci.org/hengxin/chameleon-transactional-kvstore)

A distributed, partitioned, replicated, transactional, main-memory key-value data store prototype which implements RVSI.
For details, please refer to [this wiki](https://github.com/hengxin/chameleon-transactional-kvstore/wiki).

# Branches

- master: `Chameleon` when restricted to the master-slave replication mechanism.
- 2pc: the current working branch; `Chameleon` with both master-slave replication and data partitions among masters.

***Note:*** The `2pc` branch has been merged into the `master` branch.
