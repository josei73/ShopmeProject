package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.admin.user.user.service.SettingService;
import com.shopme.common.entity.setting.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private SettingService settingService;



    @GetMapping("/reports")
    public String viewSalesReportHome(HttpServletRequest request){

        loadCurrencySetting(request);
        return "reports/reports";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        //Wenn ich den request die Attribute setze kann auf der Seite per Thymleaf automatisch drauf zugreifen ohne sie im model fest zu setzen guck dir im fragment formart_currency an dann verstehst
        List<Setting> currencySetting = settingService.getCurrencySetting();
        for (Setting setting : currencySetting) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
}
