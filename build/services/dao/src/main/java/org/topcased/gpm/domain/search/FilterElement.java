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
package org.topcased.gpm.domain.search;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "FILTER_ELEMENT")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Proxy(lazy = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class FilterElement implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = 6539043370267111939L;

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
     * Returns <code>true</code> if the argument is an FilterElement instance
     * and all identifiers for this entity equal the identifiers of the argument
     * entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof FilterElement)) {
            return false;
        }
        final FilterElement lFilterElement = (FilterElement) pObject;
        if (this.getId() == null || lFilterElement.getId() == null
                || !lFilterElement.getId().equals(getId())) {
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
