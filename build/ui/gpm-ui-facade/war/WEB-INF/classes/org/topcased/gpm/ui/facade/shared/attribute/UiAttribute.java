/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.attribute;

import java.io.Serializable;
import java.util.List;

/**
 * UiAttribute
 * 
 * @author nveillet
 */
public class UiAttribute implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 2281905021032918742L;

    private String name;

    private List<String> values;

    /**
     * Empty Constructor for serialization
     */
    public UiAttribute() {
    }

    /**
     * Constructor with values
     * 
     * @param pName
     *            the name
     * @param pValues
     *            the values
     */
    public UiAttribute(String pName, List<String> pValues) {
        super();
        name = pName;
        values = pValues;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * get values
     * 
     * @return the values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * set values
     * 
     * @param pValues
     *            the values to set
     */
    public void setValues(List<String> pValues) {
        values = pValues;
    }
}
