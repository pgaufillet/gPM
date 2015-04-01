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
@javax.persistence.Table(name = "SIMPLE_FIELD")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class SimpleField extends org.topcased.gpm.domain.fields.Field {
    private static final long serialVersionUID = -4946518785036302670L;

    protected org.topcased.gpm.domain.fields.FieldType type;

    /**
     * 
     */
    @javax.persistence.Column(name = "TYPE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "org.topcased.gpm.domain.fields.FieldTypeEnum")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    public org.topcased.gpm.domain.fields.FieldType getType() {
        return this.type;
    }

    public void setType(org.topcased.gpm.domain.fields.FieldType pType) {
        this.type = pType;
    }

    protected int maxSize;

    /**
     * 
     */
    @javax.persistence.Column(name = "MAX_SIZE", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "int")
    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(int pMaxSize) {
        this.maxSize = pMaxSize;
    }

    protected org.topcased.gpm.domain.fields.ScalarValue defaultValue;

    /**
     * 
     */
    @javax.persistence.OneToOne(cascade = { javax.persistence.CascadeType.ALL }, targetEntity = org.topcased.gpm.domain.fields.ScalarValue.class)
    @javax.persistence.JoinColumn(name = "DEFAULT_VALUE_FK")
    @org.hibernate.annotations.ForeignKey(name = "SIMPLE_FIELD_DEFAULT_VALUE_FKC")
    public org.topcased.gpm.domain.fields.ScalarValue getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(
            org.topcased.gpm.domain.fields.ScalarValue pDefaultValue) {
        this.defaultValue = pDefaultValue;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.FieldImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.Field#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.FieldImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.Field#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.fields.SimpleField}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.fields.SimpleField}
     */
    public static org.topcased.gpm.domain.fields.SimpleField newInstance() {
        return new org.topcased.gpm.domain.fields.SimpleField();
    }

    // HibernateEntity.vsl merge-point
}