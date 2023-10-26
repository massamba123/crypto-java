package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.HashUtil;
import com.farhan.staradmin.service.AlgorithmeService;
import com.farhan.staradmin.service.HachageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utils.Utils;

import java.util.*;

@Controller
@RequestMapping(value = "pages")
public class HachageController {

    @Autowired
    private HachageService hachageService;
    @Autowired
    private AlgorithmeService algorithmeService;
    private String hash = "";
    @GetMapping(value = "hachage")
    public ModelMap mmHachage() {
        List<Algorithme> haches = algorithmeService.getAllAlgoHachage();
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
    @PostMapping("hash")
    public ResponseEntity<String> hash(  @RequestParam(name = "algorithme") String algorithme,
                                         @RequestParam(name = "message") String message,
                                         @RequestParam(name = "file",required = false) MultipartFile file) throws Exception {
        if (!message.isEmpty() && file == null){
             hash =  hachageService.hash(algorithme, message.getBytes());
        }
        else if (file != null && message.isEmpty()){
            hash =  hachageService.hash(algorithme, file.getBytes());
        }
        return ResponseEntity.ok().body(hash);
    }
    @PostMapping("hash-verify")
    public ResponseEntity<Boolean> hashVerfy(  @RequestParam(name = "algorithme") String algorithme,
                                         @RequestParam(name = "message") String message,
                                         @RequestParam(name = "empreinte") String empreinte,
                                         @RequestParam(name = "file",required = false) MultipartFile file) throws Exception {
        if (!message.isEmpty() && file == null){
            hash =  hachageService.hash(algorithme, message.getBytes());
        }
        else if (file != null && message.isEmpty()){
            hash =  hachageService.hash(algorithme, file.getBytes());
        }
        boolean result = Arrays.equals(hash.getBytes(),empreinte.getBytes());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("hash-file")
    public ResponseEntity<String> hashFile(String algorithme, MultipartFile file) throws Exception{
        String hash  = hachageService.hash(algorithme, file.getBytes());
        return ResponseEntity.ok().body(hash);
    }

}
