/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier Gehin (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.util;

/**
 * Enumeration of Filter Usage.
 * 
 * @author ogehin
 */
public enum FilterUsageEnum {
    TREE_VIEW, TABLE_VIEW, BOTH_VIEWS;

    public String getValue() {
        return this.name();
    }
}
