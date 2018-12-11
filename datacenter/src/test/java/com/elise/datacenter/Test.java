package com.elise.datacenter;

import com.elise.datacenter.file.FileIdFactory;

/**
 * Created by Glenn on  2017/9/18 0018 23:39.
 */


public class Test {
    public static void main(String args[]){
        System.out.println(FileIdFactory.Generate("gk541-20tv4JDeFVegfsSNoAAAAAAAAAAQ").getMySQLFileId());
    }
}
