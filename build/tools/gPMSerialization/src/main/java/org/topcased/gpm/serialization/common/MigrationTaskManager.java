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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Migration task manager
 * 
 * @author nveillet
 */
public class MigrationTaskManager {

    private List<MigrationProperties> inProgressTasks;

    private List<MigrationLog> terminatedTasks;

    private LinkedBlockingQueue<MigrationProperties> waitingTasks;

    /**
     * Constructor
     */
    public MigrationTaskManager() {
        waitingTasks =
                new LinkedBlockingQueue<MigrationProperties>(Integer.MAX_VALUE);

        inProgressTasks =
                Collections.synchronizedList(new ArrayList<MigrationProperties>());

        terminatedTasks =
                Collections.synchronizedList(new ArrayList<MigrationLog>());
    }

    /**
     * Add a task into the queue
     * 
     * @param pTask
     *            the task
     * @throws InterruptedException
     *             thread interrupted exception
     */
    public void addTask(final MigrationProperties pTask)
        throws InterruptedException {
        waitingTasks.put(pTask);
    }

    /**
     * Get next task
     * 
     * @return the task
     * @throws InterruptedException
     *             thread interrupted exception
     */
    public MigrationProperties getTask() throws InterruptedException {
        MigrationProperties lTask = waitingTasks.take();

        if (lTask instanceof MigrationStopProperties) {
            waitingTasks.add(lTask);
        }
        else {
            inProgressTasks.add(lTask);
        }

        return lTask;
    }

    /**
     * get terminated tasks
     * 
     * @return the terminated tasks
     */
    public List<MigrationLog> getTerminatedTasks() {
        return terminatedTasks;
    }

    /**
     * Register the end of task
     * 
     * @param pLog
     *            the log
     */
    public void terminateTask(final MigrationLog pLog) {
        inProgressTasks.remove(pLog.getTask());
        terminatedTasks.add(pLog);
    }
}
