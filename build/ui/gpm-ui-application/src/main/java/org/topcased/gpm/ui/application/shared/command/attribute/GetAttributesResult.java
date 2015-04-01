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

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

/**
 * GetAttributesResult
 * 
 * @author nveillet
 */
public abstract class GetAttributesResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 7946445254564827997L;

    private UiAttributeContainer attributeContainer;

    private List<UiAttribute> attributes;

    private String elementId;

    private String productName;

    /**
     * Empty constructor for serialization.
     */
    public GetAttributesResult() {
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
    public GetAttributesResult(String pProductName,
            UiAttributeContainer pAttributeContainer, String pElementId,
            List<UiAttribute> pAttributes) {
        productName = pProductName;
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
     * get product name
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
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

    /**
     * set product name
     * 
     * @param pProductName
     *            the product name to set
     */
    public void setProductName(String pProductName) {
        productName = pProductName;
    }
}
