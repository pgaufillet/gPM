/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic;

import java.util.ArrayList;
import java.util.List;

/**
 * Register classes generated dynamically
 * 
 * @author tpanuel
 */
public class DynamicClassRegister {
    private static final List<Class<?>> DYNAMIC_CLASSES =
            new ArrayList<Class<?>>();

    /**
     * Register class generated dynamically
     * 
     * @param pGeneratedMappedClass
     *            The class generated dynamically
     */
    public static void registerDynamicClass(Class<?> pGeneratedMappedClass) {
        DYNAMIC_CLASSES.add(pGeneratedMappedClass);
    }

    /**
     * Remove class generated dynamically
     * 
     * @param pGeneratedMappedClass
     *            The class generated dynamically
     */
    public static void removeDynamicClass(Class<?> pGeneratedMappedClass) {
        DYNAMIC_CLASSES.remove(pGeneratedMappedClass);
    }

    /**
     * Get all the classes generated dynamically
     * 
     * @return The classes generated dynamically
     */
    public static List<Class<?>> getDynamicClasses() {
        return DYNAMIC_CLASSES;
    }
}
