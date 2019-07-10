
package com.r.library.common.test;

import com.r.library.common.util.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusTest {
    public static void main(String[] args) {
        new Thread(new TestRunnable()).start();

        EventBusTest eventBusTest = new EventBusTest();
        EventBus.getDefault().register(eventBusTest);

        ThreadUtils.sleep(5000);

        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setMsg("main-EventBus 测试");
        EventBus.getDefault().postSticky(messageEvent);
        ThreadUtils.sleep(10000);

        EventBus.getDefault().unregister(eventBusTest);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void subscriber(MessageEvent messageEvent) {
        System.out.println("Thread: " + Thread.currentThread().getName() + " Msg: " + messageEvent.getMsg());
    }

    public static class TestRunnable implements Runnable {
        @Override
        public void run() {


            EventBus.getDefault().register(this);

            ThreadUtils.sleep(10000);

            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setMsg("TestRunnable-EventBus 测试");
            EventBus.getDefault().postSticky(messageEvent);

            EventBus.getDefault().unregister(this);
        }

        @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
        public void subscriber(MessageEvent messageEvent) {
            System.out.println("Thread: " + Thread.currentThread().getName() + " Msg: " + messageEvent.getMsg());
        }
    }

    public static class MessageEvent {
        private String msg;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "MessageEvent{" +
                    "msg='" + msg + '\'' +
                    '}';
        }
    }
}
