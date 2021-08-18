package com.css.work.common;

import static com.css.work.constant.Constant.COURIER_ARRIVAL_MIN;
import static com.css.work.constant.Constant.COURIER_ARRIVAL_MAX;

import com.css.work.exception.SimulatorException;
import com.css.work.model.Order;
import com.css.work.model.Request;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import javax.inject.Inject;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class is used to implement reusable functions
 */
@Builder
@AllArgsConstructor
public class Utils {

    @Inject
    private final ObjectMapper mapper;

    // Deserialize json to Request
    public Request getRequest(@NonNull final String requestJsonPath) throws SimulatorException {
        final File requestJson = new File(requestJsonPath);
        if (!requestJson.exists()) {
            throw new SimulatorException("Please ensure to input the request json!");
        }

        try {
            final Request request = mapper.readValue(requestJson, Request.class);
            return request;
        } catch (Exception e) {
            throw new SimulatorException("Please check your input request!", e);
        }
    }

    // Deserialize json to order list
    public List<Order> getOrderList(@NonNull final String orderJsonPath) throws SimulatorException {
        final File orderJson = new File(orderJsonPath);
        if (!orderJson.exists()) {
            throw new SimulatorException("Please ensure to input the order json!");
        }

        try {
            final List<Order> orders = mapper.readValue(orderJson, new TypeReference<List<Order>>() {});
            return orders;
        } catch (Exception e) {
            throw new SimulatorException("Please check your input order!", e);
        }
    }

    public String dateToFormatString(final Date date) {
        final String pattern = "yyyy-MM-dd HH:mm:ss";
        final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    public int getCourierArrivalTime() {
        return (int)(Math.random() * (COURIER_ARRIVAL_MAX - COURIER_ARRIVAL_MIN + 1) + COURIER_ARRIVAL_MIN);
    }
}
