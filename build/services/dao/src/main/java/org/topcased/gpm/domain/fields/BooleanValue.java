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

import org.apache.commons.lang.StringUtils;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.DiscriminatorValue("BooleanValueImpl")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BooleanValue extends org.topcased.gpm.domain.fields.ScalarValue {
    private static final long serialVersionUID = 5853299477631086585L;

    protected java.lang.Boolean boolValue;

    /**
     * 
     */
    @javax.persistence.Column(name = "BOOL_VALUE", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Boolean")
    @org.hibernate.annotations.Index(name = "boolean_value_idx")
    public java.lang.Boolean getBoolValue() {
        return this.boolValue;
    }

    public void setBoolValue(java.lang.Boolean pBoolValue) {
        this.boolValue = pBoolValue;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.ScalarValueImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValue#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.ScalarValueImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValue#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValueImpl#getAsString()
     */
    @Override
    @Transient
    public String getAsString() {
        if (boolValue != null) {
            return boolValue.toString();
        }
        // else
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValueImpl#setAsString(java.lang.String)
     */
    @Override
    @Transient
    public void setAsString(String pVal) {
        setBoolValue(Boolean.parseBoolean(pVal));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.domain.fields.ScalarValue#getAsObject()
     */
    @Override
    @Transient
    public Object getAsObject() {
        return boolValue;
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.fields.BooleanValue}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.fields.BooleanValue}
     */
    public static org.topcased.gpm.domain.fields.BooleanValue newInstance() {
        return new org.topcased.gpm.domain.fields.BooleanValue();
    }

}