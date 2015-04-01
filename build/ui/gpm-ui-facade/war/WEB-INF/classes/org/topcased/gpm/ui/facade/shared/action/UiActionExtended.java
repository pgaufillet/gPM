/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.action;

/**
 * UiActionExtended
 * 
 * @author nveillet
 */
public class UiActionExtended extends UiAction {

    /** serialVersionUID */
    private static final long serialVersionUID = 5418587107456116056L;

    private String extendedActionName;

    private String extensionContainerId;

    /**
     * Empty constructor for serialization.
     */
    public UiActionExtended() {
        super();
        super.setType(ActionType.EXTENDED_ACTION);
    }

    /**
     * Create new standard UiActionExtended with names
     * 
     * @param pName
     *            the name
     * @param pExtendedActionName
     *            the extended action name
     * @param pExtensionContainerId
     *            the extension container identifier
     */
    public UiActionExtended(String pName, String pExtendedActionName,
            String pExtensionContainerId, String pConfirmationMessage) {
        super(pName, ActionType.EXTENDED_ACTION, pConfirmationMessage);
        extendedActionName = pExtendedActionName;
        extensionContainerId = pExtensionContainerId;
    }

    /**
     * get extended action name
     * 
     * @return the extended action name
     */
    public String getExtendedActionName() {
        return extendedActionName;
    }

    /**
     * get extension container identifier
     * 
     * @return the extension container identifier
     */
    public String getExtensionContainerId() {
        return extensionContainerId;
    }

    /**
     * set extended action name
     * 
     * @param pExtendedActionName
     *            the extended action name to set
     */
    public void setExtendedActionName(String pExtendedActionName) {
        extendedActionName = pExtendedActionName;
    }

    /**
     * set extension container identifier
     * 
     * @param pExtensionContainerId
     *            the extension container identifier to set
     */
    public void setExtensionContainerId(String pExtensionContainerId) {
        extensionContainerId = pExtensionContainerId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.action.UiAction#setType(org.topcased.gpm.ui.facade.shared.action.ActionType)
     */
    @Override
    public void setType(ActionType pType) {
        super.setType(ActionType.EXTENDED_ACTION);
    }
}
