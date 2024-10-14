package com.shopme.common.entity.admin.user.user.controller;


import com.shopme.common.entity.Country;
import com.shopme.common.entity.admin.user.security.ShopmeUserDetails;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingParam;
import com.shopme.common.exception.OrderNotFoundException;
import com.shopme.common.entity.admin.user.user.service.OrderService;
import com.shopme.common.entity.admin.user.user.service.SettingService;
import com.shopme.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    private String defaultRedirectLink = "redirect:/orders/page/1?sortField=orderTime&sortDir=desc";

    @Autowired
    private SettingService settingService;


    @GetMapping("/orders")
    public String listFirstPage() {
        return defaultRedirectLink;
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "orders", moduleURL = "/orders") PagingAndSortingHelper helper,
                             @PathVariable(name = "pageNum") int pageNum, HttpServletRequest request,
                             @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        orderService.listByPage(pageNum, helper);
        loadCurrencySetting(request);

        if(!loggedUser.hasRole("Salesperson") && !loggedUser.hasRole("Admin") && loggedUser.hasRole("Shipper") ){
            return "orders/orders_shipper";
        }
        return "orders/orders";
    }


    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes
    ,@AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);

            boolean isVisibleFormAdminOrSalesperson = false;

            if(loggedUser.hasRole("Admin") || loggedUser.hasRole("Salesperson"))
                isVisibleFormAdminOrSalesperson = true;
            model.addAttribute("order", order);
            model.addAttribute("isVisibleFormAdminOrSalesperson", isVisibleFormAdminOrSalesperson);
            return "orders/order_detail_modal";
        } catch (OrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return defaultRedirectLink;
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

        try {
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Delete Order with the ID " + id);
        } catch (OrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return defaultRedirectLink;
    }


    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        try {
            Order order = orderService.get(id);
            List<Country> countries = orderService.listAllCountries();
            model.addAttribute("order", order);
            model.addAttribute("pageTitle", "Edit Order (ID: " + id + " )");
            model.addAttribute("countries", countries);
            return "orders/order_form";
        } catch (OrderNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return defaultRedirectLink;
        }
    }

    @PostMapping("/order/save")
    public String saveOrder(Order order, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String countryName = request.getParameter("countryName");
        order.setCountry(countryName);

        updateProductDetails(order,request);
        updateOrderTracks(order,request);

        orderService.save(order);

        redirectAttributes.addFlashAttribute("message","The order ID "+ order.getId()+" has been updated successfully");
        return defaultRedirectLink;
    }

    private void updateOrderTracks(Order order, HttpServletRequest request) {
        String[] trackIds = request.getParameterValues("trackId");
        String[] trackStatuses = request.getParameterValues("trackStatus");
        String[] trackNotes = request.getParameterValues("trackNotes");
        String[] trackDates = request.getParameterValues("trackDate");

        List<OrderTrack> orderTracks = order.getOrderTracks();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");


        for (int i = 0; i < trackIds.length ; i++) {
            OrderTrack orderTrack = new OrderTrack();
            Integer trackId = Integer.parseInt(trackIds[i]);

            if(trackId > 0) orderTrack.setId(trackId);
            orderTrack.setOrder(order);
            orderTrack.setStatus(OrderStatus.valueOf(trackStatuses[i]));
            orderTrack.setNotes(trackNotes[i]);
            try {
                orderTrack.setUpdatedTime(dateFormat.parse(trackDates[i]));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            orderTracks.add(orderTrack);


        }
    }

    private void updateProductDetails(Order order, HttpServletRequest request) {
        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productCosts = request.getParameterValues("productDetailCost");
        String[] quantities = request.getParameterValues("quantity");
        String[] shippingCosts = request.getParameterValues("productShippingCost");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] subtotals = request.getParameterValues("productSubtotal");

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (int i = 0; i < detailIds.length; i++) {


            OrderDetail orderDetail = new OrderDetail();
            Integer detailId = Integer.parseInt(detailIds[i]);
            if(detailId > 0){
                orderDetail.setId(detailId);
            }
            orderDetail.setOrder(order);
            orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
            orderDetail.setProductCost(Float.parseFloat(productCosts[i]));
            orderDetail.setSubtotal(Float.parseFloat(subtotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(shippingCosts[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));
            orderDetail.setQuantity(Integer.parseInt(quantities[i]));
            orderDetails.add(orderDetail);

        }

    }

    private void loadCurrencySetting(HttpServletRequest request) {
        //Wenn ich den request die Attribute setze kann auf der Seite per Thymleaf automatisch drauf zugreifen ohne sie im model fest zu setzen guck dir im fragment formart_currency an dann verstehst
        List<Setting> currencySetting = settingService.getCurrencySetting();
        for (Setting setting : currencySetting) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }
}
