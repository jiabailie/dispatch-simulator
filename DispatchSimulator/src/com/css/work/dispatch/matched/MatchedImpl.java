package com.css.work.dispatch.matched;

import com.css.work.common.Utils;
import com.css.work.dispatch.Analyst;
import com.css.work.dispatch.base.Chef;
import com.css.work.dispatch.base.Courier;
import com.css.work.dispatch.base.Strategy;
import com.css.work.model.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;

/**
 * Matched strategy implementation
 */
@Log4j
public class MatchedImpl extends Strategy {

    private final Utils utils;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final Map<String, BlockingQueue<Order>> preparedOrderMap;

    public MatchedImpl(@NonNull final Utils utils,
                       @NonNull final Analyst analyst,
                       @NonNull final List<Order> orders,
                       @NonNull final ThreadPoolExecutor threadPoolExecutor,
                       @NonNull final BlockingQueue<Order> analysisOrderQueue,
                       @NonNull final Map<String, BlockingQueue<Order>> preparedOrderMap) {
        super(analyst, orders, threadPoolExecutor);

        this.utils = utils;
        this.threadPoolExecutor = threadPoolExecutor;
        this.analysisOrderQueue = analysisOrderQueue;
        this.preparedOrderMap = preparedOrderMap;
    }

    @Override
    public void simulate(@NonNull final Order order) {
        order.setOrderCreateTime(new Date());
        log.info(String.format("Order received: %s", order.getId()));

        // Assign chef to prepare this order
        final Chef chef = MatchedChef
                .builder()
                .order(order)
                .preparedOrderMap(preparedOrderMap)
                .build();
        threadPoolExecutor.execute(chef);

        // Assign courier to pick up this order
        final Courier courier = MatchedCourier
                .builder()
                .order(order)
                .utils(utils)
                .analysisOrderQueue(analysisOrderQueue)
                .preparedOrderMap(preparedOrderMap)
                .build();
        threadPoolExecutor.execute(courier);
    }
}
