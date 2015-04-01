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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.NotImplementedException;
import org.topcased.gpm.business.authorization.impl.filter.FilterAdditionalConstraints;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.search.criterias.impl.GPMQuery;
import org.topcased.gpm.business.search.service.ExecutableFilterData;
import org.topcased.gpm.util.lang.StringUtils;

/**
 * Generator for a filter.
 * <p>
 * The filter generator is separated in two classes:
 * <ol>
 * <li>Generator for filters on one fields container</li>
 * <li>Generator for filters on many fields container</li>
 * </ol>
 * 
 * @author mkargbo
 */
public class FilterQueryGenerator implements IQueryGenerator {
    private final ExecutableFilterData filter;

    private final FilterAdditionalConstraints additionalConstraints;

    protected GPMQuery gpmQuery;

    private static String staticStringQuery = null;

    /**
     * FilterQueryGenerator constructor.
     * 
     * @param pFilter
     *            Filter to analyze for query generating.
     * @param pAdditionalConstraints
     *            The additional constraints to add on the filter.
     */
    public FilterQueryGenerator(final ExecutableFilterData pFilter,
            final FilterAdditionalConstraints pAdditionalConstraints) {
        filter = pFilter;
        additionalConstraints = pAdditionalConstraints;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Call to (depends of number of fields container):
     * <ol>
     * <li>{@link MonoFieldsContainerQueryGenerator}</li>
     * <li>{@link MultiFieldsContainerQueryGenerator}</li>
     * </ol>
     * <p>
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate()
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator) {
        if (filter.getFilterData().getFieldsContainerIds().length == 1) {
            return new MonoFieldsContainerQueryGenerator(filter,
                    additionalConstraints,
                    filter.getFilterData().getFieldsContainerIds()[0], false,
                    new GPMQuery(false)).generate(pFilterQueryConfigurator);
        }
        else {
            return new MultiFieldsContainerQueryGenerator(filter,
                    additionalConstraints).generate(pFilterQueryConfigurator);
        }
    }

    /**
     * Generate a query
     * 
     * @param pFilterQueryConfigurator the filter configurator
     * @param pContainerIdentifiers the container(s)
     * @return a query
     */
    public String generate(FilterQueryConfigurator pFilterQueryConfigurator,
            Collection<String> pContainerIdentifiers) {
        if (filter.getFilterData().getFieldsContainerIds().length == 1) {
            return new MonoFieldsContainerQueryGenerator(filter,
                    additionalConstraints,
                    filter.getFilterData().getFieldsContainerIds()[0], false,
                    new GPMQuery(false)).generate(pFilterQueryConfigurator,
                    pContainerIdentifiers);
        }
        else {
            return new MultiFieldsContainerQueryGenerator(filter,
                    additionalConstraints).generate(pFilterQueryConfigurator,
                    pContainerIdentifiers);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.search.impl.query.IQueryGenerator#generate(org.topcased.gpm.business.search.criterias.impl.GPMQuery)
     * @deprecated
     */
    public void generate(GPMQuery pQuery,
            FilterQueryConfigurator pFilterQueryConfigurator) {
        throw new NotImplementedException(
                "This method cannot generate a filter query");

    }

    /**
     * Order parameter Map according to the parameter order in the query
     * 
     * @param pQuery
     *            The SQL query
     * @param pNativeParameterMap
     *            The original parameter map, entries may not be in the correct
     *            order
     * @return Map<Integer, QueryParameter> A new Map with with correct entries
     *         order
     * @throws GDMException
     *             An exception is raised when query parameter position is -1
     *             which means an error occur when the parameter's position was
     *             setting
     */
    public static Map<Integer, QueryParameter> computeStringQueryParameterPosition(
            String pQuery, Map<Integer, QueryParameter> pNativeParameterMap) {
        StringTokenizer lStringTokenizer = new StringTokenizer(pQuery);
        setStringQuery(pQuery);
        Map<Integer, QueryParameter> lNewParameterMap =
                new HashMap<Integer, QueryParameter>(pNativeParameterMap.size());

        // buffer counter for parameter position in the query
        int lPositionInQuery = 1;

        // browse all tokens
        while (lStringTokenizer.hasMoreTokens()) {
            String lToken = lStringTokenizer.nextToken();
            // Check if the token is a uni code token
            if (org.apache.commons.lang.StringUtils.isNotBlank(lToken)
                    && lToken.contains(StringUtils.UNI_CODE_TOKEN)) {

                // get the parameter position by the token, 
                //which is similar to the parameter index in the Map
                int lPositionInMap = StringUtils.getPositinFromTag(lToken);

                // magic value, raised when an error occur
                if (lPositionInMap != -1) {
                    // replace the current tag by "?"which is the correct SQL parameter symbol
                    staticStringQuery = staticStringQuery.replaceAll(" " + lToken + " ", ("?"));

                    // get the parameter value form the native map 
                    // then put it in the new map at the same position
                    // as the parameter position in the query 
                    lNewParameterMap.put(lPositionInQuery,
                            pNativeParameterMap.get(lPositionInMap));

                    // go to the next position in the query
                    lPositionInQuery++;
                }
                else {
                    throw new GDMException("Error in query parameter !");
                }

            }
        }
        return lNewParameterMap;
    }

    /**
     * Retrieves the string query in which all uni code tag have been replaced
     * by <code>"?"</code>
     * 
     * @return string query or <code>null</code> if the native query has no
     *         criteria
     */
    public static String getStringQuery() {
        return staticStringQuery;
    }

    private static void setStringQuery(String pStringQuery) {
        staticStringQuery = pStringQuery;
    }
}
