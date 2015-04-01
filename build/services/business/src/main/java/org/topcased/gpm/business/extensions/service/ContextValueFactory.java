/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;

/**
 * Factory used to create context values 'on demand'.
 * 
 * @author llatil
 * @deprecated
 * @since 1.8
 * @see CacheableValuesContainer
 */
public interface ContextValueFactory {
    /**
     * This method is called (by the context implementation) to actually create
     * a given object.
     * 
     * @param pName
     *            Name of the object
     * @param pObjClass
     *            Expected class of the object
     * @return The actual object created by this factory.
     */
    Object create(String pName, Class<?> pObjClass);
}
