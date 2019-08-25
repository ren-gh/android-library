
package com.rengh.java.study.algorithm;

import java.util.Scanner;

/**
 * 二分查找 有序数组
 */
public class OrderedArray {
    private int[] mArray;
    private int mMax;
    private int mElems;

    public OrderedArray(int max) {
        mMax = max;
        mArray = new int[mMax];
        mElems = 0;
    }

    public OrderedArray(int[] arr) {
        mMax = arr.length;
        mArray = new int[mMax];
        mElems = 0;
        for (int i = 0; i < arr.length; i++) {
            insert(arr[i]);
        }
    }

    public int size() {
        return mElems;
    }

    public int max() {
        return mMax;
    }

    /**
     * 二分法查找
     * <p>
     * 数组越大，二分查找的优势就越明显
     * <p>
     * 公式：2的n次幂>=arr.size, n为查找所需次数 ,2的n次幂为n次查找可覆盖的范围 即 范围是次数的指数，次数是范围的对数
     *
     * @param searchValue 要查找的值
     * @return 查找结果，该值对应的角标，-1表示未找到
     */
    public int find(long searchValue) {
        int lowerIndex = 0;
        int upperIndex = mElems - 1;
        int currIndex;
        while (true) {
            currIndex = (lowerIndex + upperIndex) / 2;
            System.out.println("mArray[" + currIndex + "]=" + mArray[currIndex]);
            if (lowerIndex > upperIndex) {
                return -1;
            } else if (mArray[currIndex] == searchValue) {
                return currIndex;
            } else if (mArray[currIndex] < searchValue) {
                lowerIndex = currIndex + 1;
            } else {
                upperIndex = currIndex - 1;
            }
        }
    }

    /**
     * 使用递归实现二分法查找 代码更简洁,但效率稍差
     */
    public int find(long searchValue, int fromIndex, int toIndex) {
        System.out.println("searchValue:" + searchValue + "_fromIndex:" + fromIndex + "_toIndex:" + toIndex);
        int currentIndex = (fromIndex + toIndex) / 2;
        if (mArray[currentIndex] == searchValue)
            return currentIndex;
        else if (fromIndex > toIndex)
            return -1;
        else if (mArray[currentIndex] < searchValue)
            return find(searchValue, currentIndex + 1, toIndex);
        else
            return find(searchValue, fromIndex, currentIndex - 1);
    }

    /**
     * 插入数据
     *
     * @param value
     */
    public void insert(int value) {
        int i;
        for (i = 0; i < mElems; i++) {
            if (mArray[i] > value)
                break;
        }
        for (int j = mElems; j > i; j--) {
            mArray[j] = mArray[j - 1];
        }
        mArray[i] = value;
        mElems++;
    }

    /**
     * 删除
     *
     * @param value
     * @return
     */
    public boolean delete(long value) {
        int i = find(value);
        if (i == -1) {
            return false;
        } else {
            for (int j = i; j < mElems; j++) {
                mArray[j] = mArray[j + 1];
            }
            mElems--;
            return true;
        }
    }

    /**
     * 打印数组
     */
    public void display() {
        System.out.print("OrderedArray: ");
        for (int i = 0; i < mElems; i++) {
            System.out.print("[" + i + "]-->" + mArray[i] + "  ");
        }
        System.out.println();

    }

    public static void main(String[] args) {
        OrderedArray orderedArray = new OrderedArray(10);
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < orderedArray.max(); i++) {
            if (scanner.hasNext())
                orderedArray.insert(scanner.nextInt());
        }

        orderedArray.display();
        int index = orderedArray.find(6);
        System.out.println(index);
    }

}
