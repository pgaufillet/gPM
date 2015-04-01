/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dictionary;

import java.util.List;

import org.hibernate.Query;

/**
 * CategoryValue DAO implementation.
 * 
 * @author llatil
 */
public class CategoryValueDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.dictionary.CategoryValue, java.lang.String>
        implements org.topcased.gpm.domain.dictionary.CategoryValueDao {
    /**
     * Constructor
     */
    public CategoryValueDaoImpl() {
        super(org.topcased.gpm.domain.dictionary.CategoryValue.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryValueDaoBase#isValueUsed(org.topcased.gpm.domain.dictionary.CategoryValue)
     */
    public Boolean isValueUsed(final CategoryValue pCategoryValue) {
        final String lQueryStr =
                "from org.topcased.gpm.domain.fields.ChoiceFieldValue "
                        + "as choiceValue where :categoryValue in elements(choiceValue.categoryValues)";
        // Get all ChoiceFieldValue referencing this category value.
        final Query lQuery = getSession(false).createQuery(lQueryStr);
        lQuery.setParameter("categoryValue", pCategoryValue);

        return lQuery.list().size() > 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryValueDaoBase#get(org.topcased.gpm.domain.dictionary.Category,
     *      java.lang.String)
     */
    public CategoryValue get(final Category pCategory, final String pValueStr) {
        final Query lQuery =
                getSession().createFilter(pCategory.getCategoryValues(),
                        "where this.value = :value");
        lQuery.setParameter("value", pValueStr);

        return (CategoryValue) lQuery.uniqueResult();
    }

    private final static String QUERY_GETPOSSIBLEVALUES =
            "from CategoryValue catValue where "
                    + "(exists (select environment.name from Environment environment "
                    + "where environment in elements(catValue.environments) "
                    + "and environment.name in (:envNames))) and ("
                    + "catValue.id in ("
                    + "select categoryValue.id from CategoryValue categoryValue,"
                    + "Category category "
                    + "where categoryValue in elements(category.categoryValues) "
                    + "and category.name = :catName "
                    + "and (category.dictionary in ( "
                    + "select bp.dictionary from BusinessProcess bp where bp.name = :bpName)) "
                    + "))";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.CategoryValueDaoBase#getPossibleValues(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<CategoryValue> getPossibleValues(
            final String pBusinessProcessName, final String pCategoryName,
            final List pEnvironmentNames) {
        final Query lQuery =
                getSession(false).createQuery(QUERY_GETPOSSIBLEVALUES);

        lQuery.setParameter("catName", pCategoryName);
        lQuery.setParameterList("envNames", pEnvironmentNames);
        lQuery.setParameter("bpName", pBusinessProcessName);

        return lQuery.list();
    }
}
