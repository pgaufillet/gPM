/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
//import org.apache.log4j.Logger;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.FilterException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.scalar.DateValueData;
import org.topcased.gpm.business.scalar.IntegerValueData;
import org.topcased.gpm.business.scalar.ScalarValueData;
import org.topcased.gpm.business.scalar.StringValueData;
import org.topcased.gpm.business.search.criterias.CriteriaData;
import org.topcased.gpm.business.search.criterias.CriteriaFieldData;
import org.topcased.gpm.business.search.criterias.FilterData;
import org.topcased.gpm.business.search.criterias.OperationData;
import org.topcased.gpm.business.search.criterias.impl.Operators;
import org.topcased.gpm.business.search.impl.fields.UsableFieldsManager;
import org.topcased.gpm.business.search.impl.query.FilterQueryConfigurator;
import org.topcased.gpm.business.search.impl.query.FilterQueryExecutor;
import org.topcased.gpm.business.search.impl.query.FilterQueryGenerator;
import org.topcased.gpm.business.search.impl.query.FilterResultIdIterator;
import org.topcased.gpm.business.search.impl.query.FilterResultIterator;
import org.topcased.gpm.business.search.impl.query.SqlFunction;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.serialization.data.User;
import org.topcased.gpm.business.util.FieldsContainerUtils;
import org.topcased.gpm.domain.fields.ValuesContainer;

/**
 * FilterManager
 * <p>
 * Manage the filters
 * 
 * @author mkargbo
 */
public class FilterManager extends ServiceImplBase {
    /** The Logger. */
//    private static final Logger LOGGER = Logger.getLogger(FilterManager.class);

    private Map<SqlFunction, String> globalSqlFunctions =
            new HashMap<SqlFunction, String>();

    private Map<String, Map<SqlFunction, String>> processSqlFunctions =
            new HashMap<String, Map<SqlFunction, String>>();

    /**
     * Execute the filter. The type of results depends of the given class.
     * <p>
     * Check filter access control.
     * 
     * @param <S>
     *            Type of SummaryData (depends of the given ValuesContainer
     *            class)
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @return Iterator for results
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            final String pRoleToken,
            final ExecutableFilterData pExecutableFilterData,
            final FilterQueryConfigurator pFilterQueryConfigurator)
        throws FilterException, AuthorizationException {
        // FIXME Improve execute methods by factorization (of similar code)
        //checkFilterAccess(pRoleToken, pExecutableFilterData);

        // Load constraint on filter execution
        final FilterAdditionalConstraints lAdditionalConstraints =
                filterAccessManager.getAdditionalConstraints(pRoleToken,
                        pExecutableFilterData);
        final FilterResultIterator<S> lFilterResultIterator;
        if (MapUtils.isNotEmpty(
                lAdditionalConstraints.getExecutionReport().getExecutableProducts())) {

            // get SQL functions
            Map<SqlFunction, String> lSqlFunctions =
                    getSqlFunctions(authorizationService.getProcessNameFromToken(pRoleToken));
            pFilterQueryConfigurator.setSqlFunctions(lSqlFunctions);

            // Get current user login
            String lCurrentUserLogin =
                    authorizationService.getLoginFromToken(pRoleToken);
            pFilterQueryConfigurator.addAddtionalInformation(
                    FilterQueryConfigurator.AdditionalInformationType.CURRENT_USER_LOGIN,
                    lCurrentUserLogin);
            pFilterQueryConfigurator.setGlobalAdminRole(
                    authorizationService.hasGlobalAdminRole(pRoleToken));

            fillCriteriaValue(pRoleToken, pExecutableFilterData.getFilterData());

            Class<? extends ValuesContainer> lClazz =
                    FieldsContainerUtils.getEntityValuesContainerClass(
                            pExecutableFilterData.getFilterData().getType());
            FilterQueryGenerator lFilterQueryGenerator =
                    new FilterQueryGenerator(pExecutableFilterData,
                            lAdditionalConstraints);
            String lQuery =
                    lFilterQueryGenerator.generate(pFilterQueryConfigurator);
            ResultSet lResults = null;
            try {
                lResults =
                        FilterQueryExecutor.execute(lQuery,
                                pFilterQueryConfigurator);
            }
            catch (SQLException e) {
//                LOGGER.error("Invalid query: " + lQuery);
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_QUERY_ERROR, e);
            }

            try {
                lFilterResultIterator =
                        new FilterResultIterator<S>(lClazz,
                                pExecutableFilterData,
                                pFilterQueryConfigurator, lResults,
                                lAdditionalConstraints.getExecutionReport());
            }
            catch (SQLException e) {
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_RESULTS_READING, e);
            }
        }
        else {
            lFilterResultIterator =
                    new FilterResultIterator<S>(pFilterQueryConfigurator,
                            lAdditionalConstraints.getExecutionReport());
        }
        return lFilterResultIterator;
    }

    /**
     * Execute the filter. The result are the container's identifiers.
     * <p>
     * Check filter access control.
     * </p>
     * 
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @return Iterator for results (as String)
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public FilterResultIdIterator executeFilterIdentifier(
            final String pRoleToken,
            final ExecutableFilterData pExecutableFilterData,
            final FilterQueryConfigurator pFilterQueryConfigurator)
        throws FilterException, AuthorizationException {
        checkFilterAccess(pRoleToken, pExecutableFilterData);

        // Load constraint on filter execution
        final FilterAdditionalConstraints lAdditionalConstraints =
                filterAccessManager.getAdditionalConstraints(pRoleToken,
                        pExecutableFilterData);
        final FilterResultIdIterator lFilterResultIdIterator;
        if (MapUtils.isNotEmpty(
                lAdditionalConstraints.getExecutionReport().getExecutableProducts())) {

            // get SQL functions
            Map<SqlFunction, String> lSqlFunctions =
                    getSqlFunctions(authorizationService.getProcessNameFromToken(pRoleToken));
            pFilterQueryConfigurator.setSqlFunctions(lSqlFunctions);

            // Get current user login
            String lCurrentUserLogin =
                    authorizationService.getLoginFromToken(pRoleToken);
            pFilterQueryConfigurator.addAddtionalInformation(
                    FilterQueryConfigurator.AdditionalInformationType.CURRENT_USER_LOGIN,
                    lCurrentUserLogin);
            pFilterQueryConfigurator.setGlobalAdminRole(
                    authorizationService.hasGlobalAdminRole(pRoleToken));
            pFilterQueryConfigurator.setOnlyIdentifier(true);

            fillCriteriaValue(pRoleToken, pExecutableFilterData.getFilterData());

            FilterQueryGenerator lFilterQueryGenerator =
                    new FilterQueryGenerator(pExecutableFilterData,
                            lAdditionalConstraints);
            String lQuery =
                    lFilterQueryGenerator.generate(pFilterQueryConfigurator);
            ResultSet lResults = null;
            try {
                lResults =
                        FilterQueryExecutor.execute(lQuery,
                                pFilterQueryConfigurator);
            }
            catch (SQLException e) {
//                LOGGER.error("Invalid query: " + lQuery);
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_QUERY_ERROR, e);
            }
            try {
                lFilterResultIdIterator =
                        new FilterResultIdIterator(pFilterQueryConfigurator,
                                lResults,
                                lAdditionalConstraints.getExecutionReport());
            }
            catch (SQLException e) {
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_RESULTS_READING, e);
            }
        }
        else {
            lFilterResultIdIterator =
                    new FilterResultIdIterator(pFilterQueryConfigurator,
                            lAdditionalConstraints.getExecutionReport());
        }
        return lFilterResultIdIterator;
    }

    /**
     * Execute the filter. The type of results depends of the given class.
     * <p>
     * This execution does not care about criteria. The selected containers are
     * only those containing in the specified list.
     * </p>
     * <p>
     * This execution is used to retrieve result elements and also to sort
     * containers that have been specified.
     * </p>
     * <p>
     * Check filter access control.
     * </p>
     * 
     * @param <S>
     *            Type of SummaryData (depends of the given ValuesContainer
     *            class)
     * @param pRoleToken
     *            Role token
     * @param pExecutableFilterData
     *            Filter to execute
     * @param pFilterQueryConfigurator
     *            Configuration of the filter
     * @param pContainerIdentifiers
     *            Identifiers of container that must be use in this Filter.
     * @return Iterator for results
     * @throws FilterException
     *             Filter query cannot be execute or Filter results cannot be
     *             read.
     * @throws AuthorizationException
     *             If no rights to execute this filter
     */
    public <S extends SummaryData> FilterResultIterator<S> executeFilter(
            final String pRoleToken,
            final ExecutableFilterData pExecutableFilterData,
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final Collection<String> pContainerIdentifiers)
        throws FilterException, AuthorizationException {
        checkFilterAccess(pRoleToken, pExecutableFilterData);

        // Load constraint on filter execution
        final FilterAdditionalConstraints lAdditionalConstraints =
                filterAccessManager.getAdditionalConstraints(pRoleToken,
                        pExecutableFilterData);
        final FilterResultIterator<S> lFilterResultIterator;
        if (MapUtils.isNotEmpty(lAdditionalConstraints.getExecutionReport().getExecutableProducts())
                && pContainerIdentifiers != null
                && !pContainerIdentifiers.isEmpty()) {
            // get SQL functions
            Map<SqlFunction, String> lSqlFunctions =
                    getSqlFunctions(authorizationService.getProcessNameFromToken(pRoleToken));
            pFilterQueryConfigurator.setSqlFunctions(lSqlFunctions);

            // Get current user login
            String lCurrentUserLogin =
                    authorizationService.getLoginFromToken(pRoleToken);
            pFilterQueryConfigurator.addAddtionalInformation(
                    FilterQueryConfigurator.AdditionalInformationType.CURRENT_USER_LOGIN,
                    lCurrentUserLogin);
            pFilterQueryConfigurator.setGlobalAdminRole(
                    authorizationService.hasGlobalAdminRole(pRoleToken));

            fillCriteriaValue(pRoleToken, pExecutableFilterData.getFilterData());

            Class<? extends ValuesContainer> lClazz =
                    FieldsContainerUtils.getEntityValuesContainerClass(
                            pExecutableFilterData.getFilterData().getType());
            FilterQueryGenerator lFilterQueryGenerator =
                    new FilterQueryGenerator(pExecutableFilterData,
                            lAdditionalConstraints);
            String lQuery =
                    lFilterQueryGenerator.generate(pFilterQueryConfigurator,
                            pContainerIdentifiers);
            ResultSet lResults = null;
            try {
                lResults =
                        FilterQueryExecutor.execute(lQuery,
                                pFilterQueryConfigurator);
            }
            catch (SQLException e) {
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_QUERY_ERROR, e);
            }
            try {
                lFilterResultIterator =
                        new FilterResultIterator<S>(lClazz,
                                pExecutableFilterData,
                                pFilterQueryConfigurator, lResults,
                                lAdditionalConstraints.getExecutionReport());
            }
            catch (SQLException e) {
                throw new FilterException(pExecutableFilterData.getLabelKey(),
                        FilterException.DEFAULT_MESSAGE_RESULTS_READING, e);
            }
        }
        else {
            lFilterResultIterator =
                    new FilterResultIterator<S>(pFilterQueryConfigurator,
                            lAdditionalConstraints.getExecutionReport());
        }
        return lFilterResultIterator;
    }

    private void checkFilterAccess(final String pRoleToken,
            final ExecutableFilterData pFilter) throws AuthorizationException {
        // Apply access control on filter : for additional constraints
        filterAccessManager.applyAccessControl(pRoleToken, pFilter);
        // Filter must be executable
        if (!pFilter.isExecutable()) {
            throw new AuthorizationException("Filter " + pFilter.getLabelKey()
                    + " cannot be executed.");
        }
    }

    /**
     * Fill the criteria value.<br />
     * Fill the dynamic criteria value.
     * <p>
     * <b>Note:</b> Recursive method because a criteria data can be an
     * OperationData (containing CriteriaData) or a CriteriaFieldData.
     * 
     * @param pRoleToken
     *            Role token
     * @param pFilterData
     *            root filter data
     * @param pCriteriaData
     *            Criteria data to fill
     */
    private void fillCriteriaValue(String pRoleToken, FilterData pFilterData) {
        if (pFilterData.getCriteriaData() != null) {
            pFilterData.setCriteriaData(updateCriteriaData(pRoleToken,
                    pFilterData.getCriteriaData()));
        }
    }

    /**
     * Update the criteria data: fill dynamic values and transform some
     * criteria.
     * <p>
     * <b>Note:</b> Recursive method because a criteria data can be an
     * OperationData (containing CriteriaData) or a CriteriaFieldData.
     * 
     * @param pRoleToken
     *            Role token
     * @param pCriteriaData
     *            Criteria data to fill
     * @return the modified CriteriaData
     */
    private CriteriaData updateCriteriaData(String pRoleToken,
            CriteriaData pCriteriaData) {
        CriteriaData lNewData;
        if (pCriteriaData instanceof OperationData) {
            OperationData lOperationData = (OperationData) pCriteriaData;
            CriteriaData[] lSubData = lOperationData.getCriteriaDatas();
            for (int i = 0; i < lSubData.length; i++) {
                lSubData[i] = updateCriteriaData(pRoleToken, lSubData[i]);
            }
            lNewData = pCriteriaData;
        }
        else if (pCriteriaData instanceof CriteriaFieldData) {
            CriteriaFieldData lCriteriaFieldData =
                    (CriteriaFieldData) pCriteriaData;
            lNewData = handleCriterionData(pRoleToken, lCriteriaFieldData);
        }
        else {
            throw new GDMException("Invalid Criteria Data for '"
                    + pCriteriaData.getClass() + "'");
        }
        return lNewData;
    }

    /**
     * Update the criterion.<br />
     * Update the criterion value if necessary with the real value of the
     * dynamic value, or even change the criterion if necessary.
     * 
     * @param pRoleToken
     *            Role token
     * @param pCriteriaFieldData
     *            the criterion
     * @return the updated or new criterion
     */
    private CriteriaData handleCriterionData(String pRoleToken,
            CriteriaFieldData pCriteriaFieldData) {
        // Most of the cases, the same CriteriaData will be returned.
        // Sometimes though, its internal value will be updated,
        // and in some rare cases, The CriteriaFieldData will be
        // transformed into an OperationData (containing two CriteriaFieldDatas).
        // E.g.: a filter on "LAST MONTH" might be transformed into
        // AND(>= 01.11.2011, < 01.12.2011)
        CriteriaData lNewData = pCriteriaFieldData; // default value (most cases)

        String lOperator = pCriteriaFieldData.getOperator();
        ScalarValueData lValue = pCriteriaFieldData.getScalarValueData();
        Calendar lReferenceDate = new GregorianCalendar();
        lReferenceDate.set(Calendar.HOUR_OF_DAY, 0);
        lReferenceDate.set(Calendar.MINUTE, 0);
        lReferenceDate.set(Calendar.SECOND, 0);
        lReferenceDate.set(Calendar.MILLISECOND, 0);
        if (lOperator.equals(Operators.SINCE)
                && lValue instanceof IntegerValueData) {
            int lDays = (Integer) lValue.getValue();
            lReferenceDate.add(Calendar.DAY_OF_MONTH, -lDays);

            pCriteriaFieldData.setOperator(Operators.GE); // >=
            pCriteriaFieldData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

            // End date
            CriteriaFieldData lEndDateData = new CriteriaFieldData();
            lEndDateData.setOperator(Operators.LT); // <
            lReferenceDate.add(Calendar.DAY_OF_MONTH, lDays + 1);
            lEndDateData.setCaseSensitive(Boolean.FALSE);
            lEndDateData.setUsableFieldData(pCriteriaFieldData.getUsableFieldData());
            lEndDateData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

            // AND operation that groups start date and end date
            CriteriaData[] lDataPair = new CriteriaData[2];
            lDataPair[0] = pCriteriaFieldData;
            lDataPair[1] = lEndDateData;
            lNewData = new OperationData(FilterCreator.AND, lDataPair);
        }
        else if (lOperator.equals(Operators.OTHER)
                && lValue instanceof StringValueData) {
            String lStringValue = (String) lValue.getValue();
            pCriteriaFieldData.setOperator(Operators.GE); // >=
            if (lStringValue.equals(UsableFieldsManager.CURRENT_MONTH)) {
                lReferenceDate.set(Calendar.DAY_OF_MONTH, 1);
                pCriteriaFieldData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // End date
                CriteriaFieldData lEndDateData = new CriteriaFieldData();
                lEndDateData.setOperator(Operators.LT); // <
                lReferenceDate.add(Calendar.MONTH, 1);
                lEndDateData.setCaseSensitive(Boolean.FALSE);
                lEndDateData.setUsableFieldData(pCriteriaFieldData.getUsableFieldData());
                lEndDateData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // AND operation that groups start date and end date
                CriteriaData[] lDataPair = new CriteriaData[2];
                lDataPair[0] = pCriteriaFieldData;
                lDataPair[1] = lEndDateData;
                lNewData = new OperationData(FilterCreator.AND, lDataPair);
            }
            else if (lStringValue.equals(UsableFieldsManager.CURRENT_WEEK)) {
                // lReferenceDate.set(Calendar.DAY_OF_WEEK, 0) generates unpredictable results
                while (lReferenceDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    lReferenceDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                pCriteriaFieldData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // End date
                CriteriaFieldData lEndDateData = new CriteriaFieldData();
                lEndDateData.setOperator(Operators.LT); // <
                lReferenceDate.add(Calendar.WEEK_OF_YEAR, 1);
                lEndDateData.setCaseSensitive(Boolean.FALSE);
                lEndDateData.setUsableFieldData(pCriteriaFieldData.getUsableFieldData());
                lEndDateData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // AND operation that groups start date and end date
                CriteriaData[] lDataPair = new CriteriaData[2];
                lDataPair[0] = pCriteriaFieldData;
                lDataPair[1] = lEndDateData;
                lNewData = new OperationData(FilterCreator.AND, lDataPair);
            }
            else if (lStringValue.equals(UsableFieldsManager.PREVIOUS_MONTH)) {
                lReferenceDate.set(Calendar.DAY_OF_MONTH, 1);
                lReferenceDate.add(Calendar.MONTH, -1);
                pCriteriaFieldData.setScalarValueData(new DateValueData(
                        lReferenceDate.getTime()));

                // End date
                CriteriaFieldData lEndDateData = new CriteriaFieldData();
                lEndDateData.setOperator(Operators.LT); // <
                lReferenceDate.add(Calendar.MONTH, 1);
                lEndDateData.setCaseSensitive(Boolean.FALSE);
                lEndDateData.setUsableFieldData(pCriteriaFieldData.getUsableFieldData());
                lEndDateData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // AND operation that groups start date and end date
                CriteriaData[] lDataPair = new CriteriaData[2];
                lDataPair[0] = pCriteriaFieldData;
                lDataPair[1] = lEndDateData;
                lNewData = new OperationData(FilterCreator.AND, lDataPair);
            }
            else if (lStringValue.equals(UsableFieldsManager.PREVIOUS_WEEK)) {
                // lReferenceDate.set(Calendar.DAY_OF_WEEK, 0) generates unpredictable results 
                while (lReferenceDate.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                    lReferenceDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                lReferenceDate.add(Calendar.WEEK_OF_YEAR, -1);
                pCriteriaFieldData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // End date
                CriteriaFieldData lEndDateData = new CriteriaFieldData();
                lEndDateData.setOperator(Operators.LT); // <
                lReferenceDate.add(Calendar.WEEK_OF_YEAR, 1);
                lEndDateData.setCaseSensitive(Boolean.FALSE);
                lEndDateData.setUsableFieldData(pCriteriaFieldData.getUsableFieldData());
                lEndDateData.setScalarValueData(new DateValueData(lReferenceDate.getTime()));

                // AND operation that groups start date and end date
                CriteriaData[] lDataPair = new CriteriaData[2];
                lDataPair[0] = pCriteriaFieldData;
                lDataPair[1] = lEndDateData;
                lNewData = new OperationData(FilterCreator.AND, lDataPair);
            }
        }
        else if (lValue instanceof StringValueData) {
            String lStringValue = ((StringValueData) lValue).getValue();

            // Handle current product dynamic value
            if (UsableFieldsManager.CURRENT_PRODUCT.equals(lStringValue)) {
                String lCurrentProduct =
                    getAuthService().getProductNameFromSessionToken(pRoleToken);
                pCriteriaFieldData.setScalarValueData(SearchUtils.getScalarValue(lCurrentProduct));
            }
            else if (UsableFieldsManager.CURRENT_USER_LOGIN.equals(lStringValue)) {
                String lCurrentUser =
                        getAuthService().getLoginFromToken(pRoleToken);
                pCriteriaFieldData.setScalarValueData(SearchUtils.getScalarValue(lCurrentUser));
            }
            else if (UsableFieldsManager.CURRENT_USER_NAME.equals(lStringValue)) {
                User lUser = getAuthService().getUserFromRole(pRoleToken);
                String lCurrentUserName = lUser.getName();
                pCriteriaFieldData.setScalarValueData(SearchUtils.getScalarValue(lCurrentUserName));
            }
        }
        return lNewData;
    }

    /**
     * The default max number of results (setting by properties file, the
     * property name is 'filterMaxResults'
     */
    private int maxNbResults;

    /**
     * Set the maximum number of elements returned by a search
     * 
     * @param pMaxNbResults
     *            the maxNbResults to set
     */
    public final void setMaxNbResults(int pMaxNbResults) {
        maxNbResults = pMaxNbResults;
    }

    /**
     * Get the max number of results to display for a filter
     * 
     * @return the max number of results
     */
    public int getMaxNbResults() {
        return maxNbResults;
    }

    /**
     * Set the case sensitive function used in SQL
     * 
     * @param pCaseSensitiveSqlFunction
     *            The case sensitive function
     */
    public void setCaseSensitiveSqlFunction(String pCaseSensitiveSqlFunction) {
        globalSqlFunctions.put(SqlFunction.CASE_SENSITIVE,
                pCaseSensitiveSqlFunction);
    }

    private Map<SqlFunction, String> getSqlFunctions(String pProcessName) {

        Map<SqlFunction, String> lSqlFunctions =
                processSqlFunctions.get(pProcessName);

        if (lSqlFunctions == null) {

            // init process with global map
            lSqlFunctions =
                    new HashMap<SqlFunction, String>(globalSqlFunctions);

            AttributeData[] lAttributeDatas =
                    getAttributesService().get(
                            getBusinessProcess(pProcessName).getId(),
                            new String[] { SqlFunction.CASE_SENSITIVE.toString() });

            if (lAttributeDatas[0] != null) {
                lSqlFunctions.put(SqlFunction.CASE_SENSITIVE,
                        lAttributeDatas[0].getValues()[0]);
            }

            processSqlFunctions.put(pProcessName, lSqlFunctions);
        }

        return lSqlFunctions;
    }
}
