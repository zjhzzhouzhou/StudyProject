package com.zz.amqp1.tree;

import lombok.Data;

/**
 * Description: 二叉树节点类
 * User: zhouzhou
 * Date: 2018-12-29
 * Time: 2:24 PM
 */
@Data
public class BinNode <T> {

    BinNode(){}
    // 左节点
    private BinNode left;
    // 右节点
    private BinNode right;
    // 值
    private T value;

    public BinNode(T t){
        this.value = t;
    }

}
