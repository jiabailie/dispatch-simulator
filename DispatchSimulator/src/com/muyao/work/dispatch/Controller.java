package com.muyao.work.dispatch;

import com.muyao.work.dispatch.base.Strategy;
import com.muyao.work.exception.SimulatorException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Simulate mode controller of Dispatch Simulator
 */
@Log4j
@Builder
@AllArgsConstructor
public class Controller {

    private final Strategy strategyImpl;
    private final ThreadPoolExecutor threadPoolExecutor;

    public void start() throws SimulatorException {
        try {
            threadPoolExecutor.execute(strategyImpl);
        } catch (Exception e) {
            throw new SimulatorException(e.getMessage());
        }
    }
}
