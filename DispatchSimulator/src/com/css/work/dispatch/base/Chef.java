package com.css.work.dispatch.base;

/**
 * Base class of Chef for all strategies
 */
public abstract class Chef implements Runnable {
    @Override
    public void run() {
        prepare();
    }

    public abstract void prepare();
}
