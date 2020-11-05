package edu.utdallas.cs6303.finalproject.controllers;

import javax.annotation.security.RolesAllowed;
import javax.mail.StoreClosedException;
import javax.validation.Valid;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.validation.store.ItemForm;
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

    public static final String STORE_ITEM_FORM_ATTRIBUTE_NAME = "item";

    @GetMapping("/new/")
    //TODO: ADD ID
    //TODO: uncomment after testing
    //@RolesAllowed({"ADMIN","EMPLOYEE"})
    public String newItem(Model model) {
        if (!model.containsAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME)) {
            model.addAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME, new ItemForm());
        }
        model.addAttribute("categories",StoreItemCategoryEnum.values());
        return "store/newItem";
    }

    @PostMapping("/new/")
    public String forgotPasswordStepTwo(@Valid ItemForm itemForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + STORE_ITEM_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME, itemForm);
            return HomeController.REDIRECT_TO + StoreController.REQUEST_MAPPING + "/new/";
        }
        StoreItem storeItem = storeService.createStoreItemFromForm(itemForm);
        return HomeController.REDIRECT_TO + StoreController.REQUEST_MAPPING + "/";
    }

}
