package com.css.work.exception;

import lombok.extern.log4j.Log4j;

/**
 * Warp exceptions as SimulatorException
 */
@Log4j
public class SimulatorException extends Exception {

    public SimulatorException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }

    public SimulatorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        log.error(errorMessage);
    }
}
