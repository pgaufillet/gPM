/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.authorization;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;

/**
 * ChangeRoleAction
 * 
 * @author nveillet
 */
public class ChangeRoleAction extends
        AbstractCommandAction<AbstractConnectionResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6646654106082166701L;

    private List<String> openedSheetIds;

    private String roleName;

    /**
     * create action
     */
    public ChangeRoleAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pRoleName
     *            the role name
     * @param pOpenedSheetIds
     *            the opened sheet identifiers
     */
    public ChangeRoleAction(String pProductName, String pRoleName,
            List<String> pOpenedSheetIds) {
        super(pProductName);
        roleName = pRoleName;
        openedSheetIds = pOpenedSheetIds;
    }

    /**
     * get opened sheet identifiers
     * 
     * @return the opened sheet identifiers
     */
    public List<String> getOpenedSheetIds() {
        return openedSheetIds;
    }

    /**
     * get role name
     * 
     * @return the role name
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * set opened sheet identifiers
     * 
     * @param pOpenedSheetIds
     *            the opened sheet identifiers to set
     */
    public void setOpenedSheetIds(List<String> pOpenedSheetIds) {
        openedSheetIds = pOpenedSheetIds;
    }

    /**
     * set role name
     * 
     * @param pRoleName
     *            the role name to set
     */
    public void setRoleName(String pRoleName) {
        roleName = pRoleName;
    }
}
