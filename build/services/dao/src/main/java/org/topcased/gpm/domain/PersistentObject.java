/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain;

import java.io.Serializable;

/**
 * Interface used by all gPM object store on data base
 * 
 * @author tpanuel
 */
public interface PersistentObject {

    /** Constant used for hashcode */
    public static final int HASHCODE_CONSTANT = 29;

    /**
     * Get the id use to store the object on data base
     * 
     * @return The id
     */
    public Serializable getId();
}