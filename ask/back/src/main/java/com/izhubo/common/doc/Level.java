package com.izhubo.common.doc;

import groovy.transform.CompileStatic;

/**
 * 用户等级
 * date: 13-2-28 下午5:30
 *
 * @author: wubinjie@ak.cc
 */
@CompileStatic
public class Level {

    static final int max_level = 200;

    static final int[] levels = new int[max_level];
    static {
        for(int i=1;i<max_level;i++){
            levels[i] = levels[i-1] + deltaCoin(i);
        }
    }

    static int deltaCoin(int level){
        return 1000* (level * level+level-1);
    }


    public static int starLevel(long coin){
        for (int i=1;i<max_level;i++){
            if(coin < levels[i]){
                return i-1;
            }
        }
        return max_level;
    }


    //http://red.izhubo.com/redmine/projects/xinyuan/wiki/%E6%98%9F%E6%84%BFAPI%E6%96%87%E6%A1%A3#点歌

    public static int orderSongNeed(int level){
        if(level<=3){
            return 500;
        }else if (level<=9){
            return 1000;
        }else {
            return 1500;
        }
    }


//http://192.168.1.181/redmine/projects/xinyuan/wiki/%E6%98%9F%E6%84%BFAPI%E6%96%87%E6%A1%A3#消费星级
    public static int userSpendLevel(int coin){
        if(coin<20000){
            return 0;
        }
        if(coin<50000){
            return 1;
        }
        if(coin <100000){
            return 2;
        }
        if(coin<200000){
            return 3;
        }
        if(coin<500000){
            return 4;
        }
        return 5;

    }


    public static void main(String[] args) {

        System.out.println(
                starLevel(898988)
        );
    }

}
