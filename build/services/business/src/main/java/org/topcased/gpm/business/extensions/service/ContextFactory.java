/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available 
 * under the terms of the Lesser Gnu Public License (LGPL)which 
 * accompanies this distribution, and is available 
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.extensions.service;

/**
 * Interface implemented by all extension point commands context factories
 * implementation.
 * 
 * @author llatil
 * @deprecated
 * @since 1.8
 * @see Context
 */
public interface ContextFactory {
    /**
     * Create an execution context
     * 
     * @return New context
     */
    Context getContext();
}
