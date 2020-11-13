package edu.utdallas.cs6303.finalproject.services.store;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.Cart;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItemSize;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;
import edu.utdallas.cs6303.finalproject.model.database.repositories.CartRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemSizeRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UserRepository;
import edu.utdallas.cs6303.finalproject.model.pages.CartSummary;
import edu.utdallas.cs6303.finalproject.model.pages.ChangeInformation;
import edu.utdallas.cs6303.finalproject.model.validation.store.GroupItemForm;
import edu.utdallas.cs6303.finalproject.services.oauth.OidcUserAuthenticationService;
import edu.utdallas.cs6303.finalproject.services.user.UserServiceInterface;

@Service
public class StoreService implements StoreServiceInterface {

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Autowired
    private StoreItemSizeRepository storeItemSizeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    OidcUserAuthenticationService oAuth2UserAuthenticationService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserServiceInterface userService;

    @Override
    public List<StoreItem> findAll() {
        return storeItemRepository.findAll();
    }

    @Override
    public Optional<StoreItem> findById(long id) {
        return storeItemRepository.findById(id);
    }

    @Override
    public StoreItem createStoreItemFromForm(GroupItemForm itemForm) {
        StoreItem storeItem = new StoreItem();
        storeItem.setName(itemForm.getName());
        storeItem.setImage(uploadedFileRepository.findByFileName(itemForm.getImageName()));
        storeItem.setShortDescription(itemForm.getShortDescription());
        storeItem.setLongDescription(itemForm.getLongDescription());
        storeItem.setOpenDate(itemForm.getOpenDate());
        storeItem.setArchiveDate(itemForm.getArchiveDate());
        storeItem.setVisible(itemForm.isVisible());
        storeItem.setAmountInStock(itemForm.getAmountInStock());

        List<StoreItemSize> groupStoreItems = new ArrayList<>();

        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.SNACK, itemForm.getSnackCost()));
        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.EXTRA_SMALL, itemForm.getXsCost()));
        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.SMALL, itemForm.getSmallCost()));
        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.MEDIUM, itemForm.getMediumCost()));
        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.LARGE, itemForm.getLargeCost()));
        groupStoreItems.add(generateStoreItemSize(StoreItemSizeEnum.DOUBLE_EXTRA_LARGE, itemForm.getDoubleXlCost()));
        storeItem.setGroupStoreItems(groupStoreItems);
        storeItem = storeItemRepository.save(storeItem);
        for (StoreItemSize storeItems : storeItem.getGroupStoreItems()) {
            storeItems.setStoreItem(storeItem);
        }
        storeItemSizeRepository.saveAll(storeItem.getGroupStoreItems());
        return storeItem;
    }

    private StoreItemSize generateStoreItemSize(StoreItemSizeEnum size, String cost) {
        var returnItem = new StoreItemSize();
        returnItem.setSize(size);
        returnItem.setCost(new BigDecimal(cost));
        return returnItem;
    }

    @Override
    public List<StoreItem> findByCategoryIn(Collection<StoreItemCategoryEnum> categories) {
        return storeItemRepository.findByCategoryIn(categories);
    }

    @Override
    public int addItemToCart(long id, StoreItemSizeEnum size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User           user           = userService.getUserFromAuthentication(authentication);
        if (user == null) {
            throw new IllegalStateException("Unable to get User's cart");
        }
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
        }
        StoreItemSize storeItemSize = storeItemSizeRepository.findFirstByStoreItemIdEqualsAndSizeIs(id, size);
        if (storeItemSize != null) {
            List<StoreItemSize> cartItems = cart.getCartItems();
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
            cartItems.add(storeItemSize);
            cart.setCartItems(cartItems);
            cartRepository.save(cart);
        }
        user.setCart(cart);
        userRepository.save(user);
        return cart.getCartItems().size();
    }

    @Override
    public Cart getUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User           user           = userService.getUserFromAuthentication(authentication);
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setCartItems(new ArrayList<>());
            cart.setUser(user);
            cart = cartRepository.save(cart);
            user.setCart(cart);
            userRepository.save(user);
        }
        return cart;
    }

    @Override
    public Map<StoreItem, List<CartSummary>> createUserCartSummary() {
        Map<StoreItem, List<CartSummary>> returnMap = new HashMap<>();
        Cart                              cart      = this.getUserCart();
        if (cart.getCartItems() == null) {
            return null;
        }
        var                               modelMap  = cart.getCartItems().stream().collect(Collectors.groupingBy(StoreItemSize::getStoreItem, Collectors.groupingBy(StoreItemSize::getSize)));
        for (var item : modelMap.entrySet()) {
            List<CartSummary> appendList = new ArrayList<>();
            for (var size : item.getValue().entrySet()) {
                List<StoreItemSize> storeItemSizes = size.getValue();
                CartSummary         summary        = new CartSummary();
                summary.setCost(storeItemSizes.get(0).getCost());
                summary.setCount(storeItemSizes.size());
                summary.setItemName(item.getKey().getName());
                summary.setSize(size.getKey());
                summary.setStoreItemSizeId(storeItemSizes.get(0).getId());
                appendList.add(summary);
            }
            appendList = appendList.stream().sorted(Comparator.comparing(CartSummary::getSize)).collect(Collectors.toList());
            returnMap.put(item.getKey(), appendList);
        }
        return returnMap;
    }

    @Override
    public ChangeInformation deleteItemFromCart(long storeItemSizeId, long newQuantity) {
        Cart                cart          = this.getUserCart();
        List<StoreItemSize> items         = cart.getCartItems();
        var                 filteredItems = items.stream().filter(i -> i.getId() == storeItemSizeId).skip(newQuantity).collect(Collectors.toList());
        StoreItemSize       item          = storeItemSizeRepository.findById(storeItemSizeId).orElseThrow();
        if (filteredItems.isEmpty()) {
            for (int i = 0; i < newQuantity - cart.getCartItems().stream().filter(j -> j.getId() == storeItemSizeId).count(); i++) {
                items.add(item);
            }
        } else {
            for (StoreItemSize storeItem : filteredItems) {
                items.remove(storeItem);
            }
        }
        cart.setCartItems(items);
        cartRepository.save(cart);
        ChangeInformation information = new ChangeInformation();
        information.setItemSubtotal(item.getCost().multiply(new BigDecimal(newQuantity)));
        information.setSubtotal(items.stream().map(StoreItemSize::getCost).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
        MathContext mc = new MathContext(3);
        information.setTax(information.getSubtotal().multiply(new BigDecimal("0.0825"), mc));
        information.setTotal(information.getSubtotal().add(information.getTax()));
        information.setTotalItemCount(items.size());
        return information;
    }

    @Override
    public BigDecimal getSubtotalOfCart() {
        return this.getUserCart().getCartItems().stream().map(StoreItemSize::getCost).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }
}
