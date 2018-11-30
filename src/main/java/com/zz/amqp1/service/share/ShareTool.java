package com.zz.amqp1.service.share;

import com.zz.amqp1.utils.SpringContextUtil;

/**
 * Description:共享工具类
 * User: zhouzhou
 * Date: 2018-09-07
 * Time: 10:28
 */
public class ShareTool {

    private ShareHandler shareHandler;

    private final String  lock = "我只是一个同步锁,别管我";

    public  ShareHandler initHandler(String type) {
        if (type.equals("1")) {
//            this.shareHandler = oneTools;
            this.shareHandler = (ShareHandler)SpringContextUtil.getBean("oneTools" );
        } else if (type.equals("2")) {
//            this.shareHandler = twoTools;
            this.shareHandler = (ShareHandler)SpringContextUtil.getBean("twoTools" );

        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  shareHandler;
    }

    public void pirnt(){

        if (shareHandler == null){
            throw new RuntimeException("请初始化工具");
        }
        shareHandler.print();
    }
}
