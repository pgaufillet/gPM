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
package org.topcased.gpm.domain.attributes;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "ATTRIBUTE_VALUE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class AttributeValue implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = -190081920801159833L;

    protected java.lang.String stringValue;

    /**
     * Max length of string that can be stored in a VARCHAR (larger strings are
     * stored in a CLOB).
     */
    private static final int MAX_VARCHAR_LENGTH = 4000;

    /**
     * 
     */
    @javax.persistence.Column(name = "STRING_VALUE", length = 255, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "attribute_value_idx")
    public java.lang.String getStringValue() {
        return this.stringValue;
    }

    public void setStringValue(java.lang.String pStringValue) {
        this.stringValue = pStringValue;
    }

    protected java.lang.String largeStringValue;

    /**
     * 
     */
    @javax.persistence.Column(name = "LARGE_STRING_VALUE", unique = false)
    @org.hibernate.annotations.Type(type = "org.andromda.persistence.hibernate.usertypes.HibernateStringClobType")
    public java.lang.String getLargeStringValue() {
        return this.largeStringValue;
    }

    public void setLargeStringValue(java.lang.String pLargeStringValue) {
        this.largeStringValue = pLargeStringValue;
    }

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
     * 
     */
    @javax.persistence.Transient
    public java.lang.String getValue() {
        if (largeStringValue != null) {
            return largeStringValue;
        }
        else {
            return stringValue;
        }
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public void setValue(java.lang.String pValue) {
        if (pValue != null && pValue.length() > MAX_VARCHAR_LENGTH) {
            largeStringValue = pValue;
            stringValue = null;
        }
        else {
            stringValue = pValue;
            largeStringValue = null;
        }
    }

    /**
     * Returns <code>true</code> if the argument is an AttributeValue instance
     * and all identifiers for this entity equal the identifiers of the argument
     * entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof AttributeValue)) {
            return false;
        }
        final AttributeValue lAttributeValue = (AttributeValue) pObject;
        if (this.getId() == null || lAttributeValue.getId() == null
                || !lAttributeValue.getId().equals(getId())) {
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

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.attributes.AttributeValue}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.attributes.AttributeValue}
     */
    public static org.topcased.gpm.domain.attributes.AttributeValue newInstance() {
        return new org.topcased.gpm.domain.attributes.AttributeValue();
    }
}
