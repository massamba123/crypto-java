package com.farhan.staradmin.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/404"); // "error_page" est le nom de votre page d'erreur
        modelAndView.addObject("errorMessage", "Une erreur s'est produite. Veuillez réessayer.");
        // Vous pouvez personnaliser le message d'erreur ici.

        return modelAndView;
    }
}
