package com.fxa.image.client.control;


import com.fxa.image.client.proto.ContentOuter;
import com.fxa.image.client.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/content")
public class ContentController {

    private Logger LOGGER =  LoggerFactory.getLogger(ContentController.class);

    @Autowired
    ContentService contentService;
    @PostMapping(value = "/publish", consumes = "application/x-protobuf")
    public ResponseEntity<String> publish(@RequestBody ContentOuter.Content content) {
        contentService.saveContent(content);
        return ResponseEntity.ok("publish success");
    }

}
