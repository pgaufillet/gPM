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

import java.io.Serializable;
import java.util.List;

/**
 * AbstractUiFilterTreeResultNode
 * 
 * @author nveillet
 */
public class AbstractUiFilterTreeResultNode implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3195372142348643007L;

    private List<UiFilterTreeResultNode> resultNodes;

    /**
     * Create AbstractUiFilterTreeResultNode
     */
    protected AbstractUiFilterTreeResultNode() {
    }

    /**
     * Create AbstractUiFilterTreeResultNode with result nodes
     * 
     * @param pResultNodes
     *            the result nodes
     */
    protected AbstractUiFilterTreeResultNode(
            List<UiFilterTreeResultNode> pResultNodes) {
        resultNodes = pResultNodes;
    }

    /**
     * get result nodes
     * 
     * @return the result nodes
     */
    public List<UiFilterTreeResultNode> getResultNodes() {
        return resultNodes;
    }

    /**
     * set result nodes
     * 
     * @param pResultNodes
     *            the result nodes to set
     */
    public void setResultNodes(List<UiFilterTreeResultNode> pResultNodes) {
        resultNodes = pResultNodes;
    }
}
