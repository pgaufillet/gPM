/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard(Atos Origin), Michael Kargbo
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.search;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * FilterWithResultDaoImpl. This class overrides DAO methods which need to be
 * manually implemented.
 * 
 * @see org.topcased.gpm.domain.search.FilterWithResult
 */
public class FilterWithResultDaoImpl
        extends
        org.topcased.gpm.domain.AbstractDao<org.topcased.gpm.domain.search.FilterWithResult, java.lang.String>
        implements org.topcased.gpm.domain.search.FilterWithResultDao {

    /**
     * Create a DAO.
     */
    public FilterWithResultDaoImpl() {
        super(org.topcased.gpm.domain.search.FilterWithResult.class);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterWithResultDaoBase#getVisibleExecutableFilters(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.domain.search.FilterUsage)
     */
    @SuppressWarnings("unchecked")
    public List<FilterWithResult> getVisibleExecutableFilters(
            final String pBusinessProcessName, final String pProductName,
            final String pUserLogin, final FilterUsage pUsage,
            final FilterType pFilterType, final boolean pIsAdmin) {
        String lQueryStringForUsage = StringUtils.EMPTY;
        String lQueryStringForHidden = StringUtils.EMPTY;
        String lQueryStringForType = StringUtils.EMPTY;

        FilterUsage lBoth = null;

        if (pUsage != null) {
            if (pUsage.equals(FilterUsage.BOTH_VIEWS)) {
                lQueryStringForUsage = " AND executableFilter.usage = :usage ";
            }
            else {
                lQueryStringForUsage =
                        " AND (executableFilter.usage = :usage "
                                + "or executableFilter.usage = :both_usages ) ";
                lBoth = FilterUsage.BOTH_VIEWS;
            }
        }
        // If the user is not admin, hide filters defined as hidden...
        if (!pIsAdmin && !FilterType.PRODUCT.equals(pFilterType)) {
            lQueryStringForHidden = "AND (executableFilter.hidden = false) ";
        }

        if (pFilterType != null) {
            lQueryStringForType =
                    "AND (executableFilter.filter.type = :filterType) ";
        }

        final Query lQuery =
                getSession(false).createQuery(
                        "select executableFilter "
                                + "FROM org.topcased.gpm.domain.search.FilterWithResult "
                                + "AS executableFilter "
                                + "left join executableFilter.product as product "
                                + "left join executableFilter.endUser as user "
                                + "WHERE ("
                                + "executableFilter.businessProcess.name = :businessProcessName "
                                + lQueryStringForHidden
                                + lQueryStringForUsage
                                + lQueryStringForType
                                + ") AND (product is null OR product.name = :productName) "
                                + "AND (user is null OR (user.login = :userLogin)) "
                                + "ORDER by executableFilter.name ASC");
        lQuery.setParameter("businessProcessName", pBusinessProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("userLogin", pUserLogin);

        if (pUsage != null) {
            lQuery.setParameter("usage", pUsage);
            if (lBoth != null) {
                lQuery.setParameter("both_usages", lBoth);
            }
        }

        if (pFilterType != null) {
            lQuery.setParameter("filterType", pFilterType.getValue());
        }
        return (List<FilterWithResult>) lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterWithResultDaoBase#getTreeviewFilterIds(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<String> getTreeviewFilterIds(final String pProcessName,
            final String pProductName, final String pLogin,
            final boolean pIncludeHidden) {
        final String lQueryPrefixStr =
                "select executableFilter.id FROM "
                        + FilterWithResult.class.getName()
                        + " as executableFilter "
                        + "left join executableFilter.product as product "
                        + "left join executableFilter.endUser as user "
                        + "WHERE "
                        + "(executableFilter.businessProcess.name = :businessProcessName "
                        + "and executableFilter.usage <> 'TABLE_VIEW' "
                        + "and executableFilter.filter.type = 'SHEET' ";

        final String lQueryStringForHidden =
                "AND (executableFilter.hidden = false) ";

        final String lQuerySuffixStr =
                ") AND "
                        + "((product is null) OR product.name = :productName) "
                        + "AND "
                        + "((user is null) OR user.login = :userLogin) "
                        + "order by executableFilter.name";

        String lQueryStr = lQueryPrefixStr;
        if (!pIncludeHidden) {
            lQueryStr += lQueryStringForHidden;
        }
        lQueryStr += lQuerySuffixStr;

        Session lSession = getSession(false);
        final Query lQuery = lSession.createQuery(lQueryStr);

        lQuery.setParameter("businessProcessName", pProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("userLogin", pLogin);

        return lQuery.list();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterWithResultDaoBase#getFilterSummaries(java.lang.String,
     *      java.lang.String, java.lang.String, boolean)
     */
    @SuppressWarnings("unchecked")
    public List<FilterSummary> getFilterInfo(final String pProcessName,
            final String pProductName, final String pUserLogin,
            final FilterType pFilterType, final boolean pIncludeHidden) {
        final String lQueryPrefixStr =
                "select distinct new "
                        + FilterSummary.class.getName()
                        + "(executableFilter.id, executableFilter.name, "
                        + "executableFilter.description, executableFilter.usage, "
                        + "product.name, user.login) "
                        + "from "
                        + FilterWithResult.class.getName()
                        + " AS executableFilter "
                        + "left join executableFilter.product as product "
                        + "left join executableFilter.endUser as user "
                        + "WHERE "
                        + "executableFilter.businessProcess.name = :businessProcessName "
                        + "AND executableFilter.filter.type = :filterType ";

        final String lQueryStringForHidden =
                "AND (executableFilter.hidden = false) ";

        final String lQuerySuffixStr =
                "AND ((product is null) OR product.name = :productName) "
                        + "AND ((user is null) OR user.login = :userLogin ) "
                        + "order by executableFilter.name";

        String lQueryStr = lQueryPrefixStr;
        if (!pIncludeHidden) {
            lQueryStr += lQueryStringForHidden;
        }
        lQueryStr += lQuerySuffixStr;

        final Session lSession = getSession(false);
        final Query lQuery = lSession.createQuery(lQueryStr);

        lQuery.setParameter("businessProcessName", pProcessName);
        lQuery.setParameter("productName", pProductName);
        lQuery.setParameter("userLogin", pUserLogin);
        lQuery.setParameter("filterType", pFilterType.getValue());

        return lQuery.list();
    }

    private final static String ALL_FILTERS =
            "select id from " + FilterWithResult.class.getName()
                    + " where businessProcess.name = :processName";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterWithResultDaoBase#filtersIterator(java.lang.String)
     */
    @Override
    public Iterator<String> filtersIterator(final String pProcessName) {
        final Query lQuery = createQuery(ALL_FILTERS);

        lQuery.setParameter("processName", pProcessName);

        return iterate(lQuery, String.class);
    }

    private final static String ALL_PRODUCT_FILTERS =
            "select id from " + FilterWithResult.class.getName()
                    + " where product.businessProcess.name = :processName"
                    + " and product.name in (:productNames)";

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.search.FilterWithResultDaoBase#productFiltersIterator(java.lang.String,
     *      java.util.List)
     */
    @Override
    public Iterator<String> productFiltersIterator(final String pProcessName,
            final List<String> pProductNames) {
        final Query lQuery = createQuery(ALL_PRODUCT_FILTERS);

        lQuery.setParameter("processName", pProcessName);
        lQuery.setParameterList("productNames", pProductNames);

        return iterate(lQuery, String.class);
    }

    @Override
    public String getId(String pProcessName, String pProductName,
            String pUserLogin, String pFilterName) {
        final String lProductClause;
        if (StringUtils.isBlank(pProductName)) {
            lProductClause = "filterComp.product is null ";
        }
        else {
            lProductClause = "filterComp.product.name = :productName ";
        }

        final String lUserClause;
        if (StringUtils.isBlank(pUserLogin)) {
            lUserClause = "filterComp.endUser is null ";
        }
        else {
            lUserClause = "filterComp.endUser.login = :userLogin ";
        }
        String lStringQuery =
                "SELECT filterComp.id from " + FilterWithResult.class.getName()
                        + " filterComp "
                        + "where filterComp.businessProcess.name = :bPName "
                        + "and " + lProductClause + "and " + lUserClause
                        + "and filterComp.name = :name ";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("name", pFilterName);
        lQuery.setParameter("bPName", pProcessName);
        if (StringUtils.isNotBlank(pProductName)) {
            lQuery.setParameter("productName", pProductName);
        }
        if (StringUtils.isNotBlank(pUserLogin)) {
            lQuery.setParameter("userLogin", pUserLogin);
        }
        return (String) lQuery.uniqueResult();
    }

    @Override
    public Boolean isExists(String pProcessName, String pProductName,
            String pUserLogin, String pFilterName) {
        String lProductClause = "filterComp.product.name = :productName ";
        if (StringUtils.isBlank(pProductName)) {
            lProductClause = "filterComp.product is null ";
        }
        String lUserClause = "filterComp.endUser.login = :userLogin ";
        if (StringUtils.isBlank(pUserLogin)) {
            lUserClause = "filterComp.endUser is null ";
        }
        String lStringQuery =
                "SELECT filterComp.id from " + FilterComponent.class.getName()
                        + " filterComp "
                        + "where filterComp.businessProcess.name = :bPName "
                        + "and " + lProductClause + "and " + lUserClause
                        + "and filterComp.name = :name ";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("name", pFilterName);
        lQuery.setParameter("bPName", pProcessName);
        if (StringUtils.isNotBlank(pProductName)) {
            lQuery.setParameter("productName", pProductName);
        }
        if (StringUtils.isNotBlank(pUserLogin)) {
            lQuery.setParameter("userLogin", pUserLogin);
        }
        return hasResult(lQuery);
    }

    @Override
    public String getCriteriaId(String pFilterId) {
        String lStringQuery =
                "SELECT filterComp.filter.id from "
                        + FilterWithResult.class.getName() + " filterComp "
                        + "where filterComp.id = :pFilterId";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("pFilterId", pFilterId);
        return (String) lQuery.uniqueResult();
    }

    @Override
    public String getResultFieldsId(String pFilterId) {
        String lStringQuery =
                "SELECT filterComp.resultSummary.id from "
                        + FilterWithResult.class.getName() + " filterComp "
                        + "where filterComp.id = :pFilterId";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("pFilterId", pFilterId);
        return (String) lQuery.uniqueResult();
    }

    @Override
    public String getSortFieldsId(String pFilterId) {
        String lStringQuery =
                "SELECT filterComp.resultSorter.id from "
                        + FilterWithResult.class.getName() + " filterComp "
                        + "where filterComp.id = :pFilterId";

        final Query lQuery = getSession(false).createQuery(lStringQuery);
        lQuery.setParameter("pFilterId", pFilterId);
        return (String) lQuery.uniqueResult();
    }

}