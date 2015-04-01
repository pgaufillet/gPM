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

import javax.persistence.Transient;

/**
 * <p>
 * This class instantiates all the change control sheets.
 * </p>
 * <p>
 * It is possible to regroup sheets by creating a parent/children link between
 * them.
 * </p>
 * 
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "SHEET")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Proxy(lazy = false)
public class Sheet extends org.topcased.gpm.domain.fields.ValuesContainer {
    private static final long serialVersionUID = 7120448623247362530L;

    protected org.topcased.gpm.domain.process.Node currentNode;

    /**
     *
     *
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.Node.class)
    @javax.persistence.JoinColumn(nullable = false, name = "CURRENT_NODE_FK")
    @org.hibernate.annotations.ForeignKey(name = "CURRENT_NODE_FKC")
    @org.hibernate.annotations.Index(name = "node_fk_idx")
    public org.topcased.gpm.domain.process.Node getCurrentNode() {
        return this.currentNode;
    }

    public void setCurrentNode(org.topcased.gpm.domain.process.Node pCurrentNode) {
        this.currentNode = pCurrentNode;
    }

    protected java.util.List<org.topcased.gpm.domain.sheet.SheetHistory> sheetHistoryList =
            new java.util.LinkedList<org.topcased.gpm.domain.sheet.SheetHistory>();

    /**
     * Sheet Creation Date. Add to avoid lost of the sheet creation Date
     * information. This parameter was managed in JBPM PROCESSINSTANCE table,
     * since JBPM is not longer used, the information will be stored in the
     * SHEET table
     */
    protected java.sql.Timestamp creationDate;

    /**
     * <p>
     * Sheet creation date
     * 
     * @return a Timestamp
     * </p>
     */
    @javax.persistence.Column(name = "CREATION_DATE", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.sql.Timestamp")
    public java.sql.Timestamp getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(java.sql.Timestamp pCreationDate) {
        this.creationDate = pCreationDate;
    }  
    
    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, targetEntity = org.topcased.gpm.domain.sheet.SheetHistory.class)
    @javax.persistence.JoinColumn(name = "SHEET_FK")
    @org.hibernate.annotations.IndexColumn(name = "SHEET_SHEET_HISTORIES_IDX")
    @org.hibernate.annotations.OrderBy(clause = "SHEET_FK")
    @org.hibernate.annotations.ForeignKey(name = "SHEET_SHEET_HISTORIES_FKC")
    public java.util.List<org.topcased.gpm.domain.sheet.SheetHistory> getSheetHistories() {
        return this.sheetHistoryList;
    }

    public void setSheetHistories(
            java.util.List<org.topcased.gpm.domain.sheet.SheetHistory> pSheetHistories) {
        this.sheetHistoryList = pSheetHistories;
    }

    /**
     * Add a org.topcased.gpm.domain.sheet.SheetHistory.
     * 
     * @param pSheetHistories
     *            the entity to add.
     */
    public void addToSheetHistoryList(
            org.topcased.gpm.domain.sheet.SheetHistory pSheetHistories) {
        if (this.sheetHistoryList == null) {
            this.sheetHistoryList =
                    new java.util.LinkedList<org.topcased.gpm.domain.sheet.SheetHistory>();
        }
        this.sheetHistoryList.add(pSheetHistories);
    }

    /**
     * Remove a org.topcased.gpm.domain.sheet.SheetHistory.
     * 
     * @param pSheetHistories
     *            the entity to remove.
     */
    public void removeFromSheetHistoryList(
            org.topcased.gpm.domain.sheet.SheetHistory pSheetHistories) {
        if (this.sheetHistoryList != null) {
            this.sheetHistoryList.remove(pSheetHistories);
        }
    }

    protected org.topcased.gpm.domain.product.Product product;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.product.Product.class)
    @javax.persistence.JoinColumn(nullable = false, name = "PRODUCT_FK")
    @org.hibernate.annotations.ForeignKey(name = "SHEET_PRODUCT_FKC")
    @org.hibernate.annotations.Index(name = "product_fkc_idx")
    public org.topcased.gpm.domain.product.Product getProduct() {
        return this.product;
    }

    public void setProduct(org.topcased.gpm.domain.product.Product pProduct) {
        this.product = pProduct;
    }

    /**
     * <p>
     * Get the type id of this sheet.
     * </p>
     */
    @javax.persistence.Transient
    public java.lang.String getSheetTypeId() {
        return getDefinition().getId();
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public org.topcased.gpm.domain.sheet.SheetType getSheetType() {
        return (SheetType) super.getDefinition();
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
     * Get the functional reference of the sheet.
     * 
     * @return Functional reference
     */
    @Transient
    public String getFunctionalReference() {
        return getReference();
    }
}