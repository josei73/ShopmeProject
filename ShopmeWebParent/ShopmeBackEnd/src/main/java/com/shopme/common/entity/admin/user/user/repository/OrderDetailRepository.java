package com.shopme.common.entity.admin.user.user.repository;

import com.shopme.common.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query("Select NEW com.shopme.common.entity.order.OrderDetail(d.product.category.name,d.quantity,d.productCost," +
            "d.shippingCost,d.subtotal)" +
            "FROM OrderDetail d WHERE d.order.orderTime between ?1 AND ?2")
    public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);


    @Query("Select NEW com.shopme.common.entity.order.OrderDetail(d.quantity,d.product.name,d.productCost," +
            "d.shippingCost,d.subtotal)" +
            "FROM OrderDetail d WHERE d.order.orderTime between ?1 AND ?2")
    public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);
}
