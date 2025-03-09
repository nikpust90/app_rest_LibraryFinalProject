package app_rest_libraryfinalproject.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import app_rest_libraryfinalproject.security.PersonDetails;


@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails principal = (PersonDetails) authentication.getPrincipal();

        System.out.println(principal.getUsername());

        return "hello";
    }

}
