/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter.result.tree;

import java.util.List;

/**
 * UiFilterTreeResultNode
 * 
 * @author nveillet
 */
public class UiFilterTreeResultNode extends AbstractUiFilterTreeResultNode {

    /** serialVersionUID */
    private static final long serialVersionUID = 5675707033233537886L;

    private String description;

    private String value;

    /**
     * Create UiFilterTreeResultNode
     */
    public UiFilterTreeResultNode() {
        super();
    }

    /**
     * Create UiFilterTreeResultNode with values
     * 
     * @param pValue
     *            the value
     * @param pDescription
     *            the description
     * @param pResultNodes
     *            the result nodes
     */
    public UiFilterTreeResultNode(String pValue, String pDescription,
            List<UiFilterTreeResultNode> pResultNodes) {
        super(pResultNodes);
        value = pValue;
        description = pDescription;
    }

    /**
     * get description
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * get value
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * set description
     * 
     * @param pDescription
     *            the description to set
     */
    public void setDescription(String pDescription) {
        description = pDescription;
    }

    /**
     * set value
     * 
     * @param pValue
     *            the value to set
     */
    public void setValue(String pValue) {
        value = pValue;
    }
}
