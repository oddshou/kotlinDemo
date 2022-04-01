package com.odds.kotlindemo;


import java.util.concurrent.FutureTask;
import java.util.regex.Pattern;

/**
 * ClassName:ThreadTest
 * Description: 线程，synchronized，sleep，join，
 * 2022/3/24 5:19 下午
 * wiki:
 */
public class TestThread {

    /**
     * 测试 synchronized， wait，notify
     */
    public static void run() {
        new Thread(new Thread1()).start();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Thread2()).start();

    }

    public void test(){
//        String test = "".su;
//        Pattern.compile("[A-Z]").matcher("").find();



    }

    public static void testFutureTask(){

        FutureTask<String> task = new FutureTask<>(new Runnable(){

            @Override
            public void run() {

            }
        }, "");

    }

    private static class Thread1 implements Runnable{
        @Override
        public void run(){
            synchronized (TestThread.class) {
                System.out.println("enter thread1...");
                System.out.println("thread1 is waiting...");
                try {
                    //调用wait()方法，线程会放弃对象锁，进入等待直到 notify，notifyAll 唤醒
                    TestThread.class.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("thread1 is going on ....");
                System.out.println("thread1 is over!!!");
            }
        }
    }

    private static class Thread2 implements Runnable{
        @Override
        public void run(){
            synchronized (TestThread.class) {
                System.out.println("enter thread2....");
                System.out.println("thread2 is sleep....");
                //只有针对此对象调用notify()方法后 唤醒正在此对象的监视器上等待的单个线程。如果有任何线程正在等待该对象，则选择其中一个被唤醒
                //唤醒并不代表立即执行，只是代表可以竞争锁的权限。唤醒并不释放锁
                TestThread.class.notify();
                //==================
                //区别
                //如果我们把代码：TestD.class.notify();给注释掉，即TestD.class调用了wait()方法，但是没有调用notify()
                //方法，则线程永远处于挂起状态。
                try {
                    //sleep()方法导致了程序暂停执行指定的时间，让出cpu该其他线程，
                    //但是他的监控状态依然保持者，当指定的时间到了又会自动恢复运行状态。
                    //在调用sleep()方法的过程中，线程不会释放对象锁。
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 is going on....");
                System.out.println("thread2 is over!!!");
            }
        }
    }


    //sleep 是 Thread 的静态方法。wait 是 Object 方法
    // wait 释放锁，需要 notify 再次获得锁
    public static void joinTest(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start sleep");
                try {
                    //sleep 是 Thread 的静态方法
                    Thread.sleep(2000);
                    //yield 让出cpu，但是cpu还是有可能继续分配给它
//                    Thread.yield();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread sleep over");
            }
        });
        thread.start();
        System.out.println("thread start!!!");
        try {
            //join 是thread 对象方法, 等待这个线程结束
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("thread is over!!!");

    }
}
