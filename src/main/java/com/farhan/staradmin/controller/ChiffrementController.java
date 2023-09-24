package com.farhan.staradmin.controller;

import com.farhan.staradmin.crypto.symetrique.SecretKeyImpl;
import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Chiffrement;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.AlgorithmeService;
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
import utils.KeyLoader;
import utils.Utils;

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
    private AlgorithmeService algorithmeService;
    @Autowired
    private ChiffrementService chiffrementService;
    @Autowired
    private KeyService keyService;
    @GetMapping("symetrique")
    public ModelMap mmSymetrique() {
        ModelMap modelMap = new ModelMap();
        List<Algorithme> algorithmes = algorithmeService.getAllAlgorithmes();
        Chiffrement chiffrement = new Chiffrement();
        chiffrement.setType("symetrique");
        chiffrement.setMode("Chiffrement");
        Chiffrement dechiffrement = new Chiffrement();
        dechiffrement.setType("asymetrique");
        dechiffrement.setMode("Dechiffrement");
        modelMap.addAttribute("chiffrement", chiffrement); // 'user' is the attribute name
        modelMap.addAttribute("dechiffrement", dechiffrement); // 'user' is the attribute name
        modelMap.addAttribute("algorithmes", algorithmes); // 'user' is the attribute name
        return modelMap;
    }
    @PostMapping("getkeys")
    @ResponseBody // Cette annotation indique que le résultat doit être converti en JSON
    public List<Key> genererCle(@RequestBody Map<String, String> params) {
        // Traitez les données reçues depuis la requête AJAX
        String algorithme = params.get("algorithme");
        int size = Integer.parseInt(params.get("size"));
        String type = algorithmeService.getTypeAlgo(algorithme).getType();
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
    @PostMapping("/encrypt")
    public ResponseEntity<Resource> generateKey(@ModelAttribute("chiffrement") Chiffrement chiffrement,
                                                @RequestParam("file") MultipartFile file,
                                                @RequestParam("import") MultipartFile key,
                                                BindingResult bindingResult,
                                                Model model) throws Exception {

        byte[] out = new byte[0];
        if (!key.isEmpty() || chiffrement.getKeyPath() != null) {
            chiffrement.setMode("Chiffrement");
            // Process the uploaded file here
            java.security.Key sk = null;
            Algorithme algorithm =  algorithmeService.getTypeAlgo(chiffrement.getAlgorithme());
            String type =algorithm.getType();
            String provider = algorithm.getProvider();
            chiffrement.setType(type);
            if (type.equals("symetrique")){
                sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
            }
            else if (type.equals("asymetrique")){
                sk = KeyLoader.deserializePublicKey(key.getBytes(), chiffrement.getAlgorithme());
            }

//            if (chiffrement.getMode().equals("Chiffrement")){
//                if (type.equals("symetrique")){
//                    sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
//                }
//                else if (type.equals("asymetrique")){
//                    sk = KeyLoader.deserializePublicKey(key.getBytes(), chiffrement.getAlgorithme());
//                }
//            }
//            else if (chiffrement.getMode().equals("Dechiffrement")){
//                if (type.equals("symetrique")){
//                    sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
//                }
//                else if (type.equals("asymetrique")){
//                    sk = KeyLoader.deserializePrivateKey(key.getBytes(), chiffrement.getAlgorithme());
//                }
//            }
            if (chiffrement.getKeyPath() != null) {
                sk = SecretKeyImpl.getKey(chiffrement.getKeyPath());
            }
            byte[] fileBytes = new byte[0];
            System.out.println(chiffrement.getMessage().length());
            if (!file.isEmpty() && chiffrement.getMessage().isEmpty()){
                String fileName = file.getOriginalFilename();
                fileBytes = file.getBytes();
                out  = chiffrementService.createChiffrement(chiffrement,fileBytes,fileName,sk);
            } else if (chiffrement.getMessage() != null && file.isEmpty()) {
                fileBytes = chiffrement.getMessage().getBytes();
                String filenames  = "cipher";
                out  = chiffrementService.createChiffrement(chiffrement,fileBytes,filenames,sk);
            }
            // Save the file or perform any necessary operations
            // For example, you can save the file to a specific location on your server
            // Here, we're just prin
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
    @PostMapping("/decrypt")
    public ResponseEntity<Resource> generateKeyde(@ModelAttribute("dechiffrement") Chiffrement chiffrement,
                                                @RequestParam("file1") MultipartFile file,
                                                @RequestParam("import1") MultipartFile key,
                                                BindingResult bindingResult,
                                                Model model) throws Exception {

        byte[] out = new byte[0];
        System.out.println("cle"+Utils.toHex(key.getBytes()));
        if (!key.isEmpty()) {
            // Process the uploaded file here
            java.security.Key sk = null;
            chiffrement.setMode("Dechiffrement");
            Algorithme algorithm =  algorithmeService.getTypeAlgo(chiffrement.getAlgorithme());
            String type =algorithm.getType();
            String provider = algorithm.getProvider();
            chiffrement.setType(type);
            System.out.println("cle type"+type);
            if (type.equals("symetrique")){
                sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
                System.out.println("cle"+Utils.toHex(sk.getEncoded()));
            }
            else if (type.equals("asymetrique")){
                    sk = KeyLoader.deserializePrivateKey(key.getBytes(), chiffrement.getAlgorithme());
            }

//            if (chiffrement.getMode().equals("Chiffrement")){
//                if (type.equals("symetrique")){
//                    sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
//                }
//                else if (type.equals("asymetrique")){
//                    sk = KeyLoader.deserializePublicKey(key.getBytes(), chiffrement.getAlgorithme());
//                }
//            }
//            else if (chiffrement.getMode().equals("Dechiffrement")){
//                if (type.equals("symetrique")){
//                    sk = SecretKeyImpl.getKeyFromBytes(key.getBytes(), chiffrement.getAlgorithme());
//                }
//                else if (type.equals("asymetrique")){
//                    sk = KeyLoader.deserializePrivateKey(key.getBytes(), chiffrement.getAlgorithme());
//                }
//            }
            if (chiffrement.getKeyPath() != null) {
                sk = SecretKeyImpl.getKey(chiffrement.getKeyPath());
            }
            byte[] fileBytes = new byte[0];
            System.out.println(file);
            if (!file.isEmpty() && chiffrement.getMessage().isEmpty()){
                String fileName = file.getOriginalFilename();
                fileBytes = file.getBytes();
                out  = chiffrementService.createChiffrement(chiffrement,fileBytes,fileName,sk);
            } else if (chiffrement.getMessage() != null && file.isEmpty()) {
                fileBytes = chiffrement.getMessage().getBytes();
                String filenames  = "cipher";
                out  = chiffrementService.createChiffrement(chiffrement,fileBytes,filenames,sk);
            }
            // Save the file or perform any necessary operations
            // For example, you can save the file to a specific location on your server
            // Here, we're just prin
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
