/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class AccessCtl.
 * 
 * @author llatil
 */
public abstract class AccessCtl {

    /** The role. */
    @XStreamAsAttribute
    private String role;

    /** The product name. */
    @XStreamAsAttribute
    private String productName;

    /** The state name. */
    @XStreamAsAttribute
    private String stateName;

    /**
     * The sheet type. (Keep it for compatibility reason with previous versions)
     */
    @XStreamAsAttribute
    private String sheetType;

    /** The container type */
    @XStreamAsAttribute
    private String typeName;

    /** The attributes. */
    private List<Attribute> attributes;

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Gets the role.
     * 
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Gets the sheet type.
     * 
     * @return the sheet type
     * @deprecated
     */
    public String getSheetType() {
        return sheetType;
    }

    /**
     * Gets the state name.
     * 
     * @return the state name
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * get type
     * 
     * @return the type
     */
    public String getTypeName() {
        if (null != typeName) {
            return typeName;
        }
        return sheetType;
    }

    public void setTypeName(String pTypeName) {
        typeName = pTypeName;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(List<Attribute> pAttributes) {
        attributes = pAttributes;
    }
}
