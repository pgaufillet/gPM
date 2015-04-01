/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.values.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Key used to access on the cache of the TypeMappingManager.
 * 
 * @author tpanuel
 */
public class TypeMappingDefinition implements Serializable {
    private static final long serialVersionUID = 5923338420893751002L;

    private Map<String, String> fieldMapping;

    private Map<String, String> extensionPoints;

    private Map<String, Map<String, String>> valueMapping;

    /**
     * Constructor
     */
    public TypeMappingDefinition() {
        fieldMapping = new HashMap<String, String>();
        extensionPoints = new HashMap<String, String>();
        valueMapping = new HashMap<String, Map<String, String>>();
    }

    /**
     * Get the fieldMapping
     * 
     * @return the fieldMapping
     */
    public Map<String, String> getFieldMapping() {
        return fieldMapping;
    }

    /**
     * Set the fieldMapping
     * 
     * @param pFieldMapping
     *            the new fieldMapping
     */
    public void setFieldMapping(Map<String, String> pFieldMapping) {
        this.fieldMapping = pFieldMapping;
    }

    /**
     * Get the extensionPoints
     * 
     * @return the extensionPoints
     */
    public Map<String, String> getExtensionPoints() {
        return extensionPoints;
    }

    /**
     * Set the extensionPoints
     * 
     * @param pExtensionPoints
     *            the new extensionPoints
     */
    public void setExtensionPoints(Map<String, String> pExtensionPoints) {
        this.extensionPoints = pExtensionPoints;
    }

    /**
     * Get the valueMapping
     * 
     * @return the valueMapping
     */
    public Map<String, Map<String, String>> getValueMapping() {
        return valueMapping;
    }

    /**
     * Set the valueMapping
     * 
     * @param pValueMapping
     *            the new valueMapping
     */
    public void setValueMapping(Map<String, Map<String, String>> pValueMapping) {
        this.valueMapping = pValueMapping;
    }
}