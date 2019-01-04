package com.zz.amqp1.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:二叉树主类
 * User: zhouzhou
 * Date: 2018-12-29
 * Time: 2:53 PM
 */
public class BinTree <T>{

    private List<BinNode<T>> nodes = new ArrayList<>();

    private BinNode<T>root = new BinNode<>();

    public void createTree(List<T> list ){
        // 将list 中的值, 依次转换成Node节点
        list.forEach(t->{
            nodes.add(new BinNode<>(t));
        });

    }


    public void leftDisplay(BinNode node){

    }

}
