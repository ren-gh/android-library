
package com.rengh.java.study.listmap;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class MyClass {
    public static void main(String[] args) {
        System.out.println("Start...");

        System.out.println("===================");

        /**
         * Enumeration 一个古老的迭代器
         */
        Enumeration enumeration = new StringTokenizer("A-B-C", "-");
        while (enumeration.hasMoreElements()) {
            System.out.println(enumeration.nextElement());
        }

        System.out.println("===================");

        /**
         * Iterator 新迭代器 代码与具体集合类型耦合性弱，复用性更强。缺点就是无法获取指定的元素，只能挨个遍历。
         */
        List<String> list = new ArrayList<>();
        list.add("J");
        list.add("Q");
        list.add("K");
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("===================");

        /**
         * ListIterator 允许我们向前、向后两个方向遍历 List; 在遍历时修改 List 的元素； 遍历时获取迭代器当前游标所在位置。
         */
        List<String> list2 = new ArrayList<>();
        list2.add("A");
        list2.add("B");
        list2.add("C");
        // ListIterator listIterator = list.listIterator();
        ListIterator listIterator = list2.listIterator(2);
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
            System.out.println(listIterator.nextIndex());
        }

        System.out.println("===================");

        // list.retainAll(list2);// 保留两个集合都有的元素，允许为空
        list.addAll(list2);// 合并两个集合所有的元素，允许重复
        Iterator iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            System.out.println(iterator2.next());
        }

        System.out.println("===================");

        Object[] arr = new Object[list.size()]; // 大小必须语 list 的大小一致
        list.toArray(arr);
        for (Object tmp : arr) {
            System.out.println(tmp);
        }

        System.out.println("===================");

        // list.stream().forEach(new Consumer<String>() {
        // @Override
        // public void accept(String s) {
        // System.out.println(s);
        // }
        // });
        // Lamba
        list.stream().forEach(s -> System.out.println(s));

        System.out.println("===================");

        System.out.println("End!");
    }
}
