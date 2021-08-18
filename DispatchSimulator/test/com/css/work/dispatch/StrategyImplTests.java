package com.css.work.dispatch;

import static com.css.work.constant.Constant.THREAD_POOL_CORE_SIZE;
import static com.css.work.constant.Constant.THREAD_POOL_KEEP_ALIVE_TIME;
import static com.css.work.constant.Constant.THREAD_POOL_MAX_SIZE;

import com.css.work.common.Utils;
import com.css.work.dispatch.base.Strategy;
import com.css.work.dispatch.fifo.FifoImpl;
import com.css.work.dispatch.matched.MatchedImpl;
import com.css.work.exception.SimulatorException;
import com.css.work.model.Deliveryman;
import com.css.work.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class StrategyImplTests {

    private static final String ORDER_JSON_PATH = "resources/dispatch_orders_min.json";
    private static final int DOWNLATCH_COUNT_DOWN = 2;
    private static final int DOWNLATCH_AWAIT_TIME = 20;

    private final Utils utils;
    private final List<Order> orders;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final Analyst analyst;
    private final ThreadPoolExecutor threadPoolExecutor;
    private final CountDownLatch downLatch;

    public StrategyImplTests() throws Exception {
        this.utils = Utils.builder().mapper(new ObjectMapper()).build();
        this.orders = utils.getOrderList(ORDER_JSON_PATH);

        this.analysisOrderQueue = new LinkedBlockingDeque<>();
        this.analyst = Analyst
                .builder()
                .orderSize(orders.size())
                .analysisOrderQueue(analysisOrderQueue)
                .build();
        this.threadPoolExecutor = new ThreadPoolExecutor(
                THREAD_POOL_CORE_SIZE,
                THREAD_POOL_MAX_SIZE,
                THREAD_POOL_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(THREAD_POOL_CORE_SIZE));

        this.downLatch = new CountDownLatch(DOWNLATCH_COUNT_DOWN);
    }

    @Test
    public void matchedImplTests() throws Exception {
        final Map<String, BlockingQueue<Order>> preparedOrderMap = new ConcurrentHashMap<>();
        final Strategy matchedImpl =
                new MatchedImpl(utils, analyst, orders, threadPoolExecutor, analysisOrderQueue, preparedOrderMap);

        final Controller controller = new Controller(matchedImpl, threadPoolExecutor);
        controller.start();

        downLatch.await(DOWNLATCH_AWAIT_TIME, TimeUnit.SECONDS);
    }

    @Test
    public void fifoImplTests() throws SimulatorException, InterruptedException {
        final BlockingQueue<Deliveryman> arrivalCourierQueue = new LinkedBlockingDeque<>();
        final Strategy fifoImpl =
                new FifoImpl(utils, analyst, orders, threadPoolExecutor, analysisOrderQueue, arrivalCourierQueue);

        final Controller controller = new Controller(fifoImpl, threadPoolExecutor);
        controller.start();

        downLatch.await(DOWNLATCH_AWAIT_TIME, TimeUnit.SECONDS);
    }
}
