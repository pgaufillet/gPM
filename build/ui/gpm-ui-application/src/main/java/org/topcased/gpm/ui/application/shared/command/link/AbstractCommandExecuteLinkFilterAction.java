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
import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.application.shared.command.filter.AbstractCommandFilterResult;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;

/**
 * AbstractCommandExecuteLinkFilterAction
 * 
 * @author nveillet
 */
public class AbstractCommandExecuteLinkFilterAction extends
        AbstractCommandAction<AbstractCommandFilterResult> {

    /** serialVersionUID */
    private static final long serialVersionUID = -3708394780455763470L;

    private FieldsContainerType fieldsContainerType;

    private UiFilter filter;

    private String linkTypeName;

    private String valuesContainerId;

    /**
     * create action
     */
    protected AbstractCommandExecuteLinkFilterAction() {
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
    protected AbstractCommandExecuteLinkFilterAction(String pProductName,
            FieldsContainerType pFieldsContainerType,
            String pValuesContainerId, String pLinkTypeName) {
        super(pProductName);
        fieldsContainerType = pFieldsContainerType;
        valuesContainerId = pValuesContainerId;
        linkTypeName = pLinkTypeName;
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
    protected AbstractCommandExecuteLinkFilterAction(String pProductName,
            FieldsContainerType pFieldsContainerType,
            String pValuesContainerId, String pLinkTypeName, UiFilter pFilter) {
        super(pProductName);
        fieldsContainerType = pFieldsContainerType;
        valuesContainerId = pValuesContainerId;
        linkTypeName = pLinkTypeName;
        filter = pFilter;
    }

    /**
     * get fields container type
     * 
     * @return the fields container type
     */
    public FieldsContainerType getFieldsContainerType() {
        return fieldsContainerType;
    }

    /**
     * get filter
     * 
     * @return the filter
     */
    public UiFilter getFilter() {
        return filter;
    }

    /**
     * get link type name
     * 
     * @return the link type name
     */
    public String getLinkTypeName() {
        return linkTypeName;
    }

    /**
     * get values container identifier
     * 
     * @return the values container identifier
     */
    public String getValuesContainerId() {
        return valuesContainerId;
    }

    /**
     * set fields container type
     * 
     * @param pFieldsContainerType
     *            the fields container type to set
     */
    public void setFieldsContainerType(FieldsContainerType pFieldsContainerType) {
        fieldsContainerType = pFieldsContainerType;
    }

    /**
     * set filter
     * 
     * @param pFilter
     *            the filter to set
     */
    public void setFilter(UiFilter pFilter) {
        filter = pFilter;
    }

    /**
     * set link type name
     * 
     * @param pLinkTypeName
     *            the link type name to set
     */
    public void setLinkTypeName(String pLinkTypeName) {
        linkTypeName = pLinkTypeName;
    }

    /**
     * set values container identifier
     * 
     * @param pValuesContainerId
     *            the values container identifier to set
     */
    public void setValuesContainerId(String pValuesContainerId) {
        valuesContainerId = pValuesContainerId;
    }

}
