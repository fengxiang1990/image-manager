package com.fxa.image.client.robot;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RobotManager implements CommandLineRunner {

    @Resource
    RegisterRobot registerRobot;
    @Resource
    LoginRobot loginRobot;
    @Resource
    ContentPublishRobot contentPublishRobot;


    @Override
    public void run(String... args) {
        registerRobot.execute();
        loginRobot.execute();
        contentPublishRobot.execute();
    }
}