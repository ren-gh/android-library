
package com.rengh.java.study.own.stack;

import java.util.Stack;

public class StackUtils {
    public static void main(String[] args) {
        baseConversion(10);
    }

    /**
     * 十进制转八进制
     * 
     * @param input
     */
    public static void baseConversion(int input) {
        Stack s = new Stack();
        while (input > 0) {
            s.push(input % 8);
            input = input / 8;
        }
        while (!s.isEmpty()) {
            System.out.println(s.pop());
        }
    }
}
