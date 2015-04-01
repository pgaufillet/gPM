/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.product.service;

import org.topcased.gpm.business.product.impl.CacheableProductType;

/**
 * Old product type data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableProductType
 */
public class ProductTypeData implements java.io.Serializable {
    private static final long serialVersionUID = -1846894519337251073L;

    /**
     * Default constructor.
     */
    public ProductTypeData() {
    }

    /**
     * Constructor taking all properties.
     */
    public ProductTypeData(final String pId, final String pName,
            final String pDescription) {
        id = pId;
        name = pName;
        description = pDescription;
    }

    /**
     * Copies constructor from other ProductTypeData
     */
    public ProductTypeData(ProductTypeData pOtherBean) {
        if (pOtherBean != null) {
            id = pOtherBean.getId();
            name = pOtherBean.getName();
            description = pOtherBean.getDescription();
        }
    }

    private String id;

    /**
     * 
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private String name;

    /**
     * 
     */
    public String getName() {
        return name;
    }

    public void setName(final String pName) {
        name = pName;
    }

    private String description;

    /**
     * <p>
     * Translated value of description if exists. The description non translated
     * value otherwise.
     * </p>
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(final String pDescription) {
        description = pDescription;
    }
}