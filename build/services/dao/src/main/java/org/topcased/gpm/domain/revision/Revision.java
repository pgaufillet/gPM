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
package org.topcased.gpm.domain.revision;

import javax.persistence.Transient;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "REVISION")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Proxy(lazy = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Revision extends org.topcased.gpm.domain.fields.ValuesContainer {
    private static final long serialVersionUID = 2222443844571065951L;

    protected java.lang.String label;

    /**
     * 
     */
    @javax.persistence.Column(name = "LABEL", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "revision_label_idx")
    public java.lang.String getLabel() {
        return this.label;
    }

    public void setLabel(java.lang.String pLabel) {
        this.label = pLabel;
    }

    protected java.util.Date creationDate;

    /**
     * 
     */
    @javax.persistence.Column(name = "CREATION_DATE", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.util.Date")
    public java.util.Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(java.util.Date pCreationDate) {
        this.creationDate = pCreationDate;
    }

    protected java.lang.String author;

    /**
     * 
     */
    @javax.persistence.Column(name = "AUTHOR", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getAuthor() {
        return this.author;
    }

    public void setAuthor(java.lang.String pAuthor) {
        this.author = pAuthor;
    }

    protected java.lang.String referenceCache;

    /**
     * 
     */
    @javax.persistence.Column(name = "REFERENCE_CACHE", length = 50, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "ref_cache_idx")
    public java.lang.String getReferenceCache() {
        return this.referenceCache;
    }

    public void setReferenceCache(java.lang.String pReferenceCache) {
        this.referenceCache = pReferenceCache;
    }

    protected java.lang.String id;

    /**
     * 
     */
    protected org.topcased.gpm.domain.attributes.AttributesContainer revisionAttrs;

    /**
     * 
     */
    @javax.persistence.OneToOne(cascade = { javax.persistence.CascadeType.ALL }, fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.attributes.AttributesContainer.class)
    @javax.persistence.JoinColumn(name = "REVISION_ATTRS_FK")
    @org.hibernate.annotations.ForeignKey(name = "REVISION_REVISION_ATTRS_FKC")
    @org.hibernate.annotations.Index(name = "revison_attribute_fk_idx")
    public org.topcased.gpm.domain.attributes.AttributesContainer getRevisionAttrs() {
        return this.revisionAttrs;
    }

    public void setRevisionAttrs(
            org.topcased.gpm.domain.attributes.AttributesContainer pRevisionAttrs) {
        this.revisionAttrs = pRevisionAttrs;
    }

    /**
     * Returns <code>true</code> if the argument is an Revision instance and all
     * identifiers for this entity equal the identifiers of the argument entity.
     * The <code>equals</code> method of the parent entity will also need to
     * return <code>true</code>. Returns <code>false</code> otherwise.
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        if (this == pObject) {
            return true;
        }
        if (!(pObject instanceof Revision)) {
            return false;
        }
        final Revision lRevision = (Revision) pObject;
        if (this.getId() == null || lRevision.getId() == null
                || !lRevision.getId().equals(getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers and the hash code
     * of the parent entity.
     * 
     * @return a hash code value for this object.
     * @see org.topcased.gpm.domain.fields.ValuesContainer#hashCode()
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
      * 
      */
    @Override
    @Transient
    public String getFunctionalReference() {
        return getReferenceCache();
    }

}