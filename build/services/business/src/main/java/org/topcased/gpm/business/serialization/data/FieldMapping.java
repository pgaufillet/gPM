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
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Field Mapping Tag.
 * 
 * @author tpanuel
 */

@XStreamAlias("fieldMapping")
public class FieldMapping implements Serializable {
    private static final long serialVersionUID = -2903990583496038116L;

    @XStreamAsAttribute
    private String originLabelKey;

    @XStreamAsAttribute
    private String destinationLabelKey;

    @XStreamAsAttribute
    private String extensionPointName;

    @XStreamImplicit(itemFieldName = "valueMapping")
    private List<ValueMapping> valueMapping;

    /**
     * Get the origin label key.
     * 
     * @return The origin label key.
     */
    public String getOriginLabelKey() {
        return originLabelKey;
    }

    /**
     * Set the origin label key.
     * 
     * @param pOriginLabelKey
     *            The new origin label key.
     */
    public void setOriginLabelKey(final String pOriginLabelKey) {
        originLabelKey = pOriginLabelKey;
    }

    /**
     * Get the destination label key.
     * 
     * @return The destination label key.
     */
    public String getDestinationLabelKey() {
        return destinationLabelKey;
    }

    /**
     * Set the destinationLabelKey
     * 
     * @param pDestinationLabelKey
     *            The new destination label key.
     */
    public void setDestinationLabelKey(final String pDestinationLabelKey) {
        destinationLabelKey = pDestinationLabelKey;
    }

    /**
     * Get the extension point name.
     * 
     * @return The extension point name.
     */
    public String getExtensionPointName() {
        return extensionPointName;
    }

    /**
     * Set the extension point name.
     * 
     * @param pExtensionPointName
     *            The new extension point name.
     */
    public void setExtensionPointName(final String pExtensionPointName) {
        extensionPointName = pExtensionPointName;
    }

    /**
     * Get the value mapping.
     * 
     * @return The value mapping.
     */
    public List<ValueMapping> getValueMapping() {
        return valueMapping;
    }

    /**
     * Set the value mapping.
     * 
     * @param pValueMapping
     *            The new value mapping.
     */
    public void setValueMapping(final List<ValueMapping> pValueMapping) {
        valueMapping = pValueMapping;
    }
}