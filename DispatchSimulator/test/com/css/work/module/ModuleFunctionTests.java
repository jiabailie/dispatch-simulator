package com.css.work.module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.css.work.common.Utils;
import com.css.work.dispatch.Analyst;
import com.css.work.dispatch.Application;
import com.css.work.dispatch.Controller;
import com.css.work.dispatch.base.Strategy;
import com.css.work.dispatch.fifo.FifoImpl;
import com.css.work.dispatch.matched.MatchedImpl;
import com.css.work.exception.SimulatorException;
import com.css.work.model.Order;
import com.css.work.model.Request;
import com.css.work.model.SimulateMode;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ModuleFunctionTests {

    private final ApplicationModule applicationModule;

    public ModuleFunctionTests() throws SimulatorException {
        final String[] args = new String[] { "resources/request.json" };
        applicationModule = new ApplicationModule(args);
    }

    @Test
    public void ApplicationModuleTests() throws SimulatorException {
        assertThrows(SimulatorException.class, () -> {
            final String[] blankArgs = new String[] {};
            final ApplicationModule invalidModule = new ApplicationModule(blankArgs);
        });

        final Utils utils = applicationModule.provideUtils();
        final Request request = applicationModule.provideRequest(utils);
        final List<Order> orders = applicationModule.provideOrders(utils, request);
        final Analyst analyst = applicationModule.provideAnalyst(orders);

        request.setMode(SimulateMode.Matched);
        final Strategy matchedStrategy = applicationModule.provideStrategy(request, orders, utils, analyst);
        assertThat(matchedStrategy.getClass()).isEqualTo(MatchedImpl.class);

        request.setMode(SimulateMode.FIFO);
        final Strategy fifoStrategy = applicationModule.provideStrategy(request, orders, utils, analyst);
        assertThat(fifoStrategy.getClass()).isEqualTo(FifoImpl.class);

        request.setMode(SimulateMode.Undefined);
        assertThrows(SimulatorException.class, () -> {
            final Strategy invalidStrategy = applicationModule.provideStrategy(request, orders, utils, analyst);
        });

        final Controller controller = applicationModule.provideController(fifoStrategy);

        final Application application = applicationModule.provideApplication(controller);
    }
}
