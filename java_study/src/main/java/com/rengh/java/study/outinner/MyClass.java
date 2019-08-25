
package com.rengh.java.study.outinner;

public class MyClass {
    public static void main(String[] args) {
        System.out.println("Start...");

        /**
         * 内部类调用
         */
        // OutClass.InNormalClass inNormalClass = new OutClass().new InNormalClass();
        OutClass publicClass = new OutClass();
        publicClass.println("test 0...");
        OutClass.InNormalClass inNormalClass = publicClass.new InNormalClass();
        inNormalClass.println("test 1...");
        publicClass.printNormalType();
        // Or
        new OutClass().new InNormalClass().println("test 2...");

        /**
         * 静态内部类
         */
        OutClass.InStaticClass.println("test...");

        System.out.println("End!");
    }
}
