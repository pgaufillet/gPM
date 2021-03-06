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

import javax.persistence.Transient;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "SCALAR_VALUE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@javax.persistence.DiscriminatorColumn(name = "class", discriminatorType = javax.persistence.DiscriminatorType.STRING, length = 50)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Proxy(lazy = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ScalarValue implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = -4809580553434134759L;

    protected java.lang.String id;

    /**
     * 
     */
    @javax.persistence.Id
    @org.hibernate.annotations.GenericGenerator(name = "UUID_GEN", strategy = "org.topcased.gpm.domain.util.UuidGenerator")
    @javax.persistence.GeneratedValue(generator = "UUID_GEN")
    @javax.persistence.Column(name = "ID", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValue#getAsString()
     */
    @Transient
    public java.lang.String getAsString() {
        throw new java.lang.UnsupportedOperationException(
                "org.topcased.gpm.domain.fields.ScalarValue.getAsString() not implemented!");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValue#setAsString(java.lang.String)
     */
    @Transient
    public void setAsString(java.lang.String pValueAsString) {
        throw new java.lang.UnsupportedOperationException(
                "org.topcased.gpm.domain.fields.ScalarValue.setAsString(java.lang.String valueAsString) not implemented!");
    }

    /**
     * <p>
     * Returns the underlying Java object representing the value.
     * </p>
     */
    @javax.persistence.Transient
    public java.lang.Object getAsObject() {
        return this;
    }

    /**
     * <p>
     * Returns true if this scalar value represents a 'null' value (that is an
     * empty field value).
     * </p>
     */
    @Transient
    public final boolean isNull() {
        return (null == getAsObject());
    }

    /**
     * Returns <code>true</code> if the argument is an ScalarValue instance and
     * all identifiers for this entity equal the identifiers of the argument
     * entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof ScalarValue)) {
            return false;
        }
        final ScalarValue lScalarValue = (ScalarValue) pObject;
        if (this.getId() == null || lScalarValue.getId() == null
                || !lScalarValue.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     * 
     * @return a hash code value for this object.
     * @see java.lang.Object#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        int lHashCode = 0;
        if (id != null) {
            lHashCode = id.hashCode();
        }
        return lHashCode;
    }

}