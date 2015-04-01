/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.link;

import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * ExecuteLinkCreationFilterAction
 * 
 * @author nveillet
 */
public class ExecuteLinkCreationFilterAction extends
        AbstractCommandExecuteLinkFilterAction {

    /** serialVersionUID */
    private static final long serialVersionUID = -3108260287887364337L;

    /**
     * create action
     */
    public ExecuteLinkCreationFilterAction() {
    }

    /**
     * create action with link type name (for existing filter)
     * 
     * @param pProductName
     *            The product name
     * @param pFieldsContainerType
     *            The fields container container
     * @param pValuesContainerId
     *            The values container identifier
     * @param pLinkTypeName
     *            The link type name
     */
    public ExecuteLinkCreationFilterAction(String pProductName,
            FieldsContainerType pFieldsContainerType,
            String pValuesContainerId, String pLinkTypeName) {
        super(pProductName, pFieldsContainerType, pValuesContainerId,
                pLinkTypeName);
    }

    /**
     * create action with filter
     * 
     * @param pProductName
     *            The product name
     * @param pFieldsContainerType
     *            The fields container container
     * @param pValuesContainerId
     *            The values container identifier
     * @param pLinkTypeName
     *            The link type name
     * @param pFilter
     *            The filter
     */
    public ExecuteLinkCreationFilterAction(String pProductName,
            FieldsContainerType pFieldsContainerType,
            String pValuesContainerId, String pLinkTypeName, UiFilter pFilter) {
        super(pProductName, pFieldsContainerType, pValuesContainerId,
                pLinkTypeName, pFilter);
    }
}
