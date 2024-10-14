package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.user.exception.ShippingAlreadyExistException;
import com.shopme.common.entity.admin.user.user.exception.ShippingRateNotFound;
import com.shopme.common.entity.admin.user.user.exception.UserNotFoundException;
import com.shopme.common.entity.admin.user.user.repository.CountryRepository;
import com.shopme.common.entity.admin.user.user.repository.ProductRepository;
import com.shopme.common.entity.admin.user.user.repository.ShippingRateRepository;
import com.shopme.common.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShippingRateService {
    public static final int RATE_PER_PAGE = 10;
    public static final int DIM_DIVISOR = 139;
    @Autowired private ShippingRateRepository shipRepo;
    @Autowired private CountryRepository countryRepo;

    @Autowired private ProductRepository productRepo;

    public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber,RATE_PER_PAGE,shipRepo);
    }





    public void delete(Integer id) throws ShippingRateNotFound {
        Long countById = shipRepo.countById(id);
        if (countById == null | countById == 0) throw new ShippingRateNotFound("Could not find any shipping_rate with ID" + id);
        shipRepo.deleteById(id);
    }

    public void updateUserEnableStatus(Integer id, boolean enable) throws ShippingRateNotFound {
        Long count = shipRepo.countById(id);
        if (count == null | id == 0) throw new ShippingRateNotFound("Could not find any shipping rate with ID" + id);

        shipRepo.updateCodSupport(id, enable);
    }

    public List<Country> listAllCountries() {
        return countryRepo.findAllByOrderByNameAsc();
    }

    public ShippingRate get(Integer id) throws ShippingRateNotFound {
        ShippingRate shippingRate = shipRepo.findById(id).get();
        if(shippingRate == null)
            throw new ShippingRateNotFound("Could not find any shipping_rate with ID" + id);
        return shippingRate;
    }

    public void save(ShippingRate rateInForm) throws ShippingAlreadyExistException {
        ShippingRate rateInDB = shipRepo.findByCountryIdAndState(rateInForm.getCountry().getId(), rateInForm.getState());

        boolean foundExistingRateInNewMode = rateInForm.getId() == null && rateInDB != null;

        boolean foundDifferentExistingRateInEditMode = rateInForm.getId() != null && rateInDB != null && !rateInForm.equals(rateInDB) ;

        if(foundExistingRateInNewMode || foundDifferentExistingRateInEditMode)
            throw new ShippingAlreadyExistException("There's already a rate for the destination "+
                    rateInForm.getCountry().getName()+" , "+rateInForm.getState());
        shipRepo.save(rateInForm);
    }

    public float calculateShippingCost (Integer productId, Integer countryId, String state) throws ShippingRateNotFound {
        ShippingRate shippingRate = shipRepo.findByCountryIdAndState(countryId, state);

        if(shippingRate == null)
            throw new ShippingRateNotFound("No shipping rate found for the given " +
                    "destination. You have to enter shipping cost manually");

        Product product = productRepo.findById(productId).get();

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;

        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;


        return finalWeight * shippingRate.getRate();
    }
}
