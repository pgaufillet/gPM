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
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;

/**
 * Abstract migration consumer
 * 
 * @author nveillet
 * @param <P>
 *            the migration properties
 */
public abstract class MigrationConsumer<P extends MigrationProperties>
        implements Runnable {

    private final Class<P> classProperties;

    private MigrationOptions options;

    private MigrationTaskManager taskManager;

    private Thread thread;

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     * @param pClassProperties
     *            the task properties class
     */
    public MigrationConsumer(final MigrationTaskManager pTaskManager,
            final MigrationOptions pOptions, final Class<P> pClassProperties) {
        taskManager = pTaskManager;
        options = pOptions;

        classProperties = pClassProperties;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Execute task
     * 
     * @param pProperties
     *            the task properties
     * @throws ImportException
     *             the import exception
     * @throws SchemaValidationException
     *             the schema validation exception
     */
    protected abstract void execute(final P pProperties)
        throws SchemaValidationException, ImportException;

    /**
     * get options
     * 
     * @return the options
     */
    public MigrationOptions getOptions() {
        return options;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Runnable#run()
     */
    @SuppressWarnings("unchecked")
    public void run() {
        while (true) {
            MigrationProperties lProperties;

            try {
                lProperties = taskManager.getTask();
            }
            catch (InterruptedException e) {
                throw new GDMException(e);
            }

            // End of Task
            if (lProperties instanceof MigrationStopProperties) {
                break;
            }
            else if (classProperties.isAssignableFrom(lProperties.getClass())) {
                Exception lException = null;

                try {
                    execute((P) lProperties);
                }
                catch (SchemaValidationException e) {
                    lException = e;
                }
                catch (ImportException e) {
                    lException = e;
                }
                catch (RuntimeException e) {
                    lException = e;
                }

                taskManager.terminateTask(new MigrationLog(lProperties,
                        lException));
            }
            else {
                throw new GDMException("Bad properties.");
            }
        }
    }
}
