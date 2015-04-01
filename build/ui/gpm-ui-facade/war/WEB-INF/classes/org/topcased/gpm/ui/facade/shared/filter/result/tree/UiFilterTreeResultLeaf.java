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
 * UiFilterTreeResultLeaf
 * 
 * @author nveillet
 */
public class UiFilterTreeResultLeaf extends UiFilterTreeResultNode {

    /** serialVersionUID */
    private static final long serialVersionUID = 3367199571859387961L;

    private String id;

    /**
     * Create UiFilterTreeResultLeaf
     */
    public UiFilterTreeResultLeaf() {
    }

    /**
     * Create UiFilterTreeResultLeaf with value
     * 
     * @param pId
     *            the container identifier
     * @param pValue
     *            the node value
     * @param pDescription
     *            the description
     */
    public UiFilterTreeResultLeaf(String pId, String pValue, String pDescription) {
        super(pValue, pDescription, null);
        id = pId;
    }

    /**
     * get container identifier
     * 
     * @return the container identifier
     */
    public String getId() {
        return id;
    }

    /**
     * set container identifier
     * 
     * @param pId
     *            the container identifier to set
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.shared.filter.result.tree.UiFilterTreeResultNode#setResultNodes(java.util.List)
     */
    @Override
    public void setResultNodes(List<UiFilterTreeResultNode> pResultNodes) {
        super.setResultNodes(null);
    }
}
