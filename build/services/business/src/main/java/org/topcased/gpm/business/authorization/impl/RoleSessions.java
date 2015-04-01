/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.authorization.impl;

import org.topcased.gpm.domain.accesscontrol.EndUser;
import org.topcased.gpm.domain.accesscontrol.Role;
import org.topcased.gpm.domain.product.Product;

/**
 * Implementation of role sessions map.
 * 
 * @author llatil
 */
public class RoleSessions extends Sessions<RoleContext> {

    /**
     * Create a new logged session for a given end user.
     * 
     * @param pUserContext
     *            The User session context
     * @param pEndUser
     *            The user to 'log'
     * @param pRole
     *            Role
     * @param pProduct
     *            Product associated with this role session
     * @return The session token.
     */
    public String create(UserContext pUserContext, EndUser pEndUser,
            Role pRole, Product pProduct) {
        if (pUserContext == null || !pUserContext.isValid()) {
            throw new IllegalArgumentException("User token session invalid");
        }
        return create(new RoleContext(pUserContext, pEndUser, pRole, pProduct,
                getRandomToken()));
    }
}
