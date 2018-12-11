package com.hq.answerapi;


import com.hq.common.util.MobileNoRegular;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class TestMobile implements Runnable {
    //共享资源(临界资源)
    static int i=0;

    /**
     * synchronized 修饰实例方法
     */
    public synchronized void increase() {
        out();
        try {
            System.out.println("***********sleep********************");
            Thread.sleep(10000);
        }catch (InterruptedException e) {
        }
        i++;
    }

    private void out() {
        System.out.println("66666");
    }

    @Override
    public void run() {
        System.out.println("88888");
        increase();
        System.out.println("999999");
        /*for(int j=0;j<1000000;j++){
            increase();
        }*/
    }

    public static void main(String args[]) throws InterruptedException{

        MobileNoRegular mobileNoRegular = new MobileNoRegular("13810195917");
        System.out.println(mobileNoRegular.isValidMobileNo());

        TestMobile instance=new TestMobile();
        Thread t1=new Thread(instance);
        Thread t2=new Thread(instance);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
