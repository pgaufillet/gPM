/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
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
 * Type Mapping Tag.
 * 
 * @author tpanuel
 */

@XStreamAlias("typeMapping")
public class TypeMapping implements Serializable {
    private static final long serialVersionUID = 6019669987218186668L;

    @XStreamAsAttribute
    private String originTypeName;

    @XStreamAsAttribute
    private String destinationTypeName;

    @XStreamAsAttribute
    private String originProcessName;

    @XStreamAsAttribute
    private String destinationProcessName;

    @XStreamImplicit(itemFieldName = "fieldMapping")
    private List<FieldMapping> fieldMapping;

    /**
     * Get the origin type name.
     * 
     * @return The origin type name.
     */
    public String getOriginTypeName() {
        return originTypeName;
    }

    /**
     * Set the origin type Name.
     * 
     * @param pOriginTypeName
     *            The new origin type name.
     */
    public void setOriginTypeName(final String pOriginTypeName) {
        originTypeName = pOriginTypeName;
    }

    /**
     * Get the destination type name.
     * 
     * @return The destination type name.
     */
    public String getDestinationTypeName() {
        return destinationTypeName;
    }

    /**
     * Set the destination type name.
     * 
     * @param pDestinationTypeName
     *            The new destination type name.
     */
    public void setDestinationTypeName(final String pDestinationTypeName) {
        destinationTypeName = pDestinationTypeName;
    }

    /**
     * Get the origin process name.
     * 
     * @return The origin process name.
     */
    public String getOriginProcessName() {
        return originProcessName;
    }

    /**
     * Set the origin process name.
     * 
     * @param pOriginProcessName
     *            The new origin process name.
     */
    public void setOriginProcessName(final String pOriginProcessName) {
        originProcessName = pOriginProcessName;
    }

    /**
     * Get the destination process name.
     * 
     * @return The destination process name.
     */
    public String getDestinationProcessName() {
        return destinationProcessName;
    }

    /**
     * Set the destination process name.
     * 
     * @param pDestinationProcessName
     *            The new destination process name.
     */
    public void setDestinationProcessName(final String pDestinationProcessName) {
        destinationProcessName = pDestinationProcessName;
    }

    /**
     * Get the field mapping.
     * 
     * @return The field mapping.
     */
    public List<FieldMapping> getFieldMapping() {
        return fieldMapping;
    }

    /**
     * Set the field mapping.
     * 
     * @param pFieldMapping
     *            The new field mapping.
     */
    public void setFieldMapping(final List<FieldMapping> pFieldMapping) {
        fieldMapping = pFieldMapping;
    }
}