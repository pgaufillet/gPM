/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.product;

import java.util.ArrayList;
import java.util.List;

import org.topcased.gpm.business.values.field.BusinessFieldGroup;
import org.topcased.gpm.business.values.product.BusinessProduct;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.field.UiFieldGroup;

/**
 * UiProduct
 * 
 * @author nveillet
 */
public class UiProduct extends UiContainer implements BusinessProduct {

    /** serialVersionUID */
    private static final long serialVersionUID = 6974230039226376100L;

    //list of display groups - used to sort fields
    private ArrayList<UiFieldGroup> fieldGroups;

    private List<String> children;

    private String name;

    private String description;

    private List<String> parents;

    /**
     * Create new UiProduct
     */
    public UiProduct() {
        super();
        //initialize display groups
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
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFieldGroup(java.lang.String)
     */
    @Override
    public BusinessFieldGroup getFieldGroup(String pFieldGroupName) {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.sheet.BusinessSheet#getFieldGroupNames()
     */
    @Override
    public List<String> getFieldGroupNames() {
        throw new IllegalStateException("Not implemented method");
    }

    /**
     * Get the field groups
     * 
     * @return The field groups
     */
    public List<UiFieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getChildren()
     */
    @Override
    public List<String> getChildren() {
        return children;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.product.BusinessProduct#getParents()
     */
    @Override
    public List<String> getParents() {
        return parents;
    }

    /**
     * set children
     * 
     * @param pChildren
     *            the children to set
     */
    public void setChildren(List<String> pChildren) {
        children = pChildren;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set parents
     * 
     * @param pParents
     *            the parents to set
     */
    public void setParents(List<String> pParents) {
        parents = pParents;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

}
