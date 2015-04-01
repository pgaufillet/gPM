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
// Generated by: HibernateEntity.vsl in andromda-hibernate-cartridge.
//
package org.topcased.gpm.domain.accesscontrol;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "TYPE_ACCESS_CONTROL")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TypeAccessControl extends
        org.topcased.gpm.domain.accesscontrol.AccessControl {
    private static final long serialVersionUID = -511612897233268070L;

    protected java.lang.Boolean updatable;

    /**
     * 
     */
    @javax.persistence.Column(name = "UPDATABLE", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(java.lang.Boolean pUpdatable) {
        this.updatable = pUpdatable;
    }

    protected java.lang.Boolean confidential;

    /**
     * 
     */
    @javax.persistence.Column(name = "CONFIDENTIAL", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getConfidential() {
        return this.confidential;
    }

    public void setConfidential(java.lang.Boolean pConfidential) {
        this.confidential = pConfidential;
    }

    protected java.lang.Boolean deletable;

    /**
     * 
     */
    @javax.persistence.Column(name = "DELETABLE", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getDeletable() {
        return this.deletable;
    }

    public void setDeletable(java.lang.Boolean pDeletable) {
        this.deletable = pDeletable;
    }

    protected java.lang.Boolean creatable;

    /**
     * 
     */
    @javax.persistence.Column(name = "CREATABLE", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getCreatable() {
        return this.creatable;
    }

    public void setCreatable(java.lang.Boolean pCreatable) {
        this.creatable = pCreatable;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.accesscontrol.AccessControlImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AccessControl#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.accesscontrol.AccessControlImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.accesscontrol.AccessControl#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.accesscontrol.TypeAccessControl}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.accesscontrol.TypeAccessControl}
     */
    public static org.topcased.gpm.domain.accesscontrol.TypeAccessControl newInstance() {
        return new org.topcased.gpm.domain.accesscontrol.TypeAccessControl();
    }

    // HibernateEntity.vsl merge-point
}