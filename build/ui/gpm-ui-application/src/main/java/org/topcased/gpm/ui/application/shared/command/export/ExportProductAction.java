/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.export;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

/**
 * ExportProductAction
 * 
 * @author nveillet
 */
public class ExportProductAction implements Action<ExportFileResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 2512283447577395071L;

    private List<String> productIds;

    /**
     * Create action
     */
    public ExportProductAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductIds
     *            the product identifiers
     */
    public ExportProductAction(List<String> pProductIds) {
        productIds = pProductIds;
    }

    /**
     * get product identifiers
     * 
     * @return the product identifiers
     */
    public List<String> getProductIds() {
        return productIds;
    }

    /**
     * set product identifiers
     * 
     * @param pProductIds
     *            the product identifiers to set
     */
    public void setProductIds(List<String> pProductIds) {
        productIds = pProductIds;
    }
}
