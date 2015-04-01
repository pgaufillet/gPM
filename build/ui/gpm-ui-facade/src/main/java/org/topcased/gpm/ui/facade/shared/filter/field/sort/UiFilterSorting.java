/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.sort;

import java.util.HashMap;
import java.util.Map;

/**
 * UiFilterSorting
 * 
 * @author nveillet
 */
public enum UiFilterSorting {

    ASCENDANT("filter.edition.order.ascendant"),

    ASCENDANT_DEFINED("filter.edition.order.ascendantDefined"),

    DESCENDANT("filter.edition.order.descendant"),

    DESCENDANT_DEFINED("filter.edition.order.descendantDefined"),

    NONE("filter.edition.order.none");

    private final static Map<String, UiFilterSorting> VALUES_MAP =
            new HashMap<String, UiFilterSorting>();

    /**
     * get a UiFilterSorting from its key
     * 
     * @param pKey
     *            the key
     * @return the UiFilterSorting
     */
    public static UiFilterSorting fromKey(String pKey) {
        if (VALUES_MAP.isEmpty()) {
            for (UiFilterSorting lFilterSorting : values()) {
                VALUES_MAP.put(lFilterSorting.getKey(), lFilterSorting);
            }
        }
        return VALUES_MAP.get(pKey);
    }

    private String key;

    private UiFilterSorting(String pKey) {
        key = pKey;
    }

    public String getKey() {
        return key;
    }
}
