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
package org.topcased.gpm.domain.product;

import javax.persistence.Transient;

import org.topcased.gpm.domain.util.IdentityFieldsContainerVisitor;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "PRODUCT")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Proxy(lazy = false)
public class Product extends org.topcased.gpm.domain.fields.ValuesContainer {
    private static final long serialVersionUID = -3176453151881251284L;

    protected java.lang.String name;

    /**
     * 
     */
    @javax.persistence.Column(name = "NAME", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    @org.hibernate.annotations.Index(name = "product_name_idx")
    public java.lang.String getName() {
        return this.name;
    }

    public void setName(java.lang.String pName) {
        this.name = pName;
    }

    protected java.lang.String description;

    /**
     * 
     */
    @javax.persistence.Column(name = "DESCRIPTION", length = 50, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String pDescription) {
        this.description = pDescription;
    }

    protected java.util.Set<org.topcased.gpm.domain.product.Product> productList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.product.Product>();

    /**
     * 
     */
    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.product.Product.class)
    @javax.persistence.JoinTable(name = "CHILDREN2PARENTS", joinColumns = @javax.persistence.JoinColumn(name = "PARENTS_FK"), inverseJoinColumns = @javax.persistence.JoinColumn(name = "CHILDREN_FK"))
    @org.hibernate.annotations.OrderBy(clause = "PARENTS_FK")
    @org.hibernate.annotations.ForeignKey(inverseName = "PRODUCT_PARENTS_FKC", name = "PRODUCT_CHILDREN_FKC")
    public java.util.Set<org.topcased.gpm.domain.product.Product> getChildren() {
        return this.productList;
    }

    public void setChildren(
            java.util.Set<org.topcased.gpm.domain.product.Product> pChildren) {
        this.productList = pChildren;
    }

    /**
     * Add a org.topcased.gpm.domain.product.Product.
     * 
     * @param pChildren
     *            the entity to add.
     */
    public void addToProductList(
            org.topcased.gpm.domain.product.Product pChildren) {
        if (this.productList == null) {
            this.productList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.product.Product>();
        }
        this.productList.add(pChildren);
    }

    /**
     * Remove a org.topcased.gpm.domain.product.Product.
     * 
     * @param pChildren
     *            the entity to remove.
     */
    public void removeFromProductList(
            org.topcased.gpm.domain.product.Product pChildren) {
        if (this.productList != null) {
            this.productList.remove(pChildren);
        }
    }

    protected org.topcased.gpm.domain.businessProcess.BusinessProcess businessProcess;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.businessProcess.BusinessProcess.class)
    @javax.persistence.JoinColumn(nullable = false, name = "BUSINESS_PROCESS_FK")
    @org.hibernate.annotations.ForeignKey(name = "PRODUCT_BUSINESS_PROCESS_FKC")
    @org.hibernate.annotations.Index(name = "product_bp_fk_idx")
    public org.topcased.gpm.domain.businessProcess.BusinessProcess getBusinessProcess() {
        return this.businessProcess;

    }

    public void setBusinessProcess(
            org.topcased.gpm.domain.businessProcess.BusinessProcess pBusinessProcess) {
        this.businessProcess = pBusinessProcess;
    }

    protected java.util.Set<org.topcased.gpm.domain.search.FilterComponent> filterComponentList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.search.FilterComponent>();

    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, mappedBy = "product", fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.search.FilterComponent.class)
    @org.hibernate.annotations.OrderBy(clause = "PRODUCT_FK")
    @org.hibernate.annotations.ForeignKey(name = "PRODUCT_FILTER_COMPONENTS_FKC")
    public java.util.Set<org.topcased.gpm.domain.search.FilterComponent> getFilterComponents() {
        return this.filterComponentList;
    }

    public void setFilterComponents(
            java.util.Set<org.topcased.gpm.domain.search.FilterComponent> pFilterComponents) {
        this.filterComponentList = pFilterComponents;
    }

    /**
     * Add a org.topcased.gpm.domain.search.FilterComponent.
     * 
     * @param pFilterComponents
     *            the entity to add.
     */
    public void addToFilterComponentList(
            org.topcased.gpm.domain.search.FilterComponent pFilterComponents) {
        if (this.filterComponentList == null) {
            this.filterComponentList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.search.FilterComponent>();
        }
        this.filterComponentList.add(pFilterComponents);
    }

    /**
     * Remove a org.topcased.gpm.domain.search.FilterComponent.
     * 
     * @param pFilterComponents
     *            the entity to remove.
     */
    public void removeFromFilterComponentList(
            org.topcased.gpm.domain.search.FilterComponent pFilterComponents) {
        if (this.filterComponentList != null) {
            this.filterComponentList.remove(pFilterComponents);
        }
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public org.topcased.gpm.domain.product.ProductType getProductType() {
        return (ProductType) IdentityFieldsContainerVisitor.getIdentity(super.getDefinition());
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.ValuesContainerImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.fields.ValuesContainerImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.fields.ValuesContainer#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
      * 
      */
    @Override
    @Transient
    public String getFunctionalReference() {
        return getName();
    }
    // HibernateEntity.vsl merge-point
}
