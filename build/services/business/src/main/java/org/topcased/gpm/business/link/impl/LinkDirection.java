/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.link.impl;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exception.InvalidIdentifierException;

/**
 * LinkDirection.
 * <p>
 * Available direction of a link.
 * 
 * @author mkargbo
 */
public enum LinkDirection {

    UNDEFINED("UNDEFINED"), ORIGIN("ORIGIN"), DESTINATION("DESTINATION");

    private String directionName;

    private LinkDirection(String pDirectionName) {
        directionName = pDirectionName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Enum#toString()
     */
    public String toString() {
        return directionName;
    }

    /**
     * Gets the 'TO' direction.
     * <p>
     * e.g: If the fields container corresponds to the origin, returns 'ORIGIN'.
     * 
     * @param pNextFieldsContainerId
     *            Identifier of the following fields container.
     * @param pOriginId
     *            Identifier of the origin (fields container)
     * @param pDestinationId
     *            Identifier of the destination (fields container)
     * @return The 'TO' direction.
     * @throws InvalidIdentifierException
     *             If the given fields container identifier doesn't correspond
     *             to the origin or destination identifier.
     */
    public static LinkDirection getToLinkDirection(
            String pNextFieldsContainerId, String pOriginId,
            String pDestinationId) {
        if ((pOriginId.equals(pNextFieldsContainerId))
                && ((pDestinationId.equals(pNextFieldsContainerId)))) {
            return LinkDirection.UNDEFINED;
        }
        else if (pOriginId.equals(pNextFieldsContainerId)) {
            return LinkDirection.ORIGIN;
        }
        else if (pDestinationId.equals(pNextFieldsContainerId)) {
            return LinkDirection.DESTINATION;
        }
        else {
            throw new InvalidIdentifierException(
                    "The identifier doesn't corresponds to the origin (or destination), "
                            + "impossible to determined the direction of the link");
        }
    }

    /**
     * Gets the 'FROM' direction.
     * <p>
     * e.g: If the fields container corresponds to the origin, returns
     * 'DESTINATION'.
     * 
     * @param pPreviousFieldsContainerId
     *            Identifier of the previous fields container.
     * @param pOriginId
     *            Identifier of the origin (fields container)
     * @param pDestinationId
     *            Identifier of the destination (fields container)
     * @return The 'FROM' direction.
     * @throws InvalidIdentifierException
     *             If the given fields container identifier doesn't correspond
     *             to the origin or destination identifier.
     */
    public static LinkDirection getFromLinkDirection(
            String pPreviousFieldsContainerId, String pOriginId,
            String pDestinationId) {
        if (StringUtils.isBlank(pPreviousFieldsContainerId)) {
            return null;
        }
        else if ((pOriginId.equals(pPreviousFieldsContainerId))
                && ((pDestinationId.equals(pPreviousFieldsContainerId)))) {
            return LinkDirection.UNDEFINED;
        }
        else if (pOriginId.equals(pPreviousFieldsContainerId)) {
            return LinkDirection.DESTINATION;
        }
        else if (pDestinationId.equals(pPreviousFieldsContainerId)) {
            return LinkDirection.ORIGIN;
        }
        else {
            throw new InvalidIdentifierException(
                    "The identifier doesn't corresponds to the origin (or destination), "
                            + "impossible to determined the direction of the link");
        }
    }
}