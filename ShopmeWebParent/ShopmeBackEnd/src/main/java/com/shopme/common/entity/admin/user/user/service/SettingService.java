package com.shopme.common.entity.admin.user.user.service;


import com.shopme.common.entity.setting.Setting;
import com.shopme.common.enums.SettingCategory;
import com.shopme.common.entity.admin.user.user.model.GeneralSettingBag;
import com.shopme.common.entity.admin.user.user.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired private SettingRepository repository;


    public List<Setting> listAllSettings(){
        return repository.findAll();
    }

    public GeneralSettingBag getGeneralSetting(){
        List<Setting> settings = new ArrayList<>();
        List<Setting> generalSettings= repository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings= repository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);
        return new GeneralSettingBag(settings);
    }


    public void saveAll(Iterable<Setting> settings){
        repository.saveAll(settings);
    }

    public List<Setting> getMailServerSetting(){
        return repository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSetting(){
        return repository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySetting(){return repository.findByCategory(SettingCategory.CURRENCY);}

    public List<Setting> getPaymentSetting(){return repository.findByCategory(SettingCategory.PAYMENT);}
}


