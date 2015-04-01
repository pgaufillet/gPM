/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.dictionary;

import java.util.List;

import net.customware.gwt.dispatch.shared.Result;

/**
 * GetEnvironmentCategoryValuesResult
 * 
 * @author jlouisy
 */
public class GetEnvironmentCategoryValuesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private List<String> dictionaryValues;

    private List<String> environmentValues;

    /**
     * Create GetEnvironmentCategoryValuesResult
     */
    public GetEnvironmentCategoryValuesResult() {
    }

    /**
     * Create GetEnvironmentCategoryValuesResult with values
     * 
     * @param pDictionaryValues
     *            all values.
     * @param pEnvironmentValues
     *            selected values.
     */
    public GetEnvironmentCategoryValuesResult(List<String> pDictionaryValues,
            List<String> pEnvironmentValues) {
        super();
        dictionaryValues = pDictionaryValues;
        environmentValues = pEnvironmentValues;
    }

    /**
     * Get dictionary values.
     * 
     * @return all values.
     */
    public List<String> getDictionaryValues() {
        return dictionaryValues;
    }

    /**
     * Get environment values.
     * 
     * @return selected values.
     */
    public List<String> getEnvironmentValues() {
        return environmentValues;
    }

}
