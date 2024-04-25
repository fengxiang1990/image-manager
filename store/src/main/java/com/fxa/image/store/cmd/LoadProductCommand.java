package com.fxa.image.store.cmd;

import com.fxa.image.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 把库存商品加载到redis中
 * 假设redis1的处理能力更强
 * redis2的能力稍逊
 * 把商品按照一定比例预加载到不同的redis实例中
 */
@Component
public class LoadProductCommand  implements CommandLineRunner {

    private final StoreService storeService;


    public LoadProductCommand(StoreService yourService) {
        this.storeService = yourService;
    }

    @Autowired
    ScheduledExecutorService scheduledExecutorService;


    @Override
    public void run(String... args) throws Exception {
        storeService.loadToCache();

        int arr[] = new int[5000];
        for(int i =0;i< 5000;i++){
            arr[i] = i;
        }
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Arrays.stream(arr)
                        .parallel() // 将串行流转换为并行流
                        .forEach(i->{
                            Executors.newCachedThreadPool().execute(new Runnable() {
                                @Override
                                public void run() {
                                    long orderId = storeService.ordered();
//                                    System.out.println("orderId:"+(orderId == -1 ?"下单失败，商品无库存":"下单成功:"+orderId));
                                }
                            });
                        });

            }
        },15,30, TimeUnit.SECONDS);
    }
}
