/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.field.result;

import java.io.Serializable;
import java.util.LinkedList;

import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;

/**
 * UiFilterResultField
 * 
 * @author nveillet
 */
public class UiFilterResultField implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -7922837598070716097L;

    private String label;

    private LinkedList<UiFilterFieldName> name;

    /**
     * Constructor
     */
    public UiFilterResultField() {
    }

    /**
     * Constructor with values
     * 
     * @param pLabel
     *            the label
     * @param pName
     *            the name
     */
    public UiFilterResultField(String pLabel,
            LinkedList<UiFilterFieldName> pName) {
        label = pLabel;
        name = pName;
    }

    /**
     * get label
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * get name
     * 
     * @return the name
     */
    public LinkedList<UiFilterFieldName> getName() {
        return name;
    }

    /**
     * set label
     * 
     * @param pLabel
     *            the label to set
     */
    public void setLabel(String pLabel) {
        label = pLabel;
    }

    /**
     * set name
     * 
     * @param pName
     *            the name to set
     */
    public void setName(LinkedList<UiFilterFieldName> pName) {
        name = pName;
    }

}
