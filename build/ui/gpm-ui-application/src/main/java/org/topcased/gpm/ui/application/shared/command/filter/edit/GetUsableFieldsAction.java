/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.filter.edit;

import java.util.List;

import org.topcased.gpm.business.util.FilterType;

/**
 * PreSaveFilterAction
 * 
 * @author nveillet
 */
public class GetUsableFieldsAction extends
        AbstractCommandEditFilterAction<GetUsableFieldsResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -7117117820554967195L;

    private List<String> selectedContainerIds;

    /**
     * create action
     */
    public GetUsableFieldsAction() {
    }

    /**
     * create action with product name
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pSelectedContainerIds
     *            the container Id.
     */
    public GetUsableFieldsAction(String pProductName, FilterType pFilterType,
            List<String> pSelectedContainerIds) {
        super(pProductName, pFilterType);
        selectedContainerIds = pSelectedContainerIds;
    }

    /**
     * create action with product and filter identifier
     * 
     * @param pProductName
     *            the product name
     * @param pFilterType
     *            the filter type
     * @param pFilterId
     *            the filter identifier
     * @param pSelectedContainerIds
     *            the container Id.
     */
    public GetUsableFieldsAction(String pProductName, FilterType pFilterType,
            String pFilterId, List<String> pSelectedContainerIds) {
        super(pProductName, pFilterType, pFilterId);
        selectedContainerIds = pSelectedContainerIds;
    }

    /**
     * Get container Ids.
     * 
     * @return the container Ids
     */
    public List<String> getContainerIds() {
        return selectedContainerIds;
    }

    /**
     * Set container Ids.
     * 
     * @param pSelectedContainerIds
     *            the container Ids
     */
    public void setContainerIds(List<String> pSelectedContainerIds) {
        selectedContainerIds = pSelectedContainerIds;
    }

}
