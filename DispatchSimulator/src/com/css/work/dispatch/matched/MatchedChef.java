package com.css.work.dispatch.matched;

import static com.css.work.constant.Constant.SCALE_MILLI_SECOND;

import com.css.work.dispatch.base.Chef;
import com.css.work.exception.SimulatorException;
import com.css.work.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Chef for Matched strategy
 */
@Log4j
@Builder
@AllArgsConstructor
public class MatchedChef extends Chef {

    private final Order order;
    private final Map<String, BlockingQueue<Order>> preparedOrderMap;

    @SneakyThrows
    @Override
    public void prepare() {
        // For matched strategy
        // A courier is dispatched for a specific order and may only pick up that order
        final String orderId = order.getId();
        final BlockingQueue<Order> queue = new LinkedBlockingDeque<>();
        if (preparedOrderMap.containsKey(orderId)) {
            throw new SimulatorException("Encounter repeated order id!");
        }
        preparedOrderMap.put(orderId, queue);

        Thread.sleep(order.getPrepTime() * SCALE_MILLI_SECOND);
        order.setOrderPreparedTime(new Date());
        preparedOrderMap.get(orderId).put(order);
        log.info(String.format("Order prepared: %s", order.getId()));
    }
}
