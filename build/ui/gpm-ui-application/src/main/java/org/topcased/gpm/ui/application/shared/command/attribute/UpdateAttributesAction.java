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

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

/**
 * UpdateAttributesAction
 * 
 * @author nveillet
 */
public class UpdateAttributesAction extends
        AbstractCommandAction<UpdateAttributesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 6781837074873297374L;

    private UiAttributeContainer attributeContainer;

    private List<UiAttribute> attributes;

    private String elementId;

    /**
     * Create action
     */
    public UpdateAttributesAction() {
    }

    /**
     * Create action with values
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
    public UpdateAttributesAction(String pProductName,
            UiAttributeContainer pAttributeContainer, String pElementId,
            List<UiAttribute> pAttributes) {
        super(pProductName);
        attributeContainer = pAttributeContainer;
        elementId = pElementId;
        attributes = pAttributes;
    }

    /**
     * get attribute container
     * 
     * @return the attribute container
     */
    public UiAttributeContainer getAttributeContainer() {
        return attributeContainer;
    }

    /**
     * get attributes
     * 
     * @return the attributes
     */
    public List<UiAttribute> getAttributes() {
        return attributes;
    }

    /**
     * get element identifier
     * 
     * @return the element identifier
     */
    public String getElementId() {
        return elementId;
    }

    /**
     * set attribute container
     * 
     * @param pAttributeContainer
     *            the attribute container to set
     */
    public void setAttributeContainer(UiAttributeContainer pAttributeContainer) {
        attributeContainer = pAttributeContainer;
    }

    /**
     * set attributes
     * 
     * @param pAttributes
     *            the attributes to set
     */
    public void setAttributes(List<UiAttribute> pAttributes) {
        attributes = pAttributes;
    }

    /**
     * set element identifier
     * 
     * @param pElementId
     *            the element identifier to set
     */
    public void setElementId(String pElementId) {
        elementId = pElementId;
    }
}
