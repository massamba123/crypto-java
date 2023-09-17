package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.AlgorithmeService;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.SecretKey;
import java.util.List;

@Controller
@RequestMapping(value = "pages")
public class AlgorithmeController {
    @Autowired
    private AlgorithmeService algorithmeService;
    @Autowired
    private KeyService keyService;
    @GetMapping("algorithme")
    public ModelMap mmAlgorithm() {
        List<Algorithme> algorithmes = algorithmeService.getAllAlgorithmes();
        List<Key> keys = keyService.getAllKeys();
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("algorithme", new Algorithme()); // 'user' is the attribute name
        Key key = new Key();
        modelMap.addAttribute("key",key);
        modelMap.addAttribute("keys",keys);
        modelMap.addAttribute("algorithmes", algorithmes); // 'user' is the attribute name
        return modelMap;
    }
    @PostMapping("/save-algorithm")
    public String saveAlgorithm(@ModelAttribute("algorithme") Algorithme algorithme, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors if needed
            return "algorithme"; // Redirect back to the form
        }

        // Save the algorithm using your service
        algorithmeService.saveAlgorithme(algorithme);

        // Redirect to a success page or back to the list of algorithms
        return "redirect:/pages/algorithme"; // Redirect to your algorithm list page
    }
    @PostMapping("/generate-key")
    public ResponseEntity<Resource> generateKey(@ModelAttribute("key") Key key, BindingResult bindingResult, Model model) throws Exception {

        // Save the algorithm using your service
        SecretKey secretKey = keyService.saveKey(key);
        // Vérifier si le fichier existe et est lisible
            // Déterminer le type MIME du fichier à partir de son extension (vous pouvez ajuster cette logique)
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            // Renvoyer le fichier en tant que réponse HTTP
            String filaName = "secret-key-"+key.getName() + "-"+key.getSize()+".key";
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
        // Redirect to a success page or back to the list of algorithms
    }
}
