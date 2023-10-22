package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.Signature;
import com.farhan.staradmin.service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utils.KeyLoader;
import utils.Utils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

@Controller
@RequestMapping(value = "pages")

public class SignatureController {
    @Autowired
    private SignatureService signatureService;
    @GetMapping(value = "signature")
    public ModelMap mmHachage() {
        List<String> haches = Arrays.asList("SHA-1","SHA-256","SHA-384","SHA-512","SHA-3-256","MD5","Whirlpool","GOST R 34.11-94");
        ModelMap modelMap = new ModelMap();
        Signature signature = new Signature();
        modelMap.addAttribute("algos",signature.getAlgoSignature());
        modelMap.addAttribute("algoOptions",signature.getAlgoSignature());
        modelMap.addAttribute("signature",signature);

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
    @PostMapping("sign")
    public ResponseEntity<String> sign(
                                       @RequestParam(name = "algorithme") String algorithme,
                                       @RequestParam(name = "message") String message,
                                       @RequestParam(name = "option") String option,
                                       @RequestParam(name = "key") MultipartFile key,
                                       @RequestParam(name = "file",required = false) MultipartFile file) throws Exception {
        PrivateKey priv = KeyLoader.deserializePrivateKey(key.getBytes(),algorithme);
        String hash = "";
        if (!message.isEmpty() && file == null){
            hash = signatureService.sign(option,priv,message.getBytes());
        }
        else if (!file.isEmpty() && message.isEmpty()){
            System.out.println("fffffffffffffffffffffffffff");
            hash = signatureService.sign(option,priv,file.getBytes());
        }
        return ResponseEntity.ok().body(hash);
    }
    @PostMapping("sign-verify")
    public ResponseEntity<Boolean> Veriysign(
            @RequestParam(name = "algorithme") String algorithme,
            @RequestParam(name = "message") String message,
            @RequestParam(name = "option") String option,
            @RequestParam(name = "empreinte") String empreinte,
            @RequestParam(name = "key") MultipartFile key,
            @RequestParam(name = "file",required = false) MultipartFile file) throws Exception {
        PublicKey pub = KeyLoader.deserializePublicKey(key.getBytes(),algorithme);
        boolean hash = false;
        if (!message.isEmpty() && file == null){
            hash = signatureService.verifySign(option,pub,message.getBytes(), Utils.hextoBytes(empreinte));
        }
        else if (file != null && message.isEmpty()){
            hash = signatureService.verifySign(option,pub,file.getBytes(),Utils.hextoBytes(empreinte));
        }
        return ResponseEntity.ok().body(hash);
    }
}

