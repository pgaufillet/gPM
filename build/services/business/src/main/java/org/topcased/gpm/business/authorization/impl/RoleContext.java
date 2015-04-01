/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.Role;
import org.topcased.gpm.domain.product.Product;

/**
 * Class RoleContext
 * 
 * @author tpanuel
 */
public class RoleContext extends AbstractContext {
    /** The id of the role */
    private final Long roleId;

    /** The name of the role */
    private final String roleName;

    /** The id of the end user */
    private final String endUserId;

    /** The id of the product */
    private String productId;

    /** The name of the product */
    private String productName;

    /** The name of the business process */
    private final String processName;

    /** The user context */
    private final UserContext userContext;

    /** If the role context is valid */
    private boolean valid;

    /**
     * Constructs a new role session context
     * 
     * @param pUserContext
     *            User session token
     * @param pEndUser
     *            User entity
     * @param pRole
     *            Role entity
     * @param pProduct
     *            Product entity
     * @param pSessionToken
     *            the session token
     */
    RoleContext(UserContext pUserContext, EndUser pEndUser, Role pRole,
            Product pProduct, String pSessionToken) {
        super(pSessionToken);
        roleId = pRole.getId();
        roleName = pRole.getName();
        processName = pRole.getBusinessProcess().getName();

        endUserId = pEndUser.getId();
        userContext = pUserContext;

        if (pProduct == null) {
            productId = null;
            productName = null;
        }
        else {
            productId = pProduct.getId();
            productName = pProduct.getName();
        }

        valid = true;
    }

    /**
     * Get the entity identifier of role in DB
     * 
     * @return Entity identifier of the role in DB
     */
    public Long getRoleId() {
        return roleId;
    }

    /**
     * Get the entity identifier of user
     * 
     * @return Entity identifier of user in DB
     */
    public String getEndUserId() {
        return endUserId;
    }

    /**
     * Get product technical identifier.
     * 
     * @return Product technical identifier (or null if no product attached to
     *         session)
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Get product's name.
     * 
     * @return Product name (or null if no product attached to session)
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Get process name.
     * 
     * @return Process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Get the context of the user session
     * 
     * @return The context of the user session
     */
    public UserContext getUserContext() {
        return userContext;
    }

    /**
     * Get the name of the role
     * 
     * @return The name of the role
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Get the user session token which was used to create this role session.
     * 
     * @return The user session token
     */
    public String getUserToken() {
        return userContext.getToken();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#refresh()
     */
    public boolean refresh() {
        return valid && userContext.refresh();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#isValid()
     */
    public boolean isValid() {
        return valid && userContext.isValid();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.authorization.impl.AbstractContext#invalid()
     */
    public void invalid() {
        valid = false;
    }
}
