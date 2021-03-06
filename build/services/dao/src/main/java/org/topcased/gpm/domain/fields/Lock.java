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
@javax.persistence.Table(name = "Access_Lock")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Lock implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {

    private static final long serialVersionUID = 9169779002611203373L;

    protected java.lang.String owner;

    /**
     * 
     */
    @javax.persistence.Column(name = "OWNER", length = 100, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "lock_owner_idx")
    public java.lang.String getOwner() {
        return this.owner;
    }

    public void setOwner(java.lang.String pOwner) {
        this.owner = pOwner;
    }

    protected org.topcased.gpm.common.valuesContainer.LockType lockType;

    /**
     * 
     */
    @javax.persistence.Column(name = "LOCK_TYPE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "org.topcased.gpm.common.valuesContainer.LockTypeEnum")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    public org.topcased.gpm.common.valuesContainer.LockType getLockType() {
        return this.lockType;
    }

    public void setLockType(
            org.topcased.gpm.common.valuesContainer.LockType pLockType) {
        this.lockType = pLockType;
    }

    protected java.lang.String sessionToken;

    /**
     * 
     */
    @javax.persistence.Column(name = "SESSION_TOKEN", length = 50, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "lock_session_token_idx")
    public java.lang.String getSessionToken() {
        return this.sessionToken;
    }

    public void setSessionToken(java.lang.String pSessionToken) {
        this.sessionToken = pSessionToken;
    }

    protected java.lang.String containerId;

    /**
     * 
     */
    @javax.persistence.Column(name = "CONTAINER_ID", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "lock_container_id_idx")
    public java.lang.String getContainerId() {
        return this.containerId;
    }

    public void setContainerId(java.lang.String pContainerId) {
        this.containerId = pContainerId;
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
     * Returns <code>true</code> if the argument is an Lock instance and all
     * identifiers for this entity equal the identifiers of the argument entity.
     * Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof Lock)) {
            return false;
        }
        final Lock lLock = (Lock) pObject;
        if (this.getId() == null || lLock.getId() == null
                || !lLock.getId().equals(getId())) {
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
     * Constructs new instances of {@link org.topcased.gpm.domain.fields.Lock}.
     * 
     * @return a new instance of {@link org.topcased.gpm.domain.fields.Lock}
     */
    public static org.topcased.gpm.domain.fields.Lock newInstance() {
        return new org.topcased.gpm.domain.fields.Lock();
    }

}
