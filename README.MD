# SpringBoot Quartz 集群
SpringBoot +Quartz + Mybatis + LayUI<br/>
通过数据库实现了Quartz集群。<br/>

一个动态管理Quartz任务的例子。不需要重启服务，即可以增加、修改、删除、停止、启动任务。

## SQL脚本
本例中使用的是MySQL数据库。<br/>
在resources\db目录下，需要创建gupao数据库，和sys_job表。<br/>
集群功能需要创建11张表：<br/>
在官网的Downloads链接中，提供了11张表的建表语句：
quartz-2.2.3-distribution\quartz-2.2.3\docs\dbTables
2.3版本之后在这个路径下：src\org\quartz\impl\jdbcjobstore 

注意：如果修改了任务的类名或者路径，因为任务已经保存在数据库中，需要清空上述11张表和sys_job重新添加。<br/>
否则启动会报错。

## 测试说明
1. 如果直接在数据库修改了任务，启动之前，必须清空quartz的11张表，参考：resources/db/清空表脚本
2. 启动工程，访问： http://localhost:9096/job/jobList   <br/>
3. 第三个任务是发送邮件的任务，SQL语句需要修改为自己的QQ账号和授权码，不需要可以把状态改成0或者删掉。

## 代码说明
1. 需要运行的任务类放在task包下面，必须实现BaseJob接口。
2. config下面的类是保证SpringBoot工程启动的时候任务可以运行。
3. JobFactory是保证Spring的对象可以注入到Job里面。
3. 最核心的类是 SchedulerUtil。

## 并发功能说明
1.  在Job类上添加`@DisallowConcurrentExecution` 注解以后，任务就不会并行执行。<br/>
比如Task1，休眠60s（代表任务执行耗时60s），即使运行频率是1秒一次，在60s内也只会执行一次。<br/>
这时会造成其后的任务misfire（错过触发）。当正在执行的任务数超过线程池大小时，也会发生这种情况。<br/>
比如Task1去掉`@DisallowConcurrentExecution` 注解，如果1次运行60s，最多也只有10个Task1在运行。<br/>
造成misfire以后怎么办？CronTrigger和SimpleTrigger有不同的策略。


## 集群功能测试
1. 复制多个工程，修改端口启动服务。
可以看到任务会随机地分配到几个机器，但是一定不会重复执行。

2. 停掉一个或者多个服务，其他的服务会检测到，并接管任务。