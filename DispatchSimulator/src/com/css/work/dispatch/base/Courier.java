package com.css.work.dispatch.base;

/**
 * Base class of Courier for all strategies
 */
public abstract class Courier implements Runnable {
    @Override
    public void run() {
        send();
    }

    public abstract void send();
}
