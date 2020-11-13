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

    @GetMapping("Contact")
    public String contactUs(Model model) {
        return "contact";
    }

    @GetMapping("About")
    public String aboutUs(Model model) {
        return "about";
    }

    public static final String REDIRECT_TO = "redirect:";
}