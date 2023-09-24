package com.farhan.staradmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(value = "pages")

public class SignatureController {
    @GetMapping(value = "signature")
    public ModelMap mmHachage() {
        List<String> haches = Arrays.asList("SHA-1","SHA-256","SHA-384","SHA-512","SHA-3-256","MD5","Whirlpool","GOST R 34.11-94");
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("algos",haches);
        modelMap.addAttribute("algoOptions",getAlgoHaches());
        return modelMap;
    }
    public Map<String,List<Integer>> getAlgoHaches(){
        //        // Algorithmes de hachage
        Map<String, List<Integer>> algorithmsWithKeyOptions = new HashMap<>();
        algorithmsWithKeyOptions.put("SHA-1", Collections.singletonList(160));
        algorithmsWithKeyOptions.put("SHA-256", Collections.singletonList(256));
        algorithmsWithKeyOptions.put("SHA-384", Collections.singletonList(384));
        algorithmsWithKeyOptions.put("SHA-512", Collections.singletonList(512));
        algorithmsWithKeyOptions.put("SHA-3-256", Collections.singletonList(256));
        algorithmsWithKeyOptions.put("MD5", Collections.singletonList(128));
        algorithmsWithKeyOptions.put("Whirlpool", Collections.singletonList(512));
        algorithmsWithKeyOptions.put("GOST R 34.11-94", Collections.singletonList(256));
        return algorithmsWithKeyOptions;

    }

}
