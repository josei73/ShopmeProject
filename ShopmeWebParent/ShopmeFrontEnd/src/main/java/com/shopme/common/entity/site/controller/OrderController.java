package com.shopme.common.entity.site.controller;


import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.service.CustomerService;
import com.shopme.common.entity.site.service.OrderService;
import com.shopme.common.entity.site.service.ProductService;
import com.shopme.common.entity.site.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;

    @Autowired
    private CustomerService customerService;









    @GetMapping("/orders")
    public String listFirstPage(HttpServletRequest request,Model model){
        return listByPage(1,"orderTime","desc",null,request,model);
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") Integer pageNum, String sortField,String sortDir,String orderKeyword, HttpServletRequest request , Model model) {

        Customer customer = getAuthenticatedCustomer(request);

        Page<Order> pageOrders = orderService.listForCustomerByPage(pageNum,customer.getId(),sortDir,sortField,orderKeyword );
        List<Order> result = pageOrders.getContent();

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        long endCount = startCount + OrderService.ORDERS_PER_PAGE  - 1;
        if (endCount > pageOrders.getTotalElements()) endCount = pageOrders.getTotalElements();


        model.addAttribute("startCount", startCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalPages", pageOrders.getTotalPages());
        model.addAttribute("totalItems", pageOrders.getTotalElements());
        model.addAttribute("pageTitle", orderKeyword +" - Search Result");
        model.addAttribute("reverseSortDir",sortDir.equals("asc") ? "desc":"asc");
        model.addAttribute("sortField",sortField);
        model.addAttribute("sortDir",sortDir);
        model.addAttribute("moduleURL","/orders");

        model.addAttribute("orderKeyword",orderKeyword);
        model.addAttribute("orders",result);
        return "orders/orders_customer";

    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes
            ) {

            Customer customer = getAuthenticatedCustomer(request);
            Order order = orderService.get(id,customer);
            model.addAttribute("order", order);
            return "orders/order_detail_modal";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String emailOfAuthenticatedCustomer = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getByEmail(emailOfAuthenticatedCustomer);

    }

}
