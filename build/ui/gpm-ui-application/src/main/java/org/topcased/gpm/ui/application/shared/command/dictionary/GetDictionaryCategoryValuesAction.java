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

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * GetDictionaryAction
 * 
 * @author jlouisy
 */
public class GetDictionaryCategoryValuesAction extends
        AbstractCommandAction<GetDictionaryCategoryValuesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3598666658319625943L;

    private String categoryName;

    /**
     * create action
     */
    public GetDictionaryCategoryValuesAction() {
    }

    /**
     * create action with values
     * 
     * @param pCategoryName
     *            the category name.
     */
    public GetDictionaryCategoryValuesAction(String pCategoryName) {
        super();
        categoryName = pCategoryName;
    }

    /**
     * Get category name.
     * 
     * @return category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Set category name.
     * 
     * @param pCategoryName
     *            category name.
     */
    public void setCategoryName(String pCategoryName) {
        categoryName = pCategoryName;
    }

}