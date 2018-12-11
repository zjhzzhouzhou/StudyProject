package com.zz.amqp1.bean.common;

/**
 * Description:业务会话
 * User: zhouzhou
 * Date: 2018-12-11
 * Time: 1:30 PM
 */
public class BizSession {

    private static ThreadLocal<BizContext> INSTANCE = new ThreadLocal<BizContext>(){
        @Override
        protected BizContext initialValue() {
            // 这边初始化当前会话的环境并且初始化
            BizContext context = new BizContext();
            context.setSystemEnv("dev");
            return context;
        }
    };



    /**
     * 获取当前会话
     * @return 会话实例
     */
    public static BizContext currentSession(){
        return INSTANCE.get();
    }

    /**
     * 创建会话
     * @param session 会话实例
     */
    public static void store(BizContext session){
        INSTANCE.set(session);
    }

    /**
     * 销毁会话
     */
    public static void destroy (){
        INSTANCE.remove();
    }

}
