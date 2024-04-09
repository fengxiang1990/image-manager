package com.fxa.image.rpcreview.controller;


import com.fxa.image.common.message.Content;
import com.fxa.image.rpcreview.model.ReviewContent;
import com.fxa.image.rpcreview.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/autoreview")
public class ReviewController {
    private static final Logger LOGGER = LoggerFactory.getLogger("ReviewController");

    @Autowired
    ReviewService reviewService;

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        LOGGER.info("test invoke");
        return ResponseEntity.ok("success");
    }

    @PostMapping("/content")
    public ResponseEntity<String> contentReview(@RequestBody ReviewContent content){
        LOGGER.info("contentReview invoke");
        reviewService.contentAutoReview(content);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/comment")
    public ResponseEntity<String>  commentReview(@RequestBody ReviewContent content){
        LOGGER.info("commentReview invoke");
        return ResponseEntity.ok("success");
    }
}
