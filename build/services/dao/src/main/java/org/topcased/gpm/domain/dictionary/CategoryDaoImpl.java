/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin),
 * Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dictionary;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.fields.ChoiceField;

/**
 * Category DAO implementation
 * 
 * @author llatil
 */
public class CategoryDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.dictionary.Category, java.lang.String>
        implements org.topcased.gpm.domain.dictionary.CategoryDao {

    private final static String QUERY_GETCATEGORY =
            "from "
                    + Category.class.getName()
                    + " as category "
                    + "where category.dictionary.id = :dictionaryId AND category.name = :categoryName";

    /**
     * Get a category by given its name and the business process name. With the
     * business process name, retrieves the dictionary.
     */
    private final static String QUERY_GETCATEGORYBYPROCESSNAME =
            "select category "
                    + "from "
                    + Category.class.getName()
                    + " category, "
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "where businessProcess.name = :processName "
                    + "AND category.dictionary.id = businessProcess.dictionary.id "
                    + "AND category.name = :categoryName";

    /**
     * Constructor
     */
    public CategoryDaoImpl() {
        super(org.topcased.gpm.domain.dictionary.Category.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#getCategory(org.topcased.gpm.domain.dictionary.Dictionary,
     *      java.lang.String)
     */
    public Category getCategory(final Dictionary pDictionary,
            final String pCategoryName) {
        final Query lQuery = getSession(false).createQuery(QUERY_GETCATEGORY);

        lQuery.setParameter("dictionaryId", pDictionary.getId());
        lQuery.setParameter("categoryName", pCategoryName);

        return (Category) (lQuery.uniqueResult());
    }

    private final static String QUERY_GETMODIFIABLECATEGORIES =
            "from "
                    + Category.class.getName()
                    + " as category "
                    + "where category.dictionary.id = :dictionaryId AND "
                    + "category.accessLevel in (:accessLevels) order by category.name";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#getCategory(org.topcased.gpm.domain.dictionary.Dictionary,
     *      java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public List<Category> getModifiableCategories(final Dictionary pDictionary,
            final String[] pAccessLevels) {
        final Query lQuery =
                getSession(false).createQuery(QUERY_GETMODIFIABLECATEGORIES);

        lQuery.setParameter("dictionaryId", pDictionary.getId());
        lQuery.setParameterList("accessLevels", pAccessLevels);

        return lQuery.list();
    }

    private static final String QUERY_ISCATEGORYUSED =
            "from "
                    + ChoiceField.class.getName()
                    + " as choiceField where choiceField.category.id = :categoryId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#isCategoryUsed(org.topcased.gpm.domain.dictionary.Category)
     */
    public Boolean isCategoryUsed(final Category pCategory) {
        // Get all ChoiceField referencing this category.
        final Query lQuery =
                getSession(false).createQuery(QUERY_ISCATEGORYUSED);

        lQuery.setParameter("categoryId", pCategory.getId());

        return lQuery.list().size() > 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#getCategory(java.lang.String,
     *      java.lang.String)
     */
    public Category getCategory(final String pBusinessProcessName,
            final String pCategoryName) {
        final Query lQuery =
                getSession(false).createQuery(QUERY_GETCATEGORYBYPROCESSNAME);

        lQuery.setParameter("processName", pBusinessProcessName);
        lQuery.setParameter("categoryName", pCategoryName);

        return (Category) (lQuery.uniqueResult());
    }

    final static String ALL_CATEGORIES =
            "select c.id from " + Category.class.getName() + " c, "
                    + BusinessProcess.class.getName() + " b "
                    + "where b.dictionary = c.dictionary "
                    + "and b.name = :processName";

    public Iterator<String> categoriesIterator(final String pProcessName) {
        final Query lQuery = createQuery(ALL_CATEGORIES);

        lQuery.setParameter("processName", pProcessName);

        return iterate(lQuery, String.class);
    }

    private static final String GET_ID =
            "SELECT cat.id " + "FROM " + Category.class.getName() + " cat "
                    + "WHERE cat.name = :pName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#getId(java.lang.String)
     */
    @Override
    public String getId(String pName) {
        final Query lQuery = createQuery(GET_ID);
        lQuery.setParameter("pName", pName);
        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#isExists(java.lang.String)
     */
    @Override
    public Boolean isExists(String pName) {
        final Query lQuery = createQuery(GET_ID);
        lQuery.setParameter("pName", pName);
        return hasResult(lQuery);
    }

    private static final String IS_USED =
            "SELECT choiceField.id "
                    + "FROM "
                    + ChoiceField.class.getName()
                    + " as choiceField where choiceField.category.name = :pName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryDaoBase#isUsed(java.lang.String)
     */
    @Override
    public Boolean isUsed(String pName) {
        final Query lQuery = createQuery(IS_USED);
        lQuery.setParameter("pName", pName);
        return hasResult(lQuery);
    }

    @Override
    public java.lang.Boolean addValueAtPosition(
            org.topcased.gpm.domain.dictionary.Category pCategory,
            org.topcased.gpm.domain.dictionary.CategoryValue pValue, int pPosition) {
        pCategory.getCategoryValues().add(pPosition, pValue);
        return true;
    }
}