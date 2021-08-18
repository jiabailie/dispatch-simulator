package com.css.work.dispatch.fifo;

import com.css.work.common.Utils;
import com.css.work.dispatch.base.Courier;
import com.css.work.model.Deliveryman;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.css.work.constant.Constant.SCALE_MILLI_SECOND;

/**
 * Courier for FIFO strategy
 */
@Log4j
@Builder
@AllArgsConstructor
public class FifoCourier extends Courier {

    private final Utils utils;
    private final BlockingQueue<Deliveryman> arrivalCourierQueue;

    @SneakyThrows
    @Override
    public void send() {
        // Simulate Courier pick up order process
        // Upon receiving an order, the system should immediately dispatch a courier to pick it up.
        // Couriers arrive randomly following a uniform distribution,
        // 3-15 seconds after they’ve been dispatched.
        log.info("Courier dispatched");

        // Simulate the process of courier arrival
        final int arrivalTime = utils.getCourierArrivalTime();
        Thread.sleep(arrivalTime * SCALE_MILLI_SECOND);

        final Deliveryman courier =
                Deliveryman.builder().arrivalTime(new Date()).build();
        log.info("Courier arrived");

        // FIFO will assign order to the earliest arrival courier
        // thus put the arrival courier into one queue
        arrivalCourierQueue.put(courier);
    }
}
