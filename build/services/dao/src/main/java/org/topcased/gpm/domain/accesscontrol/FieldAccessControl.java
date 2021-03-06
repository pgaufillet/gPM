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
@javax.persistence.Table(name = "FIELD_ACCESS_CONTROL")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class FieldAccessControl extends
        org.topcased.gpm.domain.accesscontrol.AccessControl {
    private static final long serialVersionUID = 1176476144059407981L;

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

    protected java.lang.Boolean mandatory;

    /**
     * 
     */
    @javax.persistence.Column(name = "MANDATORY", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getMandatory() {
        return this.mandatory;
    }

    public void setMandatory(java.lang.Boolean pMandatory) {
        this.mandatory = pMandatory;
    }

    protected java.lang.Boolean exportable;

    /**
     * 
     */
    @javax.persistence.Column(name = "EXPORTABLE", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    public java.lang.Boolean getExportable() {
        return this.exportable;
    }

    public void setExportable(java.lang.Boolean pExportable) {
        this.exportable = pExportable;
    }

    protected java.lang.String visibleTypeId;

    /**
     * 
     */
    @javax.persistence.Column(name = "VISIBLE_TYPE_ID", length = 50, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getVisibleTypeId() {
        return this.visibleTypeId;
    }

    public void setVisibleTypeId(java.lang.String pVisibleTypeId) {
        this.visibleTypeId = pVisibleTypeId;
    }

    protected java.lang.String id;

    /**
     * 
     */
    protected org.topcased.gpm.domain.fields.Field fieldControl;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.fields.Field.class)
    @javax.persistence.JoinColumn(nullable = false, name = "FIELD_CONTROL_FK")
    @org.hibernate.annotations.ForeignKey(name = "FIELD_ACCESS_CONTROL_FIELD_CON")
    public org.topcased.gpm.domain.fields.Field getFieldControl() {
        return this.fieldControl;
    }

    public void setFieldControl(
            org.topcased.gpm.domain.fields.Field pFieldControl) {
        this.fieldControl = pFieldControl;
    }

    /**
     * Returns <code>true</code> if the argument is an FieldAccessControl
     * instance and all identifiers for this entity equal the identifiers of the
     * argument entity. The <code>equals</code> method of the parent entity will
     * also need to return <code>true</code>. Returns <code>false</code>
     * otherwise.
     * 
     * @param pObject
     *            the object to compare with
     * @return a boolean
     * @see org.topcased.gpm.domain.accesscontrol.AccessControl#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof FieldAccessControl)) {
            return false;
        }
        final FieldAccessControl lFieldAccessControl =
                (FieldAccessControl) pObject;
        if (this.getId() == null || lFieldAccessControl.getId() == null
                || !lFieldAccessControl.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code
     * of the parent entity.
     * 
     * @return a hash code value for this object.
     * @see org.topcased.gpm.domain.accesscontrol.AccessControl#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        int lHashCode = super.hashCode();
        int lId = 0;
        if (id != null) {
            lId = id.hashCode();
        }
        lHashCode = HASHCODE_CONSTANT * lHashCode + lId;

        return lHashCode;
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.accesscontrol.FieldAccessControl}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.accesscontrol.FieldAccessControl}
     */
    public static org.topcased.gpm.domain.accesscontrol.FieldAccessControl newInstance() {
        return new org.topcased.gpm.domain.accesscontrol.FieldAccessControl();
    }

    // HibernateEntity.vsl merge-point
}