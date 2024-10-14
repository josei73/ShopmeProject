package com.shopme.common.entity.admin.user;


import com.shopme.common.entity.admin.user.user.repository.OrderDetailRepository;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderDetail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class OrderDetailTestRepository {

    @Autowired
    private OrderDetailRepository repository;


    @Test
    public void testFindByCategoryNameBetween() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = formatter.parse("2021-09-01");
        Date endTime = formatter.parse("2021-09-31");

        List<OrderDetail> details = repository.findWithCategoryAndTimeBetween(startTime, endTime);

        assertThat(details.size()).isGreaterThan(0);


        details.forEach(detail -> {
            System.out.printf("%-30s | %d | %10.2f | %10.2f | %10.2f \n",
                    detail.getProduct().getCategory().getName(), detail.getQuantity(), detail.getProductCost(),
                    detail.getShippingCost(), detail.getSubtotal());
        });
    }


    @Test
    public void testFindByProductNameTimeBetween() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = formatter.parse("2021-10-01");
        Date endTime = formatter.parse("2021-10-31");

        List<OrderDetail> details = repository.findWithProductAndTimeBetween(startTime, endTime);

        assertThat(details.size()).isGreaterThan(0);


        details.forEach(detail -> {
            System.out.printf("%-70s | %d | %10.2f | %10.2f | %10.2f \n",
                    detail.getProduct().getName(), detail.getQuantity(), detail.getProductCost(),
                    detail.getShippingCost(), detail.getSubtotal());
        });
    }
}
