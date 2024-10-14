package com.shopme.common.entity.admin.user.user.repository;


import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.admin.user.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends SearchRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE CONCAT('#',o.id) LIKE %?1% OR CONCAT(o.firstName,' ', o.lastName,' '" +
            ",o.city,' ',o.state,' ',o.country,' ',o.paymentMethod,' ',o.status,' ',o.customer.firstName,' ',o.customer.lastName) LIKE %?1% ")
    public Page<Order> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);



    @Query("Select NEW com.shopme.common.entity.order.Order(o.id,o.orderTime,o.productCost,o.subtotal,o.total)" +
            "FROM Order o WHERE o.orderTime between ?1 AND ?2 ORDER BY o.orderTime ASC ")
    public List<Order> findByOrderTimeBetween(Date startTime, Date endTime);


}
