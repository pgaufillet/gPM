/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Magali Franchet (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util.action;

/**
 * ApplicationAction
 * 
 * @author mfranche
 */
public enum AdministrationAction {

    // PRODUCT MANAGEMENT
    PRODUCT_CREATE("gpm.administration.product.create", false),

    PRODUCT_VIEW("gpm.administration.product.view", true),

    PRODUCT_DELETE("gpm.administration.product.delete", false),

    PRODUCT_EXPORT("gpm.administration.product.export", false),

    PRODUCT_IMPORT("gpm.administration.product.import", false),

    PRODUCT_SEARCH_NEW_EDIT("gpm.administration.product.search.createAndEdit", true),

    PRODUCT_SEARCH_DELETE("gpm.administration.product.search.delete", true),

    PRODUCT_UPDATE("gpm.administration.product.update", true),

    // USER MANAGEMENT
    USER_CREATE("gpm.administration.user.create", false),

    USER_MODIFY("gpm.administration.user.modify", true),
    
    // This is a sub action of USER_MODIFY.
    // This sub-action makes the role DELEGABLE when applied to a product:
    // Within a single product, if a user gets a DELEGABLE role, the former
    // recipient of the role loses it. 
    USER_ROLES_MODIFY("gpm.administration.user.modify.roles", true),
    
    USER_DELETE("gpm.administration.user.delete", false),

    USER_MODIFY_CURRENT("gpm.administration.user.current.modify", true),

    // DICTIONARY MANAGEMENT
    DICT_MODIFY("gpm.administration.dictionary.modify", true),

    ENV_MODIFY("gpm.administration.environment.modify", true),

    ENV_CREATE("gpm.administration.environment.create", false);

    private final String actionKey;

    private final boolean specificProductAvailable;

    private AdministrationAction(String pActionKey,
            boolean pSpecificProductAvailable) {
        actionKey = pActionKey;
        specificProductAvailable = pSpecificProductAvailable;
    }

    /**
     * Get the key as string value
     * 
     * @return Action key as string
     */
    public String getActionKey() {
        return actionKey;
    }

    /**
     * get if the access is available on a specific product
     * 
     * @return if the access is available on a specific product
     */
    public boolean isSpecificProductAvailable() {
        return specificProductAvailable;
    }
}
