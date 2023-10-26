package com.farhan.staradmin.controller;

import com.farhan.staradmin.entity.User;
import com.farhan.staradmin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping( "/login")
    public String mmLogin() {
        return "login";
    }
    @GetMapping( "/error")
    public String mmError() {
        return "error/404";
    }
    @GetMapping( "/inscription")
    public String mmacceuil() {
        return "inscription";
    }
    @PostMapping("save-user")
    public RedirectView creationCompte(
            @RequestParam("first_name") String first_name,
            @RequestParam("last_name") String last_name,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model
    ) {
        userService.createUser(new User(first_name, last_name, username, password));
        return new RedirectView("login"); // Remplacez "viewName" par le nom de votre vue JSP, Thymeleaf ou autre.
    }

    @PostMapping("connexion")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session
    ) {
        RedirectView redirectView = new RedirectView();
        User user = userService.login(username, password);
        ModelAndView modelAndView = new ModelAndView();
        if (user != null) {
            // Utilisateur connecté avec succès, redirigez vers la page de tableau de bord.
            // Stockez l'utilisateur dans la session
            session.setAttribute("user", user);
            String welcome = "Bienvenue "+user.getFirst_name() + " "+user.getLast_name();
            session.setAttribute("welcome",welcome);
            redirectView.setUrl("/pages/documentation");
            return "redirect:/pages/documentation";

        } else {
            // Connexion échouée, redirigez vers la page de connexion.
            modelAndView.setViewName("login");
            modelAndView.addObject("error", "Nom d'utilisateur ou mot de passe incorrect");
            redirectView.setUrl("/login");
            return "redirect:/login";
        }
    }
}
