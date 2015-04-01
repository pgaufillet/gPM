/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.util.proxy;

/**
 * interface for immutable objects
 * 
 * @author tpanuel
 */
public interface ImmutableGpmObject {
    /***
     * Return a mutable copy of the object
     * 
     * @return A mutable object
     */
    Object getMutableCopy();
}
