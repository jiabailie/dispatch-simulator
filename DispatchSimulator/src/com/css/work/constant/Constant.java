package com.css.work.constant;

import lombok.AccessLevel;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {

    public static final int GENERATE_ORDERS_PER_SECOND = 2;

    public static final int GENERATE_ORDERS_SLEEP_ELAPSE = 1000;

    public static final int THREAD_POOL_CORE_SIZE = 128;

    public static final int THREAD_POOL_MAX_SIZE = 128;

    public static final int THREAD_POOL_KEEP_ALIVE_TIME = 200;

    public static final int SCALE_MILLI_SECOND = 1000;

    public static final int COURIER_ARRIVAL_MIN = 3;

    public static final int COURIER_ARRIVAL_MAX = 15;
}
