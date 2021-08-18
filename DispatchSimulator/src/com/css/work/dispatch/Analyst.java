package com.css.work.dispatch;

import com.css.work.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.BlockingQueue;

/**
 * Analyst for all strategies
 * Average food wait time (ms) = Average time between order ready and pickup
 * Average courier wait time (ms) = Average time between courier arrival and order pickup
 */
@Log4j
@Data
@Builder
@AllArgsConstructor
public class Analyst implements Runnable {

    private final int orderSize;
    private final BlockingQueue<Order> analysisOrderQueue;

    @SneakyThrows
    @Override
    public void run() {
        int processedOrderAmount = 0;
        long totalFoodWaitTime = 0;
        long totalCourierWaitTime = 0;
        log.info("Start result analyst...");

        while (true) {
            final Order order = analysisOrderQueue.take();
            processedOrderAmount += 1;
            totalFoodWaitTime += order.getFoodWaitTime();
            totalCourierWaitTime += order.getCourierWaitTime();

            // Print Food Wait Time and Courier Wait Time for each picked up order
            final String strategyStatistics = "\n" +
                    "=================Strategy Statistics=================\n" +
                    "Amount of Picked up Orders = " + processedOrderAmount + "\n" +
                    "Food Wait Time = " + order.getFoodWaitTime() + " ms\n" +
                    "Courier Wait Time = " + order.getCourierWaitTime() + " ms\n" +
                    "=====================================================";
            log.info(strategyStatistics);

            if (processedOrderAmount == orderSize) {

                // Print out the average Food Wait Time and average Courier Wait Time
                final long avgFoodWaitTime = totalFoodWaitTime / orderSize;
                final long avgCourierWaitTime = totalCourierWaitTime / orderSize;

                final String avgStrategyStatistics = "\n" +
                        "=================Strategy Statistics=================\n" +
                        "Amount of Picked up Orders = " + processedOrderAmount + "\n" +
                        "Average Food Wait Time = " + avgFoodWaitTime + " ms\n" +
                        "Average Courier Wait Time = " + avgCourierWaitTime + " ms\n" +
                        "=====================================================";
                log.info(avgStrategyStatistics);

                break;
            }
        }
    }
}
