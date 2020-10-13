package edu.utdallas.cs6303.finalproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("myValue","Hello world!");
        model.addAttribute("mySecondValue","Hello Second world!");
        return "index";
    }

    
    public static final String REDIRECT_TO = "redirect:";
}