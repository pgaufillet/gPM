/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.authorization.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.authorization.service.ActionAccessControlData;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.authorization.service.RoleProperties;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.product.impl.CacheableProduct;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.authorization.UiUserSession;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.product.UiProductHierarchy;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

/**
 * AuthorizationFacade
 * 
 * @author nveillet
 */
public class AuthorizationFacadeImpl extends AbstractFacade implements
        AuthorizationFacade {

    private String version;

    private void cleanDisabledProducts(UiProductHierarchy pHierarchy) {
        List<UiProductHierarchy> lChildren = pHierarchy.getChildren();
        List<UiProductHierarchy> lChildrenToRemove =
                new ArrayList<UiProductHierarchy>();
        //for all children of the parent
        for (UiProductHierarchy lChild : lChildren) {
            //Clean this child and his children
            cleanDisabledProducts(lChild);
            //Check if this child has children
            if (lChild.getChildren() == null || lChild.getChildren().isEmpty()) {
                if (!lChild.isSelectable()) {
                    //No children and not selectable
                    lChildrenToRemove.add(lChild);
                }
            }
        }
        if (!lChildrenToRemove.isEmpty()) {
            lChildren.removeAll(lChildrenToRemove);
        }
    }

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
            String pRoleName) {
        String lRoleToken;
        // if user was authorized to login without product and has admin role
        if (pProductName.isEmpty()
                && pRoleName.equals(AuthorizationService.ADMIN_ROLE_NAME)) {
            lRoleToken =
                    getAuthorizationService().getAdminRoleToken(
                            pUserSession.getProcessName());
        }
        else { // else Normal connection
            lRoleToken =
                    getAuthorizationService().selectRole(
                            pUserSession.getToken(), pRoleName, pProductName,
                            pUserSession.getProcessName());
        }
        UiSession lSession = new UiSession();
        lSession.setParent(pUserSession);
        lSession.setProductName(pProductName);
        lSession.setRoleName(pRoleName);
        lSession.setRoleToken(lRoleToken);
        return lSession;
    }

    /**
     * get the action access
     * 
     * @param pSession
     *            the session
     * @param pActionName
     *            the action name
     * @param pAccessControlContextData
     *            the access control context
     * @return the action access
     */
    private boolean getAccessAction(UiSession pSession, String pActionName,
            AccessControlContextData pAccessControlContextData) {
        ActionAccessControlData lActionAccess =
                getAuthorizationService().getApplicationActionAccessControl(
                        pSession.getRoleToken(), pAccessControlContextData,
                        pActionName);
        return !lActionAccess.getConfidential() && lActionAccess.getEnabled();
    }

    /**
     * get the action access for a container type list
     * 
     * @param pSession
     *            the session
     * @param pActionName
     *            the action name
     * @param pContainerTypes
     *            the current container types
     * @param pAccessControlContextData
     *            the access control context
     * @return the action access
     */
    private boolean getAccessAction(UiSession pSession, String pActionName,
            List<UiFilterContainerType> pContainerTypes,
            AccessControlContextData pAccessControlContextData) {

        boolean lAccess = true;

        Iterator<UiFilterContainerType> lIterator = pContainerTypes.iterator();

        while (lAccess && lIterator.hasNext()) {
            pAccessControlContextData.setContainerTypeId(lIterator.next().getId());
            lAccess =
                    getAccessAction(pSession, pActionName,
                            pAccessControlContextData);
        }

        return lAccess;
    }

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
            List<UiFilterContainerType> pContainerTypes, UiContainer pContainer) {

        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setProductName(pSession.getProductName());
        lAccessControlContextData.setRoleName(pSession.getRoleName());

        if (pContainerTypes != null && !pContainerTypes.isEmpty()) {
            return getAccessAction(pSession, pActionName, pContainerTypes,
                    lAccessControlContextData);
        }

        if (pContainer != null) {
            lAccessControlContextData.setContainerTypeId(pContainer.getTypeId());
            if (pContainer instanceof UiSheet) {
                lAccessControlContextData.setStateName(((UiSheet) pContainer).getState());
                lAccessControlContextData.setValuesContainerId(pContainer.getId());
            }
        }

        return getAccessAction(pSession, pActionName, lAccessControlContextData);
    }

    /**
     * get all roles for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of role names
     */
    public List<String> getAllRoles(UiUserSession pUserSession) {
        return new ArrayList<String>(getAuthorizationService().getRolesNames(
                pUserSession.getProcessName()));
    }

    /**
     * get available process for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of available process
     */
    public List<String> getAvailableProcesses(UiUserSession pUserSession) {
        return new ArrayList<String>(
                getAuthorizationService().getBusinessProcessNames(
                        pUserSession.getToken()));
    }

    /**
     * get available products for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of available products
     */
    public List<String> getAvailableProducts(UiUserSession pUserSession) {
        validateProcessName(pUserSession.getProcessName());
        return new ArrayList<String>(getAuthorizationService().getProductNames(
                pUserSession.getToken(), pUserSession.getProcessName()));
    }

    /**
     * get available products hierarchy for given user session
     * 
     * @param pUserSession
     *            current user session
     * @return list of products hierarchy
     */
    public List<UiProductHierarchy> getAvailableProductsHierarchy(
            UiUserSession pUserSession) {

        String lProcessName = pUserSession.getProcessName();
        List<UiProduct> lAvailableProductNames =
                getAvailableUiProducts(pUserSession);
        List<String> lAllProducts =
                getAuthorizationService().getAllProductNames(lProcessName);
        List<String> lTopLevelProducts = new LinkedList<String>();
        Map<String, UiProductHierarchy> lProductHierarchies =
                new HashMap<String, UiProductHierarchy>();

        for (String lProductName : lAllProducts) {
            if (!lProductHierarchies.containsKey(lProductName)) {
                lProductHierarchies.put(lProductName, getUiProductHierarchy(
                        lProductName, lProcessName, lAvailableProductNames,
                        lTopLevelProducts, lProductHierarchies));
            }
        }

        List<UiProductHierarchy> lResult = new ArrayList<UiProductHierarchy>();
        // Sort top level products
        Collections.sort(lTopLevelProducts);
        for (String lTopLevelProduct : lTopLevelProducts) {
            UiProductHierarchy lHierarchy =
                    lProductHierarchies.get(lTopLevelProduct);
            // Remove not reachable products
            cleanDisabledProducts(lHierarchy);
            //Add this product only if itself or one of his children it selectable 
            if ((lHierarchy.getChildren() != null && !lHierarchy.getChildren().isEmpty())
                    || lHierarchy.isSelectable()) {
                lResult.add(lHierarchy);
            }
        }

        return lResult;
    }

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
            String pProductName) {
        // replace HTML and java script code by real character 
        String lProductName = StringEscapeUtils.unescapeHtml(pProductName);
        ArrayList<String> lRoles =
                new ArrayList<String>(getAuthorizationService().getRolesNames(
                        pUserSession.getToken(), pUserSession.getProcessName(),
                        lProductName));
        if (lRoles.size() == 0) {
            // the user did not have any role 
            throw new AuthorizationException("");
        }
        return lRoles;
    }

    /**
     * Get the default role for a product
     * 
     * @param pUserSession
     *            the current user session
     * @param pProductName
     *            the product name
     * @return the default role for the product
     */
    public String getDefaultRole(UiUserSession pUserSession, String pProductName) {
        List<String> lAvailableRoles =
                getAvailableRoles(pUserSession, pProductName);
        if (!lAvailableRoles.isEmpty()) {
            return lAvailableRoles.get(0);
        }
        return null;
    }

    /**
     * Get the product when a user have the high role
     * 
     * @param pUserSession
     *            the current user session
     * @return the product name with high role
     */
    public String getProductWithHighRole(UiUserSession pUserSession) {

        List<String> lProductNames = getAvailableProducts(pUserSession);

        ArrayList<String> lRoleNames =
                new ArrayList<String>(getAuthorizationService().getRolesNames(
                        pUserSession.getProcessName()));

        int lDefaultRoleIndex = -1;
        String lDefaultProductName = null;

        for (String lProductName : lProductNames) {
            String lRole = getDefaultRole(pUserSession, lProductName);
            if (lRole != null) {
                int lTmpRoleIndex = lRoleNames.indexOf(lRole);
                if (lTmpRoleIndex < lDefaultRoleIndex || lDefaultRoleIndex < 0) {
                    lDefaultRoleIndex = lTmpRoleIndex;
                    lDefaultProductName = lProductName;
                }
            }
        }

        return lDefaultProductName;
    }

    private UiProductHierarchy getUiProductHierarchy(String pProductName,
            String pProcessName, List<UiProduct> pAvailableProducts,
            List<String> pTopLevelProducts,
            Map<String, UiProductHierarchy> pProductHierarchies) {

        boolean lSelectable = false;
        UiProduct lMatchingProduct = null;
        for (UiProduct lP : pAvailableProducts) {
            if (lP.getName().equals(pProductName)) {
                lMatchingProduct = lP;
                lSelectable = true;
                break;
            }
        }

        String lDescription = null;
        if (lMatchingProduct != null) {
            lDescription = lMatchingProduct.getDescription();
        }
        UiProductHierarchy lProductHierarchy =
                new UiProductHierarchy(pProductName, lDescription,
                        new ArrayList<UiProductHierarchy>(), lSelectable);

        // Get parents
        List<String> lProductParents =
                getProductService().getProductParentsNames(pProcessName,
                        pProductName);

        if (lProductParents == null || lProductParents.isEmpty()) {
            pTopLevelProducts.add(lProductHierarchy.getProductName());
        }
        else {
            for (String lParentName : lProductParents) {
                if (!pProductHierarchies.containsKey(lParentName)) {
                    pProductHierarchies.put(lParentName, getUiProductHierarchy(
                            lParentName, pProcessName, pAvailableProducts,
                            pTopLevelProducts, pProductHierarchies));
                }
                pProductHierarchies.get(lParentName).getChildren().add(
                        lProductHierarchy);
                Collections.sort(pProductHierarchies.get(lParentName).getChildren());
            }
        }

        return lProductHierarchy;
    }

    /**
     * Get the version.
     * 
     * @return The version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Return true if a user has the required administration access
     * (global, *CURRENT* process, or ANY product)
     * 
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    @Override
    public boolean hasAnyAdminAccess(UiSession pSession, AdministrationAction pAdministrationAction) {
    	UiUserSession lUserSession = pSession.getParent();
    	AuthorizationService lAuthService = getAuthorizationService();
    	String lRoleToken = pSession.getRoleToken();
 
    	// Global admin ?
        if (lAuthService.hasGlobalAdminRole(lRoleToken)) {
        	return true;
        }
        
        // Admin on current process ?
        if (lAuthService.isAdminAccessDefinedOnInstance(lRoleToken, pAdministrationAction.getActionKey())) {
        	return true;
        }
        
        // Admin on ANY product ? 
        if (pAdministrationAction.isSpecificProductAvailable()) {
        	List<String> lAllProductNames = getAvailableProducts(lUserSession);
        	for (String lProductName : lAllProductNames) {
        		try {
        			List<String> lRoles = getAvailableRoles(lUserSession, lProductName);
        			for (String lRole : lRoles) {
        				if (lAuthService.isAdminAccessDefinedOnProduct(
        						lRoleToken,
        						pAdministrationAction.getActionKey(),
        						lProductName,
        						lRole)) {
        					return true;
        				}
        			}
        		} catch (AuthorizationException ex) {
        			// No role for this product
        		}
        	}
        }
        return false;
    }

    
    /**
     * Return true if a user has the required administration access
     * (global, *CURRENT* process, or CURRENT product)
     * 
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    @Override
    public boolean hasSpecifiedAdminAccess(String pProductName, UiSession pSession, AdministrationAction pAdministrationAction) {
    	UiUserSession lUserSession = pSession.getParent();
    	AuthorizationService lAuthService = getAuthorizationService();
    	String lRoleToken = pSession.getRoleToken();
 
    	// Global admin ?
		if (lAuthService.hasGlobalAdminRole(lRoleToken)) {
			return true;
		}
		
        // Admin on current process ?
		if (lAuthService.isAdminAccessDefinedOnInstance(lRoleToken, pAdministrationAction.getActionKey())) {
			return true;
		}
		
        // Admin on CURRENT product ? 
        if (pAdministrationAction.isSpecificProductAvailable()) {
        	try {
        		List<String> lRoles = getAvailableRoles(lUserSession, pProductName);
        		for (String lRole : lRoles) {
        			if (lAuthService.isAdminAccessDefinedOnProduct(
        					lRoleToken,
        					pAdministrationAction.getActionKey(),
        					pProductName,
        					lRole)) {
        				return true;
        			}
        		}
        	} catch (AuthorizationException ex) {
        		// No role for this product
        	}
        }
        return false;
    }

    /**
     * Return true if a user has the required administration access
     * (global, *CURRENT* process, or CURRENT product)
     * 
     * @param pSession the session
     * @param pAdministrationAction the administration action
     * @return true if access is granted
     */
    @Override
    public boolean hasCurrentAdminAccess(UiSession pSession, AdministrationAction pAdministrationAction) {
    	UiUserSession lUserSession = pSession.getParent();
    	AuthorizationService lAuthService = getAuthorizationService();
    	String lRoleToken = pSession.getRoleToken();
 
    	// Global admin ?
		if (lAuthService.hasGlobalAdminRole(lRoleToken)) {
			return true;
		}
		
        // Admin on current process ?
		if (lAuthService.isAdminAccessDefinedOnInstance(lRoleToken, pAdministrationAction.getActionKey())) {
			return true;
		}
		
        // Admin on CURRENT product ? 
        if (pAdministrationAction.isSpecificProductAvailable()) {
        	try {
        		List<String> lRoles = getAvailableRoles(lUserSession, pSession.getProductName());
        		for (String lRole : lRoles) {
        			if (lAuthService.isAdminAccessDefinedOnProduct(
        					lRoleToken,
        					pAdministrationAction.getActionKey(),
        					pSession.getProductName(),
        					lRole)) {
        				return true;
        			}
        		}
        	} catch (AuthorizationException ex) {
        		// No role for this product
        	}
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade#hasAdminAccessOnInstance(org.topcased.gpm.ui.facade.server.authorization.UiSession,
     *      org.topcased.gpm.business.util.action.AdministrationAction)
     */
    @Override
    public boolean hasAdminAccessOnInstance(UiSession pSession,
            AdministrationAction pAdministrationAction) {
        return getAuthorizationService().hasGlobalAdminRole(
                pSession.getRoleToken())
                || getAuthorizationService().isAdminAccessDefinedOnInstance(
                        pSession.getRoleToken(),
                        pAdministrationAction.getActionKey());
    }

    /**
     * Return if a user can access to the administration module
     * 
     * @param pUserSession
     *            the user session
     * @return the access
     */
    public boolean hasAdministrationModuleAccess(UiUserSession pUserSession) {
        String lProductName = getProductWithHighRole(pUserSession);
        boolean lAccess = false;
        if (lProductName != null) {
            String lRole = getDefaultRole(pUserSession, lProductName);
            if (!AuthorizationService.ADMIN_ROLE_NAME.equals(lRole)) {
                UiSession lSession = connect(pUserSession, lProductName, lRole);

                lAccess = hasProductManagementAccess(lSession);
                if (!lAccess) {
                    lAccess = hasUserManagementAccess(lSession);
                }
                if (!lAccess) {
                    lAccess = hasDictionaryManagementAccess(lSession);
                }
            }
            else {
                lAccess = true;
            }
        }
        else { // For null product name (no product name in database)
            Collection<String> lRoles =
                    getAuthorizationService().getRolesNames(
                            pUserSession.getToken(),
                            pUserSession.getProcessName(), null,
                            new RoleProperties(true, false));
            // If one of user role is ADMIN_ROLE, grant access to administration module
            if (lRoles.contains(AuthorizationService.ADMIN_ROLE_NAME)) {
                lAccess = true;
            }
        }

        return lAccess;
    }

    /**
     * Return if a user can access to the dictionary management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasDictionaryManagementAccess(UiSession pSession) {
        boolean lAccess = hasAnyAdminAccess(pSession, AdministrationAction.DICT_MODIFY);

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.ENV_CREATE);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.ENV_MODIFY);
        }

        return lAccess;
    }

    /**
     * Return if a user has global admin role
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasGlobalAdminAccess(UiSession pSession) {
        return getAuthorizationService().hasGlobalAdminRole(
                pSession.getRoleToken());
    }

    /**
     * Return if a user can create a private environment
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasPrivateEnvironmentCreationAccess(UiSession pSession) {
        return getAuthorizationService().hasGlobalAdminRole(
                pSession.getRoleToken());
    }

    /**
     * Return if a user can access to the product management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasProductManagementAccess(UiSession pSession) {
        AccessControlContextData lAccessControlContextData =
                new AccessControlContextData();
        lAccessControlContextData.setRoleName(pSession.getRoleName());

        boolean lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_VIEW);

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_CREATE);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_UPDATE);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_DELETE);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_SEARCH_NEW_EDIT);
        }

        if (!lAccess) {
            lAccess =
            		hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_SEARCH_DELETE);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_EXPORT);
        }

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.PRODUCT_IMPORT);
        }

        return lAccess;
    }

    /**
     * Return if a user can access to the user management part of the
     * administration module
     * 
     * @param pSession
     *            the session
     * @return the access
     */
    public boolean hasUserManagementAccess(UiSession pSession) {
        boolean lAccess = hasAnyAdminAccess(pSession, AdministrationAction.USER_CREATE);

        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.USER_MODIFY);
        }
        
        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.USER_ROLES_MODIFY);
        }
        
        if (!lAccess) {
            lAccess = hasAnyAdminAccess(pSession, AdministrationAction.USER_DELETE);
        }

        return lAccess;
    }

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
    public UiUserSession login(String pUsername, String pPassword) {
        String lToken = getAuthorizationService().login(pUsername, pPassword);
        final UiUserSession lUserSession;
        if (StringUtils.isNotBlank(lToken)) {
            lUserSession = new UiUserSession();
            lUserSession.setLogin(pUsername);
            lUserSession.setToken(lToken);
            lUserSession.setLanguage(FacadeLocator.instance().getI18nFacade().getUserLanguage(
                    lUserSession));
        }
        else {
            lUserSession = null;
        }
        return lUserSession;
    }

    /**
     * Logout a logged on user.
     * 
     * @param pUserSession
     *            the user session
     */
    public void logout(UiUserSession pUserSession) {
        getAuthorizationService().logout(pUserSession.getToken());
    }

    /**
     * Set the version.
     * 
     * @param pVersion
     *            The version.
     */
    public void setVersion(final String pVersion) {
        version = pVersion;
    }

    /**
     * Check if a process exist in database
     * 
     * @param pSession
     *            the session
     * @param pProcessName
     *            the process name
     */
    private void validateProcessName(String pProcessName) {
        getInstanceService().getBusinessProcessId(pProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.authorization.AuthorizationFacade#getAvailableUiProducts(org.topcased.gpm.ui.facade.server.authorization.UiUserSession)
     */
    @Override
    public List<UiProduct> getAvailableUiProducts(UiUserSession pUserSession) {
        validateProcessName(pUserSession.getProcessName());
        List<UiProduct> lList = new ArrayList<UiProduct>();

        for (CacheableProduct lP : getAuthorizationService().getProductNamesAndDescriptions(
                pUserSession.getToken(), pUserSession.getProcessName())) {
            UiProduct lProd = new UiProduct();
            // encode  HTML tags and java script code before set them to the browser 
            lProd.setName(StringEscapeUtils.escapeHtml(lP.getProductName()));
            lProd.setDescription(lP.getDescription());
            lList.add(lProd);
        }

        return lList;
    }

    /**
     * Refresh the user session. after a HMI call for example.
     * 
     * @param pUserSession
     *            The user session
     * @return If the session is valid
     */
    @Override
    public boolean refreshSession(UiUserSession pUserSession) {
        return getAuthorizationService().refreshSession(pUserSession.getToken());
    }

    /**
     * Return true if the user has at least one product administration access
     * 
     * @param pSession the session
     * @return true if a product administration access exists
     */
	@Override
	public boolean hasProductAdminAccess(UiSession pSession) {
		// TODO 
		return false;
	}
}
