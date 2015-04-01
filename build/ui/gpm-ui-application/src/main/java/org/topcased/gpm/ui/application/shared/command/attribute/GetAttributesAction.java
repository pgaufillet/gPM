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

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttributeContainer;

/**
 * GetAttributesAction
 * 
 * @author nveillet
 */
public class GetAttributesAction extends
        AbstractCommandAction<GetAttributesResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -3221300170459967500L;

    private UiAttributeContainer attributeContainer;

    private DisplayMode displayMode;

    private String elementId;

    /**
     * Create action
     */
    public GetAttributesAction() {
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
     * @param pDisplayMode
     *            the display mode
     */
    public GetAttributesAction(String pProductName,
            UiAttributeContainer pAttributeContainer, String pElementId,
            DisplayMode pDisplayMode) {
        super(pProductName);
        attributeContainer = pAttributeContainer;
        elementId = pElementId;
        displayMode = pDisplayMode;
    }

    /**
     * Constructor
     * 
     * @param pAttributeContainer
     *            the attribute container
     * @param pElementId
     *            the element identifier
     * @param pDisplayMode
     *            the display mode
     */
    public GetAttributesAction(UiAttributeContainer pAttributeContainer,
            String pElementId, DisplayMode pDisplayMode) {
        super();
        attributeContainer = pAttributeContainer;
        elementId = pElementId;
        displayMode = pDisplayMode;
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
     * get display mode
     * 
     * @return the display mode
     */
    public DisplayMode getDisplayMode() {
        return displayMode;
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
     * set displayMode
     * 
     * @param pDisplayMode
     *            the display mode to set
     */
    public void setDisplayMode(DisplayMode pDisplayMode) {
        displayMode = pDisplayMode;
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
