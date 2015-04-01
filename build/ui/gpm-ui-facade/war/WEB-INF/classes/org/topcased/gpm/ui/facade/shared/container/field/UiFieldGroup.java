/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessFieldGroup;

/**
 * UiFieldGroup
 * 
 * @author nveillet
 */
public class UiFieldGroup implements BusinessFieldGroup, Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -6548412532060924601L;

    private List<String> fieldNames;

    private String groupName;

    private boolean open;

    /**
     * Create new UiFieldGroup
     */
    public UiFieldGroup() {
        fieldNames = new ArrayList<String>();
        open = true;
    }

    /**
     * create new UiFieldGroup
     * 
     * @param pGroupName
     *            The group name (translated)
     * @param pFieldNames
     *            Name of fields contained in the group
     * @param pIsOpen
     *            The open property : if group is open or close
     */
    public UiFieldGroup(String pGroupName, List<String> pFieldNames,
            boolean pIsOpen) {
        groupName = pGroupName;
        fieldNames = pFieldNames;
        open = pIsOpen;
    }

    /**
     * Add a field name
     * 
     * @param pFieldName
     *            The field name to add
     */
    public void addFieldName(String pFieldName) {
        fieldNames.add(pFieldName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#getFieldNames()
     */
    @Override
    public List<String> getFieldNames() {
        return fieldNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#getGroupName()
     */
    @Override
    public String getGroupName() {
        return groupName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.field.BusinessFieldGroup#isOpen()
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Set the group name (translated)
     * 
     * @param pGroupName
     *            The group name to set
     */
    public void setGroupName(String pGroupName) {
        groupName = pGroupName;
    }

    /**
     * Set the open property
     * 
     * @param pOpen
     *            The open property to set
     */
    public void setOpen(boolean pOpen) {
        open = pOpen;
    }
}
