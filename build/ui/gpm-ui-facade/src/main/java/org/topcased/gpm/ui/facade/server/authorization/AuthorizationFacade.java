/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.authorization;

import java.util.List;

import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * AuthorizationFacade
 * 
 * @author nveillet
 */
public interface AuthorizationFacade {

    public static String ADMIN_ROLE_NAME = AuthorizationService.ADMIN_ROLE_NAME;

    /**
     * Connect user to a product.
     * 
     * @param pUserSession
     *            User session
     * @param pProductName
     *            Product name
     * @param pRoleName
     *            Role name
     * @return Role session.
     */
    public UiSession connect(UiUserSession pUserSession, String pProductName,
            String pRoleName);

    /**
     * get the action access
     * 
     * @param pSession
     *            the session
     * @param pActionName
     *            the action name
     * @param pContainerTypes
     *            the current container types (filter view) (can be null)
     * @param pContainer
     *            the container (container view) (can be null)
     * @return the action access
     */
    public boolean getActionAccess(UiSession pSession, String pActionName,
            List<UiFilterContainerType> pContainerTypes, UiContainer pContainer);

    /**
     * get all roles for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of role names
     */
    public List<String> getAllRoles(UiUserSession pUserSession);

    /**
     * get available process for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of available process
     */
    public List<String> getAvailableProcesses(UiUserSession pUserSession);

    /**
     * get available products for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of available products
     */
    public List<String> getAvailableProducts(UiUserSession pUserSession);

    /**
     * get available products hierarchy for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of products hierarchy
     */
    public List<UiProductHierarchy> getAvailableProductsHierarchy(
            UiUserSession pUserSession);

    /**
     * get available roles on product for given user session
     * 
     * @param pUserSession
     *            current user session
     * @param pProductName
     *            product on which roles are to be retrieved
     * @return list of role names
     */
    public List<String> getAvailableRoles(UiUserSession pUserSession,
            String pProductName);

    /**
     * Get the default role for a product
     * 
     * @param pUserSession
     *            the current user session
     * @param pProductName
     *            the product name
     * @return the default role for the product
     */
    public String getDefaultRole(UiUserSession pUserSession, String pProductName);

    /**
     * Get the product when a user have the high role
     * 
     * @param pUserSession
     *            the current user session
     * @return the product name with high role
     */
    public String getProductWithHighRole(UiUserSession pUserSession);

    /**
     * Get the version.
     * 
     * @return The version.
     */
    public String getVersion();

    /**
     * Return true if a user has the required administration access
     * (global, process, or ANY product)
     * 
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    public boolean hasAnyAdminAccess(UiSession pSession, AdministrationAction pAdministrationAction);

    /**
     * Return true if a user has the required administration access
     * (global, process, or CURRENT product)
     * 
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    public boolean hasCurrentAdminAccess(UiSession pSession, AdministrationAction pAdministrationAction);
    
    /**
     * Return true if a user has the specified administration access
     * (global, process, or SPECIFIED product)
     * 
     * @param pProductName the specified product
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    boolean hasSpecifiedAdminAccess(String pProductName, UiSession pSession, AdministrationAction pAdministrationAction);
	
    /**
     * Return true if the user has at least one product administration access
     * 
     * @param pSession the session
     * @return true if a product administration access exists
     */
    public boolean hasProductAdminAccess(UiSession pSession);
    
    /**
     * Return if a user has an given administration access on instance
     * 
     * @param pSession
     *            the session
     * @param pAdministrationAction
     *            the administration action
     * @return the access
     */
    public boolean hasAdminAccessOnInstance(UiSession pSession,
            AdministrationAction pAdministrationAction);

    /**
     * Return if a user can access to the administration module
     * 
     * @param pUserSession
     *            the user session
     * @return the access
     */
    public boolean hasAdministrationModuleAccess(UiUserSession pUserSession);

    /**
     * Return if a user can access to the dictionary management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasDictionaryManagementAccess(UiSession pSession);

    /**
     * Return if a user has global admin role
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasGlobalAdminAccess(UiSession pSession);

    /**
     * Return if a user can create a private environment
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasPrivateEnvironmentCreationAccess(UiSession pSession);

    /**
     * Return if a user can access to the product management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasProductManagementAccess(UiSession pSession);

    /**
     * Return if a user can access to the user management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasUserManagementAccess(UiSession pSession);

    /**
     * Login a user, and begin a session.
     * 
     * @param pUsername
     *            login name of the user
     * @param pPassword
     *            User password
     * @return UIUserSession to identify the user, null if the user cannot be
     *         identified.
     */
    public UiUserSession login(String pUsername, String pPassword);

    /**
     * Logout a logged on user.
     * 
     * @param pUserSession
     *            the user session
     */
    public void logout(UiUserSession pUserSession);

    /**
     * Set the version.
     * 
     * @param pVersion
     *            The version.
     */
    public void setVersion(final String pVersion);

    /**
     * Get available products. Names and descriptions only will be filled
     * 
     * @param pUserSession
     *            The user session
     * @return Available products for the user session
     */
    public List<UiProduct> getAvailableUiProducts(UiUserSession pUserSession);

    /**
     * Refresh the user session. after a HMI call for example.
     * 
     * @param pUserSession
     *            The user session
     * @return If the session is valid
     */
    public boolean refreshSession(UiUserSession pUserSession);
}
