package com.farhan.staradmin.controller;

import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.AlgorithmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import utils.Utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "pages")
public class MacController {
    @Autowired
    private AlgorithmeService algorithmeService;

    public List<String> getAllAlgorithms(){
        return Arrays.asList(
                "HmacMD5",
                "HmacSHA1",
                "HmacSHA224",
                "HmacSHA256",
                "HmacSHA384",
                "HmacSHA512"
        );
    }
    @GetMapping("mac")
    public ModelMap mmMac() {
        ModelMap modelMap = new ModelMap();
        List<String> algos = Arrays.asList(
                "HmacMD5",
                "HmacSHA1",
                "HmacSHA224",
                "HmacSHA256",
                "HmacSHA384",
                "HmacSHA512"
        );
        modelMap.addAttribute("algorithmes", algos); // 'user' is the attribute name
        return modelMap;
    }
    @PostMapping("/generate")
    public String generateMac(@RequestParam("algorithm") String algorithm,
                              @RequestParam("password") String password,
                              @RequestParam(value = "secretKey",required = false) MultipartFile secretKey,
                              @RequestParam("message") String message,
                              @RequestParam(value = "import",required = false) MultipartFile file) {
        try {
            SecretKey sk = null;
            byte[] messageBytes = new byte[0];

            if (!password.isEmpty() && secretKey.isEmpty()) {
                sk = new SecretKeySpec(password.getBytes(), algorithm);
            } else if (!secretKey.isEmpty() && password.isEmpty()) {
                sk = SecretKeyImpl.getKeyFromBytes(secretKey.getBytes(), algorithm);
            }

            if (!message.isEmpty() && file.isEmpty()) {
                messageBytes = message.getBytes();
            } else if (!file.isEmpty() && message.isEmpty()) {
                messageBytes = file.getBytes();
            }

            Mac mac = Mac.getInstance(algorithm, "BC");
            mac.init(sk);
            mac.update(messageBytes);

            byte[] macBytes = mac.doFinal();

            // Convertissez le MAC en une représentation hexadécimale pour l'affichage
            String macHex = Utils.toHex(macBytes);

            // Vous pouvez également stocker le MAC dans un modèle ou le retourner sous une autre forme si nécessaire.

            return macHex;
        } catch (Exception e) {
            // Gérez les exceptions ici, par exemple, en renvoyant un message d'erreur à l'utilisateur ou en journalisant l'exception.
            e.printStackTrace();
            return "Erreur lors de la génération du MAC : " + e.getMessage();
        }
    }

}
