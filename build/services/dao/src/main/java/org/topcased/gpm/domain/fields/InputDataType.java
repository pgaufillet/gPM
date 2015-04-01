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
package org.topcased.gpm.domain.fields;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "INPUT_DATA_TYPE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class InputDataType extends
        org.topcased.gpm.domain.fields.FieldsContainer {
    private static final long serialVersionUID = 3084718634437577046L;

    protected java.lang.String id;

    /**
     * 
     */
    protected org.topcased.gpm.domain.extensions.ExtendedAction extendedAction;

    /**
     * 
     */
    @javax.persistence.OneToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.extensions.ExtendedAction.class)
    @javax.persistence.JoinColumn(name = "EXTENDED_ACTION_FK")
    @org.hibernate.annotations.ForeignKey(name = "INPUT_DATA_TYPE_EXTENDED_ACTIO")
    public org.topcased.gpm.domain.extensions.ExtendedAction getExtendedAction() {
        return this.extendedAction;
    }

    public void setExtendedAction(
            org.topcased.gpm.domain.extensions.ExtendedAction pExtendedAction) {
        this.extendedAction = pExtendedAction;
    }

    /**
     * Returns <code>true</code> if the argument is an InputDataType instance
     * and all identifiers for this entity equal the identifiers of the argument
     * entity. The <code>equals</code> method of the parent entity will also
     * need to return <code>true</code>. Returns <code>false</code> otherwise.
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof InputDataType)) {
            return false;
        }
        final InputDataType lInputDataType = (InputDataType) pObject;
        if (this.getId() == null || lInputDataType.getId() == null
                || !lInputDataType.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code
     * of the parent entity.
     * 
     * @return a hash code value for this object.
     * @see org.topcased.gpm.domain.fields.FieldsContainer#hashCode()
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
     * {@link org.topcased.gpm.domain.fields.InputDataType}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.fields.InputDataType}
     */
    public static org.topcased.gpm.domain.fields.InputDataType newInstance() {
        return new org.topcased.gpm.domain.fields.InputDataType();
    }

    // HibernateEntity.vsl merge-point
}