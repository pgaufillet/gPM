/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.link;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.product.GetProductResult;

/**
 * CreateProductLinkAction
 * 
 * @author nveillet
 */
public class CreateProductLinkAction extends
        AbstractCommandLinkAction<GetProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 5139097252090073340L;

    /**
     * create action
     */
    public CreateProductLinkAction() {
    }

    /**
     * Create action with values
     * 
     * @param pLinkTypeName
     *            the link type name
     * @param pOriginId
     *            the origin container identifier
     * @param pDestinationIds
     *            the destination container identifiers
     */
    public CreateProductLinkAction(String pLinkTypeName, String pOriginId,
            List<String> pDestinationIds) {
        super(null, pLinkTypeName, pOriginId, pDestinationIds);
    }
}
