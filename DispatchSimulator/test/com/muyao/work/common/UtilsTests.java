package com.muyao.work.common;

import static com.muyao.work.constant.Constant.COURIER_ARRIVAL_MIN;
import static com.muyao.work.constant.Constant.COURIER_ARRIVAL_MAX;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.muyao.work.exception.SimulatorException;
import com.muyao.work.model.Order;
import com.muyao.work.model.Request;
import com.muyao.work.model.SimulateMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class UtilsTests {
    private final Utils utils;

    public UtilsTests() {
        this.utils = Utils.builder().mapper(new ObjectMapper()).build();
    }
    @Test
    public void dateToFormatStringTests() {
        final Date date = new Date(1629095723043L);
        final String actualDateStr = utils.dateToFormatString(date);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getDefault());
        final String expectedDateStr = dateFormat.format(date);
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

        final String invalidJsonPath = "DispatchSimulator/resources/test_invalid_request.json";
        assertThrows(SimulatorException.class, () -> {
            utils.getRequest(invalidJsonPath);
        });

        final String validPath = "DispatchSimulator/resources/request.json";
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

        final String invalidJsonPath = "DispatchSimulator/resources/test_invalid_orders.json";
        assertThrows(SimulatorException.class, () -> {
            utils.getOrderList(invalidJsonPath);
        });

        final String validPath = "DispatchSimulator/resources/dispatch_orders_min.json";
        final List<Order> orders = utils.getOrderList(validPath);

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
    }
}
