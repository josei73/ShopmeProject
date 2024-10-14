package com.shopme.common.entity.site.service;


import com.shopme.common.entity.Currency;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.site.Settingmodel.CurrencySettingBag;
import com.shopme.common.entity.site.Settingmodel.PaymentSettingBag;
import com.shopme.common.entity.site.repository.CurrencyRepository;
import com.shopme.common.enums.SettingCategory;
import com.shopme.common.entity.site.Settingmodel.EmailSettingBag;
import com.shopme.common.entity.site.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired private SettingRepository settingRepository;



    @Autowired private CurrencyRepository currencyRepository;




    public List<Setting> getGeneralSetting(){
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL,SettingCategory.CURRENCY);
    }


    public EmailSettingBag getEmailSettings(){
        List<Setting> settings = settingRepository.findByTwoCategories(SettingCategory.MAIL_SERVER,SettingCategory.MAIL_TEMPLATES);
        return new EmailSettingBag(settings);
    }

    public CurrencySettingBag getCurrencySettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingBag(settings);
    }

    public PaymentSettingBag getPaymentSettings(){
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
    }

    public String getCurrencyCode(){
        Setting setting = settingRepository.findByKey("CURRENCY_ID");
        Integer currencyID = Integer.parseInt(setting.getValue());
        Currency currency = currencyRepository.findById(currencyID).get();
        return currency.getCode();

    }
}


