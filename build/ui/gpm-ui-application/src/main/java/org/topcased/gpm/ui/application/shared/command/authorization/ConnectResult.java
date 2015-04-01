/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * ConnectResult
 * 
 * @author nveillet
 */
public class ConnectResult extends AbstractConnectionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 8094991365793975468L;

    private Map<String, UiAction> creatableSheetTypes;

    private List<UiAction> extendedActions;

    private boolean filterCreatable;

    private UiFilterSummaries filters;

    private String homePageUrl;

    private String productName;

    private String roleName;

    private List<Translation> roles;

    /**
     * Empty constructor for serialization.
     */
    public ConnectResult() {
    }

    /**
     * Create ConnectResult with values
     * 
     * @param pProductName
     *            the product name
     * @param pRoleName
     *            the role name
     * @param pRoleNames
     *            the role names
     * @param pActions
     *            the actions
     * @param pExtendedActions
     *            the extended actions
     * @param pFilters
     *            the filters summaries
     * @param pFilterCreatable
     *            the filter creatable access
     * @param pHomePageUrl
     *            the home page Url
     */
    public ConnectResult(String pProductName, String pRoleName,
            List<Translation> pRoleNames, Map<String, UiAction> pActions,
            List<UiAction> pExtendedActions, UiFilterSummaries pFilters,
            boolean pFilterCreatable, String pHomePageUrl) {
        roleName = pRoleName;
        roles = pRoleNames;
        creatableSheetTypes = pActions;
        extendedActions = pExtendedActions;
        filters = pFilters;
        filterCreatable = pFilterCreatable;
        homePageUrl = pHomePageUrl;
        productName = pProductName;
    }

    /**
     * get actions
     * 
     * @return the actions
     */
    public Map<String, UiAction> getActions() {
        return creatableSheetTypes;
    }

    /**
     * get extended actions
     * 
     * @return the extended actions
     */
    public List<UiAction> getExtendedActions() {
        return extendedActions;
    }

    /**
     * get filters
     * 
     * @return the filters
     */
    public UiFilterSummaries getFilters() {
        return filters;
    }

    /**
     * get home page Url
     * 
     * @return the home page Url
     */
    public String getHomePageUrl() {
        return homePageUrl;
    }

    /**
     * get product name
     * 
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * get role name
     * 
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * get roles
     * 
     * @return the roles
     */
    public List<Translation> getRoles() {
        return roles;
    }

    /**
     * get filter creatable access
     * 
     * @return the filter creatable access
     */
    public boolean isFilterCreatable() {
        return filterCreatable;
    }
}
