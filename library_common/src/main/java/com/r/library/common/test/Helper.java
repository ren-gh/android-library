
package com.r.library.common.test;

import com.r.library.common.util.ThreadUtils;

public class Helper {
    public synchronized void hello() {
        System.out.println("hello() START Thread: " + Thread.currentThread().getName());

        ThreadUtils.sleep(3000);

        System.out.println("Hello! Thread: " + Thread.currentThread().getName());

        ThreadUtils.sleep(3000);

        System.out.println("hello() END Thread: " + Thread.currentThread().getName());
    }
}
