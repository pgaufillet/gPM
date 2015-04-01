/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.scalar;

import java.io.Serializable;

/**
 * An scalar value interface.
 * 
 * @author tpanuel
 */
public abstract class ScalarValueData implements Serializable {
    private static final long serialVersionUID = 7497229699659599498L;

    /**
     * Get the value.
     * 
     * @return The value.
     */
    public abstract Object getValue();
}