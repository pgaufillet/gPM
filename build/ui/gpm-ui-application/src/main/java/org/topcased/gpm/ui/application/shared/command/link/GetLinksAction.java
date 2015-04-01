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

import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * GetLinksAction
 * 
 * @author nveillet
 */
public class GetLinksAction extends AbstractCommandAction<GetLinksResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 7652782327550780377L;

    private DisplayMode displayMode;

    private String linkTypeName;

    private String valuesContainerId;

    /**
     * create action
     */
    public GetLinksAction() {
        super();
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            The product name
     * @param pValuesContainerId
     *            The values container identifier
     * @param pLinkTypeName
     *            The link type name
     * @param pDisplayMode
     *            The display mode
     */
    public GetLinksAction(String pProductName, String pValuesContainerId,
            String pLinkTypeName, DisplayMode pDisplayMode) {
        super(pProductName);
        valuesContainerId = pValuesContainerId;
        linkTypeName = pLinkTypeName;
        displayMode = pDisplayMode;
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
     * get link type name
     * 
     * @return the link type name
     */
    public String getLinkTypeName() {
        return linkTypeName;
    }

    /**
     * get values container identifier
     * 
     * @return the values container identifier
     */
    public String getValuesContainerId() {
        return valuesContainerId;
    }

    /**
     * set display mode
     * 
     * @param pDisplayMode
     *            the display mode to set
     */
    public void setDisplayMode(DisplayMode pDisplayMode) {
        displayMode = pDisplayMode;
    }

    /**
     * set link type name
     * 
     * @param pLinkTypeName
     *            the link type name to set
     */
    public void setLinkTypeName(String pLinkTypeName) {
        linkTypeName = pLinkTypeName;
    }

    /**
     * set values container identifier
     * 
     * @param pValuesContainerId
     *            the values container identifier to set
     */
    public void setValuesContainerId(String pValuesContainerId) {
        valuesContainerId = pValuesContainerId;
    }

}
