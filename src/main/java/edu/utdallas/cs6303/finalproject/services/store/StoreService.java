package edu.utdallas.cs6303.finalproject.services.store;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.UploadedFileRepository;
import edu.utdallas.cs6303.finalproject.model.validation.store.ItemForm;

@Service
public class StoreService implements StoreServiceInterface {

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    @Override
    public List<StoreItem> findAll() {
        return storeItemRepository.findAll();
    }

    @Override
    public Optional<StoreItem> findById(long id) {
        return storeItemRepository.findById(id);
    }

    @Override
    public StoreItem createStoreItemFromForm(ItemForm itemForm) {
        StoreItem storeItem = new StoreItem();
        storeItem.setName(itemForm.getName());
        storeItem.setImage(uploadedFileRepository.findByFileName(itemForm.getImageName()));
        storeItem.setShortDescription(itemForm.getShortDescription());
        storeItem.setLongDescription(itemForm.getLongDescription());
        storeItem.setOpenDate(itemForm.getOpenDate());
        storeItem.setArchiveDate(itemForm.getArchiveDate());
        storeItem.setVisible(itemForm.isVisible());
        storeItem.setAmountInStock(itemForm.getAmountInStock());
        storeItem.setSnackCost(new BigDecimal(itemForm.getSnackCost()));
        storeItem.setXsCost(new BigDecimal(itemForm.getXsCost()));
        storeItem.setSmallCost(new BigDecimal(itemForm.getSmallCost()));
        storeItem.setMediumCost(new BigDecimal(itemForm.getMediumCost()));
        storeItem.setLargeCost(new BigDecimal(itemForm.getLargeCost()));
        storeItem.setDoubleXlCost(new BigDecimal(itemForm.getDoubleXlCost()));
        storeItem.setCategory(itemForm.getCategory());

        return storeItemRepository.save(storeItem);
    }
    
}
