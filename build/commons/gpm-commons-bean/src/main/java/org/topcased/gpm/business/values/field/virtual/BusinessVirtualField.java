/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.virtual;

import org.topcased.gpm.business.values.field.BusinessField;

/**
 * Interface used to access on a virtual field.
 * 
 * @author tpanuel
 */
public interface BusinessVirtualField extends BusinessField {
    /**
     * Get the value of the virtual field.
     * 
     * @return The value of the virtual field.
     */
    public String getValue();
}