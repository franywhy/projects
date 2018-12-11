package com.izhubo.common.util;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.izhubo.web.api.sing.CountDown;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-3-13 下午5:51
 */
public class Test {

    private static AtomicInteger index = new AtomicInteger();

    public static void begin (long millis){
        CountDown gameBegin = new CountDown(){
            public void execute() {

                int i = index.addAndGet(1);
                System.out.println(i+"轮比赛结束 计算比较结算");
                printDate();
                System.out.println("是否需要下轮比赛");
                if(i == 50){
                    System.out.println("比赛结束");
                    return;
                }
                System.out.println("需要则休息2分钟");
                try {
                    Thread.sleep( 2 * 60 * 1000);
                    System.out.println("休息结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("===========================");
                System.out.println(++i+"轮比赛开始");
                printDate();
                begin(15*60*1000);
            }
        };

        gameBegin.CountDownBegin(millis);
    }

    public static void main(String args[]){
    	
    	
     /*   System.out.println("比赛开始");
        printDate();
        index.set(0);
        begin(15*60*1000);*/

     /* try
      {

          Long zerro0605 = new SimpleDateFormat("yyyyMMdd").parse("20140605").getTime() ;
          Long zerro0604 = new SimpleDateFormat("yyyyMMdd").parse("20140604").getTime() ;
          System.out.println("zerro0605---->:"+zerro0605);
          System.out.println("zerro0604---->:"+zerro0604);

          java.text.DateFormat format1 = new java.text.SimpleDateFormat(
                  "yyyy-MM-dd hh:mm:ss");
          String s = format1.format(new Date(1401942016237L));


          System.out.println("s---->:"+s);


      }
      catch(Exception ex)
      {
          ex.printStackTrace();
      }
*/
        //begin(3000);
    	
//    	List list = new ArrayList();
//    	list.add("a");
//    	list.add("b");
//    	list.add("c");
//    	String s = list.toString();
//    	System.out.println(s);
    	
    	/*String json = "{array:['a' , 'b' , 'c']}";
    	Map map = JSONUtil.jsonToMap(json);
    	System.out.println(map);
    	System.out.println(map.get("array").getClass());
    	List list = (List) map.get("array");*/
    	
    	for(int i = 0 ; i < 20 ; i ++){
    		Random rand = new Random();
    		int randInt = rand.nextInt(9)+1;
    		System.out.println(randInt%10);
    	}
    	System.out.println("============================");
    	Random random1 = new Random(100);
        System.out.println(random1.nextInt());
        System.out.println(random1.nextFloat());
        System.out.println(random1.nextBoolean());
        Random random2 = new Random(100);
        System.out.println(random2.nextInt());
        System.out.println(random2.nextFloat());
        System.out.println(random2.nextBoolean());
        
        Random random3 = new Random();
        for (int i = 0; i < 3; i++) {
			System.out.println(random3.nextInt());
		}
    }

    public static void printDate(){
        java.text.DateFormat format1 = new java.text.SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        String s = format1.format(new Date());
        System.out.println(s);
    }
}
