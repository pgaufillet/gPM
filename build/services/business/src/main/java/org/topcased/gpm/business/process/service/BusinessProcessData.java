/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.process.service;

/**
 * @author Atos
 */
public class BusinessProcessData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public BusinessProcessData() {
    }

    /**
     * Constructor taking all properties.
     */
    public BusinessProcessData(final java.lang.String pName) {
        this.name = pName;
    }

    /**
     * Copies constructor from other BusinessProcessData
     */
    public BusinessProcessData(BusinessProcessData pOtherBean) {
        if (pOtherBean != null) {
            this.name = pOtherBean.getName();
        }
    }

    private java.lang.String name;

    /**
     * 
     */
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

}