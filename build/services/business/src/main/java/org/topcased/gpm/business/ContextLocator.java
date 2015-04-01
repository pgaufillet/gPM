/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring context locator.
 * 
 * @author llatil
 */
public class ContextLocator {

    /**
     * Spring context.
     */
    private static ApplicationContext staticContext;

    /**
     * Create the Spring application context.
     * 
     * @return Application context
     */
    private static ApplicationContext createGlobalContext() {

        //        throw new RuntimeException("applicationContext.xml is created here !");
        String[] lLocations = new String[] { "applicationContext.xml" };

        ClassPathXmlApplicationContext lAppCtx =
                new ClassPathXmlApplicationContext(lLocations, false);
        lAppCtx.refresh();
        return lAppCtx;
    }

    /**
     * Get the current Spring ApplicationContext. A new context is created if
     * needed.
     * 
     * @return The Spring context.
     */
    public static synchronized ApplicationContext getContext() {
        if (staticContext == null) {
            createContext();
        }
        return staticContext;
    }

    static void setContext(ApplicationContext pContext) {
        staticContext = pContext;
    }

    /**
     * Create a new Spring ApplicationContext. The created context becomes the
     * current one.
     */
    private static void createContext() {
        staticContext = createGlobalContext();
    }
}
