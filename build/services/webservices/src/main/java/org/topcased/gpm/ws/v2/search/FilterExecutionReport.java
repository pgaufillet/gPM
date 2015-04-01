/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien EBALLARD (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ws.v2.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.authorization.impl.filter.FilterAccessContraint;
import org.topcased.gpm.business.search.impl.fields.UsableTypeData;

/**
 * FilterExecutionReport
 * 
 * @author jeballar
 */
public class FilterExecutionReport implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 311364852548270950L;

    /** List of non executable products */
    private List<String> nonExecutableProducts = new ArrayList<String>();

    /** Executable products map */
    private FilterExecutionReportExecutableProductsMap executableProducts =
            new FilterExecutionReportExecutableProductsMap();

    /** Additional constraints map */
    private FilterExecutionReportAdditionalConstraintsMap additionalConstraints =
            new FilterExecutionReportAdditionalConstraintsMap();

    /**
     * Default empty Constructor
     */
    public FilterExecutionReport() {
    }

    /**
     * Constructor from the business filter execution report
     * 
     * @param pFilterExecutionReport
     *            the business filter execution report
     */
    public FilterExecutionReport(
            org.topcased.gpm.business.authorization.impl.filter.FilterExecutionReport pFilterExecutionReport) {
        // Non executable product
        nonExecutableProducts =
                new ArrayList<String>(
                        pFilterExecutionReport.getNonExecutableProducts());

        // Executable products
        for (String lKey : pFilterExecutionReport.getExecutableProducts().keySet()) {
            executableProducts.put(lKey, new ArrayList<String>(
                    pFilterExecutionReport.getExecutableProducts().get(lKey)));
        }

        // Additional Constraints
        for (UsableTypeData lPrimaryKey : pFilterExecutionReport.getAdditionalConstraints().keySet()) {
            FilterExecutionReportConstraintsMap lSecondaryMap =
                    new FilterExecutionReportConstraintsMap();
            for (String lSecondaryKey : pFilterExecutionReport.getAdditionalConstraints().get(
                    lPrimaryKey).keySet()) {
                FilterAccessContraint[] lTable =
                        new FilterAccessContraint[pFilterExecutionReport.getAdditionalConstraints().get(
                                lPrimaryKey).get(lSecondaryKey).size()];
                lSecondaryMap.put(lSecondaryKey,
                        pFilterExecutionReport.getAdditionalConstraints().get(
                                lPrimaryKey).get(lSecondaryKey).toArray(lTable));
            }
            additionalConstraints.put(lPrimaryKey, lSecondaryMap);
        }
    }

    /**
     * Get nonExecutableProducts
     * 
     * @return the nonExecutableProducts
     */
    public List<String> getNonExecutableProducts() {
        return nonExecutableProducts;
    }

    /**
     * Set nonExecutableProducts
     * 
     * @param pNonExecutableProducts
     *            the nonExecutableProducts to set
     */
    public void setNonExecutableProducts(List<String> pNonExecutableProducts) {
        nonExecutableProducts = pNonExecutableProducts;
    }

    /**
     * Get executableProducts
     * 
     * @return the executableProducts
     */
    public FilterExecutionReportExecutableProductsMap getExecutableProducts() {
        return executableProducts;
    }

    /**
     * Set executableProducts
     * 
     * @param pExecutableProducts
     *            the executableProducts to set
     */
    public void setExecutableProducts(
            FilterExecutionReportExecutableProductsMap pExecutableProducts) {
        executableProducts = pExecutableProducts;
    }

    /**
     * Get additionalConstraints
     * 
     * @return the additionalConstraints
     */
    public FilterExecutionReportAdditionalConstraintsMap getAdditionalConstraints() {
        return additionalConstraints;
    }

    /**
     * Set additionalConstraints
     * 
     * @param pAdditionalConstraints
     *            the additionalConstraints to set
     */
    public void setAdditionalConstraints(
            FilterExecutionReportAdditionalConstraintsMap pAdditionalConstraints) {
        additionalConstraints = pAdditionalConstraints;
    }
}
