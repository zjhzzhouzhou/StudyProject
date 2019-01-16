package com.zz.amqp1.multithread;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:性能较好的线程池
 * User: zhouzhou
 * Date: 2018-12-04
 * Time: 5:14 PM
 */
public class ThreadPoolTest {

    private static final int count = 20;

    @Test
    public void tsetSimpleThreadPool() throws Exception {
        Long start = System.currentTimeMillis();

        // 5个核心线程, 队列为10个长度, 超过10个回归主线程走
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10), new ThreadPoolExecutor.CallerRunsPolicy());

        List<Future> list = Lists.newArrayList();
        // 小哥哥来五发操作
        for (int i = 0; i < count; i++) {
            list.add(doExecute(threadPoolExecutor));
        }

        // 遍历抽奖结果
        for (Future future : list) {
            System.out.println(future.get());
        }

        System.out.println(String.format("操作时间共计为:{%s}毫秒", System.currentTimeMillis() - start));

    }

    // 模拟2秒抽奖
    private class TestTask implements Callable<String> {

        @Override
        public String call() throws Exception {
            // 休息2秒
            TimeUnit.SECONDS.sleep(2);
            return String.format("随机号码为: {%s}", new Random().nextInt(100));
        }
    }

    // 线程池执行任务
    private Future<String> doExecute(ThreadPoolExecutor threadPoolExecutor) {
        return threadPoolExecutor.submit(new TestTask());
    }
}
