
package com.r.library.common.test;

public class Main {
    public static void main(String[] args) {
        System.out.println("main() START...");

        long a = 1024 * 1024 * 1024;
        long b = 1024 * 1024 * 999;
        double c;
        c = (double) Math.round((double) b / (double) a * 10000) / 10000;

        System.out.println(c * 100 + "%");

        System.out.println("main() END.");
    }
}
