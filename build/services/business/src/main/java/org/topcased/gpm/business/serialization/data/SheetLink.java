/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * SheetLink.
 * <p>
 * A sheet's link can be defined in two ways:
 * <ol>
 * <li>With linked sheets identifier: using Link object attribute 'originId' and
 * 'destinationId'.</li>
 * <li>Without the identifier but with the linked sheets reference using
 * attributes 'originReference' and 'destinationReference'.<br />
 * Using this way, the attributes 'originProductName' and
 * 'destinationProductName' must be defined with the linked sheets product's
 * name.</li>
 * </ol>
 * </p>
 * 
 * @author mkargbo
 */
@XStreamAlias("sheetLink")
public class SheetLink extends Link {
    private static final long serialVersionUID = 4140370930490786255L;

    /** The origin sheet reference. */
    @XStreamAsAttribute
    private String originReference;

    /** The destination sheet reference. */
    @XStreamAsAttribute
    private String destinationReference;

    /**
     * Get the origin sheet reference.
     * 
     * @return The origin sheet reference.
     */
    public String getOriginReference() {
        return originReference;
    }

    /**
     * Set the origin sheet reference.
     * 
     * @param pOriginReference
     *            The origin sheet reference.
     */
    public void setOriginReference(final String pOriginReference) {
        originReference = pOriginReference;
    }

    /**
     * Get the destination sheet reference.
     * 
     * @return The destination sheet reference.
     */
    public String getDestinationReference() {
        return destinationReference;
    }

    /**
     * Set the destination sheet reference.
     * 
     * @param pDestinationReference
     *            The destination sheet reference.
     */
    public void setDestinationReference(final String pDestinationReference) {
        destinationReference = pDestinationReference;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.serialization.data.Link#getTagName()
     */
    @Override
    public String getTagName() {
        return "sheetLink";
    }
}