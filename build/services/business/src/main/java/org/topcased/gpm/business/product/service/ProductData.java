/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.product.service;

import org.topcased.gpm.business.fields.MultipleLineFieldData;
import org.topcased.gpm.business.product.impl.CacheableProduct;

/**
 * Old product data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableProduct
 */
public class ProductData implements java.io.Serializable {
    private static final long serialVersionUID = 6651287950641612450L;

    /**
     * Default constructor.
     */
    public ProductData() {
    }

    /**
     * Constructor taking all properties.
     */
    public ProductData(final String pName, final String pId,
            final String pProcessName, final String[] pEnvironmentNames,
            final String[] pChildrenNames, final String pDescription,
            final String pProductTypeName, final String pProductTypeId,
            final MultipleLineFieldData[] pMultipleLineFieldDatas) {
        name = pName;
        id = pId;
        processName = pProcessName;
        environmentNames = pEnvironmentNames;
        childrenNames = pChildrenNames;
        description = pDescription;
        productTypeName = pProductTypeName;
        productTypeId = pProductTypeId;
        multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

    /**
     * Copies constructor from other ProductData
     */
    public ProductData(ProductData pOtherBean) {
        if (pOtherBean != null) {
            name = pOtherBean.getName();
            id = pOtherBean.getId();
            processName = pOtherBean.getProcessName();
            environmentNames = pOtherBean.getEnvironmentNames();
            childrenNames = pOtherBean.getChildrenNames();
            description = pOtherBean.getDescription();
            productTypeName = pOtherBean.getProductTypeName();
            productTypeId = pOtherBean.getProductTypeId();
            multipleLineFieldDatas = pOtherBean.getMultipleLineFieldDatas();
        }
    }

    private String name;

    /**
     * 
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(final String pName) {
        name = pName;
    }

    private String id;

    /**
     * <p>
     * DB identifier of the product
     * </p>
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private String processName;

    /**
     * 
     */
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(final String pProcessName) {
        processName = pProcessName;
    }

    private String[] environmentNames;

    /**
     * 
     */
    public String[] getEnvironmentNames() {
        return environmentNames;
    }

    public void setEnvironmentNames(final String[] pEnvironmentNames) {
        environmentNames = pEnvironmentNames;
    }

    private String[] childrenNames;

    /**
     * 
     */
    public String[] getChildrenNames() {
        return childrenNames;
    }

    public void setChildrenNames(final String[] pChildrenNames) {
        childrenNames = pChildrenNames;
    }

    private String description;

    /**
     * 
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(final String pDescription) {
        description = pDescription;
    }

    private String productTypeName;

    /**
     * 
     */
    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(final String pProductTypeName) {
        productTypeName = pProductTypeName;
    }

    private java.lang.String productTypeId;

    /**
     * 
     */
    public String getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(final String pProductTypeId) {
        productTypeId = pProductTypeId;
    }

    private MultipleLineFieldData[] multipleLineFieldDatas;

    /**
     * Get the multipleLineFieldDatas
     */
    public MultipleLineFieldData[] getMultipleLineFieldDatas() {
        return multipleLineFieldDatas;
    }

    /**
     * Set the multipleLineFieldDatas
     */
    public void setMultipleLineFieldDatas(
            final MultipleLineFieldData[] pMultipleLineFieldDatas) {
        multipleLineFieldDatas = pMultipleLineFieldDatas;
    }

}