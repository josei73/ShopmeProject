package com.shopme.common.entity.site.Settingmodel;

import com.shopme.common.Constants;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.site.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@Component
public class SettingFilter implements Filter {
    @Autowired
    private SettingService settingService;

    // wird immer aufgerufen, wenn ein Request an der Anwendung gesendet wird.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        List<Setting> generalSetting=settingService.getGeneralSetting();
        generalSetting.forEach(setting -> {
            servletRequest.setAttribute(setting.getKey(),setting.getValue());
        });
        request.setAttribute("S3_BASE_URI", Constants.S3_BASE_URI);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
