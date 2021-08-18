package com.css.work.dispatch.fifo;

import com.css.work.common.Utils;
import com.css.work.dispatch.Analyst;
import com.css.work.dispatch.base.Chef;
import com.css.work.dispatch.base.Courier;
import com.css.work.dispatch.base.Strategy;
import com.css.work.model.Deliveryman;
import com.css.work.model.Order;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * First-in-first-out strategy implementation
 */
@Log4j
public class FifoImpl extends Strategy {

    private final Utils utils;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final BlockingQueue<Deliveryman> arrivalCourierQueue;

    public FifoImpl(@NonNull final Utils utils,
                    @NonNull final Analyst analyst,
                    @NonNull final List<Order> orders,
                    @NonNull final ThreadPoolExecutor threadPoolExecutor,
                    @NonNull final BlockingQueue<Order> analysisOrderQueue,
                    @NonNull final BlockingQueue<Deliveryman> arrivalCourierQueue) {
        super(analyst, orders, threadPoolExecutor);

        this.utils = utils;
        this.threadPoolExecutor = threadPoolExecutor;
        this.analysisOrderQueue = analysisOrderQueue;
        this.arrivalCourierQueue = arrivalCourierQueue;
    }

    @Override
    public void simulate(Order order) {
        order.setOrderCreateTime(new Date());
        log.info(String.format("Order received: %s", order.getId()));

        // Assign chef to prepare this order
        final Chef chef = FifoChef.builder()
                .order(order)
                .analysisOrderQueue(analysisOrderQueue)
                .arrivalCourierQueue(arrivalCourierQueue)
                .build();
        threadPoolExecutor.execute(chef);

        // Assign courier to pick up this order
        final Courier courier = FifoCourier.builder()
                .utils(utils)
                .arrivalCourierQueue(arrivalCourierQueue)
                .build();
        threadPoolExecutor.execute(courier);
    }
}
