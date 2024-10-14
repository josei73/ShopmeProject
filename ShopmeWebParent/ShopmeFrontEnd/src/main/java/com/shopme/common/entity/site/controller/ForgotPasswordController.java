package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Customer;
import com.shopme.common.entity.site.Settingmodel.EmailSettingBag;
import com.shopme.common.entity.site.exception.CustomerNotFoundException;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.SettingService;
import com.shopme.common.entity.site.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;


    @GetMapping("/forgot_password")
    public String showRequestForm() {


        return "customer/forgot_password_form";
    }


    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;

            sendMail(email, link);
            model.addAttribute("message", "We have sent a reset password link to your email." +
                    "Please check.");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Could not send email");
        }
        return "customer/forgot_password_form";
    }


    private void sendMail(String email, String link) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);
        System.out.println(link);

        String toAddress = email;
        String subject = "Here's the link to reset your password";
        String content = "<p> Hello, </p>"
                + "<p> You have requested to reset your password. </p>"
                + "Click the link below to change your password:"
                + "<p><a href=" + link + "> Change my password </a></p>"
                + "<p>Ignore this email if you do remember your password," +
                "or you have not made the request .</p>";

        // Nutzen um E-Mails zu versenden
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        // DIE E-Mail festlegen, von wo die Verifizierung verschickt wie z. B. noreply@gmail.com und Sender ist dann Shopme
        helper.setFrom(emailSettingBag.getFromAddress(), emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        System.out.println(content);


        helper.setText(content, true);
        mailSender.send(message);

    }

    @GetMapping("/reset_password")
    public String showResetForm(String token, Model model) {

        Customer customer = customerService.getByResetPasswordToken(token);
        if (customer != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("message", "Invalid Token");
            model.addAttribute("pageTitle", "Invalid Token");
            return "message";
        }
        return "customer/reset_password_form";

    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request,Model model){
        String password = request.getParameter("password");
        String token = request.getParameter("token");

        try {
            customerService.updatePassword(token,password);
            model.addAttribute("message", "You have successfully changed your password");
            model.addAttribute("pageTitle", "Reset Your Password");
            model.addAttribute("title", "Reset Your password");
            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("pageTitle", "Invalid Token");
            return "message";

        }




    }

}
