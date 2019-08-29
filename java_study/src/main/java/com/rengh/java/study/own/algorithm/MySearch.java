
package com.rengh.java.study.own.algorithm;

import com.rengh.java.study.utils.BaseUtils;

/**
 * 顺序查找、折半查找（二分查找）
 */
public class MySearch {
    public static void main(String[] args) {
        long start, end;

        // 生成数组
        start = System.currentTimeMillis();
        int[] array = new int[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = BaseUtils.getRandom(999);
        }
        end = System.currentTimeMillis();
        System.out.println("生成数组耗时：" + (end - start) + " ms");

        BaseUtils.print(array);

        start = System.currentTimeMillis();
        MySort.selectSort(array);
        end = System.currentTimeMillis();
        System.out.println("选择排序耗时：" + (end - start) + " ms");

        // 进行查找
        start = System.currentTimeMillis();
        int index = search(array, 4);
        end = System.currentTimeMillis();
        System.out.println("顺序查找结果：" + index + "，耗时：" + (end - start) + " ms");

        start = System.currentTimeMillis();
        index = binSearch(array, 4);
        end = System.currentTimeMillis();
        System.out.println("折半查找结果：" + index + "，耗时：" + (end - start) + " ms");
    }

    /**
     * 顺序查找
     * 
     * @param array
     * @param value
     * @return
     */
    public static int search(int[] array, int value) {
        return search(array, 0, array.length - 1, value);
    }

    /**
     * 顺序查找
     * 
     * @param array
     * @param from
     * @param to
     * @param value
     * @return
     */
    public static int search(int[] array, int from, int to, int value) {
        if (null == array) {
            return -1;
        }
        if (from < 0) {
            from = 0;
        }
        if (to >= array.length) {
            to = array.length - 1;
        }
        for (int i = from; i <= to; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 折半查找（二分查找）
     * 
     * @param array
     * @param value
     * @return
     */
    public static int binSearch(int[] array, int value) {
        if (null == array) {
            return -1;
        }
        return binSearch(array, 0, array.length - 1, value);
    }

    /**
     * 折半查找（二分查找）
     *
     * @param array
     * @param low
     * @param high
     * @param value
     * @return
     */
    public static int binSearch(int[] array, int low, int high, int value) {
        if (null == array) {
            return -1;
        }
        if (0 > low || 0 > high) {
            return -1;
        }
        if (high > array.length - 1) {
            high = array.length - 1;
        }
        if (low > high) {
            return -1;
        }
        int mid = (low + high) / 2;
        if (array[mid] == value) {
            return mid;
        }
        if (array[mid] < value) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
        return binSearch(array, low, high, value);
    }


     // 二叉查找树：与折半查找类似，该方法要求二叉树左小又大，按顺序排列，根节点相当于第一次的中间值，然后对比大小;
     // 要查找的数据小，则选取左侧孩子重新判断，递归实现，但是由于要走完整的路径，所以时间复杂度比折半查找高。
}
