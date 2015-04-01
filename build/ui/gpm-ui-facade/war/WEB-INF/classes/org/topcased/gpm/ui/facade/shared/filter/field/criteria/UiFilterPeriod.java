/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Olivier JUIN (ATOS)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.criteria;

import java.util.HashMap;
import java.util.Map;

/**
 * UiFilterPeriod Enumeration for date filters.
 * 
 * @author Olivier Juin
 */
public enum UiFilterPeriod {

    LAST_MONTH("filter.edition.criteria.period.lastMonth"), THIS_MONTH(
            "filter.edition.criteria.period.thisMonth"), LAST_WEEK(
            "filter.edition.criteria.period.lastWeek"), THIS_WEEK(
            "filter.edition.criteria.period.thisWeek");

    private final static Map<String, UiFilterPeriod> VALUES_MAP =
            new HashMap<String, UiFilterPeriod>();

    /**
     * Get a UiFilterPeriod from its key
     * 
     * @param pKey
     *            the key
     * @return the UiFilterPeriod
     */
    public static UiFilterPeriod fromKey(String pKey) {
        if (VALUES_MAP.isEmpty()) {
            for (UiFilterPeriod lFilterPeriod : values()) {
                VALUES_MAP.put(lFilterPeriod.getKey(), lFilterPeriod);
            }
        }
        return VALUES_MAP.get(pKey);
    }

    private String key;

    private UiFilterPeriod(String pKey) {
        key = pKey;
    }

    public String getKey() {
        return key;
    }
}
