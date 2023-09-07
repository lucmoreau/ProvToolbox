


# TODO
- upgrade scala version
- services using webapp.filter and upgrade webjar versions
- upgrade to servlet5, at the same time sort out recurring conflicting package
- remove viz, clean swagger api (check provman), add template as options in pull down


# DeleteJob/Redis issue
```agsl
06:01:18.861 [main] INFO  org.openprovenance.prov.service.translator.StorageConfiguration - Configuration --- {PSERVICE_DEL_PERIOD=600, PSERVICE_REDIS_PORT=6379, PSERVICE_DBNAME=prov, PSERVICE_STORAGE=fs, PSERVICE_INDEX=redis, PSERVICE_AUTODELETE=true, PSERVICE_CACHE=200, PSERVICE_REDIS_HOST=localhost}
```
```
6:21:41.389 [DefaultQuartzScheduler_Worker-6] ERROR org.quartz.core.JobRunShell - Job graph.r26572 threw an unhandled Exception: 
redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketTimeoutException: Read timed out
	at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:201) ~[jedis-2.8.1.jar:?]
	at redis.clients.util.RedisInputStream.readByte(RedisInputStream.java:40) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Protocol.process(Protocol.java:141) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Protocol.read(Protocol.java:205) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Connection.readProtocolWithCheckingBroken(Connection.java:297) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Connection.getBinaryMultiBulkReply(Connection.java:233) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Connection.getMultiBulkReply(Connection.java:226) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Jedis.hmget(Jedis.java:671) ~[jedis-2.8.1.jar:?]
	at org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex.get(RedisDocumentResourceIndex.java:85) ~[prov-storage-index-redis-2.0.0-SNAPSHOT.jar:?]
	at org.openprovenance.prov.service.core.jobs.JobDeleteDocumentResource.execute(JobDeleteDocumentResource.java:37) ~[prov-service-core-2.0.0-SNAPSHOT.jar:?]
	at org.quartz.core.JobRunShell.run(JobRunShell.java:202) [quartz-2.3.2.jar:?]
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573) [quartz-2.3.2.jar:?]
Caused by: java.net.SocketTimeoutException: Read timed out
	at java.net.SocketInputStream.socketRead0(Native Method) ~[?:?]
	at java.net.SocketInputStream.socketRead(SocketInputStream.java:115) ~[?:?]
	at java.net.SocketInputStream.read(SocketInputStream.java:168) ~[?:?]
	at java.net.SocketInputStream.read(SocketInputStream.java:140) ~[?:?]
	at java.net.SocketInputStream.read(SocketInputStream.java:126) ~[?:?]
	at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:195) ~[jedis-2.8.1.jar:?]
	... 11 more

```


```agsl
org.quartz.SchedulerException: Job threw an unhandled exception.
	at org.quartz.core.JobRunShell.run(JobRunShell.java:213) [quartz-2.3.2.jar:?]
	at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573) [quartz-2.3.2.jar:?]
Caused by: java.lang.ClassCastException: class java.lang.Long cannot be cast to class java.util.List (java.lang.Long and java.util.List are in module java.base of loader 'bootstrap')
	at redis.clients.jedis.Connection.getBinaryMultiBulkReply(Connection.java:233) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Connection.getMultiBulkReply(Connection.java:226) ~[jedis-2.8.1.jar:?]
	at redis.clients.jedis.Jedis.hmget(Jedis.java:671) ~[jedis-2.8.1.jar:?]
	at org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex.get(RedisDocumentResourceIndex.java:85) ~[prov-storage-index-redis-2.0.0-SNAPSHOT.jar:?]
	at org.openprovenance.prov.service.core.jobs.JobDeleteDocumentResource.execute(JobDeleteDocumentResource.java:37) ~[prov-service-core-2.0.0-SNAPSHOT.jar:?]
	at org.quartz.core.JobRunShell.run(JobRunShell.java:202) ~[quartz-2.3.2.jar:?]

```