package com.zz.amqp1.highrailway;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:高铁选位子
 * <p>
 * 需求, 作为分为abcde,车厢有1-17
 * </p>
 * User: zhouzhou
 * Date: 2018-12-05
 * Time: 5:11 PM
 */
public class ChoseSeat {

    // 17排座位
    private static final int row = 17;
    // 座位种类abcde
    private static final List<String> seat_Type = Lists.newArrayList("a", "b", "c", "d", "e");
    // 剩余可用的座位
    private static volatile List<String> availableSeatNo = Lists.newArrayList();
    // 抢票人数
    private static final int people = 100;


    @Test
    public void choseSeat() {
        // 初始化线程池, 满足5人同时抢票,10人最大
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10), new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化所有的座位
        initSeats();
        // 初始化潘晔情抢号位置
        int rank = new Random().nextInt(100);
        System.out.println(String.format("-------------潘晔情抢号位置为:{%s}--------------", rank));
        List<Future> futures = Lists.newArrayList();
        for (int i = 0; i < people; i++) {
            String name = "急着回家的热心市民" + i;
            if (i == rank) {
                name = "潘晔晴";
            }
            futures.add(threadPoolExecutor.submit(new RushTask(name)));
        }

        futures.forEach(future -> {
            try {
                String o = (String) future.get();
                System.out.println(o);
                if (o.contains("潘晔晴")) {
                    System.out.println("------------------------------\n" +
                            o + "\n -------------------------------");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }


    // 初始化所有座位
    private void initSeats() {
        for (int i = 1; i <= row; i++) {
            for (String s : seat_Type) {
                availableSeatNo.add(i + s);
            }
        }
    }

    //抢票task
    @Data
    @AllArgsConstructor
    private class RushTask implements Callable<String> {

        private String name;

        @Override
        public String call() throws Exception {
            Thread.sleep(1000L);
            synchronized (RushTask.class) {
                // 判断有没有票了, 没票则抛异常
                if (CollectionUtils.isEmpty(availableSeatNo)) {
                    System.out.println("对不起,没票了");
                    return name + "购票失败, 无票";
                }
                if (name.contains("潘晔晴")) {
                    List<String> nos = Lists.newArrayList();
                    for (int i = 0; i < 2; i++) {
                        for (String no : availableSeatNo) {
                            if (no.contains("a")) {
                                availableSeatNo.remove(no);
                                nos.add(no);
                                break;
                            }
                            if (i == 0) {
                                return "潘晔晴" +
                                        "一张票都没有买到";
                            }
                        }
                    }
                    System.out.println(String.format("{%s}号座位已经被抢,购票者为:{%s}", nos, name));
                    return String.format("恭喜乘客{%s}购得车票{%s}", name, nos);

                } else {
                    String data = availableSeatNo.remove(new Random().nextInt(availableSeatNo.size()));
                    System.out.println(String.format("{%s}号座位已经被抢,购票者为:{%s}", data, name));
                    return String.format("恭喜乘客{%s}购得车票{%s}", name, data);
                }

            }

        }
    }

}
