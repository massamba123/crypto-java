package com.farhan.staradmin.controller;

import com.farhan.staradmin.crypto.asymetrique.AsymetriqueKeyGen;
import com.farhan.staradmin.entity.Algorithme;
import com.farhan.staradmin.entity.Key;
import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.AlgorithmeService;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDate;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(value = "pages")
public class KeyController {
    @Autowired
    private KeyService keyService;
    @Autowired
    private AlgorithmeService algorithmeService;
    @GetMapping("fichier")
    public String mmAlgorithm(ModelMap modelMap, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Algorithme> algorithmes = algorithmeService.getAllAlgorithmes();
        List<Key> keys = keyService.getKeyByUser(user.getId());
        modelMap.addAttribute("algorithme", new Algorithme()); // 'user' is the attribute name
        Key key = new Key();
        modelMap.addAttribute("key",key);
        modelMap.addAttribute("keys",keys);
        modelMap.addAttribute("algorithmes", algorithmes); // 'user' is the attribute name
        return "pages/fichier";
    }
    @PostMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path,
                                             @RequestParam("type") String type,
                                             @RequestParam("algo") String algo
    ) throws Exception {
        if (type.equals("symetrique")) {
            // Télécharger la ressource spécifiée par le path (par exemple, un fichier)
            FileSystemResource resource = new FileSystemResource(path);
            String file =  LocalDate.now().toString() +"_secret_key.sk";
            // Préparez les en-têtes de réponse
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        }
        else if (type.equals("asymetrique") || type.equals("signature")) {
            PublicKey pubKey = AsymetriqueKeyGen.getPub(path+".pub",algo);
            PrivateKey privKey = AsymetriqueKeyGen.getPrivate(path+".priv",algo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zipOut = new ZipOutputStream(baos)) {
                addToZip(zipOut,   "cle.pub", pubKey.getEncoded());
                addToZip(zipOut,   "cle.priv", privKey.getEncoded());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            String fileName =  LocalDate.now().toString() +"_cle_pub_priv.zip";
            ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());
            return buildFileResponse(fileName, resource);
        }
        return null;
    }
    private void addToZip(ZipOutputStream zipOut, String fileName, byte[] content) throws IOException {
        zipOut.putNextEntry(new ZipEntry(fileName));
        zipOut.write(content, 0, content.length);
        zipOut.closeEntry();
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
    @PostMapping("delete")
    public String delete(@RequestParam("id") int id){
        keyService.deleteKey(id);
        return "redirect:/pages/fichier";
    }
}
