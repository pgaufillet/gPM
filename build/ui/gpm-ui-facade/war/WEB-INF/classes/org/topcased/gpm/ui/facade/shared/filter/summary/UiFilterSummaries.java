/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.summary;

import java.io.Serializable;
import java.util.List;

/**
 * UiFilterSummaries
 * 
 * @author nveillet
 */
public class UiFilterSummaries implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -7579758850355178643L;

    private List<UiFilterSummary> tableProcessFilters;

    private List<UiFilterSummary> tableProductFilters;

    private List<UiFilterSummary> tableUserFilters;

    private List<UiFilterSummary> treeFilters;

    /**
     * Create UiFilterSummaries
     */
    public UiFilterSummaries() {
    }

    /**
     * Constructor with values
     * 
     * @param pTableProcessFilters
     *            the table process filters
     * @param pTableProductFilters
     *            the table product filters
     * @param pTableUserFilters
     *            the tableUserFilters
     * @param pTreeFilters
     *            the tree filters
     */
    public UiFilterSummaries(List<UiFilterSummary> pTableProcessFilters,
            List<UiFilterSummary> pTableProductFilters,
            List<UiFilterSummary> pTableUserFilters,
            List<UiFilterSummary> pTreeFilters) {
        tableProcessFilters = pTableProcessFilters;
        tableProductFilters = pTableProductFilters;
        tableUserFilters = pTableUserFilters;
        treeFilters = pTreeFilters;
    }

    /**
     * get table process filters
     * 
     * @return the table process filters
     */
    public List<UiFilterSummary> getTableProcessFilters() {
        return tableProcessFilters;
    }

    /**
     * get table product filters
     * 
     * @return the table product filters
     */
    public List<UiFilterSummary> getTableProductFilters() {
        return tableProductFilters;
    }

    /**
     * get table user filters
     * 
     * @return the table user filters
     */
    public List<UiFilterSummary> getTableUserFilters() {
        return tableUserFilters;
    }

    /**
     * get tree filters
     * 
     * @return the tree filters
     */
    public List<UiFilterSummary> getTreeFilters() {
        return treeFilters;
    }

    /**
     * set table process filters
     * 
     * @param pTableProcessFilters
     *            the table process filters to set
     */
    public void setTableProcessFilters(
            List<UiFilterSummary> pTableProcessFilters) {
        tableProcessFilters = pTableProcessFilters;
    }

    /**
     * set table product filters
     * 
     * @param pTableProductFilters
     *            the table product filters to set
     */
    public void setTableProductFilters(
            List<UiFilterSummary> pTableProductFilters) {
        tableProductFilters = pTableProductFilters;
    }

    /**
     * set table user filters
     * 
     * @param pTableUserFilters
     *            the table user filters to set
     */
    public void setTableUserFilters(List<UiFilterSummary> pTableUserFilters) {
        tableUserFilters = pTableUserFilters;
    }

    /**
     * set tree filters
     * 
     * @param pTreeFilters
     *            the tree filters to set
     */
    public void setTreeFilters(List<UiFilterSummary> pTreeFilters) {
        treeFilters = pTreeFilters;
    }
}
