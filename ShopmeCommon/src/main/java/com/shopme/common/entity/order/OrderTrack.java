package com.shopme.common.entity.order;

import com.shopme.common.entity.IdBasedEntity;
import com.shopme.common.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "order_track")
@Getter
@Setter
@NoArgsConstructor
public class OrderTrack extends IdBasedEntity {

    @Column(length = 256)
    private String notes;
    private Date updatedTime;

    @Enumerated(EnumType.STRING)
    @Column(length = 45,nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Transient
    public String getUpdatedTimeOnForm(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormat.format(this.updatedTime);
    }

    public void setUpdatedTimeOnForm(String dataString){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        try {
            this.updatedTime = dateFormat.parse(dataString);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}
