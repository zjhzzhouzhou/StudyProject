package com.zz.amqp1.responsibility;

/**
 * Description:
 * User: zhouzhou
 * Date: 2019-09-04
 * Time: 11:07
 */
public class App  {

    IRequestProcessor requestProcessor;

    public App() {
        // 构建完整的责任链
        PrintProcessor printProcessor = new PrintProcessor();
        printProcessor.start();
        SaveProcessor saveProcessor = new SaveProcessor(printProcessor);
        saveProcessor.start();
        requestProcessor = new PreProcessor(saveProcessor);
        ((PreProcessor)requestProcessor).start();

    }



    public static void main(String[] args) {

        ZRequest request = new ZRequest("zhouzhou");
        IRequestProcessor requestProcessor = new App().requestProcessor;
        requestProcessor.process(request);

    }
}
