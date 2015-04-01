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

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.administration.user.UiUser;
import org.topcased.gpm.ui.facade.shared.filter.summary.UiFilterSummaries;

/**
 * OpenAdminModuleResult
 * 
 * @author nveillet
 */
public class OpenAdminModuleResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 2379720655451850038L;

    private boolean filterCreatable;

    private UiFilterSummaries filters;

    private Map<String, UiAction> productActions;

    private Map<String, UiAction> userActions;

    private List<UiUser> userListSortedByLogin;

    private List<UiUser> userListSortedByName;

    private List<String> availableLanguages;

    private List<String> availableEnvironments;

    private Map<String, UiAction> dictionaryActions;

    private boolean privateEnvironmentCreationAccess;

    /**
     * Empty constructor for serialization.
     */
    public OpenAdminModuleResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pProductActions
     *            the product actions
     * @param pFilters
     *            the product filters
     * @param pFilterCreatable
     *            the filter creatable access
     * @param pUserListSortedByLogin
     *            user list sorted by login
     * @param pUserListSortedByName
     *            user list sorted by name/forename
     * @param pAvailableLanguages
     *            available languages.
     * @param pUserActions
     *            user actions
     * @param pAvailableEnvironments
     *            visible environments
     * @param pDictionaryActions
     *            dictionary actions
     * @param pPrivateEnvironmentCreationAccess
     *            if user can create private environment
     */
    public OpenAdminModuleResult(Map<String, UiAction> pProductActions,
            UiFilterSummaries pFilters, boolean pFilterCreatable,
            List<UiUser> pUserListSortedByLogin,
            List<UiUser> pUserListSortedByName,
            Map<String, UiAction> pUserActions,
            List<String> pAvailableLanguages,
            List<String> pAvailableEnvironments,
            Map<String, UiAction> pDictionaryActions,
            boolean pPrivateEnvironmentCreationAccess) {
        productActions = pProductActions;
        filters = pFilters;
        filterCreatable = pFilterCreatable;

        // User management data
        userListSortedByLogin = pUserListSortedByLogin;
        userListSortedByName = pUserListSortedByName;
        userActions = pUserActions;
        availableLanguages = pAvailableLanguages;

        //Dictionary management data
        availableEnvironments = pAvailableEnvironments;
        dictionaryActions = pDictionaryActions;
        privateEnvironmentCreationAccess = pPrivateEnvironmentCreationAccess;
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
     * get product actions
     * 
     * @return the product actions
     */
    public Map<String, UiAction> getProductActions() {
        return productActions;
    }

    /**
     * get user actions
     * 
     * @return the user actions
     */
    public Map<String, UiAction> getUserActions() {
        return userActions;
    }

    /**
     * get user list sorted by login
     * 
     * @return user list
     */
    public List<UiUser> getUserListSortedByLogin() {
        return userListSortedByLogin;
    }

    /**
     * get user list sorted by name-forename
     * 
     * @return user list
     */
    public List<UiUser> getUserListSortedByName() {
        return userListSortedByName;
    }

    /**
     * Get available languages.
     * 
     * @return available languages.
     */
    public List<String> getAvailableLanguages() {
        return availableLanguages;
    }

    /**
     * get filterCreatable
     * 
     * @return the filterCreatable
     */
    public boolean isFilterCreatable() {
        return filterCreatable;
    }

    /**
     * get private environment creation access
     * 
     * @return the if the user can create private environment
     */
    public boolean isPrivateEnvironmentCreationAccess() {
        return privateEnvironmentCreationAccess;
    }

    /**
     * get visible environments
     * 
     * @return environments
     */
    public List<String> getAvailableEnvironments() {
        return availableEnvironments;
    }

    /**
     * get dictionary actions
     * 
     * @return dictionary actions
     */
    public Map<String, UiAction> getDictionaryActions() {
        return dictionaryActions;
    }

}
