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
 * UiFilterUsage
 * 
 * @author nveillet
 */
public enum UiFilterUsage {
    TABLE_VIEW("filter.edition.save.usage.table"),

    TREE_VIEW("filter.edition.save.usage.tree"),

    BOTH_VIEWS("filter.edition.save.usage.both");

    private final static Map<String, UiFilterUsage> VALUES_MAP =
            new HashMap<String, UiFilterUsage>();

    /**
     * get a UiFilterUsage from its key
     * 
     * @param pKey
     *            the key
     * @return the UiFilterUsage
     */
    public static UiFilterUsage fromKey(String pKey) {
        if (VALUES_MAP.isEmpty()) {
            for (UiFilterUsage lFilterUsage : values()) {
                VALUES_MAP.put(lFilterUsage.getKey(), lFilterUsage);
            }
        }
        return VALUES_MAP.get(pKey);
    }

    private String key;

    private UiFilterUsage(String pKey) {
        key = pKey;
    }

    public String getKey() {
        return key;
    }

}
