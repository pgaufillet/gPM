/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.attribute;

import java.util.List;

import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

/**
 * GetAttributesVisualizationResult
 * 
 * @author nveillet
 */
public class GetAttributesVisualizationResult extends GetAttributesResult {

    /** serialVersionUID */
    private static final long serialVersionUID = 5284140211260700806L;

    /**
     * Empty constructor for serialization.
     */
    public GetAttributesVisualizationResult() {
    }

    /**
     * Constructor
     * 
     * @param pProductName
     *            the product name
     * @param pAttributeContainer
     *            the attribute container
     * @param pElementId
     *            the element identifier
     * @param pAttributes
     *            the attributes
     */
    public GetAttributesVisualizationResult(String pProductName,
            UiAttributeContainer pAttributeContainer, String pElementId,
            List<UiAttribute> pAttributes) {
        super(pProductName, pAttributeContainer, pElementId, pAttributes);
    }
}
