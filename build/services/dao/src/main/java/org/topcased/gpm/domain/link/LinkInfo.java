/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 *
 ******************************************************************/

package org.topcased.gpm.domain.link;

public class LinkInfo {
    private String linkId;

    private String originId;

    private String destinationId;

    public LinkInfo(String pLinkId, String pOriginId, String pDestinationId) {
        linkId = pLinkId;
        originId = pOriginId;
        destinationId = pDestinationId;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getLinkId() {
        return linkId;
    }
}
