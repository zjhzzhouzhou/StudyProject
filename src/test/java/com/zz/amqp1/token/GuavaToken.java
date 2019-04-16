package com.zz.amqp1.token;

import com.google.common.util.concurrent.RateLimiter;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * Description: guava token策略执行
 * User: zhouzhou
 * Date: 2019-04-14
 * Time: 3:32 PM
 */
@Data
public class GuavaToken {

    private RateLimiter rateLimiter;

    private double tps;

    private double countOfReq;

    public GuavaToken(int tps, int countOfReq) {
        this.tps = tps;
        this.countOfReq = countOfReq;
    }

    /**
     * 初始化定义一个token 桶
     *
     * @return
     */
    public GuavaToken createBucket() {
        rateLimiter = RateLimiter.create(tps);
        return this;
    }

    /**
     * 预热时间 漏桶
     * @return
     */
    public GuavaToken createWarningup(){
        rateLimiter = RateLimiter.create(tps,100, TimeUnit.MILLISECONDS);
        return this;
    }

    private void processRequest() {
        System.out.println("RateLimiter" + rateLimiter.getClass());
        long start = System.currentTimeMillis();
        for (int i = 0; i < countOfReq; i++) {
            // 处理请求中
            rateLimiter.acquire();
        }
        long need = System.currentTimeMillis() - start;
        System.out.println(String.format("请求数量{%s},耗时为{%s},tps 为{%s},实际 tps{%s}",
                countOfReq, need, rateLimiter.getRate(), Math.ceil(countOfReq / (need / 1000.0))));

    }

    public void process(){
        for (int i = 0; i < 5; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            processRequest();
        }
    }

    public static void main(String[] args) {
        new GuavaToken(50,100).createBucket().process();
        new GuavaToken(50,100).createWarningup().process();

    }
}
