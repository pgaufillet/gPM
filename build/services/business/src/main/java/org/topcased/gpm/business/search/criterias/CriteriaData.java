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
package org.topcased.gpm.business.search.criterias;

/**
 * @author Atos
 */
public class CriteriaData implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public CriteriaData() {
    }

    /**
     * Constructor taking all properties.
     */
    public CriteriaData(final java.lang.String pId) {
        this.id = pId;
    }

    /**
     * Copies constructor from other CriteriaData
     */
    public CriteriaData(CriteriaData pOtherBean) {
        if (pOtherBean != null) {
            this.id = pOtherBean.getId();
        }
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

}