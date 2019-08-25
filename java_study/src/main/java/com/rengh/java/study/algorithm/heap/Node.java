
package com.rengh.java.study.algorithm.heap;

public class Node<K, T> {
    public K key;// 关键字
    public T data;// 数据域

    public void display() {
        System.out.print("Node:{"
                + key.toString()
                + ", "
                + data.toString()
                + "}");
    }
}
