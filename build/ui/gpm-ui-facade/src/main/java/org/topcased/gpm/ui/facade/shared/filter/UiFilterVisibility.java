/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter;

import java.util.HashMap;
import java.util.Map;

/**
 * UiFilterVisibility
 * 
 * @author nveillet
 */
public enum UiFilterVisibility {

    INSTANCE("filter.edition.save.visibility.instance"),

    PRODUCT("filter.edition.save.visibility.product"),

    USER("filter.edition.save.visibility.user");

    private final static Map<String, UiFilterVisibility> VALUES_MAP =
            new HashMap<String, UiFilterVisibility>();

    /**
     * get a UiFilterVisibility from its key
     * 
     * @param pKey
     *            the key
     * @return the UiFilterVisibility
     */
    public static UiFilterVisibility fromKey(String pKey) {
        if (VALUES_MAP.isEmpty()) {
            for (UiFilterVisibility lFilterVisibility : values()) {
                VALUES_MAP.put(lFilterVisibility.getKey(), lFilterVisibility);
            }
        }
        return VALUES_MAP.get(pKey);
    }

    private String key;

    private UiFilterVisibility(String pKey) {
        key = pKey;
    }

    public String getKey() {
        return key;
    }

}
