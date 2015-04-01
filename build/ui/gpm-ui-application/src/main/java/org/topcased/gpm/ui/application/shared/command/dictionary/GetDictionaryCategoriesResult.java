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

import org.topcased.gpm.ui.facade.shared.dictionary.UiCategory;

/**
 * GetDictionaryResult
 * 
 * @author jlouisy
 */
public class GetDictionaryCategoriesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private List<UiCategory> categories;

    /**
     * Create GetDictionaryResult
     */
    public GetDictionaryCategoriesResult() {
    }

    /**
     * Create GetDictionaryResult with values
     * 
     * @param pCategories
     *            the dictionary categories.
     */
    public GetDictionaryCategoriesResult(List<UiCategory> pCategories) {
        super();
        categories = pCategories;
    }

    /**
     * Get categories.
     * 
     * @return categories.
     */
    public List<UiCategory> getCategories() {
        return categories;
    }

    /**
     * set categories.
     * 
     * @param pCategories
     *            categories list.
     */
    public void setCategories(List<UiCategory> pCategories) {
        categories = pCategories;
    }

}
