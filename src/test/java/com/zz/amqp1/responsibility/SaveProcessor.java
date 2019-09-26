package com.zz.amqp1.responsibility;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-09-04
 * Time: 9:47
 */
public class SaveProcessor extends Thread implements IRequestProcessor {
    // 责任链
    LinkedBlockingQueue<ZRequest> requests = new LinkedBlockingQueue<>();

    // 下一个处理器
    private IRequestProcessor nextProcessor;

    //
    private volatile Boolean isFinish = true;

    public SaveProcessor() {
    }

    @Override
    public void run() {
        while (isFinish) {
            try {
                ZRequest request = requests.take();// 阻塞式获取数据
                System.out.println(String.format("save请求{%s}", request.getName()));
                if (nextProcessor != null){
                    nextProcessor.process(request);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public SaveProcessor(IRequestProcessor nextProcessor) {
        this.nextProcessor = nextProcessor;
    }

    @Override
    public void process(ZRequest request) {
        // 验证请求参数
        requests.add(request);
//        nextProcessor.process(request);
    }
}
