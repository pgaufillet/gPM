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

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * The Class DescribedElement.
 * 
 * @author llatil
 */
public abstract class DescribedElement extends NamedElement {

    private static final long serialVersionUID = 523956460803830047L;

    /** The description. */
    @XStreamAsAttribute
    private String description;

    /**
     * Get the description of the element.
     * 
     * @return Description
     */
    public String getDescription() {
        return org.topcased.gpm.util.lang.StringUtils.normalizeString(description);
    }

    /**
     * Set the description of the element.
     * 
     * @param pDescription
     *            Description text to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof DescribedElement) {
            DescribedElement lOther = (DescribedElement) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!StringUtils.equals(description, lOther.description)) {
                return false;
            }

            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // NamedElement hashcode is sufficient
        return super.hashCode();
    }
}
