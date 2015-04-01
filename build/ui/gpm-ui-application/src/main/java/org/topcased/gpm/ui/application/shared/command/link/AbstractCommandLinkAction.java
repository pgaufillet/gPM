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

import java.util.List;

import org.topcased.gpm.ui.application.shared.command.AbstractCommandAction;
import org.topcased.gpm.ui.application.shared.command.container.GetContainerResult;

/**
 * AbstractCommandLinkAction
 * 
 * @param <R>
 *            the result
 * @author nveillet
 */
public class AbstractCommandLinkAction<R extends GetContainerResult<?>> extends
        AbstractCommandAction<R> {

    /** serialVersionUID */
    private static final long serialVersionUID = 8269944335211473454L;

    private List<String> destinationIds;

    private String linkTypeName;

    private String originId;

    /**
     * create action
     */
    protected AbstractCommandLinkAction() {
    }

    /**
     * Create action with values
     * 
     * @param pProductName
     *            the product name
     * @param pLinkTypeName
     *            the link type name
     * @param pOriginId
     *            the origin container identifier
     * @param pDestinationIds
     *            the destination container identifiers
     */
    protected AbstractCommandLinkAction(String pProductName,
            String pLinkTypeName, String pOriginId, List<String> pDestinationIds) {
        super(pProductName);
        linkTypeName = pLinkTypeName;
        originId = pOriginId;
        destinationIds = pDestinationIds;
    }

    /**
     * get destination container identifiers
     * 
     * @return the destination container identifiers
     */
    public List<String> getDestinationIds() {
        return destinationIds;
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
     * get origin container identifier
     * 
     * @return the origin container identifier
     */
    public String getOriginId() {
        return originId;
    }

    /**
     * set destination container identifiers
     * 
     * @param pDestinationIds
     *            the destination container identifiers to set
     */
    public void setDestinationId(List<String> pDestinationIds) {
        destinationIds = pDestinationIds;
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
     * set origin container identifier
     * 
     * @param pOriginId
     *            the origin container identifier to set
     */
    public void setOriginId(String pOriginId) {
        originId = pOriginId;
    }
}
