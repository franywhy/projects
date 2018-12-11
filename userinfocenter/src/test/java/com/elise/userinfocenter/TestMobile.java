package com.elise.userinfocenter;


import com.hq.common.util.MobileNoRegular;

/**
 * Created by Administrator on 2017/5/12 0012.
 */
public class TestMobile {
    public static void main(String args[]){

        MobileNoRegular mobileNoRegular = new MobileNoRegular("13810195917");
        System.out.println(mobileNoRegular.isValidMobileNo());
    }
}
