package com.farhan.staradmin.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;

@Controller
@RequestMapping(value = "pages")
public class DashboardController {

    @GetMapping(value = "dashboard")
    public ModelMap mmDashboard() {
        return new ModelMap();
    }

    @GetMapping(value = "documentation")
    public ModelMap mmDocumentation() {
        return new ModelMap();
    }
    @GetMapping(value = "apropos")
    public ModelMap mmAbout() {
        return new ModelMap();
    }

    @PostMapping("logout")
    public String logout(HttpSession session) {
        // Invalidez la session de l'utilisateur pour le déconnecter.
        session.invalidate();
        return "redirect:/login"; // Redirigez vers la page de connexion après la déconnexion.
    }
    @GetMapping("/download-doc") // Utilisation de @GetMapping au lieu de @PostMapping car il s'agit d'un téléchargement
    public ResponseEntity<Resource> downloadDoc() throws Exception {
        // Spécifiez le chemin complet du fichier à télécharger
        String filePath = "src/main/resources/documentation/rapport.pdf";
        FileSystemResource resource = new FileSystemResource(filePath);

        if (resource.exists()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documentation.pdf"); // Nom du fichier téléchargé
            headers.setContentType(MediaType.APPLICATION_PDF); // Définir le type de contenu du fichier (PDF dans ce cas)

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            // Gérer le cas où le fichier n'existe pas
            return ResponseEntity.notFound().build();
        }
    }
}
