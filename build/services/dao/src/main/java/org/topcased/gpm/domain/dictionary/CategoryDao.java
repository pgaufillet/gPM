/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SpringDao.vsl in andromda-spring-cartridge.
//
package org.topcased.gpm.domain.dictionary;

/**
 * @see org.topcased.gpm.domain.dictionary.Category
 * @author Atos
 */
public interface CategoryDao
        extends
        org.topcased.gpm.domain.IDao<org.topcased.gpm.domain.dictionary.Category, java.lang.String> {
    /**
     * 
     */
    public org.topcased.gpm.domain.dictionary.Category getCategory(
            org.topcased.gpm.domain.dictionary.Dictionary pDictionnary,
            java.lang.String pCategoryName);

    /**
     * <p>
     * Check if a given category is currently referenced or not.
     * </p>
     */
    public java.lang.Boolean isCategoryUsed(
            org.topcased.gpm.domain.dictionary.Category pCategory);

    /**
     * 
     */
    public java.util.List<org.topcased.gpm.domain.dictionary.Category> getModifiableCategories(
            org.topcased.gpm.domain.dictionary.Dictionary pDictionary,
            java.lang.String[] pAccessLevels);

    /**
     * <p>
     * Retrieves a category from its name and the business process name.
     * </p>
     */
    public org.topcased.gpm.domain.dictionary.Category getCategory(
            java.lang.String pBusinessProcessName,
            java.lang.String pCategoryName);

    /**
     * 
     */
    public java.util.Iterator<String> categoriesIterator(
            java.lang.String pProcessName);

    /**
     * 
     */
    public java.lang.Boolean isUsed(java.lang.String pName);

    /**
     * 
     */
    public java.lang.Boolean isExists(java.lang.String pName);

    /**
     * 
     */
    public java.lang.String getId(java.lang.String pName);

    /**
     * 
     */
    public java.lang.Boolean addValueAtPosition(
            org.topcased.gpm.domain.dictionary.Category pCategory,
            org.topcased.gpm.domain.dictionary.CategoryValue pValue, int pPosition);
}