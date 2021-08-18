package com.css.work.dispatch.fifo;

import com.css.work.dispatch.base.Chef;
import com.css.work.model.Deliveryman;
import com.css.work.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.css.work.constant.Constant.SCALE_MILLI_SECOND;

/**
 * Chef for FIFO strategy
 */
@Log4j
@Builder
@AllArgsConstructor
public class FifoChef extends Chef {

    private final Order order;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final BlockingQueue<Deliveryman> arrivalCourierQueue;

    @SneakyThrows
    @Override
    public void prepare() {
        // Chef prepare the order
        Thread.sleep(order.getPrepTime() * SCALE_MILLI_SECOND);

        // Update order prepared time
        order.setOrderPreparedTime(new Date());

        log.info("Order prepared");

        // For fifo strategy
        // The earliest arrival courier picks up the next available order
        final Deliveryman courier = arrivalCourierQueue.take();

        // Set order's CourierArrivalTime and CourierPickupTime
        order.setCourierArrivalTime(courier.getArrivalTime());
        order.setCourierPickupTime(new Date());

        log.info("Order picked up");

        // Put order to analysis queue
        analysisOrderQueue.put(order);
    }
}
