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
@javax.persistence.Table(name = "DISPLAY_GROUP")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class DisplayGroup extends org.topcased.gpm.domain.facilities.FieldGroup {
    private static final long serialVersionUID = 8262406734681109985L;

    protected int displayOrder;

    /**
     * 
     */
    @javax.persistence.Column(name = "DISPLAY_ORDER", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "int")
    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(int pDisplayOrder) {
        this.displayOrder = pDisplayOrder;
    }

    protected boolean opened;

    /**
     * 
     */
    @javax.persistence.Column(name = "OPENED", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "boolean")
    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean pOpened) {
        this.opened = pOpened;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.facilities.FieldGroupImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.facilities.FieldGroup#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.facilities.FieldGroupImpl</code> class it
     * will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.facilities.FieldGroup#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.facilities.DisplayGroup}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.facilities.DisplayGroup}
     */
    public static org.topcased.gpm.domain.facilities.DisplayGroup newInstance() {
        return new org.topcased.gpm.domain.facilities.DisplayGroup();
    }

    // HibernateEntity.vsl merge-point
}