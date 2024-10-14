package com.shopme.common.entity.admin.user.user.service;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.admin.user.user.repository.CountryRepository;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.admin.user.paging.PagingAndSortingHelper;
import com.shopme.common.exception.OrderNotFoundException;
import com.shopme.common.entity.admin.user.user.repository.OrderRepository;
import com.shopme.common.entity.order.OrderTrack;
import com.shopme.common.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    public static final int ORDERS_PER_PAGE = 10;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CountryRepository countryRepository;

    public Order get(Integer orderId) throws OrderNotFoundException {
        Order order = orderRepository.findById(orderId).get();
        if (order == null)
            throw new OrderNotFoundException("No Order with Id " + orderId);
        return order;

    }

    public void listByPage(int pageNumber, PagingAndSortingHelper helper) {
        String sortField = helper.getSortField();
        String sortDir = helper.getSortDir();
        String keyword = helper.getKeyword();
        Sort sort = null;

        if ("destination".equals(sortField)) {
            sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, ORDERS_PER_PAGE, sort);

        Page<Order> page = null;

        if (keyword != null) {
            page = orderRepository.findAll(keyword, pageable);
        } else {
            page = orderRepository.findAll(pageable);
        }

        helper.updateModelAttribute(pageNumber, page);

    }

    public void delete(Integer orderId) throws OrderNotFoundException {
        Long byIdCount = orderRepository.countById(orderId);

        if (byIdCount == null || byIdCount == 0)
            throw new OrderNotFoundException("Could not find any Order with ID " + orderId);

        orderRepository.deleteById(orderId);
    }

    public void save(Order orderInFrom) {
        Order orderInDB = orderRepository.findById(orderInFrom.getId()).get();
        orderInFrom.setOrderTime(orderInDB.getOrderTime());
        orderInFrom.setCustomer(orderInDB.getCustomer());

        orderRepository.save(orderInFrom);

    }

    public void updateStatus(Integer orderId,String status){
        Order order = orderRepository.findById(orderId).get();
        OrderStatus statusToUpdate = OrderStatus.valueOf(status);


        if(!order.hasStatus(statusToUpdate)){
            List<OrderTrack> orderTracks = order.getOrderTracks();

            OrderTrack orderTrack = new OrderTrack();
            orderTrack.setOrder(order);
            orderTrack.setStatus(statusToUpdate);
            orderTrack.setUpdatedTime(new Date());
            orderTrack.setNotes(statusToUpdate.defaultDescription());
            orderTracks.add(orderTrack);

            order.setStatus(statusToUpdate);
            orderRepository.save(order);
        }
    }

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    }
}
