/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Field Mapping Tag.
 * 
 * @author tpanuel
 */

@XStreamAlias("valueMapping")
public class ValueMapping implements Serializable {
    private static final long serialVersionUID = 2846891086524782605L;

    @XStreamAsAttribute
    private String originValue;

    @XStreamAsAttribute
    private String destinationValue;

    /**
     * Get the origin value
     * 
     * @return The origin value.
     */
    public String getOriginValue() {
        return originValue;
    }

    /**
     * Set the origin value.
     * 
     * @param pOriginValue
     *            The new origin value.
     */
    public void setOriginValue(final String pOriginValue) {
        originValue = pOriginValue;
    }

    /**
     * Get the destination value.
     * 
     * @return The destination value.
     */
    public String getDestinationValue() {
        return destinationValue;
    }

    /**
     * Set the destination value.
     * 
     * @param pDestinationValue
     *            The new destination value.
     */
    public void setDestinationValue(final String pDestinationValue) {
        destinationValue = pDestinationValue;
    }
}