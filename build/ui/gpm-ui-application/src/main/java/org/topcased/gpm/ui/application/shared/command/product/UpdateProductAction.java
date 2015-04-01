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
import java.util.Map;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.facade.shared.container.field.UiField;

/**
 * UpdateProductAction
 * 
 * @author nveillet
 */
public class UpdateProductAction implements Action<GetProductResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -6850916400988991711L;

    private List<String> children;

    private List<UiField> fields;

    private String description;

    private Map<String, List<UiField>> links;

    private List<String> parents;

    private String productId;

    /**
     * create action
     */
    public UpdateProductAction() {
    }

    /**
     * create action with values
     * 
     * @param pProductId
     *            the product identifier
     * @param pDescription
     *            the description to update
     * @param pFields
     *            the fields to update
     * @param pLinks
     *            the links to update
     * @param pParents
     *            the parents products
     * @param pChildren
     *            the children products
     */
    public UpdateProductAction(String pProductId, String pDescription,
            List<UiField> pFields, Map<String, List<UiField>> pLinks,
            List<String> pParents, List<String> pChildren) {
        productId = pProductId;
        fields = pFields;
        links = pLinks;
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
     * get links
     * 
     * @return the links
     */
    public Map<String, List<UiField>> getLinks() {
        return links;
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
     * set links
     * 
     * @param pLinks
     *            the links to set
     */
    public void setLinks(Map<String, List<UiField>> pLinks) {
        links = pLinks;
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
