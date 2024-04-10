package com.fxa.image.client.robot;

import com.fxa.image.client.model.LoginResult;
import com.fxa.image.client.proto.ContentOuter;
import com.fxa.image.client.service.RedisService;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 内容发布机器人
 */
@Component
public class ContentPublishRobot extends BaseRobot {


    private Logger LOGGER = LoggerFactory.getLogger(ContentPublishRobot.class);

    @Value("${robot.publish.duration}")
    private int duration;


    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RedisService redisService;

    public List<String> readContentFromFile(String filePath) throws IOException {
        List<String> images = new ArrayList<>();
        // 加载resource目录下的文件
        Resource resource = resourceLoader.getResource("classpath:" + filePath);
        InputStream inputStream = resource.getInputStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                images.add(line);
            }
        }

        return images;
    }


    @Override
    public void execute() {
        try {
            List<String> urls = readContentFromFile("/images.txt");
            List<String> titles = readContentFromFile("/title.txt");
            List<String> contents = readContentFromFile("/content.txt");
            System.out.println("read image urls:" + urls.size());
            if (urls.isEmpty() || titles.isEmpty() || contents.isEmpty()) {
                return;
            }
            executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        //从已经登录的用户中随机选择一个用户
                        LoginResult loginResult = redisService.randomGetLoginUser();
                        System.out.println("ContentPublishRobot randomGetLoginUser:" + loginResult);
                        Random random = new Random();
                        String url = urls.get(random.nextInt(urls.size()));
                        String title = titles.get(random.nextInt(titles.size()));
                        String contentStr = contents.get(random.nextInt(contents.size()));
                        ContentOuter.Content content = ContentOuter.Content.getDefaultInstance().toBuilder().setAuthor(loginResult.uid).setTitle(title).setContent(contentStr).setImageUrl(url).build();
                        System.out.println("content:" + content);
                        submit(content);
                    }catch (Exception e1){
                        LOGGER.error("err->"+e1.getMessage());
                        e1.printStackTrace();
                    }
                }
            }, 0, duration, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit(ContentOuter.Content content) {
        try {
            HttpUrl url = new HttpUrl.Builder().scheme("http").host(BASE_URL).port(8082).addPathSegments("content/publish").build();
            byte[] byteArray = content.toByteArray();
            // 创建一个RequestBody对象，将JSON字符串作为请求体
            RequestBody body = RequestBody.create(byteArray, MediaType.parse("application/x-protobuf"));

            Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-protobuf").addHeader("Accept", "application/x-protobuf").method("POST", body).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LOGGER.debug("onFailure:" + e.getMessage());
                    call.cancel();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    LOGGER.debug("onResponse:" + (response.code() == 200 ? "success" : "failed"));
                    try {
                        if (response.code() != 200) {
                            call.cancel();
                        }
                    } finally {
                        response.close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
