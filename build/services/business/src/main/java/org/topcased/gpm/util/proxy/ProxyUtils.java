/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.proxy;

/**
 * Utility methods used by proxy generators
 * 
 * @author tpanuel
 */
public class ProxyUtils {

    /**
     * Get the actual class (non proxified) of a bean
     * 
     * @param pBean
     *            Bean object
     * @return The actual Class object of this bean object (any dynamically
     *         generated proxy subclasses are skipped)
     */
    public static Class<?> getNotProxySuperClass(Object pBean) {
        if (pBean instanceof ImmutableGpmObject) {
            if (pBean instanceof CheckedGpmObject) {
                // CheckedGpmObject -> ImmutableGpmObject -> Bean
                return pBean.getClass().getSuperclass().getSuperclass();
            }
            // else
            // ImmutableGpmObject -> Bean
            return pBean.getClass().getSuperclass();
        }
        // else
        if (pBean instanceof CheckedGpmObject) {
            // CheckedGpmObject -> Bean
            return pBean.getClass().getSuperclass();
        }
        // else this is a 'simple' bean (without any proxy class)
        return pBean.getClass();
    }
}
