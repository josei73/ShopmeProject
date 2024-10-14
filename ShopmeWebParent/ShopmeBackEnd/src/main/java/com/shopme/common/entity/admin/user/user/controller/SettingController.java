package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.Constants;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.admin.user.AmazonS3Util;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.admin.user.FileUpdateUtil;
import com.shopme.common.entity.admin.user.user.model.GeneralSettingBag;
import com.shopme.common.entity.admin.user.user.repository.CurrencyRepository;
import com.shopme.common.entity.admin.user.user.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {
    @Autowired
    private SettingService service;

    @Autowired
    private CurrencyRepository currencyRepository;


    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> settings = service.listAllSettings();
        List<Currency> currencies = currencyRepository.findAllByOrderByNameAsc();
        model.addAttribute("currencies", currencies);

        for (Setting setting : settings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
        return "settings/settings";
    }

    // Benutzen bei settingForm kein th: object oben in der Form deswegen HttpServletRequest
    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag settingBag = service.getGeneralSetting();
        saveSiteLogo(multipartFile, settingBag);
        saveCurrencySymbol(request, settingBag);

        updateSettingValueFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings#genral";
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSetting(
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailServerSetting = service.getMailServerSetting();
        updateSettingValueFromForm(request, mailServerSetting);
        redirectAttributes.addFlashAttribute("message", "Mail server Setting has been saved");
        return "redirect:/settings#mailServer";

    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplatesSetting(
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailTemplatesSetting = service.getMailTemplateSetting();
        updateSettingValueFromForm(request, mailTemplatesSetting);
        redirectAttributes.addFlashAttribute("message", "Mail template Setting has been saved");
        return "redirect:/settings#mail_templates";

    }

    @PostMapping("/settings/payment")
    public String savePaymentSetting(
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        List<Setting> mailTemplatesSetting = service.getPaymentSetting();
        updateSettingValueFromForm(request, mailTemplatesSetting);
        redirectAttributes.addFlashAttribute("message", "Payment setting has been saved");
        return "redirect:/settings#payment";

    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "/site-logo/" + fileName;
            settingBag.updateSiteLogo(value);
            String uploadDir = "site-logo";
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> currencyById = currencyRepository.findById(currencyId);
        if (currencyById.isPresent()) {
            Currency currency = currencyById.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValueFromForm(HttpServletRequest request, List<Setting> settings) {
        for (Setting setting : settings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }
        service.saveAll(settings);
    }


}
