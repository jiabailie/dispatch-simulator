package com.css.work.module;

import static com.css.work.constant.Constant.THREAD_POOL_CORE_SIZE;
import static com.css.work.constant.Constant.THREAD_POOL_KEEP_ALIVE_TIME;
import static com.css.work.constant.Constant.THREAD_POOL_MAX_SIZE;

import com.css.work.common.Utils;
import com.css.work.dispatch.Application;
import com.css.work.dispatch.Controller;
import com.css.work.dispatch.Analyst;
import com.css.work.dispatch.base.Strategy;
import com.css.work.dispatch.fifo.FifoImpl;
import com.css.work.dispatch.matched.MatchedImpl;
import com.css.work.exception.SimulatorException;
import com.css.work.model.Deliveryman;
import com.css.work.model.Order;

import com.css.work.model.Request;
import com.css.work.model.SimulateMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import jdk.jshell.execution.Util;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

@Log4j
public class ApplicationModule extends AbstractModule {

    private final String[] args;
    private final BlockingQueue<Order> analysisOrderQueue;
    private final BlockingQueue<Deliveryman> arrivalCourierQueue;
    private final Map<String, BlockingQueue<Order>> preparedOrderMap;
    private final ThreadPoolExecutor threadPoolExecutor;

    public ApplicationModule(final String[] args) throws SimulatorException {
        log.info("Start to parse request and initialize the system.");
        if (args.length == 0) {
            throw new SimulatorException("Please input the path of request json!");
        }

        this.args = args;
        this.analysisOrderQueue = new LinkedBlockingDeque<>();
        this.arrivalCourierQueue = new LinkedBlockingDeque<>();
        this.preparedOrderMap = new ConcurrentHashMap<>();
        this.threadPoolExecutor = new ThreadPoolExecutor(THREAD_POOL_CORE_SIZE,
                THREAD_POOL_MAX_SIZE,
                THREAD_POOL_KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(THREAD_POOL_CORE_SIZE));
    }

    @Override
    protected void configure() {}

    @Inject
    @Provides
    @Singleton
    public Utils provideUtils() {
        return Utils.builder().mapper(new ObjectMapper()).build();
    }

    @Inject
    @Provides
    public Request provideRequest(final Utils utils) throws SimulatorException {
        return utils.getRequest(args[0]);
    }

    @Inject
    @Provides
    public List<Order> provideOrders(final Utils utils,
                                     final Request request) throws SimulatorException {
        return utils.getOrderList(request.getOrderJsonPath());
    }

    @Inject
    @Provides
    @Singleton
    public Strategy provideStrategy(final Request request,
                                    final List<Order> orders,
                                    final Utils utils,
                                    final Analyst analyst) throws SimulatorException {
        final SimulateMode mode = request.getMode();
        if (mode == SimulateMode.Matched) {
            return new MatchedImpl(utils, analyst, orders, threadPoolExecutor, analysisOrderQueue, preparedOrderMap);
        } else if(mode == SimulateMode.FIFO) {
            return new FifoImpl(utils, analyst, orders, threadPoolExecutor, analysisOrderQueue, arrivalCourierQueue);
        }

        throw new SimulatorException("Encounter unknown simulate mode!");
    }

    @Inject
    @Provides
    @Singleton
    public Controller provideController(final Strategy strategyImpl) {
        return Controller
                .builder()
                .strategyImpl(strategyImpl)
                .threadPoolExecutor(threadPoolExecutor)
                .build();
    }

    @Inject
    @Provides
    @Singleton
    public Analyst provideAnalyst(final List<Order> orders) {
        return Analyst
                .builder()
                .orderSize(orders.size())
                .analysisOrderQueue(analysisOrderQueue)
                .build();
    }

    @Inject
    @Provides
    @Singleton
    public Application provideApplication(final Controller controller) {
        return Application.builder().controller(controller).build();
    }
}