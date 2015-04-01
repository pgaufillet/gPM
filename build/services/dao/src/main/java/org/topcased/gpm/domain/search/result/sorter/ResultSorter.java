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
package org.topcased.gpm.domain.search.result.sorter;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "RESULT_SORTER")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ResultSorter extends
        org.topcased.gpm.domain.search.FilterComponent {
    private static final long serialVersionUID = 1363968180789728374L;

    protected java.util.Set<org.topcased.gpm.domain.fields.FieldsContainer> fieldsContainerList =
            new java.util.LinkedHashSet<org.topcased.gpm.domain.fields.FieldsContainer>();

    /**
     * 
     */
    @javax.persistence.ManyToMany(fetch = javax.persistence.FetchType.LAZY, targetEntity = org.topcased.gpm.domain.fields.FieldsContainer.class)
    @javax.persistence.JoinTable(name = "FIELDS_CONTAINERS2RESULT_SORTE", joinColumns = @javax.persistence.JoinColumn(name = "RESULT_SORTERS_FK"), inverseJoinColumns = @javax.persistence.JoinColumn(name = "FIELDS_CONTAINERS_FK"))
    @org.hibernate.annotations.OrderBy(clause = "RESULT_SORTERS_FK")
    @org.hibernate.annotations.ForeignKey(inverseName = "FIELDS_CONTAINER_RESULT_SORTER", name = "RESULT_SORTER_FIELDS_CONTAINER")
    @org.hibernate.annotations.IndexColumn(name = "result_sorter_field_container_idx")
    public java.util.Set<org.topcased.gpm.domain.fields.FieldsContainer> getFieldsContainers() {
        return this.fieldsContainerList;
    }

    public void setFieldsContainers(
            java.util.Set<org.topcased.gpm.domain.fields.FieldsContainer> pFieldsContainers) {
        this.fieldsContainerList = pFieldsContainers;
    }

    /**
     * Add a org.topcased.gpm.domain.fields.FieldsContainer.
     * 
     * @param pFieldsContainers
     *            the entity to add.
     */
    public void addToFieldsContainerList(
            org.topcased.gpm.domain.fields.FieldsContainer pFieldsContainers) {
        if (this.fieldsContainerList == null) {
            this.fieldsContainerList =
                    new java.util.LinkedHashSet<org.topcased.gpm.domain.fields.FieldsContainer>();
        }
        this.fieldsContainerList.add(pFieldsContainers);
    }

    /**
     * Remove a org.topcased.gpm.domain.fields.FieldsContainer.
     * 
     * @param pFieldsContainers
     *            the entity to remove.
     */
    public void removeFromFieldsContainerList(
            org.topcased.gpm.domain.fields.FieldsContainer pFieldsContainers) {
        if (this.fieldsContainerList != null) {
            this.fieldsContainerList.remove(pFieldsContainers);
        }
    }

    protected java.util.List<org.topcased.gpm.domain.search.result.sorter.ResultField> resultFieldList =
            new java.util.LinkedList<org.topcased.gpm.domain.search.result.sorter.ResultField>();

    /**
     * 
     */
    @javax.persistence.OneToMany(cascade = { javax.persistence.CascadeType.ALL }, targetEntity = org.topcased.gpm.domain.search.result.sorter.ResultField.class)
    @javax.persistence.JoinColumn(name = "RESULT_SORTER_FK")
    @org.hibernate.annotations.IndexColumn(name = "RESULT_SORTER_FIELD_IDX")
    @org.hibernate.annotations.OrderBy(clause = "RESULT_SORTER_FK")
    @org.hibernate.annotations.ForeignKey(name = "RESULT_SORTER_RESULT_FIELDS_FK")
    public java.util.List<org.topcased.gpm.domain.search.result.sorter.ResultField> getResultFields() {
        return this.resultFieldList;
    }

    public void setResultFields(
            java.util.List<org.topcased.gpm.domain.search.result.sorter.ResultField> pResultFields) {
        this.resultFieldList = pResultFields;
    }

    /**
     * Add a org.topcased.gpm.domain.search.result.sorter.ResultField.
     * 
     * @param pResultFields
     *            the entity to add.
     */
    public void addToResultFieldList(
            org.topcased.gpm.domain.search.result.sorter.ResultField pResultFields) {
        if (this.resultFieldList == null) {
            this.resultFieldList =
                    new java.util.LinkedList<org.topcased.gpm.domain.search.result.sorter.ResultField>();
        }
        this.resultFieldList.add(pResultFields);
    }

    /**
     * Remove a org.topcased.gpm.domain.search.result.sorter.ResultField.
     * 
     * @param pResultFields
     *            the entity to remove.
     */
    public void removeFromResultFieldList(
            org.topcased.gpm.domain.search.result.sorter.ResultField pResultFields) {
        if (this.resultFieldList != null) {
            this.resultFieldList.remove(pResultFields);
        }
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.search.FilterComponentImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.search.FilterComponent#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.search.FilterComponentImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.search.FilterComponent#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.search.result.sorter.ResultSorter}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.search.result.sorter.ResultSorter}
     */
    public static org.topcased.gpm.domain.search.result.sorter.ResultSorter newInstance() {
        return new org.topcased.gpm.domain.search.result.sorter.ResultSorter();
    }

}