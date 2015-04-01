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
package org.topcased.gpm.domain.sheet;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "SHEET_HISTORY")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class SheetHistory implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = 4830628059597423898L;

    protected java.lang.String originState;

    /**
     * <p>
     * Name of the origin sate in the life cycle.
     * </p>
     */
    @javax.persistence.Column(name = "ORIGIN_STATE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getOriginState() {
        return this.originState;
    }

    public void setOriginState(java.lang.String pOriginState) {
        this.originState = pOriginState;
    }

    protected java.lang.String destinationState;

    /**
     * 
     */
    @javax.persistence.Column(name = "DESTINATION_STATE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getDestinationState() {
        return this.destinationState;
    }

    public void setDestinationState(java.lang.String pDestinationState) {
        this.destinationState = pDestinationState;
    }

    protected java.lang.String transitionName;

    /**
     * 
     */
    @javax.persistence.Column(name = "TRANSITION_NAME", length = 4000, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getTransitionName() {
        return this.transitionName;
    }

    public void setTransitionName(java.lang.String pTransitionName) {
        this.transitionName = pTransitionName;
    }

    protected java.sql.Timestamp changeDate;

    /**
     * <p>
     * Date of the change in the history.
     * </p>
     */
    @javax.persistence.Column(name = "CHANGE_DATE", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.sql.Timestamp")
    public java.sql.Timestamp getChangeDate() {
        return this.changeDate;
    }

    public void setChangeDate(java.sql.Timestamp pChangeDate) {
        this.changeDate = pChangeDate;
    }

    protected java.lang.String loginName;

    /**
     * 
     */
    @javax.persistence.Column(name = "LOGIN_NAME", length = 100, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(java.lang.String pLoginName) {
        this.loginName = pLoginName;
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
     * Returns <code>true</code> if the argument is an SheetHistory instance and
     * all identifiers for this entity equal the identifiers of the argument
     * entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof SheetHistory)) {
            return false;
        }
        final SheetHistory lSheetHistory = (SheetHistory) pObject;
        if (this.getId() == null || lSheetHistory.getId() == null
                || !lSheetHistory.getId().equals(getId())) {
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
     * {@link org.topcased.gpm.domain.sheet.SheetHistory}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.sheet.SheetHistory}
     */
    public static org.topcased.gpm.domain.sheet.SheetHistory newInstance() {
        return new org.topcased.gpm.domain.sheet.SheetHistory();
    }

}