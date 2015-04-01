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

import java.util.List;

/**
 * UiActionWithSubMenu
 * 
 * @author nveillet
 */
public class UiActionWithSubMenu extends UiAction {

    /** serialVersionUID */
    private static final long serialVersionUID = 3351591583841684104L;

    private List<UiAction> actions;

    /**
     * Empty constructor for serialization.
     */
    public UiActionWithSubMenu() {
        super();
        super.setType(ActionType.WITH_SUB_MENU);
    }

    /**
     * Create new standard UiActionExtended with name and sub actions
     * 
     * @param pName
     *            the name
     * @param pActions
     *            the actions
     */
    public UiActionWithSubMenu(String pName, List<UiAction> pActions) {
        super(pName, ActionType.WITH_SUB_MENU);
        actions = pActions;
    }

    /**
     * get actions
     * 
     * @return the actions
     */
    public List<UiAction> getActions() {
        return actions;
    }

    /**
     * set actions
     * 
     * @param pActions
     *            the actions to set
     */
    public void setActions(List<UiAction> pActions) {
        actions = pActions;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.action.UiAction#setType(org.topcased.gpm.ui.facade.shared.action.ActionType)
     */
    @Override
    public void setType(ActionType pType) {
        super.setType(ActionType.WITH_SUB_MENU);
    }
}
