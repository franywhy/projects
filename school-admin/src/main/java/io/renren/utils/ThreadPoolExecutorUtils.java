package io.renren.utils;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/12/29 0029.
 */
public class ThreadPoolExecutorUtils {
    /**
     *
     *
     * 1.在使用有界队列的时候：若有新的任务需要执行，如果线程池实际线程数小于corePoolSize核心线程数的时候，则优先创建线程。
     * 若大于corePoolSize时，则会将多余的线程存放在队列中，
     * 若队列已满，且最请求线程小于maximumPoolSize的情况下，则自定义的线程池会创建新的线程，
     * 若队列已满，且最请求线程大于maximumPoolSize的情况下，则执行拒绝策略，或其他自定义方式。
     */
    /*public  static ThreadPoolExecutor executor =  new ThreadPoolExecutor(// 自定义一个线程池
            Constant.THREAD_POOL_CORE_SIZE, // coreSize
            Constant.THREAD_POOL_MAX_SIZE, // maxSize
            Constant.THREAD_POOL_ALIVE_TIME, // 60s
            TimeUnit.SECONDS, new ArrayBlockingQueue<>(Constant.THREAD_POOL_QUEUE)); // 有界队列，容量是3个
    static {
        executor.allowCoreThreadTimeOut(true);
    }
    public  static ThreadPoolExecutor getThreadPoolExecutor(){


        return executor;
    }

    public  static ThreadPoolExecutor getThreadPoolExecutor(int coreSize,int maxSize,int queue){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(// 自定义一个线程池
                coreSize, // coreSize
                maxSize, // maxSize
                Constant.THREAD_POOL_ALIVE_TIME, // 60s
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queue)); // 有界队列，容量是3个
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    public  static ThreadPoolExecutor getDefaultThreadPoolExecutor(){
        ThreadPoolExecutor executor = new ThreadPoolExecutor(// 自定义一个线程池
                3, // coreSize
                6, // maxSize
                Constant.THREAD_POOL_ALIVE_TIME, // 60s
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        executor.allowCoreThreadTimeOut(true);
        return executor;
    }

    */

   /* public static void main(String[] args) {
        BlockingQueue queue = new LinkedBlockingQueue();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 1, TimeUnit.SECONDS, queue);
        executor.allowCoreThreadTimeOut(true);
        for ( int i = 0; i < 20; i++) {
            executor.execute( new Runnable() {
                public void run() {
                    try {
                        System. out.println( this.hashCode()/1000);
                        for ( int j = 0; j < 10; j++) {
                            System. out.println( this.hashCode() + ":" + j);
                            Thread. sleep(this.hashCode()%2);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System. out.println(String. format("thread %d finished", this.hashCode()));
                }
            });
        }

        try {
            Thread. sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.execute( new Runnable() {
            public void run() {
                try {

                    for ( int j = 0; j < 10; j++) {
                        System. out.println( this.hashCode() + ":[" + j+"]");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }*/
}
