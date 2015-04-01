/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.util;

import org.topcased.gpm.domain.fields.StringValue;

/**
 * Util's class used to know info on the size on column.
 * 
 * @author tpanuel
 */
public final class ColumnSizeUtils {
    /**
     * Private constructor for singleton
     */
    private ColumnSizeUtils() {
    }

    // Used to singleton pattern
    private final static ColumnSizeUtils INSTANCE = new ColumnSizeUtils();

    /**
     * Access to the singleton's instance
     * 
     * @return The singleton's instance
     */
    public final static ColumnSizeUtils getInstance() {
        return INSTANCE;
    }

    public boolean isInfineStringField(final int pMaxSize) {
        return pMaxSize < 0 || pMaxSize > StringValue.MAX_VARCHAR_LENGTH;
    }
}