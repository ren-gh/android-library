
package com.rengh.java.study.outinner;

/**
 * https://www.cnblogs.com/firefrost/p/5064210.html
 * https://blog.csdn.net/xiaobaixiongxiong/article/details/86562211
 * 区别：
 * 1.静态内部类可以有静态成员(方法，属性)，而非静态内部类则不能有静态成员(方法，属性)。
 * 2.静态内部类只能够访问外部类的静态成员,而非静态内部类则可以访问外部类的所有成员(方法，属性)。
 * 3.实例化方式不同：
 * （1）实例化成员内部类“”
 * 通过外部类对象创建
 * OutClassTest oc1 = new OutClassTest();
 * OutClassTest.InnerClass no_static_inner = oc1.new InnerClass();
 * （2）实例化静态内部类：
 * OutClassTest.InnerStaticClass inner = new OutClassTest.InnerStaticClass();
 * 作用：
 * 内部类可以访问外部类的所有成员，包括私有属性.
 * 方便将存在一定逻辑关系的类组织在一起，又可以对外界隐藏。
 * 每个内部类都能独立地继承一个接口，而无论外部类是否已经继承了某个接口。
 * 因此，内部类使多重继承的解决方案变得更加完整。
 */
public class OutClass {
    private static final String TAG_STATIC = OutClass.class.getSimpleName();
    private final String TAG = OutClass.class.getSimpleName();

    public void println(String msg) {
        System.out.println(OutClass.class.getSimpleName() + ", " + msg);
    }

    public void printNormalType(){
        System.out.println(new InNormalClass().TYPE);
        // System.out.println(InNormalClass.TYPE_STATIC);
        System.out.println(InNormalClass.TYPE_STATIC_FINAL);

        // 局部内部类
        class Class1{
            private int a = 123456;
        }
    }

    // 成员内部类
    public class InNormalClass {
        private int TYPE = 0;
        // Error 非静态内部类不支持定义静态变量
        // private static int TYPE_STATIC = 1;
        // 非静态内部类支持定义静态常量
        private static final int TYPE_STATIC_FINAL = 2;

        // 内部类不支持静态方法
        // public static void print(String msg){
        public void println(String msg) {
            System.out.println(InNormalClass.class.getSimpleName() + ", " + msg + ", parent: " + TAG);
        }
    }

    // 静态内部类
    public static class InStaticClass {
        public static void println(String msg) {
            System.out.println(InStaticClass.class.getSimpleName() + ", " + msg + ", parent: " + TAG_STATIC);
        }
    }
}
