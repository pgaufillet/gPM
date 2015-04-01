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
@javax.persistence.Table(name = "GPM")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Gpm extends org.topcased.gpm.domain.attributes.AttributesContainer {
    private static final long serialVersionUID = 1711360189887400406L;

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.attributes.AttributesContainerImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainer#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.attributes.AttributesContainerImpl</code>
     * class it will simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.attributes.AttributesContainer#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.attributes.Gpm}.
     * 
     * @return a new instance of {@link org.topcased.gpm.domain.attributes.Gpm}
     */
    public static org.topcased.gpm.domain.attributes.Gpm newInstance() {
        return new org.topcased.gpm.domain.attributes.Gpm();
    }

    // HibernateEntity.vsl merge-point
}