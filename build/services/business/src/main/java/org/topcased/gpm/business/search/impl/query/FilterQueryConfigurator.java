/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: XXX (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.search.impl.query;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.business.fields.FieldType;
import org.topcased.gpm.business.search.impl.FilterManager;

/**
 * FilterQueryConfigurator
 * 
 * @author mkargbo
 */
public class FilterQueryConfigurator {
    /**
     * The number of max row is static for all the processes : mist be
     * initialized -> use the getDefaultMaxRows method
     */
    private static int staticDefaultMaxRow = -1;

    /**
     * staticDefaultMaxRow initialized at the first call : cannot be initialized
     * with a static block because of spring injection using
     */
    private final static int getDefaultMaxRows() {
        if (staticDefaultMaxRow == -1) {
            staticDefaultMaxRow =
                    ((FilterManager) ContextLocator.getContext().getBean(
                            "filterManager")).getMaxNbResults();
        }

        return staticDefaultMaxRow;
    }

    private Map<Integer, QueryParameter> parameters =
            new HashMap<Integer, QueryParameter>();

    private int blockSize = 0;

    private int maxResult = 0;

    private int startFrom = 0;

    private int end = 0;

    private boolean onlyIdentifier = false;

    private Map<SqlFunction, String> sqlFunctions =
            new HashMap<SqlFunction, String>();

    private int index;

    private boolean globalAdminRole;

    /**
     * Additional information
     */
    private Map<AdditionalInformationType, String> additionalInformation =
            new HashMap<AdditionalInformationType, String>();

    /**
     * Constructor
     */
    public FilterQueryConfigurator() {
        maxResult = getDefaultMaxRows();
    }

    /**
     * Constructor
     * 
     * @param pBlockSize the block size
     * @param pEnd the end index
     * @param pStartFrom the start index
     */
    public FilterQueryConfigurator(int pBlockSize, int pEnd, int pStartFrom) {
        blockSize = pBlockSize;

        if (pEnd == -1) {
            end = 0;
        }
        else {
            end = pEnd;
        }

        if (pStartFrom == -1) {
            startFrom = 0;
        }
        else {
            startFrom = pStartFrom;
        }

        final int lDefaultMaxRow = getDefaultMaxRows();
        final int lMax = end + startFrom;

        if ((lMax < 1) || (lMax > lDefaultMaxRow)) {
            maxResult = lDefaultMaxRow;
        }
        else {
            maxResult = lMax;
        }
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int pBlockSize) {
        blockSize = pBlockSize;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public int getStartFrom() {
        return startFrom;
    }

    /**
     * Set start index
     * 
     * @param pStartFrom the start index
     */
    public void setStartFrom(int pStartFrom) {
        if (pStartFrom == -1) {
            startFrom = 0;
        }
        else {
            startFrom = pStartFrom;
        }
    }

    public int getEnd() {
        return end;
    }

    /**
     * Set end index
     * 
     * @param pEnd the end index
     */
    public void setEnd(int pEnd) {
        if (pEnd == -1) {
            end = 0;
        }
        else {
            end = pEnd;
        }
    }

    public Map<Integer, QueryParameter> getParameters() {
        return parameters;
    }

    /**
     * Add parameters
     * 
     * @param pParameter the parameter
     * @param pType the type
     * @param pIndex the index
     */
    public void addParameters(Object pParameter, FieldType pType, int pIndex) {
        int lSqlType = toSQLType(pType);
        parameters.put(pIndex, new QueryParameter(pParameter, lSqlType, pIndex));
    }

    /**
     * Add parameters
     * 
     * @param pParameters the parameter map
     */
    public void addParameters(Map<Integer, QueryParameter> pParameters) {
        parameters.putAll(pParameters);
    }

    /**
     * set sqlFunctions
     * 
     * @param pSqlFunctions
     *            the sqlFunctions to set
     */
    public void setSqlFunctions(Map<SqlFunction, String> pSqlFunctions) {
        sqlFunctions = pSqlFunctions;
    }

    /**
     * get sqlFunctions
     * 
     * @return the sqlFunctions
     */
    public Map<SqlFunction, String> getSqlFunctions() {
        return sqlFunctions;
    }

    /**
     * Increment index
     * 
     * @return the new index
     */
    public int increment() {
        index++;
        return index;
    }

    public boolean isOnlyIdentifier() {
        return onlyIdentifier;
    }

    public void setOnlyIdentifier(boolean pOnlyIdentifier) {
        onlyIdentifier = pOnlyIdentifier;
    }

    /**
     * Convert to SQL type
     * 
     * @param pType the field type
     * @return the SQL type
     */
    public static int toSQLType(FieldType pType) {
        switch (pType) {
            case SIMPLE_STRING_FIELD:
                return Types.VARCHAR;
            case SIMPLE_INTEGER_FIELD:
                return Types.INTEGER;
            case SIMPLE_REAL_FIELD:
                return Types.REAL;
            case SIMPLE_BOOLEAN_FIELD:
                return Types.BOOLEAN;
            case SIMPLE_DATE_FIELD:
                return Types.TIMESTAMP;
            default:
                throw new NotImplementedException("Type '" + pType
                        + "' is not yet supported");
        }
    }

    /**
     * Add additional information
     * 
     * @param pType additional information type
     * @param pInformation information string
     */
    public void addAddtionalInformation(AdditionalInformationType pType,
            String pInformation) {
        additionalInformation.put(pType, pInformation);
    }

    /**
     * get information
     * 
     * @param pType additional information type
     * @return the information
     */
    public String getInformation(AdditionalInformationType pType) {
        return additionalInformation.get(pType);
    }

    /**
     * AdditionalInformationType enum
     */
    public enum AdditionalInformationType {
        CURRENT_USER_LOGIN();
    }

    /**
     * Get if has global admin role.
     * 
     * @return If has global admin role.
     */
    public boolean hasGlobalAdminRole() {
        return globalAdminRole;
    }

    /**
     * Set if has global admin role.
     * 
     * @param pGlobalAdminRole
     *            If has global admin role.
     */
    public void setGlobalAdminRole(final boolean pGlobalAdminRole) {
        globalAdminRole = pGlobalAdminRole;
    }
}
