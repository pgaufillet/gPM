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
package org.topcased.gpm.business.values.field.simple;

/**
 * Interface used to access on a simple field of type String.
 * 
 * @author tpanuel
 */
public interface BusinessStringField extends BusinessSimpleField<String> {

    /**
     * Get the sheet reference for internal URL
     * 
     * @return The sheet reference for internal URL
     */
    public String getInternalUrlSheetReference();

    /**
     * Get the maximum character number of the field
     * 
     * @return The size of the field
     */
    public int getSize();
}