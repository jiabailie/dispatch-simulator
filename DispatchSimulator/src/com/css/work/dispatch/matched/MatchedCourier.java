package com.css.work.dispatch.matched;

import static com.css.work.constant.Constant.SCALE_MILLI_SECOND;

import com.css.work.common.Utils;
import com.css.work.dispatch.base.Courier;
import com.css.work.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Courier for Matched strategy
 */
@Log4j
@Builder
@AllArgsConstructor
public class MatchedCourier extends Courier {

    private final Order order;
    private final Utils utils;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final Map<String, BlockingQueue<Order>> preparedOrderMap;

    @SneakyThrows
    @Override
    public void send() {
        // Simulate Courier pick up order process
        // Upon receiving an order, the system should immediately dispatch a courier to pick it up.
        // Couriers arrive randomly following a uniform distribution,
        // 3-15 seconds after they’ve been dispatched.
        log.info(String.format("Courier dispatched: %s", order.getId()));

        // Simulate the process of courier arrival
        final int arrivalTime = utils.getCourierArrivalTime();
        Thread.sleep(arrivalTime * SCALE_MILLI_SECOND);

        order.setCourierArrivalTime(new Date());
        log.info(String.format("Courier arrived: %s", order.getId()));

        // Waiting until order has been prepared
        preparedOrderMap.get(order.getId()).take();
        order.setCourierPickupTime(new Date());
        log.info(String.format("Order picked up: %s", order.getId()));

        // Put order to analysis queue
        analysisOrderQueue.put(order);
    }
}
