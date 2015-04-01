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
package org.topcased.gpm.domain.extensions;

/**
 * @author Atos
 */

@javax.persistence.Entity
@javax.persistence.Table(name = "ACTION")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Action extends org.topcased.gpm.domain.extensions.Command {
    private static final long serialVersionUID = 4536248242355343365L;

    protected java.lang.String className;

    /**
     * <p>
     * Name of the java class implementing the action interface.
     * </p>
     */
    @javax.persistence.Column(name = "CLASS_NAME", length = 100, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getClassName() {
        return this.className;
    }

    public void setClassName(java.lang.String pClassName) {
        this.className = pClassName;
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.extensions.CommandImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.extensions.Command#equals(Object)
     */
    @javax.persistence.Transient
    public boolean equals(Object pObject) {
        return super.equals(pObject);
    }

    /**
     * This entity does not have any identifiers but since it extends the
     * <code>org.topcased.gpm.domain.extensions.CommandImpl</code> class it will
     * simply delegate the call up there.
     * 
     * @see org.topcased.gpm.domain.extensions.Command#hashCode()
     */
    @javax.persistence.Transient
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Constructs new instances of
     * {@link org.topcased.gpm.domain.extensions.Action}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.extensions.Action}
     */
    public static org.topcased.gpm.domain.extensions.Action newInstance() {
        return new org.topcased.gpm.domain.extensions.Action();
    }

    // HibernateEntity.vsl merge-point
}