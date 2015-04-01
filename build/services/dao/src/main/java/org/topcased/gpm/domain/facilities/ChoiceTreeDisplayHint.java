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
@javax.persistence.Table(name = "CHOICE_TREE_DISPLAY_HINT")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ChoiceTreeDisplayHint extends
        org.topcased.gpm.domain.facilities.FieldDisplayHint {
    private static final long serialVersionUID = -218442651133105605L;

    protected java.lang.String separator;

    /**
     * 
     */
    @javax.persistence.Column(name = "SEPARATOR", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getSeparator() {
        return this.separator;
    }

    public void setSeparator(java.lang.String pSeparator) {
        this.separator = pSeparator;
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
     * {@link org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint}
     */
    public static org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint newInstance() {
        return new org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint();
    }

    // HibernateEntity.vsl merge-point
}