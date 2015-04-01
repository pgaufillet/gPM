/***************************************************************
 * Copyright (c) 2013 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Cyril Marchive (Atos)
 ******************************************************************/
package org.topcased.gpm.business.util.log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 * GPM logger adapter using Log4J.
 * The adpater is used to include user name in message.
 * 
 * @author cmarchive
 */
public class GPMLogger
{

    /** Log4J logger instance */
    private Logger logger;

    /** GPM logger instance */
    private static GPMLogger instance;
    
    /** Log delimiter */
    private static final String LOG_DELIMITER = ";";
    
    /**
     * Create an GPM Logger instance
     *
     * @param pClass The name of clazz will be used as the name of the logger to retrieve
     * 
     * @return The GPM Logger instance
     */
    public synchronized static GPMLogger getLogger(Class<?> pClass) {
        Logger lLogger = Logger.getLogger(pClass);
        instance = new GPMLogger(lLogger);
        
        return instance;
    }
    
    /**
     * GPMLogger private constructor
     * 
     * @param pLogger The log4J logger to delegate
     */
    private GPMLogger(Logger pLogger) {
        logger = pLogger;
    }

    /**
     * Log a message object with the DEBUG level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void debug(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.debug(buildMessage(pUserName, pAction, pArgs));
    }

    /**
     * Log a message object with the ERROR Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void error(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.error(buildMessage(pUserName, pAction, pArgs));
    }

    /**
     * Log a message object with the FATAL Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void fatal(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.fatal(buildMessage(pUserName, pAction, pArgs));
    }

    /**
     * Log a message object with the WARN Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void warn(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.warn(buildMessage(pUserName, pAction, pArgs));
    }
    
    /**
     * Log a message object with the High Info Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void highInfo(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.log(GPMLevel.HIGH_INFO, buildMessage(pUserName, pAction, pArgs));
    }
    
    /**
     * Log a message object with the Medium Info Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void mediumInfo(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.log(GPMLevel.MEDIUM_INFO, buildMessage(pUserName, pAction, pArgs));
    }
    
    /**
     * Log a message object with the Low Info Level.
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     */
    public void lowInfo(String pUserName, GPMActionLogConstants pAction, String ... pArgs)
    {
        logger.log(GPMLevel.LOW_INFO, buildMessage(pUserName, pAction, pArgs));
    }
    
    /**
     * Log an exception.
     * 
     * @param pEx The exception to be logged
     */
    public void logException(Exception pEx) {
    	ByteArrayOutputStream lBaos = new ByteArrayOutputStream();
    	PrintStream lPrintStream = new PrintStream(lBaos);
    	pEx.printStackTrace(lPrintStream);
    	logger.error(lBaos.toString());
    	
    	lPrintStream.close();
    	try {
			lBaos.close();
		} catch (IOException lEx) {
			lEx.printStackTrace();
		}
    }
    
    /**
     * Create a message
     * 
     * @param pUserName The user name connected
     * @param pAction Action to be logged
     * @param pArgs additional parameters to be logged
     * 
     * @return The message
     */
    private String buildMessage(String pUserName, GPMActionLogConstants pAction, String ... pArgs) {
        StringBuilder lMessage = new StringBuilder(pAction.toString());
        lMessage.append(LOG_DELIMITER);
        lMessage.append(pUserName);
        for (String lArg : pArgs) {
            lMessage.append(LOG_DELIMITER);
            lMessage.append(lArg);
        }
        
        return lMessage.toString();
    }
 
}
