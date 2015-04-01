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
@javax.persistence.Table(name = "SCRIPT")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.JOINED)
@org.hibernate.annotations.Entity(dynamicInsert = false, dynamicUpdate = false)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Script extends org.topcased.gpm.domain.extensions.Command {
    private static final long serialVersionUID = -2783166190420961957L;

    protected java.lang.String language;

    /**
     * <p>
     * Language name of the script. Used to select the interpreter to execute
     * it.
     * </p>
     */
    @javax.persistence.Column(name = "LANGUAGE", length = 50, nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "java.lang.String")
    public java.lang.String getLanguage() {
        return this.language;
    }

    public void setLanguage(java.lang.String pLanguage) {
        this.language = pLanguage;
    }

    protected java.lang.String script;

    /**
     * <p>
     * Source code of the script.
     * </p>
     */
    @javax.persistence.Column(name = "SCRIPT", nullable = false, unique = false)
    @org.hibernate.annotations.Type(type = "org.andromda.persistence.hibernate.usertypes.HibernateStringClobType")
    public java.lang.String getScript() {
        return this.script;
    }

    public void setScript(java.lang.String pScript) {
        this.script = pScript;
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
     * {@link org.topcased.gpm.domain.extensions.Script}.
     * 
     * @return a new instance of
     *         {@link org.topcased.gpm.domain.extensions.Script}
     */
    public static org.topcased.gpm.domain.extensions.Script newInstance() {
        return new org.topcased.gpm.domain.extensions.Script();
    }

    // HibernateEntity.vsl merge-point
}