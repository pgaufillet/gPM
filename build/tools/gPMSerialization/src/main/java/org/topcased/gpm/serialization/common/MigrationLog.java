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

import org.apache.log4j.Logger;

/**
 * Migration log
 * 
 * @author nveillet
 */
public class MigrationLog {

    private Exception exception;

    private final Logger logger = Logger.getLogger(MigrationLog.class);

    private MigrationProperties task;

    /**
     * Constructor
     * 
     * @param pTask
     *            the task
     */
    public MigrationLog(MigrationProperties pTask) {
        this(pTask, null);
    }

    /**
     * Constructor
     * 
     * @param pTask
     *            the task
     * @param pException
     *            the exception
     */
    public MigrationLog(MigrationProperties pTask, Exception pException) {
        task = pTask;
        exception = pException;
    }

    /**
     * get exception
     * 
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * get task
     * 
     * @return the task
     */
    public MigrationProperties getTask() {
        return task;
    }

    /**
     * Log exception stack trace
     */
    public void printStackTrace() {
        for (StackTraceElement lElement : getException().getStackTrace()) {
            logger.debug(lElement);
        }
    }

    /**
     * set exception
     * 
     * @param pException
     *            the exception to set
     */
    public void setException(final Exception pException) {
        exception = pException;
    }

    /**
     * set task
     * 
     * @param pTask
     *            the task to set
     */
    public void setTask(final MigrationProperties pTask) {
        task = pTask;
    }
}
