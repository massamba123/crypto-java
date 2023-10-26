package com.farhan.staradmin.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.farhan.staradmin.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

public class CustomInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Vérifiez l'autorisation ici (par exemple, en fonction de l'utilisateur connecté).
        // Si l'utilisateur n'est pas autorisé, vous pouvez rediriger vers une page d'erreur ou une page de connexion.
        // Exemple :
        if (!isUserAuthorized(request)) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    private boolean isUserAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // Récupérez la session sans la créer si elle n'existe pas.

        if (session != null) {
            User user = (User) session.getAttribute("user"); // Récupérez l'attribut "user" de la session.

            // Ajoutez votre logique d'autorisation ici, par exemple, vérifiez si l'utilisateur est connecté ou s'il a les autorisations nécessaires.
            return user != null; // L'utilisateur est autorisé.
        }

        return false; // L'utilisateur n'est pas autorisé.
    }

}
