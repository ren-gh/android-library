
package com.rengh.java.study.own.algorithm;

import com.rengh.java.study.utils.BaseUtils;

public class MySort {

    public static void main(String[] args) {
        int[] array = new int[200000];
        for (int i = 0; i < array.length; i++) {
            int temp = BaseUtils.getRandom(0, 1000000);
            array[i] = temp;
        }
        BaseUtils.print(array);

        long start = System.currentTimeMillis();
        selectSort(array, false);
        long end = System.currentTimeMillis();
        System.out.println("选择排序，数据长度：" + array.length + ", 耗时：" + (end - start) + " ms");
        BaseUtils.print(array);
    }

    /**
     * 选择排序
     * 
     * @param array
     */
    public static void selectSort(int[] array) {
        selectSort(array, false);
    }

    public static void selectSort(int[] array, boolean big2small) {
        if (null == array) {
            throw new NullPointerException("数组不能为 null");
        }
        if (0 == array.length) {
            return;
        }
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minOrMax = i;
            for (int j = i + 1; j < n; j++) {
                if (big2small) {
                    if (array[minOrMax] < array[j]) {
                        minOrMax = j;
                    }
                } else {
                    if (array[minOrMax] > array[j]) {
                        minOrMax = j;
                    }
                }
            }
            if (i != minOrMax) {
                int temp = array[i];
                array[i] = array[minOrMax];
                array[minOrMax] = temp;
            }
        }
    }
}
