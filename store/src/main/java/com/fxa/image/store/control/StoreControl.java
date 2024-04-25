package com.fxa.image.store.control;

import com.fxa.image.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class StoreControl {

    @Autowired
    StoreService storeService;
    @GetMapping("/order")
    public long order(){
        return storeService.ordered();
    }
}
