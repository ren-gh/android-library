
package com.rengh.java.study.utils;

public class BaseUtils {
    public static boolean DEBUG = false;

    /**
     * 是否开启 DEBUG 模式
     *
     * @param debug
     */
    public static void setDebug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 获取一个随机数
     *
     * @param max 包含
     * @return
     */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 获取一个指定范围的随机数
     *
     * @param min 包含
     * @param max 包含
     * @return
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("最大数必须大于最小数");
        }
        return min + (int) (Math.random() * (max - min + 1));
    }

    /**
     * 对数组的两个元素换位
     *
     * @param array 目标数组
     * @param i 需要交换的索引i
     * @param j 需要交换的另一个索引j
     * @throws NullPointerException if array is null
     * @throws ArrayIndexOutOfBoundsException if i|j is more than array.length-1
     */
    // 公有的API方法，我们一般要对参数进行安全检查，抛出恰当的异常，并在方法的注释中说明
    public static void swap(int[] array, int i, int j) {
        if (array == null)
            throw new NullPointerException("数组不能是空");
        if (i >= array.length - 1 || j >= array.length - 1)
            throw new ArrayIndexOutOfBoundsException("需要交换的索引应在数组长度范围内");
        if (i == j)
            return;
        array[i] = array[i] + array[j];
        array[j] = array[i] - array[j];
        array[i] = array[i] - array[j];
    }

    /**
     * 打印数组
     *
     * @param array
     */
    public static void print(int[] array) {
        if (null == array) {
            System.out.println("数组为 null");
            return;
        }
        if (0 == array.length) {
            System.out.println("数组长度为 0");
            return;
        }
        int n = array.length;
        if (20 < array.length) {
            System.out.println("数组元素太多，只打印前二十个：");
            n = 20;
        }
        System.out.print("{");
        for (int i = 0; i < n; i++) {
            System.out.print(array[i]);
            if (i != n - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

}
