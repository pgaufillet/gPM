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
package org.topcased.gpm.domain.link;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "LINK_TYPE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class LinkType extends org.topcased.gpm.domain.fields.FieldsContainer {
    private static final long serialVersionUID = -6946262042282010648L;

    protected java.lang.Integer lowBound;

    /**
     * 
     */
    @javax.persistence.Column(name = "LOW_BOUND", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Integer")
    public java.lang.Integer getLowBound() {
        return this.lowBound;
    }

    public void setLowBound(java.lang.Integer pLowBound) {
        this.lowBound = pLowBound;
    }

    protected java.lang.Integer highBound;

    /**
     * 
     */
    @javax.persistence.Column(name = "HIGH_BOUND", unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.Integer")
    public java.lang.Integer getHighBound() {
        return this.highBound;
    }

    public void setHighBound(java.lang.Integer pHighBound) {
        this.highBound = pHighBound;
    }

    protected boolean unidirectionalCreation;

    /**
     * <p>
     * Specify if this link type is unidirectional for the creation (can be
     * created only from the origin element) or not.
     * </p>
     */
    @javax.persistence.Column(name = "UNIDIRECTIONAL_CREATION", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "boolean")
    public boolean isUnidirectionalCreation() {
        return this.unidirectionalCreation;
    }

    public void setUnidirectionalCreation(boolean pUnidirectionalCreation) {
        this.unidirectionalCreation = pUnidirectionalCreation;
    }

    protected boolean unidirectionalNavigation;

    /**
     * <p>
     * Specify if this link type is unidirectional for the navigation (is
     * visible, and be be followed, only from the origin element) or not.
     * </p>
     */
    @javax.persistence.Column(name = "UNIDIRECTIONAL_NAVIGATION", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "boolean")
    public boolean isUnidirectionalNavigation() {
        return this.unidirectionalNavigation;
    }

    public void setUnidirectionalNavigation(boolean pUnidirectionalNavigation) {
        this.unidirectionalNavigation = pUnidirectionalNavigation;
    }

    protected org.topcased.gpm.domain.fields.FieldsContainer originType;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.fields.FieldsContainer.class)
    @javax.persistence.JoinColumn(nullable = false, name = "ORIGIN_TYPE_FK")
    @org.hibernate.annotations.ForeignKey(name = "LINK_TYPE_ORIGIN_TYPE_FKC")
    public org.topcased.gpm.domain.fields.FieldsContainer getOriginType() {
        return this.originType;
    }

    public void setOriginType(
            org.topcased.gpm.domain.fields.FieldsContainer pOriginType) {
        this.originType = pOriginType;
    }

    protected org.topcased.gpm.domain.fields.FieldsContainer destType;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.fields.FieldsContainer.class)
    @javax.persistence.JoinColumn(nullable = false, name = "DEST_TYPE_FK")
    @org.hibernate.annotations.ForeignKey(name = "LINK_TYPE_DEST_TYPE_FKC")
    public org.topcased.gpm.domain.fields.FieldsContainer getDestType() {
        return this.destType;
    }

    public void setDestType(
            org.topcased.gpm.domain.fields.FieldsContainer pDestType) {
        this.destType = pDestType;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.FieldsContainerImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.FieldsContainerImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.FieldsContainer#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of {@link org.topcased.gpm.domain.link.LinkType}
     * 
     * @return a new instance of {@link org.topcased.gpm.domain.link.LinkType}
     */
    public static org.topcased.gpm.domain.link.LinkType newInstance() {
        return new org.topcased.gpm.domain.link.LinkType();
    }

}