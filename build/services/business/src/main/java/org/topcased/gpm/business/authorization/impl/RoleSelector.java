/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ContextLocator;
import org.topcased.gpm.domain.accesscontrol.RoleDao;

/**
 * A role select can be used to execute several actions on severals products.
 * 
 * @author tpanuel
 */
public class RoleSelector {
    private final static String NO_ROLE_TOKEN_FLAG =
            "It's a flag not a role token";

    private final AuthorizationServiceImpl authorizationServiceImpl;

    private final RoleDao roleDao;

    private final String roleToken;

    private final String processName;

    private final String productName;

    private final String login;

    private final String userToken;

    private final boolean isProcessRole;

    // So a map is used to not connect the user for each element
    // For values : null -> no connected yet, NO_ROLE_TOKEN_FLAG -> no access
    private final Map<String, String> roleTokenByProductName;

    /**
     * Create a role selector for a role token.
     * 
     * @param pRoleToken
     *            The role token.
     */
    public RoleSelector(final String pRoleToken) {
        roleToken = pRoleToken;
        authorizationServiceImpl =
                (AuthorizationServiceImpl) ContextLocator.getContext().getBean(
                        "authorizationServiceImpl");
        roleDao = (RoleDao) ContextLocator.getContext().getBean("roleDao");
        processName = authorizationServiceImpl.getProcessName(pRoleToken);
        productName = authorizationServiceImpl.getProductName(pRoleToken);
        login = authorizationServiceImpl.getLogin(pRoleToken);
        userToken =
                authorizationServiceImpl.getUserSessionFromRoleSession(pRoleToken);
        roleTokenByProductName = new HashMap<String, String>();
        // The role token is associate to the product name
        roleTokenByProductName.put(productName, pRoleToken);
        isProcessRole = authorizationServiceImpl.isProcessAccess(pRoleToken);
    }

    /**
     * Select a role token for a set of product.
     * 
     * @param pProductNames
     *            The product names.
     * @return The selected role token - null if no role.
     */
    public String selectRoleToken(final List<String> pProductNames) {
        String lSelectedRoleToken = null;

        // Search the first product name for whose the user has a role
        for (String lElementProductName : pProductNames) {
            if (lElementProductName != null) {
                // Search a connection on the current element's product
                lSelectedRoleToken =
                        roleTokenByProductName.get(lElementProductName);

                // Not connect yet
                if (lSelectedRoleToken == null) {
                    // Role by default for the current element's product
                    final String lDefaultRole =
                            roleDao.getDefaultRoleName(login, processName,
                                    lElementProductName);

                    if (lDefaultRole == null) {
                        // Current user cannot access to the element product
                        roleTokenByProductName.put(lElementProductName,
                                NO_ROLE_TOKEN_FLAG);
                    }
                    else {
                        // Create a new connection ...
                        lSelectedRoleToken =
                                authorizationServiceImpl.selectRole(userToken,
                                        lDefaultRole, lElementProductName,
                                        processName);
                        // .. and store it
                        if (lSelectedRoleToken == null) {
                            // Current user cannot access to the element product
                            roleTokenByProductName.put(lElementProductName,
                                    NO_ROLE_TOKEN_FLAG);
                        }
                        else {
                            // An access has been found
                            roleTokenByProductName.put(lElementProductName,
                                    lSelectedRoleToken);
                        }
                    }
                }
            }
            // Stop search when the first element has been found
            if (lSelectedRoleToken != null
                    && lSelectedRoleToken != NO_ROLE_TOKEN_FLAG) {
                break;
            }
        }

        if (lSelectedRoleToken == NO_ROLE_TOKEN_FLAG) {
            lSelectedRoleToken = null;
        }

        return lSelectedRoleToken;
    }

    /**
     * Close all temporary used
     */
    public void close() {
        for (String lStoredToken : roleTokenByProductName.values()) {
            // Don't disconnect the initial role token
            if (lStoredToken != NO_ROLE_TOKEN_FLAG
                    && !StringUtils.equals(lStoredToken, roleToken)) {
                authorizationServiceImpl.closeRoleSession(lStoredToken);
            }
        }
    }

    /**
     * Get the role token.
     * 
     * @return The role token.
     */
    public String getRoleToken() {
        return roleToken;
    }

    /**
     * Test if the role token is a process role.
     * 
     * @return If the role token is a process role.
     */
    public boolean isProcessRole() {
        return isProcessRole;
    }
}