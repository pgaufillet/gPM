/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dictionary;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.product.Product;

/**
 * Environment DAO implementation
 * 
 * @author llatil
 */
public class EnvironmentDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.dictionary.Environment, java.lang.String>
        implements org.topcased.gpm.domain.dictionary.EnvironmentDao {

    private static final String QUERY_VALUES_BY_ENV =
            "select new list(e.name, v) from "
                    + ValuesContainer.class.getName()
                    + " s join s.environments e join e.categoryValues v "
                    + "where v.category.name = :catName "
                    + "and s.id = :containerId "
                    + "order by env_idx, CATEGORY_VALUE_IDX";

    /**
     * Constructor
     */
    public EnvironmentDaoImpl() {
        super(org.topcased.gpm.domain.dictionary.Environment.class);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Iterator<List<?>> getValuesByEnv(final String pContainerId,
            final String pCategoryName) {
        final Query lQuery = getSession().createQuery(QUERY_VALUES_BY_ENV);

        lQuery.setParameter("containerId", pContainerId);
        lQuery.setParameter("catName", pCategoryName);

        return (Iterator) iterate(lQuery, List.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDao#getCategoryValue(java.lang.String,
     *      java.util.List, java.lang.String, java.lang.String)
     */
    public CategoryValue getContainerCategoryValue(final String pContainerId,
            final String pCategoryName, final String pValue) {
        final Iterator<List<?>> lIter =
                getValuesByEnv(pContainerId, pCategoryName);
        CategoryValue lFoundValue = null;

        try {
            // Iterator must be totally filed for close associated result set
            boolean lContinue = true;
            String lPreviousEnv = null;

            while (lIter.hasNext() && lContinue) {
                final List<?> lNext = lIter.next();
                final String lEnvName = (String) lNext.get(0);
                final CategoryValue lCatValue = (CategoryValue) lNext.get(1);

                // Search value only on the first env
                if (lPreviousEnv == null) {
                    lPreviousEnv = lEnvName;
                }
                if (lPreviousEnv.equals(lEnvName)) {
                    if (lCatValue.getValue().equals(pValue)) {
                        // Value found
                        lFoundValue = lCatValue;
                        lContinue = false;
                    }
                }
                else {
                    // Value not in first env
                    lContinue = false;
                }
            }
        }
        finally {
            // Iterator must be ended for close associated result set
            while (lIter.hasNext()) {
                lIter.next();
            }
        }

        return lFoundValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDao#getValues(java.lang.String,
     *      java.util.List, java.lang.String)
     */
    public List<String> getContainerCategoryValues(final String pContainerId,
            final String pCategoryName) {
        final Iterator<List<?>> lIter =
                getValuesByEnv(pContainerId, pCategoryName);
        final List<String> lValues = new LinkedList<String>();

        try {
            // Iterator must be totally filed for close associated result set
            boolean lContinue = true;
            String lPreviousEnv = null;

            while (lIter.hasNext() && lContinue) {
                final List<?> lNext = lIter.next();
                final String lEnvName = (String) lNext.get(0);

                // Search value only on the first env
                if (lPreviousEnv == null) {
                    lPreviousEnv = lEnvName;
                }
                if (lPreviousEnv.equals(lEnvName)) {
                    lValues.add(((CategoryValue) lNext.get(1)).getValue());
                }
                else {
                    // Value not in first env
                    lContinue = false;
                }
            }
        }
        finally {
            // Iterator must be ended for close associated result set
            while (lIter.hasNext()) {
                lIter.next();
            }
        }

        return lValues;
    }

    private static final String QUERY_GET_VALUES =
            "select v.value from " + BusinessProcess.class.getName()
                    + " p join p.dictionary.categories c "
                    + "join c.categoryValues v "
                    + "join v.environments e where p.name = :processName "
                    + "and e.name = :envName and c.name = :catName "
                    + "order by CATEGORY_VALUE_IDX";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDao#getValues(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public List<String> getEnvironmentCategoryValues(final String pProcessName,
            final String pEnvironmentName, final String pCategoryName) {
        final Query lQuery = getSession().createQuery(QUERY_GET_VALUES);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("envName", pEnvironmentName);
        lQuery.setParameter("catName", pCategoryName);

        return execute(lQuery, String.class);
    }

    private static final String QUERY_GETENV =
            "select e from "
                    + BusinessProcess.class.getName()
                    + " p join p.dictionary.environments e where p.name = :processName "
                    + "and e.name = :envName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#getEnvironment(org.topcased.gpm.domain.dictionary.bis.Dictionary,
     *      java.lang.String)
     */
    public Environment getEnvironment(final String pProcessName,
            final String pEnvName) {
        final Query lQuery = getSession().createQuery(QUERY_GETENV);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameter("envName", pEnvName);

        return getFirst(lQuery);
    }

    private static final String IS_USED =
            "SELECT valuesContainer.id "
                    + "FROM "
                    + ValuesContainer.class.getName()
                    + " valuesContainer, "
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "left join valuesContainer.environments env "
                    + "WHERE env.name = :pName AND env.dictionary.id = businessProcess.dictionary.id AND businessProcess.name= :pProcessName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#isUsed(java.lang.String)
     */
    public Boolean isUsed(String pProcessName, String pName) {
        final Query lQuery = getSession().createQuery(IS_USED);

        lQuery.setParameter("pName", pName);
        lQuery.setParameter("pProcessName", pProcessName);

        return hasResult(lQuery);
    }

    private final static String CATEGORY_VALUES_FOR_ALL_CATORIES =
            "select new "
                    + CategoryValueInfo.class.getName()
                    + "(category.name,categoryValue.value) "
                    + "from "
                    + Category.class.getName()
                    + " category "
                    + "left join category.categoryValues categoryValue, "
                    + Environment.class.getName()
                    + " env,"
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "where businessProcess.name = :processName "
                    + "AND env.name = :envName and env in elements (categoryValue.environments) "
                    + "AND env.dictionary.id = businessProcess.dictionary.id "
                    + "ORDER BY category.name ASC, CATEGORY_VALUE_IDX";

    private final static String CATEGORY_VALUES_FOR_LIMITED_CATORIES =
            "select new "
                    + CategoryValueInfo.class.getName()
                    + "(category.name,categoryValue.value) "
                    + "from "
                    + Category.class.getName()
                    + " category "
                    + "left join category.categoryValues categoryValue, "
                    + Environment.class.getName()
                    + " env,"
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "where businessProcess.name = :processName "
                    + "AND category.name in (:categoryNames) "
                    + "AND env.name = :envName and env in elements (categoryValue.environments) "
                    + "AND env.dictionary.id = businessProcess.dictionary.id "
                    + "ORDER BY category.name ASC, CATEGORY_VALUE_IDX";

    /**
     * {@inheritDoc}
     * 
     * @param pCategoryNames
     *            Name of categories : export all categories if null or empty.
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#getCategoryValues(java.lang.String,
     *      java.lang.String, java.util.List)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List getCategoryValues(final String pProcessName,
            final String pEnvName, final List<String> pCategoryNames) {
        final Query lQuery;

        if (CollectionUtils.isEmpty(pCategoryNames)) {
            lQuery = createQuery(CATEGORY_VALUES_FOR_ALL_CATORIES);
        }
        else {
            lQuery = createQuery(CATEGORY_VALUES_FOR_LIMITED_CATORIES);
            lQuery.setParameterList("categoryNames", pCategoryNames);
        }

        lQuery.setParameter("envName", pEnvName);
        lQuery.setParameter("processName", pProcessName);

        return execute(lQuery);
    }

    private final static String ALL_ENVIRONEMENTS =
            "select e.id from " + Environment.class.getName() + " e, "
                    + BusinessProcess.class.getName() + " b "
                    + "where b.dictionary = e.dictionary "
                    + "and b.name = :processName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#environmentsIterator(java.lang.String)
     */
    public Iterator<String> environmentsIterator(final String pProcessName) {
        final Query lQuery = createQuery(ALL_ENVIRONEMENTS);

        lQuery.setParameter("processName", pProcessName);

        return iterate(lQuery, String.class);
    }

    private final static String ENVIRONEMENT_PRODUCT_NAMES =
            "select p.name from " + Product.class.getName()
                    + " p join p.environments as e where e.id = :envId";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#getEnvironmentProductNames(java.lang.String)
     */
    public List<String> getEnvironmentProductNames(final String pEnvironmentId) {
        final Query lQuery = createQuery(ENVIRONEMENT_PRODUCT_NAMES);

        lQuery.setParameter("envId", pEnvironmentId);

        return execute(lQuery, String.class);
    }

    private static final String GET_ID =
            "SELECT env.id "
                    + "FROM "
                    + Environment.class.getName()
                    + " env, "
                    + BusinessProcess.class.getName()
                    + " businessProcess "
                    + "WHERE env.name = :pName AND env.dictionary.id = businessProcess.dictionary.id AND businessProcess.name= :pProcessName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#getId(java.lang.String,
     *      java.lang.String)
     */
    public String getId(String pProcessName, String pName) {
        final Query lQuery = getSession().createQuery(GET_ID);

        lQuery.setParameter("pName", pName);
        lQuery.setParameter("pProcessName", pProcessName);

        return (String) lQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.dictionary.EnvironmentDaoBase#isExists(java.lang.String)
     */
    public Boolean isExists(String pProcessName, String pName) {
        final Query lQuery = getSession().createQuery(GET_ID);

        lQuery.setParameter("pName", pName);
        lQuery.setParameter("pProcessName", pProcessName);

        return hasResult(lQuery);
    }

    /**
     * @see org.topcased.gpm.domain.dictionary.Environment#isEnvironmentUsed(org.topcased.gpm.domain.dictionary.Environment)
     */
    @SuppressWarnings("rawtypes")
    public java.lang.Boolean isEnvironmentUsed(
            final org.topcased.gpm.domain.dictionary.Environment pEnv) {
        try {
            final java.lang.String lQueryString =
                    "from org.topcased.gpm.domain.dictionary.Environment as environment where environment.env.id = :envId";
            org.hibernate.Query lQueryObject =
                    super.getSession(false).createQuery(lQueryString);
            lQueryObject.setParameter("envId", pEnv.getId());
            java.util.List lResults = lQueryObject.list();
            java.lang.Boolean lResult = null;
            if (lResults != null) {
                if (lResults.size() > 1) {
                    throw new org.springframework.dao.InvalidDataAccessResourceUsageException(
                            "More than one instance of 'java.lang.Boolean"
                                    + "' was found when executing query --> '"
                                    + lQueryString + "'");
                }
                else if (lResults.size() == 1) {
                    lResult = (java.lang.Boolean) lResults.iterator().next();
                }
            }
            return lResult;
        }
        catch (org.hibernate.HibernateException ex) {
            throw super.convertHibernateAccessException(ex);
        }
    }

}