package com.zz.amqp1.common.redistoken;

public enum Token {
    SUCCESS,
    FAILED;
    public boolean isSuccess(){
        return this.equals(SUCCESS);
    }
    public boolean isFailed(){
        return this.equals(FAILED);
    }
}
