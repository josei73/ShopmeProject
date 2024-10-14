package com.shopme.common.entity.site.util;

import com.shopme.common.entity.site.Settingmodel.CurrencySettingBag;
import com.shopme.common.entity.site.Settingmodel.EmailSettingBag;
import com.shopme.common.entity.site.oauth.CustomerOAuth2User;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Properties;

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties mailProperties = new Properties();
        mailProperties.setProperty("mail.smtp.auth", settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmtpSecured());
        mailSender.setJavaMailProperties(mailProperties);
        return mailSender;
    }


    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request) {
        Object principal = request.getUserPrincipal();
        String customerEmail = null;
        if (principal == null) return null;

        // Ausgabe -> UsernamePasswordAuthenticationToken

        // Google ausgabe OAuth2AuthenticationToken

        // Remember me RememberMeAuthenticationToken

        if (principal instanceof UsernamePasswordAuthenticationToken ||
                principal instanceof RememberMeAuthenticationToken)
            customerEmail = request.getUserPrincipal().getName();
        else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User = (CustomerOAuth2User) oAuth2Token.getPrincipal();
            customerEmail = oAuth2User.getEmail();
        }

        return customerEmail;


    }

    public static String formatCurrency(float amount, CurrencySettingBag setting) {
        String symbol = setting.getSymbol();
        String symbolPosition = setting.getSymbolPosition();
        int decimalDigits = setting.getDecimalDigits();
        String decimalPointType = setting.getDecimalPointType();
        String thousandPointType = setting.getThousandPointType();

        String pattern = symbolPosition.equals("Before price") ? symbol : "";
        pattern += "###,###";

        if (decimalDigits > 0) {
            pattern += ".";
            for (int i = 1; i <= decimalDigits; ++i) pattern += "#";
        }

        pattern += symbolPosition.equals("After price") ? symbol : "";

        char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat formatter = new DecimalFormat(pattern, decimalFormatSymbols);
        return formatter.format(amount);
    }


}
