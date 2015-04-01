/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.product.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.NamedElement;
import org.topcased.gpm.domain.product.Product;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * The Class CacheableProduct.
 * 
 * @author llatil
 */
@SuppressWarnings("serial")
public class CacheableProduct extends CacheableValuesContainer {

    /** The parent names. */
    private List<String> parentNames;

    /** Children products name */
    private List<String> childrenNames;

    /** Product description */
    private String description;

    /**
     * Constructor for mutable/immutable switch
     */
    public CacheableProduct() {
        super();
        parentNames = new ArrayList<String>();
        childrenNames = new ArrayList<String>();
    }

    /**
     * Constructs a new CacheableProduct.
     * 
     * @param pProduct
     *            Product entity
     * @param pProductType
     *            Product type (retrieved from cache)
     * @param pParentNames
     *            List of all parent names for the product
     */
    @SuppressWarnings("unchecked")
    public CacheableProduct(Product pProduct,
            CacheableFieldsContainer pProductType, List<String> pParentNames) {
        super(pProduct, pProductType);

        setProductName(getFunctionalReference());

        parentNames = new ArrayList<String>(pParentNames);

        //Use Query instead of Product.getChildren because no guaranteed order
        Session lSess = GpmSessionFactory.getHibernateSession();
        Query lQuery =
                lSess.createFilter(pProduct.getChildren(),
                        "select name order by this.name");
        childrenNames = lQuery.list();

        description = pProduct.getDescription();
    }

    /**
     * Create a new cacheable product data.
     * 
     * @param pProduct
     *            Serializable sheet
     * @param pType
     *            Type of the sheet
     */
    public CacheableProduct(
            org.topcased.gpm.business.serialization.data.Product pProduct,
            CacheableProductType pType) {
        super(pProduct, pType);

        environmentNames = pProduct.getEnvironmentNamesAsStrings();
        parentNames = pProduct.getParentsAsStrings();
        description = pProduct.getDescription();
        setProductName(pProduct.getName());
        setFunctionalReference(pProduct.getName());
    }

    /**
     * Gets the parent names.
     * 
     * @return the parent names
     */
    public List<String> getParentNames() {
        return parentNames;
    }

    /**
     * Sets the parent names.
     * 
     * @param pParentNames
     *            the new parent names
     */
    public void setParentNames(List<String> pParentNames) {
        parentNames = pParentNames;
    }

    /**
     * Get the childrenNames
     * 
     * @return the childrenNames
     */
    public List<String> getChildrenNames() {
        return childrenNames;
    }

    /**
     * Set the childrenNames
     * 
     * @param pChildrenNames
     *            the new childrenNames
     */
    public void setChildrenNames(List<String> pChildrenNames) {
        childrenNames = pChildrenNames;
    }

    /**
     * Marshal.
     * 
     * @param pSerializableContainer
     *            the serializable product
     */
    @Override
    public void marshal(Object pSerializableContainer) {
        super.marshal(pSerializableContainer);

        org.topcased.gpm.business.serialization.data.Product lSerializableProduct =
                (org.topcased.gpm.business.serialization.data.Product) pSerializableContainer;
        List<NamedElement> lEnvLinkedList =
                new ArrayList<NamedElement>(getEnvironmentNames().size());
        for (String lEnvironmentName : getEnvironmentNames()) {
            lEnvLinkedList.add(new NamedElement(lEnvironmentName));
        }
        lSerializableProduct.setEnvironmentNames(lEnvLinkedList);
        lSerializableProduct.setName(getProductName());
        lSerializableProduct.setDescription(getDescription());

        lSerializableProduct.copyInParents(getParentNames());

        // Clean unused attributes
        lSerializableProduct.setProductName(null);
        lSerializableProduct.setReference(null);
        lSerializableProduct.setVersion(null);
    }

    @Override
    public Object marshal() {
        org.topcased.gpm.business.serialization.data.Product lSerializableProduct;
        lSerializableProduct =
                new org.topcased.gpm.business.serialization.data.Product();
        marshal(lSerializableProduct);

        return lSerializableProduct;
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
