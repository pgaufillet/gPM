/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.util.LinkedList;
import java.util.List;

import org.topcased.gpm.util.lang.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The Class AttributesContainer.
 * 
 * @author llatil
 */
public class AttributesContainer extends DescribedElement {

    /** Generated UID */
    private static final long serialVersionUID = -6845636449525853299L;

    /** The attributes. */
    @XStreamAlias(value = "attributes", impl = LinkedList.class)
    private List<Attribute> attributes;

    /**
     * Gets the attributes.
     * 
     * @return the attributes
     */
    public final List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     * 
     * @param pAttributes
     *            the new attributes
     */
    public void setAttributes(List<Attribute> pAttributes) {
        attributes = pAttributes;
    }

    /**
     * Specify if this container has attributes.
     * 
     * @return True if attributes are present.
     */
    public boolean hasAttributes() {
        return null != attributes && !attributes.isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object pOther) {

        if (pOther instanceof AttributesContainer) {
            AttributesContainer lOther = (AttributesContainer) pOther;

            if (!super.equals(lOther)) {
                return false;
            }
            if (!CollectionUtils.equals(attributes, lOther.attributes)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // DescribedElement hashcode is sufficient
        return super.hashCode();
    }
}
