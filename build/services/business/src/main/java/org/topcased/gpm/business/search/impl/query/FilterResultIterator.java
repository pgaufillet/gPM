/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.impl.filter.FilterExecutionReport;
import org.topcased.gpm.business.exception.MethodNotImplementedException;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.fields.SummaryData;
import org.topcased.gpm.business.link.service.LinkSummaryData;
import org.topcased.gpm.business.product.service.ProductSummaryData;
import org.topcased.gpm.business.search.result.summary.ResultSummaryData;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.business.search.service.UsableFieldData;
import org.topcased.gpm.business.sheet.service.LockData;
import org.topcased.gpm.business.sheet.service.SheetSummaryData;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.domain.fields.ValuesContainer;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.util.FieldsUtil;

/**
 * FilterResultIterator iterates on filter's results.
 * <p>
 * Its an overlay of {@link ResultSet} and so the connexion to the database
 * still open.
 * </p>
 * <p>
 * Connexion is closed at the end of iteration.
 * </p>
 * <p>
 * Result's object {@link SummaryData} have been created during the iteration.
 * </p>
 * 
 * @param <S> a SummaryData subclass
 * 
 * @author mkargbo
 */
public class FilterResultIterator<S extends SummaryData> implements Iterator<S> {
    private Connection connection;

    private Statement statement;

    private ResultSet resultSet;

    private FilterQueryConfigurator filterQueryConfigurator;

    private SummaryDataCreator summaryDataCreator;

    private final FilterExecutionReport executionReport;

    /**
     * Default constructor.
     * <p>
     * Initialize the report.
     * </p>
     */
    public FilterResultIterator() {
        executionReport = new FilterExecutionReport();
    }

    /**
     * Constructor with a configuration and report.
     * 
     * @param pFilterQueryConfigurator
     *            Query configuration to know start of resultSet
     * @param pExecutionReport
     *            Report
     */
    public FilterResultIterator(
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final FilterExecutionReport pExecutionReport) {
        filterQueryConfigurator = pFilterQueryConfigurator;
        executionReport = pExecutionReport;
    }

    /**
     * Constructor.
     * 
     * @param pClazz
     *            Type of filter
     * @param pExecutableFilterData
     *            Filter
     * @param pFilterQueryConfigurator
     *            Query configuration to know start of resultSet
     * @param pResultSet
     *            Database iterator
     * @param pExecutionReport
     *            Report
     * @throws SQLException
     *             Error during fields creation (bad index), connexion closing.
     */
    public FilterResultIterator(final Class<? extends ValuesContainer> pClazz,
            final ExecutableFilterData pExecutableFilterData,
            final FilterQueryConfigurator pFilterQueryConfigurator,
            final ResultSet pResultSet,
            final FilterExecutionReport pExecutionReport) throws SQLException {
        resultSet = pResultSet;
        statement = pResultSet.getStatement();
        connection = pResultSet.getStatement().getConnection();
        filterQueryConfigurator = pFilterQueryConfigurator;
        summaryDataCreator =
                new SummaryDataCreator(pClazz, pExecutableFilterData);

        //Move to start
        if (filterQueryConfigurator.getStartFrom() > 0) {
            for (int i = 0; i < filterQueryConfigurator.getStartFrom(); i++) {
                resultSet.next();
            }
        }

        executionReport = pExecutionReport;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return has(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    public S next() {
        return currentElement();
    }

    /**
     * Return true if has previous
     * 
     * @return true if has previous
     */
    public boolean hasPrevious() {
        return has(false);
    }

    /**
     * Get previous
     * 
     * @return previous
     */
    public S previous() {
        return currentElement();
    }

    private S currentElement() {
        S lSummaryData = null;
        if (resultSet != null) {

            try {
                lSummaryData = summaryDataCreator.create();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lSummaryData;
    }

    private boolean has(boolean pNextOrPrevious) {
        boolean lEnd = true;

        if (resultSet != null) {
            try {
                if (pNextOrPrevious) {
                    lEnd = !resultSet.next();
                }
                else {
                    lEnd = !resultSet.previous();
                }
            }
            catch (SQLException e) {
                lEnd = true;
            }
        }

        if (lEnd) {
            close();
        }

        return !lEnd;
    }

    /**
     * Close the connection used by the iterator.
     */
    public void close() {
        if (resultSet != null) {
            try {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (SQLException e) {
                // Try to close a second time
                try {
                    resultSet.close();
                    statement.close();
                    connection.close();
                }
                catch (SQLException e1) {
                    // Cannot close SQL exception
                }
            }
            resultSet = null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new MethodNotImplementedException(
                "Cannot remove an element of a " + getClass().getName());
    }

    public static final int DEFAULT_RESULT_NUMBER = 7;

    public static final int ID_RESULT_NUMBER = 1;

    public static final int SHEET_REFERENCE_RESULT_NUMBER = 2;

    public static final int LOCK_OWNER_RESULT_NUMBER = 3;

    public static final int LOCK_TYPE_RESULT_NUMBER = 4;

    public static final int SHEET_TYPE_ID_RESULT_NUMBER = 5;

    public static final int SHEET_TYPE_NAME_RESULT_NUMBER = 6;

    /**
     * Creates the summary data according to the given type.
     * 
     * @param <S>
     *            Type of SummaryData to create
     */
    private class SummaryDataCreator {

        private Class<? extends ValuesContainer> clazz;

        private ExecutableFilterData executableFilterData;

        public SummaryDataCreator(Class<? extends ValuesContainer> pClazz,
                ExecutableFilterData pExecutableFilterData) {
            clazz = pClazz;
            executableFilterData = pExecutableFilterData;
        }

        /**
         * Creates the SummaryData from result's objects according to the given
         * type (class).
         * 
         * @param pRoleToken
         *            Role token
         * @param pResults
         *            Results to transform
         * @param pContext
         *            Context
         * @return List of results transformed into SummaryData.
         * @throws SQLException
         */
        public S create() throws IllegalArgumentException, SQLException {
            if (clazz.isAssignableFrom(Product.class)) {
                return createProductSummaryData();
            }
            else if (clazz.isAssignableFrom(Sheet.class)) {
                return createSheetSummaryData();
            }
            else if (clazz.isAssignableFrom(Link.class)) {
                return createLinkSummaryData();
            }
            else {
                throw new IllegalArgumentException(
                        "Cannot determine the filter type for the type '"
                                + clazz + "'");
            }
        }

        /**
         * Create the ProductSummaryData structures from the query results
         * obtained in "second filter query" (query for result table)
         * 
         * @param pQueryResults
         *            the Result table obtained after HQL query execution
         * @param pResultSummaryData
         *            the data structure containing all result field columns
         * @param pResultSortingData
         *            the data structure containing all field used in query
         *            ORDER BY clause
         * @return the list of ProductSummaryData obtained (one per line)
         * @throws SQLException
         */
        @SuppressWarnings("unchecked")
        private S createProductSummaryData() throws SQLException {
            final int lIdIndex = 1;
            final int lNameIndex = 2;
            final int lFieldSummaryDatasIndex = 3;
            /*
             * lObject contains : - Sheet ID - Sheet Reference - SheetType name -
             * SheetType ID - fields from ResultSummary in correct order
             */
            ProductSummaryData lProductSummaryData = new ProductSummaryData();

            lProductSummaryData.setId(resultSet.getString(lIdIndex));
            lProductSummaryData.setName(resultSet.getString(lNameIndex));

            ResultSummaryData lResultSummaryData =
                    executableFilterData.getResultSummaryData();
            FieldSummaryData[] lFieldSummaryDatas =
                    createFieldSummaryDataArray(
                            lResultSummaryData.getUsableFieldDatas(), lFieldSummaryDatasIndex);

            lProductSummaryData.setFieldSummaryDatas(lFieldSummaryDatas);
            return (S) lProductSummaryData;
        }

        /**
         * Create the SheetSummary data structure from the query results
         * obtained in "second filter query" (query for result table)
         * 
         * @param pQueryResults
         *            the Result table obtained after HQL query execution
         * @param pResultSummaryData
         *            the data structure containing all result field columns
         * @param pResultSortingData
         *            the data structure containing all field used in query
         *            ORDER BY clause
         * @return the list of SheetSummaryData obtained (one per line)
         * @throws SQLException
         */
        @SuppressWarnings("unchecked")
        private S createSheetSummaryData() throws SQLException {

            final SheetSummaryData lSheetSummaryData;
            int lElementNumber = resultSet.getMetaData().getColumnCount();

            /*
             * lObject contains :
             * - Sheet ID
             * - Sheet Reference
             * - Lock
             * - SheetType name
             * - SheetType ID
             * - fields from ResultSummary in correct order
             */
            if (lElementNumber >= DEFAULT_RESULT_NUMBER) {
                // Construction of the sheet summary
                lSheetSummaryData = new SheetSummaryData();

                // Set sheet informations
                lSheetSummaryData.setId(resultSet.getString(ID_RESULT_NUMBER));
                lSheetSummaryData.setSheetReference(
                        resultSet.getString(SHEET_REFERENCE_RESULT_NUMBER));
                String lOwner = resultSet.getString(LOCK_OWNER_RESULT_NUMBER);
                String lLockType = resultSet.getString(LOCK_TYPE_RESULT_NUMBER);
                if (StringUtils.isNotBlank(lOwner)
                        && StringUtils.isNotBlank(lLockType)) {
                    lSheetSummaryData.setLock(new LockData(lOwner,
                            LockType.fromString(lLockType)));
                }
                lSheetSummaryData.setSheetType(resultSet.getString(SHEET_TYPE_NAME_RESULT_NUMBER));
                lSheetSummaryData.setSheetTypeId(resultSet.getString(SHEET_TYPE_ID_RESULT_NUMBER));

                //Set results
                ResultSummaryData lResultSummaryData =
                        executableFilterData.getResultSummaryData();
                FieldSummaryData[] lFieldSummaryDatas =
                        createFieldSummaryDataArray(
                                lResultSummaryData.getUsableFieldDatas(),
                                DEFAULT_RESULT_NUMBER);

                lSheetSummaryData.setFieldSummaryDatas(lFieldSummaryDatas);
                lSheetSummaryData.setSelectable(true);
            }
            else {
                lSheetSummaryData = null;
            }
            return (S) lSheetSummaryData;
        }

        /**
         * Create the LinkSummary data structure from the query results obtained
         * in "second filter query" (query for result table)
         * 
         * @param pQueryResults
         *            the Result table obtained after HQL query execution
         * @param pResultSummaryData
         *            the data structure containing all result field columns
         * @param pResultSortingData
         *            the data structure containing all field used in query
         *            ORDER BY clause
         * @return the list of SheetSummaryData obtained (one per line)
         * @throws SQLException
         */
        @SuppressWarnings("unchecked")
        private S createLinkSummaryData() throws SQLException {

            LinkSummaryData lLinkSummaryData = null;
            int lElementNumber = resultSet.getMetaData().getColumnCount();
            // lObject contains : - Sheet ID - fields from ResultSummary in correct order
            if (lElementNumber >= 1) {
                // Construction of the sheet summary
                lLinkSummaryData = new LinkSummaryData();

                // Set the first infos in result
                lLinkSummaryData.setId(resultSet.getString(1));

                ResultSummaryData lResultSummaryData =
                        executableFilterData.getResultSummaryData();
                // Set the other field summary data
                FieldSummaryData[] lFieldSummaryDatas =
                        createFieldSummaryDataArray(
                                lResultSummaryData.getUsableFieldDatas(), 2);
                lLinkSummaryData.setFieldSummaryDatas(lFieldSummaryDatas);
            }
            return (S) lLinkSummaryData;
        }

        /**
         * Create an array of FieldSummaryData from the filter results query
         * 
         * @param pLang
         *            Preferred language (used to translate the field label)
         * @param pUsableFieldData
         *            Array of usable field data for a complete result (for each
         *            field present in the result).
         * @param pResultsIterator
         *            Iterator on query results (typed as 'Object')
         * @return Array of FieldSummaryData
         * @throws SQLException
         */
        private FieldSummaryData[] createFieldSummaryDataArray(
                UsableFieldData[] pUsableFieldData, int pColumnIndex)
            throws SQLException {
            int lFilterResultsNb = pUsableFieldData.length;
            FieldSummaryData[] lFieldSummaryData =
                    new FieldSummaryData[lFilterResultsNb];
            int lIndex = pColumnIndex;

            for (int i = 0; i < lFilterResultsNb; i++) {
                //if no more result
                lFieldSummaryData[i] =
                        createFieldSummaryData(pUsableFieldData[i], lIndex);
                lIndex++;
            }
            return lFieldSummaryData;
        }

        /**
         * Create a single FieldSulmmaryData from a filter results query.
         * 
         * @param pLang
         *            Preferred language of the user (used to translate the
         *            field label)
         * @param pUsableFieldData
         *            Meta-info for the field
         * @param pValue
         *            Actual value of the field
         * @return Newly created FieldSummaryData
         * @throws SQLException
         */
        private FieldSummaryData createFieldSummaryData(
                UsableFieldData pUsableFieldData, int pIndex)
            throws SQLException {
            Object lValueObject = resultSet.getObject(pIndex);
            String lValue = null;
            if (lValueObject != null) {
                lValue = lValueObject.toString();
            }
            if (FieldType.SIMPLE_BOOLEAN_FIELD.equals(pUsableFieldData.getFieldType())) {
                if (lValueObject != null) {
                    lValue = Boolean.toString(resultSet.getBoolean(pIndex));
                }
            }
            else if (FieldType.SIMPLE_DATE_FIELD.equals(pUsableFieldData.getFieldType())) {
                lValue = FieldsUtil.formatDate(resultSet.getTimestamp(pIndex));
            }
            else if (FieldType.SIMPLE_INTEGER_FIELD.equals(pUsableFieldData.getFieldType())) {
                if (lValueObject != null) {
                    lValue = Integer.toString(resultSet.getInt(pIndex));
                }
            }
            else if (FieldType.SIMPLE_REAL_FIELD.equals(pUsableFieldData.getFieldType())) {
                if (lValueObject != null) {
                    lValue = Double.toString(resultSet.getDouble(pIndex));
                }
            }
            else if (FieldType.SIMPLE_STRING_FIELD.equals(pUsableFieldData.getFieldType())) {
                lValue = resultSet.getString(pIndex);
            }

            return new FieldSummaryData(pUsableFieldData.getId(),
                    "no more translation", lValue,
                    pUsableFieldData.getFieldType().toString());
        }
    }

    /**
     * Get the execution report.
     * 
     * @return The execution report.
     */
    public FilterExecutionReport getExecutionReport() {
        return executionReport;
    }
}
