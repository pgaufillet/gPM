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
@javax.persistence.Table(name = "LINK_SHEET_SUMMARY_GROUP")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class LinkSheetSummaryGroup extends
        org.topcased.gpm.domain.facilities.FieldGroup {
    private static final long serialVersionUID = 2395023373966695384L;

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
     * {@link org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup}
     */
    public static org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup newInstance() {
        return new org.topcased.gpm.domain.facilities.LinkSheetSummaryGroup();
    }

    // HibernateEntity.vsl merge-point
}