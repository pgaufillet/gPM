/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.link;

import org.topcased.gpm.business.values.link.BusinessLink;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;

/**
 * UiLink
 * 
 * @author nveillet
 */
public class UiLink extends UiContainer implements BusinessLink {

    /** serialVersionUID */
    private static final long serialVersionUID = -2142635626097799038L;

    private String destinationId;

    private String destinationReference;

    private String originId;

    private String originReference;

    /**
     * Create new UiLink
     */
    public UiLink() {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.link.BusinessLink#getDestinationId()
     */
    @Override
    public String getDestinationId() {
        return destinationId;
    }

    /**
     * get destination container reference
     * 
     * @return the destination container reference
     */
    public String getDestinationReference() {
        return destinationReference;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.values.link.BusinessLink#getOriginId()
     */
    @Override
    public String getOriginId() {
        return originId;
    }

    /**
     * get origin container reference
     * 
     * @return the origin container reference
     */
    public String getOriginReference() {
        return originReference;
    }

    /**
     * set destination container identifier
     * 
     * @param pDestinationId
     *            the destination container identifier to set
     */
    public void setDestinationId(String pDestinationId) {
        destinationId = pDestinationId;
    }

    /**
     * set destination container reference
     * 
     * @param pDestinationReference
     *            the destination container reference to set
     */
    public void setDestinationReference(String pDestinationReference) {
        destinationReference = pDestinationReference;
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

    /**
     * set origin container reference
     * 
     * @param pOriginReference
     *            the origin container reference to set
     */
    public void setOriginReference(String pOriginReference) {
        originReference = pOriginReference;
    }

}
