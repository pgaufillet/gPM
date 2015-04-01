/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.product;

import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;

/**
 * GetProductEditionResult
 * 
 * @author nveillet
 */
public class GetProductEditionResult extends GetProductResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 3562724500411064505L;

    private List<Translation> linkGroups;

    private List<String> productNames;

    private boolean adminMode;
    
    /**
     * Empty constructor for serialization.
     */
    public GetProductEditionResult() {
    }

    /**
     * Constructor with values
     * 
     * @param pProduct
     *            the product
     * @param pActions
     *            the actions
     * @param pLinkGroups
     *            the link groups
     * @param pProductNames
     *            the products names
     */
    public GetProductEditionResult(UiProduct pProduct,
            Map<String, UiAction> pActions, List<Translation> pLinkGroups,
            List<String> pProductNames, boolean pAdminMode) {
        super(pProduct, DisplayMode.EDITION, pActions);
        linkGroups = pLinkGroups;
        productNames = pProductNames;
        adminMode = pAdminMode;
    }

    /**
     * get link groups
     * 
     * @return the link groups
     */
    public List<Translation> getLinkGroups() {
        return linkGroups;
    }

    /**
     * get product names
     * 
     * @return the product names
     */
    public List<String> getProductNames() {
        return productNames;
    }
    
    public boolean isAdmin() {
    	return adminMode;
    }
}
