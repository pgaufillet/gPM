/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class NamedElement.
 * 
 * @author llatil
 */
@XStreamAlias("namedElement")
public class NamedElement implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 356403242439859901L;

    /** The name. */
    @XStreamAsAttribute
    private String name;

    /**
     * Constructs a new NamedElement (default Constructor).
     */
    public NamedElement() {
    }

    /**
     * Constructs a new NamedElement.
     * 
     * @param pName
     *            Name of the element
     */
    public NamedElement(String pName) {
        setName(pName);
    }

    /**
     * Get the name of the element.
     * 
     * @return Name of element
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the element.
     * 
     * @param pName
     *            Name of this element
     */
    public void setName(String pName) {
        this.name = pName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {
        if (pOther instanceof NamedElement) {
            NamedElement lOther = (NamedElement) pOther;
            return StringUtils.equals(getName(), lOther.getName());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).toHashCode();
    }
}
