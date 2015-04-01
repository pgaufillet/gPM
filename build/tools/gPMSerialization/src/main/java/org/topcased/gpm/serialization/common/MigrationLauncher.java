/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.common;

import org.topcased.gpm.business.exception.GDMException;

/**
 * Migration launcher
 * 
 * @author nveillet
 * @param <P>
 *            the migration producer
 * @param <C>
 *            the migration consumer
 */
public abstract class MigrationLauncher<P extends MigrationProducer, C extends MigrationConsumer<?>> {

    private static final int SLEEP_TIME = 1000;

    private MigrationOptions options;

    private P producer;

    /**
     * Constructor
     * 
     * @param pArgs
     *            Arguments
     */
    protected MigrationLauncher(String[] pArgs) {
        options = new MigrationOptions(pArgs);
        launchThreads();

        while (producer.thread.isAlive()) {
            // wait end
            try {
                Thread.sleep(SLEEP_TIME);
            }
            catch (InterruptedException e) {
                throw new GDMException(e);
            }
        }
    }

    /**
     * Create a consumer
     * 
     * @param pTaskManager
     *            the task manager
     * @return the consumer
     */
    protected abstract C createConsumer(final MigrationTaskManager pTaskManager);

    /**
     * Create the producer
     * 
     * @param pTaskManager
     *            the task manager
     * @return the producer
     */
    protected abstract P createProducer(final MigrationTaskManager pTaskManager);

    /**
     * get options
     * 
     * @return the options
     */
    protected MigrationOptions getOptions() {
        return options;
    }

    /**
     * Launch producer and consumers threads
     * 
     * @param pConsumerCount
     *            numbers of consumers
     */
    private void launchThreads() {

        MigrationTaskManager lTaskManager = new MigrationTaskManager();

        // Create Producer
        producer = createProducer(lTaskManager);

        // Create Consumer
        for (int i = 0; i < options.getConsumerCount(); i++) {
            createConsumer(lTaskManager);
        }
    }
}
