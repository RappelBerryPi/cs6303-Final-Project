package edu.utdallas.cs6303.finalproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import edu.utdallas.cs6303.finalproject.services.store.StoreServiceInterface;


@Controller
@RequestMapping(StoreController.REQUEST_MAPPING)
public class StoreController {

    @Autowired
    private StoreServiceInterface storeService;

    public static final String REQUEST_MAPPING = "/Store";

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("storeItemsList", storeService.findAll());
        return "store/index";
    }

    @GetMapping("/view/{id}")
    public String view(Model model, @PathVariable(name = "id", required = true) long id) {
        model.addAttribute("storeItem", storeService.findById(id));
        return "store/view";
    }
    
    
}
