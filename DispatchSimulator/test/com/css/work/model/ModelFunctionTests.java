package com.css.work.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class ModelFunctionTests {
    @Test
    public void orderFunctionTests() {
        final Order order = Order
                .builder()
                .id("a8cfcb76-7f24-4420-a5ba-d46dd77bdffd")
                .name("Banana Split")
                .prepTime(4)
                .build();

        order.setOrderCreateTime(new Date(1629095723043L));
        order.setOrderPreparedTime(new Date(1629095723060L));
        order.setCourierArrivalTime(new Date(1629095723100L));
        order.setCourierPickupTime(new Date(1629095723120L));

        assertThat(order.getFoodWaitTime()).isEqualTo(60L);
        assertThat(order.getCourierWaitTime()).isEqualTo(20L);
    }
}
