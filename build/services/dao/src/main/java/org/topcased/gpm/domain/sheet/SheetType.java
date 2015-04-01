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
 * <p>
 * This class defines the content of a specific sheet type, especially its
 * fields, field groups and available status.
 * </p>
 * 
 * @author Atos
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "SHEET_TYPE")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class SheetType extends org.topcased.gpm.domain.fields.FieldsContainer {
    private static final long serialVersionUID = 6552045945512137856L;

    protected boolean selectable;

    /**
     * 
     */
    @javax.persistence.Column(name = "SELECTABLE", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "boolean")
    public boolean isSelectable() {
        return this.selectable;
    }

    public void setSelectable(boolean pSelectable) {
        this.selectable = pSelectable;
    }

    protected org.topcased.gpm.domain.process.ProcessDefinition processDefinition;

    /**
     * 
     */
    @javax.persistence.ManyToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.process.ProcessDefinition.class)
    @javax.persistence.JoinColumn(nullable = true, name = "PROCESS_DEFINITION_FK")
    @org.hibernate.annotations.ForeignKey(name = "SHEET_TYPE_PROCESS_DEFINITION_")
    @org.hibernate.annotations.Index(name = "definition_fk_idx")
    public org.topcased.gpm.domain.process.ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    public void setProcessDefinition(
            org.topcased.gpm.domain.process.ProcessDefinition pProcessDefinition) {
        this.processDefinition = pProcessDefinition;
    }

    protected org.topcased.gpm.domain.fields.MultipleField referenceField;

    /**
     * 
     */
    @javax.persistence.OneToOne(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.fields.MultipleField.class)
    @javax.persistence.JoinColumn(name = "REFERENCE_FIELD_FK")
    @org.hibernate.annotations.ForeignKey(name = "SHEET_TYPE_REFERENCE_FIELD_FKC")
    @org.hibernate.annotations.Index(name = "ref_field_idx")
    public org.topcased.gpm.domain.fields.MultipleField getReferenceField() {
        return this.referenceField;
    }

    public void setReferenceField(
            org.topcased.gpm.domain.fields.MultipleField pReferenceField) {
        this.referenceField = pReferenceField;
    }

    /**
     * 
     */
    @javax.persistence.Transient
    public void dummy() {
        // Dummy method. No actual implementation.
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
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.sheet.SheetType}.
     * 
     * @return a new instance of {@link org.topcased.gpm.domain.sheet.SheetType}
     */
    public static org.topcased.gpm.domain.sheet.SheetType newInstance() {
        return new org.topcased.gpm.domain.sheet.SheetType();
    }
}
