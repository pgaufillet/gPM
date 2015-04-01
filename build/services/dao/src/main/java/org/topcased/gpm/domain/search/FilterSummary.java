/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Vincent Hemery (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.domain.search;

/**
 * FilterSummary. A summary containing filter information.
 * 
 * @author vhemery
 */
public class FilterSummary {
    private String filterId;

    private String name;

    private String description;

    private FilterUsage usage;

    private String productName;

    private String userLogin;

    /**
     * Constructor. Create a new filter summary containing filter information.
     * 
     * @param pFilterId
     *            Filter id
     * @param pName
     *            Filter name
     * @param pDescription
     *            Filter's description
     * @param pUsage
     *            Usage
     * @param pProductName
     *            Product entity (or null if filter is not a product filter)
     * @param pUserLogin
     *            End User entity (or null if filter is not a user filter)
     */
    public FilterSummary(String pFilterId, String pName, String pDescription,
            FilterUsage pUsage, String pProductName, String pUserLogin) {
        filterId = pFilterId;
        name = pName;
        description = pDescription;
        usage = pUsage;
        productName = pProductName;
        userLogin = pUserLogin;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String pFilterId) {
        this.filterId = pFilterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public FilterUsage getUsage() {
        return usage;
    }

    public void setUsage(FilterUsage pUsage) {
        usage = pUsage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String pUserLogin) {
        userLogin = pUserLogin;
    }
}
