package com.example.imagemanager.content.controller;


import com.example.imagemanager.content.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/admin/content")
public class ContentController {


    @Autowired
    RedisService redisService;

    @GetMapping("/wait_review")
    public ModelAndView getNeedReviewContents(int page,int pageSize){
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(redisService.getNoneReviewContents(page,pageSize));
        return modelAndView;
    }

    @PostMapping("/review")
    public ModelAndView review(@RequestBody Map<String, long[]> requestBody){
        long[] ids = requestBody.get("ids");
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("result",redisService.review(ids));
        return modelAndView;
    }

    @PostMapping("/reject")
    public ModelAndView reject(@RequestBody Map<String, long[]> requestBody){
        long[] ids = requestBody.get("ids");
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("result",redisService.reject(ids));
        return modelAndView;
    }


}
