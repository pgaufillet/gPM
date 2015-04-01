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

import org.apache.log4j.Logger;
import org.topcased.gpm.business.ContextLocator;

/**
 * The Performance Analyzer is a singleton that can be call to analyze code
 * parts. Each part can be analyzed using the startAnalyze and endAnalyze
 * method. This analyzer is recursive and can analyzed imbricated parts. All
 * level is associated to each code part and this level can be set on
 * config.properies file.
 * 
 * @author tpanuel
 */
public final class PerformanceAnalyzer {
    /** Level for HTTP request */
    public static final int HTTP_REQUEST_LEVEL = 1;

    /** Level for HMI action */
    public static final int UI_ACTION_LEVEL = 2;

    /** Level for business service */
    public static final int BUSINESS_SERVICE_LEVEL = 3;

    /** Level for extension points */
    public static final int EXTENSION_POINT_LEVEL = 4;

    /** Level for domain DAO */
    public static final int DOMAIN_DAO_LEVEL = 5;

    private static final Logger LOGGER =
            Logger.getLogger(PerformanceAnalyzer.class);

    private final ThreadLocal<PerformanceLog> performanceLog =
            new ThreadLocal<PerformanceLog>();

    private int performanceAnalyzeLevel;

    /**
     * Setter on the analyzer level property for Spring injection.
     * 
     * @param pPerformanceAnalyzeLevel
     *            The analyze level property.
     */
    public void setPerformanceAnalyzeLevel(final int pPerformanceAnalyzeLevel) {
        performanceAnalyzeLevel = pPerformanceAnalyzeLevel;
    }

    private static PerformanceAnalyzer staticInstance = null;

    /**
     * Access to the singleton for class that not used the Spring injection.
     * 
     * @return The singleton.
     */
    public final static PerformanceAnalyzer getInstance() {
        if (staticInstance == null) {
            staticInstance =
                    (PerformanceAnalyzer) ContextLocator.getContext().getBean(
                            "performanceAnalyzer");
        }

        return staticInstance;
    }

    /**
     * Start an analyze.
     * 
     * @param pElementName
     *            The name of the element whose the analyze is start.
     * @param pLevel
     *            The level.
     */
    public void startAnalyze(final String pElementName, final int pLevel) {
        if (isEnabled(pLevel)) {
            // A performance log by thread
            PerformanceLog lPerformanceLog = performanceLog.get();

            if (lPerformanceLog == null) {
                lPerformanceLog = new PerformanceLog(0);
                performanceLog.set(lPerformanceLog);
            }
            lPerformanceLog.start(pElementName);
        }
    }

    /**
     * End an analyze.
     * 
     * @param pLevel
     *            The level.
     */
    public void endAnalyze(final int pLevel) {
        if (isEnabled(pLevel)) {
            // A performance log by thread
            final PerformanceLog lPerformanceLog = performanceLog.get();

            lPerformanceLog.end();
            if (lPerformanceLog.isFinished()) {
                final StringBuffer lAnalyze = new StringBuffer();

                // Display the result using the logger
                for (String lMessage : lPerformanceLog.getAnalyzeResult()) {
                    lAnalyze.append("\n\t");
                    lAnalyze.append(lMessage);
                }
                LOGGER.debug(lAnalyze.toString());
            }
        }
    }

    /**
     * Test if the logger is enable for a specific level.
     * 
     * @param pLevel
     *            The level to test.
     * @return If the logger is enable for a specific level.
     */
    public boolean isEnabled(final int pLevel) {
        return LOGGER.isDebugEnabled() && pLevel <= performanceAnalyzeLevel;
    }
}