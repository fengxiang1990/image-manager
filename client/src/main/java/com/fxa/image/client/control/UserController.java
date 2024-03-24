package com.fxa.image.client.control;

import com.fxa.image.client.model.LoginData;
import com.fxa.image.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/login")
    public ModelAndView login(@RequestBody LoginData loginData) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        try {
            modelAndView.addObject(userService.login(username, password));
        } catch (Exception ignored) {
        }
        return modelAndView;
    }

}
