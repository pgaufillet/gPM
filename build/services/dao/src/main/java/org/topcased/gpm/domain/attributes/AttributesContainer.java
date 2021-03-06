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
@javax.persistence.Table(name = "ATTRIBUTES_CONTAINER")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
public class AttributesContainer implements java.io.Serializable,
        org.topcased.gpm.domain.PersistentObject {
    private static final long serialVersionUID = 7013308844961984875L;

    protected java.lang.String id;

    /**
     * 
     */
    @javax.persistence.Id
    @org.hibernate.annotations.GenericGenerator(name = "UUID_GEN", strategy = "org.topcased.gpm.domain.util.UuidGenerator")
    @javax.persistence.GeneratedValue(generator = "UUID_GEN")
    @javax.persistence.Column(name = "ID", nullable = false, length = 50, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String pId) {
        this.id = pId;
    }

    protected java.util.Set<org.topcased.gpm.domain.attributes.Attribute> attributeList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.attributes.Attribute>();

    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.attributes.Attribute.class)
    @javax.persistence.JoinColumn(name = "ATTRIBUTES_CONTAINER_FK")
    @org.hibernate.annotations.OrderBy(clause = "name")
    @org.hibernate.annotations.ForeignKey(name = "ATTRIBUTES_CONTAINER_ATTRIBUTE")
    @org.hibernate.annotations.IndexColumn(name = "attribute_container_fk_idx")
    public java.util.Set<org.topcased.gpm.domain.attributes.Attribute> getAttributes() {
        return this.attributeList;
    }

    public void setAttributes(
            java.util.Set<org.topcased.gpm.domain.attributes.Attribute> pAttributes) {
        this.attributeList = pAttributes;
    }

    /**
     * Add a org.topcased.gpm.domain.attributes.Attribute.
     * 
     * @param pAttributes
     *            the entity to add.
     */
    public void addToAttributeList(
            org.topcased.gpm.domain.attributes.Attribute pAttributes) {
        if (this.attributeList == null) {
            this.attributeList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.attributes.Attribute>();
        }
        this.attributeList.add(pAttributes);
    }

    /**
     * Remove a org.topcased.gpm.domain.attributes.Attribute.
     * 
     * @param pAttributes
     *            the entity to remove.
     */
    public void removeFromAttributeList(
            org.topcased.gpm.domain.attributes.Attribute pAttributes) {
        if (this.attributeList != null) {
            this.attributeList.remove(pAttributes);
        }
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public void accept(org.topcased.gpm.domain.util.Visitor pVisitor) {
        pVisitor.visit(this);
    }

    /**
     * Returns <code>true</code> if the argument is an AttributesContainer
     * instance and all identifiers for this entity equal the identifiers of the
     * argument entity. Returns <code>false</code> otherwise.
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof AttributesContainer)) {
            return false;
        }
        final AttributesContainer lAttributesContainer =
                (AttributesContainer) pObject;
        if (this.getId() == null || lAttributesContainer.getId() == null
                || !lAttributesContainer.getId().equals(getId())) {
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
     * {@link org.topcased.gpm.domain.attributes.AttributesContainer}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.attributes.AttributesContainer}
     */
    public static org.topcased.gpm.domain.attributes.AttributesContainer newInstance() {
        return new org.topcased.gpm.domain.attributes.AttributesContainer();
    }

    // HibernateEntity.vsl merge-point
}
