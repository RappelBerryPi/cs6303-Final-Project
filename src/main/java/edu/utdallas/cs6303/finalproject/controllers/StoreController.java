package edu.utdallas.cs6303.finalproject.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;
import edu.utdallas.cs6303.finalproject.model.device.DeviceInfo;
import edu.utdallas.cs6303.finalproject.model.pages.ChangeInformation;
import edu.utdallas.cs6303.finalproject.model.pages.PayPalHttpResponse;
import edu.utdallas.cs6303.finalproject.model.pages.PayPalOrder;
import edu.utdallas.cs6303.finalproject.model.validation.store.GroupItemForm;
import edu.utdallas.cs6303.finalproject.services.order.OrderService;
import edu.utdallas.cs6303.finalproject.services.order.OrderServiceInterface;
import edu.utdallas.cs6303.finalproject.services.store.StoreServiceInterface;
import edu.utdallas.cs6303.finalproject.services.user.UserService;

@Controller
@RequestMapping(StoreController.REQUEST_MAPPING)
public class StoreController {

    @Autowired
    private StoreServiceInterface storeService;

    @Autowired
    private OrderServiceInterface orderService;

    @Autowired
    private UserService userService;

    public static final String REQUEST_MAPPING = "/Store";

    private static final String STORE_ITEM_FORM_ATTRIBUTE_NAME = "item";

    public static final String NEW_STORE_ITEM_MAPPING = "/new";

    public static final String VIEW_STORE_ITEM_MAPPING = "/view";

    @GetMapping({ "", "/" })
    public String index(@RequestParam(name = "category", required = false) List<StoreItemCategoryEnum> categories, Model model) {
        if (categories == null || categories.isEmpty()) {
            model.addAttribute("storeItemsList", storeService.findAll());
        } else {
            model.addAttribute("storeItemsList", storeService.findByCategoryIn(categories));
        }
        var categoriesMap = Arrays.stream(StoreItemCategoryEnum.values()).collect(Collectors.toMap(s -> StringUtils.capitalize(s.toString().toLowerCase()), s -> {
            if (categories == null) {
                return false;
            } else {
                return categories.contains(s);
            }
        }));
        model.addAttribute("categories", categoriesMap.entrySet());
        return "store/index";
    }

    @GetMapping(NEW_STORE_ITEM_MAPPING)
    @RolesAllowed({"ADMIN","EMPLOYEE"})
    public String newItem(Model model) {
        if (!model.containsAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME)) {
            model.addAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME, new GroupItemForm());
        }
        model.addAttribute("categories", StoreItemCategoryEnum.values());
        return "store/newItem";
    }

    @PostMapping(NEW_STORE_ITEM_MAPPING)
    public String newItemStepTwo(@Valid GroupItemForm itemForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult." + STORE_ITEM_FORM_ATTRIBUTE_NAME, bindingResult);
            redirectAttributes.addFlashAttribute(STORE_ITEM_FORM_ATTRIBUTE_NAME, itemForm);
            return HomeController.REDIRECT_TO + REQUEST_MAPPING + NEW_STORE_ITEM_MAPPING;
        }
        storeService.createStoreItemFromForm(itemForm);
        return HomeController.REDIRECT_TO + StoreController.REQUEST_MAPPING;
    }


    private final static String ITEM_NOT_EXIST = "itemNotExist";

    @GetMapping(VIEW_STORE_ITEM_MAPPING + "/{id}")
    public String viewStoreItem(@PathVariable long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<StoreItem> itemOptional = storeService.findById(id);
        if (itemOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute(ITEM_NOT_EXIST, id);
            return HomeController.REDIRECT_TO + REQUEST_MAPPING;
        }
        StoreItem item = itemOptional.get();
        if ((!item.isVisible() || item.isDeleted()) && !isUserInRole("ADMIN")) {
            redirectAttributes.addFlashAttribute(ITEM_NOT_EXIST, id);
            return HomeController.REDIRECT_TO + REQUEST_MAPPING;
        } else if (!item.isActive() && !isUserInRole("EMPLOYEE")) {
            redirectAttributes.addFlashAttribute(ITEM_NOT_EXIST, id);
            return HomeController.REDIRECT_TO + REQUEST_MAPPING;
        }
        model.addAttribute("item", item);
        return "store/view";
    }

    private boolean isUserInRole(String Role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(Role + "_ROLE"));
    }

    @PostMapping("/AddItemToCart/{id}")
    @ResponseBody
    public int addItemToCart(@PathVariable long id, @RequestParam("itemSize") StoreItemSizeEnum size, Model model) {
        return storeService.addItemToCart(id, size);
    }

    @GetMapping("/ViewCart")
    public String viewCart(Model model) {
        var userCartSummary = storeService.createUserCartSummary();
        if (userCartSummary == null) {
            return "redirect:" + REQUEST_MAPPING;
        }
        var subtotal        = storeService.getSubtotalOfCart();
        model.addAttribute("userCartSummary", userCartSummary);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", userCartSummary.values().stream().flatMap(l -> l.stream()).map(s -> s.getCost().multiply(new BigDecimal(OrderService.SALES_TAX)).setScale(2,RoundingMode.HALF_UP).multiply(new BigDecimal(s.getCount()))).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
        return "store/viewcart";
    }

    @PostMapping("/DeleteItemFromCart")
    @ResponseBody
    public ChangeInformation deleteItemFromCart(@RequestParam("itemSizeId") long id, @RequestParam("newQuantity") long quantity, Model model) {
        return storeService.deleteItemFromCart(id, quantity);
    }

    @PostMapping("/CreateOrder")
    @ResponseBody
    public PayPalHttpResponse<PayPalOrder> createOrder() throws IOException {
        HttpResponse<Order> resp = orderService.createPaypalOrder(storeService.createUserCartSummary());
        PayPalOrder order = new PayPalOrder(resp.result());
        return new PayPalHttpResponse<>(resp.headers(),resp.statusCode(),order);
    }

    @PostMapping("/OrderPaidFor")
    @ResponseBody
    public String[] captureOrder(@RequestBody String orderId) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User           user           = userService.getUserFromAuthentication(authentication);
        orderService.buildOrderFromUserCart(user, orderId);
        return new String[] {REQUEST_MAPPING};
    }


}