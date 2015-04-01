/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.performance;

import java.util.LinkedList;
import java.util.List;

/**
 * Log used to analyze the performance of a code part.
 * 
 * @author tpanuel
 */
public class PerformanceLog {
    private final List<PerformanceLog> subLogs;

    private final int depth;

    private String actionName;

    private PerformanceLog currentSubLog;

    private long beginTime;

    private long endTime;

    private boolean started;

    private boolean finished;

    /**
     * Create a log specifying the depth of the code part call.
     * 
     * @param pDepth
     *            The depth of the code part call.
     */
    public PerformanceLog(int pDepth) {
        subLogs = new LinkedList<PerformanceLog>();
        depth = pDepth;
        started = false;
        finished = false;
        currentSubLog = null;
    }

    /**
     * Start the analyze.
     * 
     * @param pActionName
     *            The action name.
     */
    public void start(String pActionName) {
        if (started && !finished) {
            if (currentSubLog == null || currentSubLog.finished) {
                currentSubLog = new PerformanceLog(depth + 1);
                subLogs.add(currentSubLog);
            }
            currentSubLog.start(pActionName);
        }
        else {
            actionName = pActionName;
            started = true;
            finished = false;
            subLogs.clear();
            beginTime = System.currentTimeMillis();
        }
    }

    /**
     * End the analyze.
     */
    public void end() {
        if (currentSubLog == null || currentSubLog.finished) {
            finished = true;
            endTime = System.currentTimeMillis();
        }
        else {
            currentSubLog.end();
        }
    }

    /**
     * Get the analyze result.
     * 
     * @return The analyze result.
     */
    public List<String> getAnalyzeResult() {
        final List<String> lAnalyseResult = new LinkedList<String>();

        if (subLogs.isEmpty()) {
            lAnalyseResult.add(getMessage(actionName + " in "
                    + (endTime - beginTime) + " ms"));
        }
        else {
            lAnalyseResult.add(getMessage("Begin " + actionName));
            for (PerformanceLog lSubLog : subLogs) {
                lAnalyseResult.addAll(lSubLog.getAnalyzeResult());
            }
            lAnalyseResult.add(getMessage("End " + actionName + " in "
                    + (endTime - beginTime) + " ms"));
        }

        return lAnalyseResult;
    }

    private String getMessage(final String pMessage) {
        final StringBuffer lBuffer = new StringBuffer();

        for (int i = 0; i < depth; i++) {
            lBuffer.append("\t");
        }
        lBuffer.append(pMessage);

        return lBuffer.toString();
    }

    /**
     * Test if analyze is started.
     * 
     * @return If analyze is started.
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Test if analyze is finished.
     * 
     * @return If analyze is finished.
     */
    public boolean isFinished() {
        return finished;
    }
}