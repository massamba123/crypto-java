package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.AlgorithmeService;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value = "pages")
public class AlgorithmeController {
    @Autowired
    private AlgorithmeService algorithmeService;
    @Autowired
    private KeyService keyService;
    @GetMapping("algorithme")
    public String mmAlgorithm(ModelMap modelMap) {
        List<Algorithme> algorithmes = algorithmeService.getAllAlgoGenKey();
        modelMap.addAttribute("algorithme", new Algorithme()); // 'user' is the attribute name
        Key key = new Key();
        modelMap.addAttribute("key",key);
        modelMap.addAttribute("algorithmes", algorithmes); // 'user' is the attribute name
        return "pages/algorithme";
    }
    @PostMapping("/save-algorithm")
    public String saveAlgorithm(@ModelAttribute("algorithme") Algorithme algorithme,
                                BindingResult bindingResult,
                                Model model,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors if needed
            return "algorithme"; // Redirect back to the form
        }
        User user = (User) session.getAttribute("user");
        algorithme.setUser(user);
        // Save the algorithm using your service
        algorithmeService.saveAlgorithme(algorithme);

        // Redirect to a success page or back to the list of algorithms
        return "redirect:/pages/algorithme"; // Redirect to your algorithm list page
    }
    private void addToZip(ZipOutputStream zipOut, String fileName, byte[] content) throws IOException {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.write(content, 0, content.length);
        zipOut.closeEntry();
    }
    @PostMapping("/generate-key")
    public ResponseEntity<Resource> generateKey(@ModelAttribute("key") Key key, BindingResult bindingResult,
                                                Model model,
                                                HttpSession session) throws Exception {
        String type = key.getName().split(" ")[0];
        String name = key.getName().split(" ")[1];
        key.setName(name);
        key.setType(type);
        User user = (User) session.getAttribute("user");
        key.setUser(user);
        key.setLocalDate(LocalDate.now());
        if (type.equals("symetrique") || type.equals("mac")) {
            return generateSymmetricKeyResponse(key);
        } else if (key.getType().equals("asymetrique") || key.getType().equals("signature")) {
            return generateAsymmetricKeyResponse(key);
        }

        // Gérer d'autres types si nécessaire
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private ResponseEntity<Resource> generateSymmetricKeyResponse(Key key) throws Exception {
        SecretKey secretKey = keyService.saveKey(key);

        if (secretKey == null) {
            return redirectToAlgorithmPage();
        } else {
            String fileName =  LocalDate.now().toString() +"_cle_secret.key";
            ByteArrayResource resource = new ByteArrayResource(secretKey.getEncoded());
            return buildFileResponse(fileName, resource);
        }
    }

    private ResponseEntity<Resource> generateAsymmetricKeyResponse(Key key) throws Exception {
        KeyPair kp = keyService.saveAsymetrique(key);

        if (kp == null) {
            return redirectToAlgorithmPage();
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
                addToZip(zipOut, "cle.pub", kp.getPublic().getEncoded());
                addToZip(zipOut,  "cle.priv", kp.getPrivate().getEncoded());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            String fileName = LocalDate.now().toString() +"_"+ key.getName()+"_key_pub_priv.zip";
            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
            return buildFileResponse(fileName, resource);
        }
    }

    private ResponseEntity<Resource> redirectToAlgorithmPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, "/pages/algorithme")
                .build();
    }

    private ResponseEntity<Resource> buildFileResponse(String fileName, ByteArrayResource resource) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(resource.contentLength());
        headers.setContentDispositionFormData("attachment", fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
