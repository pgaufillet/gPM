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

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * Update Dictionary Category values Action
 * 
 * @author jlouisy
 */
public class UpdateDictionaryCategoryValuesAction extends
        AbstractCommandAction<UpdateDictionaryCategoryValuesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 3598666658319625943L;

    private String categoryName;

    private List<String> categoryValues;

    /**
     * create action
     */
    public UpdateDictionaryCategoryValuesAction() {
    }

    /**
     * create action with values
     * 
     * @param pCategoryName
     *            the category name.
     * @param pCategoryValues
     *            Category values.
     */
    public UpdateDictionaryCategoryValuesAction(String pCategoryName,
            List<String> pCategoryValues) {
        super();
        categoryName = pCategoryName;
        categoryValues = pCategoryValues;
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

    /**
     * Get category values.
     * 
     * @return category values.
     */
    public List<String> getCategoryValues() {
        return categoryValues;
    }

    /**
     * Set category values.
     * 
     * @param pCategoryValues
     *            category values.
     */
    public void setCategoryValues(List<String> pCategoryValues) {
        categoryValues = pCategoryValues;
    }

}