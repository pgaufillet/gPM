/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dictionary;

/**
 * CategoryValueInfo.
 * 
 * @author ahaugommard
 */
public class CategoryValueInfo {

    /** The category name. */
    private final String categoryName;

    /** The category value. */
    private final String categoryValue;

    /**
     * Constructs a new category value info.
     * 
     * @param pCategoryName
     *            the category name
     * @param pCategoryValue
     *            the category value
     */
    public CategoryValueInfo(String pCategoryName, String pCategoryValue) {
        categoryName = pCategoryName;
        categoryValue = pCategoryValue;
    }

    /**
     * get categoryName.
     * 
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * get categoryValue.
     * 
     * @return the categoryValue
     */
    public String getCategoryValue() {
        return categoryValue;
    }
}
