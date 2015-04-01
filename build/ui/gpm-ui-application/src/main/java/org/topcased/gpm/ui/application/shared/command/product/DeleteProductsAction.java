/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.product;

import java.util.List;

import net.customware.gwt.dispatch.shared.Action;

import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;

/**
 * DeleteProductsAction
 * 
 * @author nveillet
 */
public class DeleteProductsAction implements
        Action<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = 2589187140214575686L;

    private String filterId;

    private List<String> productIds;

    /**
     * Create action
     */
    public DeleteProductsAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductIds
     *            the products identifiers
     * @param pFilterId
     *            the filter identifier
     */
    public DeleteProductsAction(List<String> pProductIds, String pFilterId) {
        productIds = pProductIds;
        filterId = pFilterId;
    }

    /**
     * get filter identifier
     * 
     * @return the filter identifier
     */
    public String getFilterId() {
        return filterId;
    }

    /**
     * get products identifiers
     * 
     * @return the products identifiers
     */
    public List<String> getProductIds() {
        return productIds;
    }

    /**
     * set filter identifier
     * 
     * @param pFilterId
     *            the filter identifier to set
     */
    public void setFilterId(String pFilterId) {
        filterId = pFilterId;
    }

    /**
     * set products identifiers
     * 
     * @param pProductIds
     *            the products identifiers to set
     */
    public void setProductIds(List<String> pProductIds) {
        productIds = pProductIds;
    }
}
