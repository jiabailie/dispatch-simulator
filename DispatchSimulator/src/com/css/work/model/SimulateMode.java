package com.css.work.model;

public enum SimulateMode {
    Undefined("Undefined"),
    Matched("Matched"),
    FIFO("FIFO");

    private String name;

    private SimulateMode(final String name) {
        this.name = name;
    }
}
