/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.product;

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * CreateProductAction
 * 
 * @author nveillet
 */
public class CreateProductAction extends
        AbstractCommandAction<GetProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1385274115707354305L;

    private List<String> children;

    private List<UiField> fields;

    private String description;

    private List<String> parents;

    private String productId;

    /**
     * create action
     */
    public CreateProductAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductId
     *            the product identifier
     * @param pProductName
     *            the product name
     * @param pFields
     *            the fields to create
     * @param pParents
     *            the parents products
     * @param pChildren
     *            the children products
     */
    public CreateProductAction(String pProductId, String pProductName,
            List<UiField> pFields, List<String> pParents,
            List<String> pChildren, String pDescription) {
        super(pProductName);
        productId = pProductId;
        fields = pFields;
        parents = pParents;
        children = pChildren;
        description = pDescription;
    }

    /**
     * get children
     * 
     * @return the children
     */
    public List<String> getChildren() {
        return children;
    }

    /**
     * get fields
     * 
     * @return the fields
     */
    public List<UiField> getFields() {
        return fields;
    }

    /**
     * get parents
     * 
     * @return the parents
     */
    public List<String> getParents() {
        return parents;
    }

    /**
     * get product identifier
     * 
     * @return the product identifier
     */
    public String getProductId() {
        return productId;
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
     * set fields
     * 
     * @param pFields
     *            the fields to set
     */
    public void setFields(List<UiField> pFields) {
        fields = pFields;
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
     * set product identifier
     * 
     * @param pProductId
     *            the product identifier to set
     */
    public void setProductId(String pProductId) {
        productId = pProductId;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getProductDescription() {
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
