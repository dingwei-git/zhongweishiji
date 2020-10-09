package com.jovision.jaws.common.util;

import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class ThreadPoolExecutor {
    ExecutorService singleThreadExecutor = null;

    public ThreadPoolExecutor() {
        if(singleThreadExecutor==null){
            singleThreadExecutor =  Executors.newCachedThreadPool();
        }
    }

}
