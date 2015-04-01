/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.facilities.AttachedDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceFieldDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceTreeDisplayHintData;
import org.topcased.gpm.business.facilities.DateDisplayHintData;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.facilities.DisplayHintData;
import org.topcased.gpm.business.facilities.GridDisplayHintData;
import org.topcased.gpm.business.facilities.TextFieldDisplayHintData;
import org.topcased.gpm.business.fields.JAppletDisplayHintData;

/**
 * DisplayGroup Service.
 * 
 * @author tszadel
 * @author mkargbo
 * @author phtsaan
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface DisplayService {
    /**
     * Gets the display group list.
     * 
     * @param pUserToken
     *            The user Token.
     * @param pSheetTypeId
     *            The associated sheetType.
     * @return List of display group.
     */
    @Transactional(readOnly = true)
    public List<DisplayGroupData> getDisplayGroupList(String pUserToken,
            String pSheetTypeId);

    /**
     * Create a display group in database.
     * 
     * @param pDisplayGroupData
     *            the data object
     * @return the identifier of the display group
     */
    public String createDisplayGroup(DisplayGroupData pDisplayGroupData);

    /**
     * Remove a display group in database.
     * 
     * @param pLabelkey
     *            the identifier of displayGroup to remove
     * @param pContainerId
     *            the identifier of container associated to this displayGroup
     */
    public void removeDisplayGroup(String pLabelkey, String pContainerId);

    /**
     * Set a display hint for a text field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setTextFieldDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, TextFieldDisplayHintData pHint);

    /**
     * Set a display hint for an attached field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setAttachedDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, AttachedDisplayHintData pHint);

    /**
     * Set a display hint for a date field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setDateDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, DateDisplayHintData pHint);

    /**
     * Set a display hint for a choice field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setChoiceFieldDisplayHint(String pRoleToken,
            String pContainerId, String pFieldName,
            ChoiceFieldDisplayHintData pHint);

    /**
     * Set a tree display hint for a choice field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pFieldsContainerId
     *            Id of the fields container
     * @param pLabelKey
     *            Field name
     * @param pHintData
     *            Display hint
     */
    public void setChoiceTreeDisplayHint(String pRoleToken,
            String pFieldsContainerId, String pLabelKey,
            ChoiceTreeDisplayHintData pHintData);

    /**
     * Set an extern display hint
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, DisplayHintData pHint);

    /**
     * Set a grid display hint for a simple field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pGridDisplayHintData
     *            Grid Display hint
     */
    public void setGridDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, GridDisplayHintData pGridDisplayHintData);

    /**
     * Set a display hint for an applet field
     * 
     * @param pRoleToken
     *            Role session token
     * @param pContainerId
     *            Id of the fields container
     * @param pFieldName
     *            Field name
     * @param pHint
     *            Display hint
     */
    public void setJAppletDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, JAppletDisplayHintData pHint);
}
