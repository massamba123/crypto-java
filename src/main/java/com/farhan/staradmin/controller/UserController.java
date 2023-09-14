package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "pages")
public class UserController {

    @GetMapping("user-form")
    public ModelMap mmUserForm() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("user", new User()); // 'user' is the attribute name
        return modelMap;
    }
}
