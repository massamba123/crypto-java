package com.farhan.staradmin.controller;

import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.Chiffrement;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.ChiffrementService;
import com.farhan.staradmin.service.ChiffrementUtil;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "pages")
public class ChiffrementController {
    @Autowired
    private ChiffrementUtil chiffrementUtil;
    @Autowired
    private ChiffrementService chiffrementService;
    @Autowired
    private KeyService keyService;
    @GetMapping("symetrique")
    public ModelMap mmSymetrique() {
        ModelMap modelMap = new ModelMap();
        Chiffrement chiffrement = new Chiffrement();
        modelMap.addAttribute("chiffrement", chiffrement); // 'user' is the attribute name
        return modelMap;
    }
    @PostMapping("getkeys")
    @ResponseBody // Cette annotation indique que le résultat doit être converti en JSON
    public List<Key> genererCle(@RequestBody Map<String, String> params) {
        // Traitez les données reçues depuis la requête AJAX
        String type = params.get("type");
        String algorithme = params.get("algorithme");
        int size = Integer.parseInt(params.get("size"));
        List<Key> keys = chiffrementUtil.getKeysByAttribut(type, algorithme, size);
        // Exécutez la logique de génération de clé ou tout autre traitement nécessaire

        // Retournez la liste de clés en réponse à la requête AJAX (elle sera automatiquement convertie en JSON)
        return keys;
    }
    @PostMapping("/gen-key")
    @ResponseBody // Cette annotation indique que le résultat doit être converti en JSON
    public ResponseEntity<Resource> generateKey(@RequestBody Map<String, String> params) throws Exception {
        String type = params.get("type");
        String algorithme = params.get("algorithme");
        int size = Integer.parseInt(params.get("size"));
        SecretKey secretKey = SecretKeyImpl.genKey(algorithme,size);
        Key key = new Key();
        key.setType(type);
        key.setSize(size);
        key.setName(algorithme);
        // Save the algorithm using your service
        keyService.saveKey(key);
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        // Renvoyer le fichier en tant que réponse HTTP
        String filaName = "secret-key-"+key.getName() + "-"+key.getSize()+"-"+ new Date().getHours() +".key";
        ByteArrayResource resource = new ByteArrayResource(secretKey.getEncoded());

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+filaName);

        // Build the ResponseEntity with the ByteArrayResource and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(secretKey.getEncoded().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    @GetMapping("asymetrique")
    public ModelMap mmAsymetrique() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("user", new User()); // 'user' is the attribute name
        return modelMap;
    }
    @PostMapping("/save-symetrique")
    public ResponseEntity<Resource> generateKey(@ModelAttribute("chiffrement") Chiffrement chiffrement,
                                                @RequestParam("file") MultipartFile file,
                                                @RequestParam("import") MultipartFile key,
                                                BindingResult bindingResult,
                                                Model model) throws Exception {

        byte[] out = new byte[0];
        if (!file.isEmpty() && !key.isEmpty()) {
            // Process the uploaded file here
            String fileName = file.getOriginalFilename();
            byte[] fileBytes = file.getBytes();
            SecretKey sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
            out  = chiffrementService.createChiffrement(chiffrement,fileBytes,fileName,sk);
            // Save the file or perform any necessary operations
            // For example, you can save the file to a specific location on your server
            // Here, we're just printing some information about the file
            System.out.println("Uploaded File Name: " + fileName);
            System.out.println("File Size: " + file.getSize() + " bytes");
        }

        ByteArrayResource resource = new ByteArrayResource(out);

        // Set the headers for the response
        HttpHeaders headers = new HttpHeaders();
        String fileout = "cipher-"+file.getOriginalFilename();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileout);

        // Build the ResponseEntity with the ByteArrayResource and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(out.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
