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
package org.topcased.gpm.business.values;

/**
 * Properties used to access on values.
 * 
 * @author tpanuel
 */
public class ValuesAccessProperties {
    /** Access rights checked & read only */
    public final static ValuesAccessProperties CHECKED_READ_ONLY =
            new ValuesAccessProperties(true, true);

    /** Access rights not checked & read only */
    public final static ValuesAccessProperties NOT_CHECKED_READ_ONLY =
            new ValuesAccessProperties(true, false);

    /** Access rights checked & read or write */
    public final static ValuesAccessProperties CHECKED_READ_OR_WRITE =
            new ValuesAccessProperties(false, true);

    /** Access rights not checked & read or write */
    public final static ValuesAccessProperties NOT_CHECKED_READ_OR_WRITE =
            new ValuesAccessProperties(false, false);

    private final boolean readOnly;

    private final boolean checkAccessRight;

    /**
     * Properties used to load values.
     * 
     * @param pReadOnly
     *            If the access in on read only.
     * @param pCheckAccessRights
     *            The access rights are checked.
     */
    public ValuesAccessProperties(final boolean pReadOnly,
            final boolean pCheckAccessRights) {
        readOnly = pReadOnly;
        checkAccessRight = pCheckAccessRights;
    }

    /**
     * If the access in on read only.
     * 
     * @return The access is on read only.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Get the access rights are checked.
     * 
     * @return The access rights are checked.
     */
    public boolean isCheckAccessRight() {
        return checkAccessRight;
    }
}