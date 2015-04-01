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

import org.topcased.gpm.ui.application.shared.command.sheet.GetSheetResult;

/**
 * DeleteSheetLinkAction
 * 
 * @author nveillet
 */
public class DeleteSheetLinkAction extends
        AbstractCommandLinkAction<GetSheetResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -4625396878648215966L;

    /**
     * create action
     */
    public DeleteSheetLinkAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pLinkTypeName
     *            the link type name
     * @param pOriginId
     *            the origin container identifier
     * @param pDestinationIds
     *            the destination container identifiers
     */
    public DeleteSheetLinkAction(String pProductName, String pLinkTypeName,
            String pOriginId, List<String> pDestinationIds) {
        super(pProductName, pLinkTypeName, pOriginId, pDestinationIds);
    }
}
