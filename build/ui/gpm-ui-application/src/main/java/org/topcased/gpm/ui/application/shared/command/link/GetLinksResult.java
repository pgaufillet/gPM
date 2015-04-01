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

import net.customware.gwt.dispatch.shared.Result;

import org.topcased.gpm.ui.facade.shared.container.link.UiLink;

/**
 * GetLinksResult
 * 
 * @author nveillet
 */
public class GetLinksResult implements Result {

    /** serialVersionUID */
    private static final long serialVersionUID = 5929593094399286637L;

    private List<UiLink> links;

    private String linkTypeName;

    private String valuesContainerId;

    /**
     * Empty constructor for serialization.
     */
    public GetLinksResult() {
    }

    /**
     * Constructor
     * 
     * @param pValuesContainerId
     *            the values container identifier
     * @param pLinkTypeName
     *            the link type name
     * @param pLinks
     *            The links
     */
    public GetLinksResult(String pValuesContainerId, String pLinkTypeName,
            List<UiLink> pLinks) {
        super();
        valuesContainerId = pValuesContainerId;
        linkTypeName = pLinkTypeName;
        links = pLinks;
    }

    /**
     * get links
     * 
     * @return the links
     */
    public List<UiLink> getLinks() {
        return links;
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

}
