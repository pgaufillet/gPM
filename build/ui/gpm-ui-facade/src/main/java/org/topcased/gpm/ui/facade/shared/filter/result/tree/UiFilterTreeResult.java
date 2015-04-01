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
 * UiFilterTreeResult
 * 
 * @author nveillet
 */
public class UiFilterTreeResult extends AbstractUiFilterTreeResultNode {

    /** serialVersionUID */
    private static final long serialVersionUID = -1527081885956751740L;

    private boolean editable;

    /**
     * Create UiFilterTreeResult
     */
    public UiFilterTreeResult() {
        super();
    }

    /**
     * Create UiFilterTreeResult with values
     * 
     * @param pResultNodes
     *            the result nodes
     * @param pEditable
     *            the editable access
     */
    public UiFilterTreeResult(List<UiFilterTreeResultNode> pResultNodes,
            boolean pEditable) {
        super(pResultNodes);
        setEditable(pEditable);
    }

    /**
     * get editable access
     * 
     * @return the editable access
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * set editable access
     * 
     * @param pEditable
     *            the editable access to set
     */
    public void setEditable(boolean pEditable) {
        editable = pEditable;
    }
}
