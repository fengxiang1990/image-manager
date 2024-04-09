package com.fxa.image.messagecenter;


import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/msg")
public class TestController {


    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/test")
    public void test(){
      String res =  restTemplate.getForObject("http://rpcreview/autoreview/test", String.class);
      System.out.println("res:"+res);
    }
}
