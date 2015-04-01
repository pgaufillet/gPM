/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.criteria;

import java.util.HashMap;
import java.util.Map;

/**
 * UiFilterOperator
 * 
 * @author nveillet
 */
public enum UiFilterOperator {

    EQUAL("filter.edition.criteria.operator.equal"),

    GREATER("filter.edition.criteria.operator.greater"),

    GREATER_OR_EQUALS("filter.edition.criteria.operator.greaterOrEquals"),

    LESS("filter.edition.criteria.operator.less"),

    LESS_OR_EQUALS("filter.edition.criteria.operator.lessOrEquals"),

    LIKE("filter.edition.criteria.operator.like"),

    NOT_EQUAL("filter.edition.criteria.operator.notEqual"),

    NOT_LIKE("filter.edition.criteria.operator.NotLike"),

    SINCE("filter.edition.criteria.operator.since"),

    OTHER("filter.edition.criteria.operator.other");

    private final static Map<String, UiFilterOperator> VALUES_MAP =
            new HashMap<String, UiFilterOperator>();

    /**
     * get a UiFilterOperator from its key
     * 
     * @param pKey
     *            the key
     * @return the UiFilterOperator
     */
    public static UiFilterOperator fromKey(String pKey) {
        if (VALUES_MAP.isEmpty()) {
            for (UiFilterOperator lFilterOperator : values()) {
                VALUES_MAP.put(lFilterOperator.getKey(), lFilterOperator);
            }
        }
        return VALUES_MAP.get(pKey);
    }

    private String key;

    private UiFilterOperator(String pKey) {
        key = pKey;
    }

    public String getKey() {
        return key;
    }
}
