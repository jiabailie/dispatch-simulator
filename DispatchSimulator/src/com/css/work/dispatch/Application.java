package com.css.work.dispatch;

import com.css.work.module.ApplicationModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.log4j.Log4j;

/**
 * Main class of Dispatch Simulator
 */
@Log4j
@Builder
@AllArgsConstructor
public class Application {

    @Inject
    private final Controller controller;

    public static void main(final String[] args) throws Exception {
        log.info("Start to run dispatch simulator.");
        Injector injector = Guice.createInjector(new ApplicationModule(args));

        Application app = injector.getInstance(Application.class);
        app.run();
    }

    public void run() throws Exception {
        controller.start();
    }
}
