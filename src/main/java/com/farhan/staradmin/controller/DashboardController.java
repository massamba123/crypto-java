package com.farhan.staradmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

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
    @PostMapping("logout")
    public String logout(HttpSession session) {
        // Invalidez la session de l'utilisateur pour le déconnecter.
        session.invalidate();
        return "redirect:/login"; // Redirigez vers la page de connexion après la déconnexion.
    }
}
