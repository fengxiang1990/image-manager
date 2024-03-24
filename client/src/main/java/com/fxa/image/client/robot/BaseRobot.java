package com.fxa.image.client.robot;

import jakarta.annotation.Resource;
import okhttp3.OkHttpClient;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 执行任务的机器人
 */
public abstract class BaseRobot {

    protected String BASE_URL = "localhost";
    @Resource
    protected OkHttpClient okHttpClient;
    protected ScheduledExecutorService executor;
    public BaseRobot(){
        executor = Executors.newScheduledThreadPool(4);
    }

    public abstract void execute();
}
