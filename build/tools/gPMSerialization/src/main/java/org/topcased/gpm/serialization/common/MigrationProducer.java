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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.topcased.gpm.business.exception.GDMException;

/**
 * Abstract migration producer
 * 
 * @author nveillet
 */
public abstract class MigrationProducer implements Runnable {

    public final static List<MigrationElementType> ELEMENTS_TYPE;

    protected static final int SLEEP_TIME = 500;

    static {
        ELEMENTS_TYPE = new ArrayList<MigrationElementType>();
    }

    private boolean error = false;

    private final Logger logger = Logger.getLogger(MigrationProducer.class);

    private MigrationOptions options;

    private MigrationTaskManager taskManager;

    protected Thread thread;

    /**
     * Constructor
     * 
     * @param pTaskManager
     *            the task manager
     * @param pOptions
     *            the options
     */
    public MigrationProducer(MigrationTaskManager pTaskManager,
            MigrationOptions pOptions) {
        taskManager = pTaskManager;

        options = pOptions;

        thread = new Thread(this);
        thread.start();

    }

    /**
     * Execute migration
     * 
     * @throws InterruptedException
     *             thread interrupted exception
     */
    protected abstract void execute() throws InterruptedException;

    /**
     * get options
     * 
     * @return the options
     */
    public MigrationOptions getOptions() {
        return options;
    }

    /**
     * get task manager
     * 
     * @return the task manager
     */
    public MigrationTaskManager getTaskManager() {
        return taskManager;
    }

    /**
     * get if the migration is in error
     * 
     * @return if the migration is in error
     */
    public boolean isError() {
        return error;
    }

    /**
     * Get if the migration must stop after error
     * 
     * @return if the migration must stop after error
     */
    protected abstract boolean isErrorStop();

    /**
     * Log the header
     * 
     * @param pActionName
     *            the action name
     */
    protected void logHeader(final String pActionName) {
        logger.info("****************************************");
        logger.info(pActionName);
        logger.info("Process : " + getOptions().getProcessName());
        logger.info("User : " + getOptions().getUserLogin());
        logger.info("Date : " + new Date(System.currentTimeMillis()).toString());
        logger.info("****************************************");
        logger.info("");
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            execute();
        }
        catch (InterruptedException e) {
            throw new GDMException(e);
        }
    }

    /**
     * wait the end of step and show logs
     * 
     * @param pElementType
     *            the element type
     * @param pPackageNumber
     *            the package number
     * @throws InterruptedException
     *             thread interrupted exception
     */
    protected void waitLogs(final MigrationElementType pElementType,
            final int pPackageNumber) throws InterruptedException {
        int lTerminatedTaskNumber = 0;
        int lErrorTackNumber = 0;

        while (pPackageNumber != lTerminatedTaskNumber) {
            if (getTaskManager().getTerminatedTasks().size() > lTerminatedTaskNumber) {
                MigrationLog lLog =
                        getTaskManager().getTerminatedTasks().get(
                                lTerminatedTaskNumber);
                if (lLog.getException() != null) {
                    logger.info(lLog.getTask().getFileName() + " : KO - "
                            + lLog.getException().getLocalizedMessage());
                    lLog.printStackTrace();
                    lErrorTackNumber++;
                }
                else {
                    logger.info(lLog.getTask().getFileName() + " : OK");
                }
                lTerminatedTaskNumber++;
            }
            else {
                Thread.sleep(SLEEP_TIME);
            }
        }

        getTaskManager().getTerminatedTasks().clear();

        logger.info("----------------------------------------");
        String lHeader = pElementType.getName() + " : ";
        if (pPackageNumber == 0) {
            logger.info(lHeader + "CANCELLED");
        }
        else if (lErrorTackNumber > 0) {
            logger.info(lHeader + "KO (" + lErrorTackNumber + "/"
                    + pPackageNumber + " files in error)");
            if (isErrorStop()) {
                error = true;
            }
        }
        else {
            logger.info(lHeader + "OK (" + pPackageNumber + " files)");
        }
        logger.info("----------------------------------------");
        logger.info("");
    }
}
