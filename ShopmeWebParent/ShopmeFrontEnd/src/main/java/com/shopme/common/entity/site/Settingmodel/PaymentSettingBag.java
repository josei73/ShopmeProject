package com.shopme.common.entity.site.Settingmodel;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingBag;

import java.util.List;

public class PaymentSettingBag extends SettingBag {
    public PaymentSettingBag(List<Setting> settings) {
        super(settings);
    }


    public String getUrl(){
        return super.getValue("PAYPAL_API_BASE_URL");
    }

    public String getClientID(){
        return super.getValue("PAYPAL_API_CLIENT_ID");
    }

    public String  getClientSecret(){
        return super.getValue("PAYPAL_API_CLIENT_SECRET");
    }
}
