# jianshu blog url: https://www.jianshu.com/p/65fe3f224414
在工作空余时间，也看了慕课网上关于高并发秒杀业务的解决方案，收货颇多。

* 1.商品详情页是产生高并发的一个点。中小型企业一般采用Nginx+页面静态化就能解决。我们可以把静态界面加入到CDN缓存中。CDN可以加速用户获取数据的速度，一般部署再离用户最近的网络节点上。

* 2.关于秒杀操作，我们无法去用CDN缓存。后端使用缓存比较困难，存在库存一致性的问题。在热度商品的秒杀上，存在一行数据竞争的情况。

* 3.关于秒杀地址暴露，我们也无法去用CDN缓存。适合用Redis进行缓存商品，一致性维护成本低。Redis和Mysql数据一致性维护可以采用超时穿透/主动更新策略。

* 4.关于获取秒杀时间的获取，其实不用优化。Java访问一次内存是10ns，而1秒等于=10亿ns。相当我1s的时间进行1亿次的new Date()。

* 5.比较成熟的解决方案： 原子计数器->Redis，记录行为消息->分布式MQ，消费消息并落地->MySQL。但是存在数据一致性和回滚问题，幂等性难以保证(会造成重复秒杀)，这种架构不适合新手架构。

* 6.经过Jmeter压力测试，一条update商品库存语句的QPS是4W。一般用户进行秒杀操作，会受到网络延迟+GC的串行化阻塞。一般来说用户执行秒杀操作，正常的业务来说先执行减少商品库存操作，再插入用户购买明细。但是update同一行商品记录会造成行级锁。行级数会在commit事务后之后释放。在并发量集中的秒杀操作，这些操作会造成阻塞，因此我们优化的方向是减少行级锁持有的时间。我们可以先执行插入用户购买明细操作，然后更新库存操作。因为insert可以并行！

* 7.关于秒杀操作，我们可以把秒杀的业务逻辑写到MySQL端(也就是存储过程)，整个事务在MySQL端完成，优化网络延迟和GC干扰。

优化总结：
* 1.前端控制：合理暴露秒杀地址，秒杀按钮防重复。
* 2.后端控制：动静态数据分离，CDN缓存，后端缓存，行级锁竞争优化，减少事务时间。
