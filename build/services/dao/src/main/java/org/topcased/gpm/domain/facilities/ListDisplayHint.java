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
package org.topcased.gpm.domain.facilities;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "LIST_DISPLAY_HINT")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ListDisplayHint extends
        org.topcased.gpm.domain.facilities.FieldDisplayHint {
    private static final long serialVersionUID = -3552937173175722268L;

    protected org.topcased.gpm.domain.facilities.ListDisplayType displayType;

    /**
     * 
     */
    @javax.persistence.Column(name = "DISPLAY_TYPE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "org.topcased.gpm.domain.facilities.ListDisplayTypeEnum")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    public org.topcased.gpm.domain.facilities.ListDisplayType getDisplayType() {
        return this.displayType;
    }

    public void setDisplayType(
            org.topcased.gpm.domain.facilities.ListDisplayType pDisplayType) {
        this.displayType = pDisplayType;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.facilities.FieldDisplayHintImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.facilities.FieldDisplayHint#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.facilities.FieldDisplayHintImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.facilities.FieldDisplayHint#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.facilities.ListDisplayHint}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.facilities.ListDisplayHint}
     */
    public static org.topcased.gpm.domain.facilities.ListDisplayHint newInstance() {
        return new org.topcased.gpm.domain.facilities.ListDisplayHint();
    }

    // HibernateEntity.vsl merge-point
}