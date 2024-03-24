package com.fxa.image.client.robot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RobotManager implements CommandLineRunner {

    private final RegisterRobot registerRobot;
    private final LoginRobot loginRobot;

    @Autowired
    public RobotManager(RegisterRobot registerRobot,LoginRobot loginRobot) {
        this.registerRobot = registerRobot;
        this.loginRobot = loginRobot;
    }




    @Override
    public void run(String... args) {
        // 在启动时执行机器人的初始方法
        registerRobot.execute();
        // 可以在这里添加其他机器人的执行逻辑
        loginRobot.execute();
    }
}