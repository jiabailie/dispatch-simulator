package com.css.work.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private int prepTime;

    private Date orderCreateTime;

    private Date orderPreparedTime;

    private Date courierArrivalTime;

    private Date courierPickupTime;

    public long getFoodWaitTime() {
        return courierPickupTime.getTime() - orderPreparedTime.getTime();
    }

    public long getCourierWaitTime() {
        return courierPickupTime.getTime() - courierArrivalTime.getTime();
    }
}
