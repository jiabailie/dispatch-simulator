package com.css.work.dispatch.base;

import static com.css.work.constant.Constant.GENERATE_ORDERS_PER_SECOND;
import static com.css.work.constant.Constant.GENERATE_ORDERS_SLEEP_ELAPSE;

import com.css.work.dispatch.Analyst;
import com.css.work.model.Order;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Base class for all strategies, it contains the function to generate orders
 */
@Log4j
@AllArgsConstructor
public abstract class Strategy implements Runnable {

    protected final Analyst analyst;
    protected final List<Order> orders;
    protected final ThreadPoolExecutor threadPoolExecutor;

    @SneakyThrows
    @Override
    public void run() {
        log.info("Start Order Generator...");

        // Start the data analyst
        threadPoolExecutor.execute(analyst);

        for (int i = 0; i < orders.size(); i += GENERATE_ORDERS_PER_SECOND) {
            for(int j = i; j < i + GENERATE_ORDERS_PER_SECOND && j < orders.size(); ++j) {
                simulate(orders.get(j));
            }

            // 2 orders per second
            Thread.sleep(GENERATE_ORDERS_SLEEP_ELAPSE);
        }

        // Shutdown thread pool
        threadPoolExecutor.shutdown();
    }

    public abstract void simulate(@NonNull final Order order);
}
