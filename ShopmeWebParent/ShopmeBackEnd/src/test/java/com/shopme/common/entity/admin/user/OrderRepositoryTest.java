package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.Customer;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.admin.user.user.repository.OrderRepository;
import com.shopme.common.enums.OrderStatus;
import com.shopme.common.enums.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;
    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateNewOrderWithSingleProduct() {
        Customer customer = entityManager.find(Customer.class, 1);
        Product product = entityManager.find(Product.class, 1);

        Order mainOrder = new Order();


        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();


        mainOrder.setShippingCost(10);
        mainOrder.setProductCost(product.getCost());
        mainOrder.setTax(0);
        mainOrder.setSubtotal(product.getCost());
        mainOrder.setTotal(product.getCost() + 10);


        mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        mainOrder.setStatus(OrderStatus.NEW);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(1);

        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setOrder(mainOrder);
        orderDetail.setProduct(product);
        orderDetail.setProductCost(product.getCost());
        orderDetail.setShippingCost(10);
        orderDetail.setQuantity(1);
        orderDetail.setSubtotal(product.getPrice());
        orderDetail.setUnitPrice(product.getPrice());

        mainOrder.getOrderDetails().add(orderDetail);

        Order savedOrder = repository.save(mainOrder);

        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateNewOrderWithMultipleProduct() {
        Customer customer = entityManager.find(Customer.class, 10);
        Product product1 = entityManager.find(Product.class, 20);
        Product product2 = entityManager.find(Product.class, 14);

        Order mainOrder = new Order();
        mainOrder.setOrderTime(new Date());
        mainOrder.setCustomer(customer);
        mainOrder.copyAddressFromCustomer();

        OrderDetail orderDetail1 = new OrderDetail();

        orderDetail1.setOrder(mainOrder);
        orderDetail1.setProduct(product1);
        orderDetail1.setProductCost(product1.getCost());
        orderDetail1.setShippingCost(10);
        orderDetail1.setQuantity(1);
        orderDetail1.setSubtotal(product1.getPrice());
        orderDetail1.setUnitPrice(product1.getPrice());

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setOrder(mainOrder);
        orderDetail2.setProduct(product2);
        orderDetail2.setProductCost(product2.getCost());
        orderDetail2.setShippingCost(10);
        orderDetail2.setQuantity(1);
        orderDetail2.setSubtotal(product2.getPrice());
        orderDetail2.setUnitPrice(product2.getPrice());

        mainOrder.getOrderDetails().add(orderDetail1);
        mainOrder.getOrderDetails().add(orderDetail2);

        mainOrder.setShippingCost(30);
        mainOrder.setProductCost(product1.getCost() + product2.getCost());
        mainOrder.setTax(0);
        float subtotal= product1.getPrice() + product2.getPrice();
        mainOrder.setSubtotal(subtotal);
        mainOrder.setTotal(subtotal + 30);


        mainOrder.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        mainOrder.setStatus(OrderStatus.PACKAGED);
        mainOrder.setDeliverDate(new Date());
        mainOrder.setDeliverDays(3);

        Order savedOrder = repository.save(mainOrder);

        assertThat(savedOrder.getId()).isGreaterThan(0);

    }

    @Test
    public void testListOrder(){
        Iterable<Order> orders = repository.findAll();
        assertThat(orders).hasSizeGreaterThan(0);
        orders.forEach(System.out::println);
    }

    @Test
    public void testUpdateOrder(){
        Integer orderId=2;
        Order order = repository.findById(orderId).get();
        order.setStatus(OrderStatus.SHIPPING);
        order.setPaymentMethod(PaymentMethod.COD);
        order.setOrderTime(new Date());
        order.setDeliverDays(2);
        Order updatedOrder = repository.save(order);

        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.SHIPPING);
    }

    @Test
    public void testUpdateOrderTracks(){
        Integer orderId = 1;
        Order order = repository.findById(orderId).get();

        OrderTrack newTrack = new OrderTrack();
        System.out.println(order);

        newTrack.setOrder(order);
        newTrack.setUpdatedTime(new Date());
        newTrack.setStatus(OrderStatus.NEW);
        newTrack.setNotes(OrderStatus.NEW.defaultDescription());

        List<OrderTrack> orderTracks = order.getOrderTracks();
        orderTracks.add(newTrack);

        Order updatedOrder = repository.save(order);

        System.out.println(updatedOrder);

        assertThat(updatedOrder.getOrderTracks()).hasSizeGreaterThan(0);
    }


    @Test
    public void testFindByOrderTimeBetween() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = formatter.parse("2021-08-01");
        Date endTime = formatter.parse("2022-08-31");

        List<Order> orders = repository.findByOrderTimeBetween(startTime, endTime);

        assertThat(orders.size()).isGreaterThan(0);


        orders.forEach(order -> {
            System.out.printf("%s | %s | %.2f | %.2f | %.2f \n",
                    order.getId(),order.getOrderTime(),order.getProductCost(),
                    order.getSubtotal(),
                    order.getTotal());
        });
    }
}
