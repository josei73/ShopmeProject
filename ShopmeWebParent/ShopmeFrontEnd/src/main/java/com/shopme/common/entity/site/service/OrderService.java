package com.shopme.common.entity.site.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.site.Checkout.CheckoutInfo;
import com.shopme.common.entity.site.model.OrderReturnRequest;
import com.shopme.common.entity.site.repository.OrderRepository;
import com.shopme.common.enums.OrderStatus;
import com.shopme.common.enums.PaymentMethod;
import com.shopme.common.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE = 5;

    @Autowired
    private OrderRepository orderRepository;

    public Page<Order> listForCustomerByPage(int pageNum, Integer customerId, String sortDir, String sortField, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword == null || keyword.isEmpty())
            return orderRepository.findAllByCustomerId(customerId, pageable);


        return orderRepository.findAll(keyword, customerId, pageable);
    }

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItemList, PaymentMethod paymentMethod,
                             CheckoutInfo checkoutInfo) {
        Order order = new Order();
        order.setOrderTime(new Date());

        if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            order.setStatus(OrderStatus.PAID);
        } else {
            order.setStatus(OrderStatus.NEW);
        }

        order.setCustomer(customer);
        order.setProductCost(checkoutInfo.getProductCost());
        // Wie viel insgesamt die Produkte zusammen kosten
        order.setSubtotal(checkoutInfo.getProductTotal());
        order.setShippingCost(checkoutInfo.getShippingCostTotal());
        order.setTax(0.0f);
        order.setTotal(checkoutInfo.getPaymentTotal());
        order.setPaymentMethod(paymentMethod);
        order.setDeliverDays(checkoutInfo.getDeliverDays());
        order.setDeliverDate(checkoutInfo.getDeliverDate());

        if (address == null) {
            order.copyAddressFromCustomer();
        } else {
            order.copyShippingFromAddress(address);
        }

        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (CartItem item : cartItemList) {
            Product product = item.getProduct();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setSubtotal(item.getSubtotal());
            orderDetail.setProductCost(product.getCost() * item.getQuantity());
            orderDetail.setShippingCost(item.getShippingCost());

            orderDetails.add(orderDetail);

        }

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrder(order);
        orderTrack.setUpdatedTime(new Date());
        orderTrack.setStatus(OrderStatus.NEW);
        orderTrack.setNotes(OrderStatus.NEW.defaultDescription());
        order.getOrderTracks().add(orderTrack);

        return orderRepository.save(order);
    }

    public Order get(Integer orderId, Customer customer) {
        return orderRepository.findByIdAndCustomer(orderId, customer);
    }

    public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
        Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);

        if (order == null) {
            throw new OrderNotFoundException("Order Id " + request.getOrderId() + " not found");
        }
        // Dann wissen wir es wird schon bearbeitet zurück zu schicken
        if (order.isReturnRequested()) return;


        OrderTrack track = new OrderTrack();

        track.setOrder(order);
        track.setUpdatedTime(new Date());
        track.setStatus(OrderStatus.RETURN_REQUESTED);

        String notes = "Reason: " + request.getReason();
        if (!"".equals(request.getNote())) notes += ". " + request.getNote();

        track.setNotes(notes);
        order.getOrderTracks().add(track);
        order.setStatus(OrderStatus.RETURN_REQUESTED);

        orderRepository.save(order);
    }
}
