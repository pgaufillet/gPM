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
 * GetDictionaryCategoryValuesResult
 * 
 * @author jlouisy
 */
public class GetDictionaryCategoryValuesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private List<String> values;

    /**
     * Create GetDictionaryCategoryValuesResult
     */
    public GetDictionaryCategoryValuesResult() {
    }

    /**
     * Create GetDictionaryCategoryValuesResult with values
     * 
     * @param pValues
     *            the dictionary category values.
     */
    public GetDictionaryCategoryValuesResult(List<String> pValues) {
        super();
        values = pValues;
    }

    /**
     * Get dictionary category values.
     * 
     * @return category values.
     */
    public List<String> getValues() {
        return values;
    }

}
