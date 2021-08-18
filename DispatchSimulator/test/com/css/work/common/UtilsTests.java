package com.css.work.common;

import static com.css.work.constant.Constant.COURIER_ARRIVAL_MIN;
import static com.css.work.constant.Constant.COURIER_ARRIVAL_MAX;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.css.work.exception.SimulatorException;
import com.css.work.model.Order;
import com.css.work.model.Request;
import com.css.work.model.SimulateMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class UtilsTests {
    private final Utils utils;

    public UtilsTests() {
        this.utils = Utils.builder().mapper(new ObjectMapper()).build();
    }
    @Test
    public void dateToFormatStringTests() {
        final Date date = new Date(1629095723043L);
        final String actualDateStr = utils.dateToFormatString(date);
        final String expectedDateStr = "2021-08-16 14:35:23";
        assertThat(actualDateStr).isEqualTo(expectedDateStr);
    }

    @Test
    public void getCourierArrivalTimeTests() {
        final int arrivalTime = utils.getCourierArrivalTime();
        assertThat(arrivalTime).isGreaterThanOrEqualTo(COURIER_ARRIVAL_MIN);
        assertThat(arrivalTime).isLessThanOrEqualTo(COURIER_ARRIVAL_MAX);
    }

    @Test
    public void getRequestTests() throws SimulatorException {
        final String invalidPath = "invalid/path";
        assertThrows(SimulatorException.class, () -> {
            utils.getRequest(invalidPath);
        });

        final String invalidJsonPath = "resources/test_invalid_request.json";
        assertThrows(SimulatorException.class, () -> {
            utils.getRequest(invalidJsonPath);
        });

        final String validPath = "resources/request.json";
        final Request request = utils.getRequest(validPath);

        assertThat(request).isNotNull();
        assertThat(request.getMode()).isEqualTo(SimulateMode.FIFO);
    }

    @Test
    public void getOrderListTests() throws SimulatorException {
        final String invalidPath = "invalid/path";
        assertThrows(SimulatorException.class, () -> {
            utils.getOrderList(invalidPath);
        });

        final String invalidJsonPath = "resources/test_invalid_orders.json";
        assertThrows(SimulatorException.class, () -> {
            utils.getOrderList(invalidJsonPath);
        });

        final String validPath = "resources/dispatch_orders_min.json";
        final List<Order> orders = utils.getOrderList(validPath);

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
    }
}
