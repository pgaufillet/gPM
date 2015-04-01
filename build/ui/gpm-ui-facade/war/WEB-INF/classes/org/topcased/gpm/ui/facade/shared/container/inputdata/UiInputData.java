/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.inputdata;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.inputdata.BusinessInputData;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;

/**
 * UiInputData
 * 
 * @author nveillet
 */
public class UiInputData extends UiContainer implements BusinessInputData {

    /** serialVersionUID */
    private static final long serialVersionUID = -5941155961954878632L;

    private ArrayList<UiFieldGroup> fieldGroups;

    /**
     * Create new UiInputData
     */
    public UiInputData() {
        super();
        fieldGroups = new ArrayList<UiFieldGroup>();
    }

    /**
     * Add a field group
     * 
     * @param pFieldGroup
     *            The field group to add
     */
    public void addFieldGroup(UiFieldGroup pFieldGroup) {
        fieldGroups.add(pFieldGroup);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroup(java.lang.String)
     */
    @Override
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroupNames()
     */
    @Override
    public List<String> getFieldGroupNames() {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.inputdata.BusinessInputData#getFieldGroups()
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<BusinessFieldGroup> getFieldGroups() {
        return (List) fieldGroups;
    }
}
