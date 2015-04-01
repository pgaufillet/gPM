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
 * GetProductVisualizationResult
 * 
 * @author nveillet
 */
public class GetProductVisualizationResult extends GetProductResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -5851518595672388817L;

    private List<Translation> linkGroups;

    /**
     * Empty constructor for serialization.
     */
    public GetProductVisualizationResult() {
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
     */
    public GetProductVisualizationResult(UiProduct pProduct,
            Map<String, UiAction> pActions, List<Translation> pLinkGroups) {
        super(pProduct, DisplayMode.VISUALIZATION, pActions);
        linkGroups = pLinkGroups;
    }

    /**
     * get link groups
     * 
     * @return the link groups
     */
    public List<Translation> getLinkGroups() {
        return linkGroups;
    }

}
