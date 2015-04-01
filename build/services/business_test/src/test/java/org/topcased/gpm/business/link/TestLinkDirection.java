/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.link;

import junit.framework.TestCase;

import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.link.impl.LinkDirection;

/**
 * TestLinkDirection
 */
public class TestLinkDirection extends TestCase {

    private static final String ORIGIN_TYPE_ID = "1";

    private static final String DESTINATION_TYPE_ID = "2";

    private static final String UNKNOWN_FIELDS_CONTAINER_ID = "3";

    /**
     * Test method for
     * {@link org.topcased.gpm.business.link.impl.LinkDirection#getToLinkDirection(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public void testGetToLinkDirection() {
        LinkDirection lLinkDirection =
                LinkDirection.getToLinkDirection(ORIGIN_TYPE_ID,
                        ORIGIN_TYPE_ID, DESTINATION_TYPE_ID);
        assertEquals(LinkDirection.ORIGIN.name(), lLinkDirection.name());

        lLinkDirection =
                LinkDirection.getToLinkDirection(DESTINATION_TYPE_ID,
                        ORIGIN_TYPE_ID, DESTINATION_TYPE_ID);
        assertEquals(LinkDirection.DESTINATION.name(), lLinkDirection.name());

        try {
            lLinkDirection =
                    LinkDirection.getToLinkDirection(
                            UNKNOWN_FIELDS_CONTAINER_ID, ORIGIN_TYPE_ID,
                            ORIGIN_TYPE_ID);
            fail("Invalid Identifier exception expected");
        }
        catch (InvalidIdentifierException e) {
            //ok
        }
    }

    /**
     * Test method for
     * {@link org.topcased.gpm.business.link.impl.LinkDirection#getFromLinkDirection(java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    public void testGetFromLinkDirection() {
        LinkDirection lLinkDirection =
                LinkDirection.getFromLinkDirection(DESTINATION_TYPE_ID,
                        ORIGIN_TYPE_ID, DESTINATION_TYPE_ID);
        assertEquals(LinkDirection.ORIGIN.name(), lLinkDirection.name());

        lLinkDirection =
                LinkDirection.getFromLinkDirection(ORIGIN_TYPE_ID,
                        ORIGIN_TYPE_ID, DESTINATION_TYPE_ID);
        assertEquals(LinkDirection.DESTINATION.name(), lLinkDirection.name());

        lLinkDirection =
                LinkDirection.getFromLinkDirection(ORIGIN_TYPE_ID,
                        ORIGIN_TYPE_ID, ORIGIN_TYPE_ID);
        assertEquals(LinkDirection.UNDEFINED.name(), lLinkDirection.name());

        try {
            lLinkDirection =
                    LinkDirection.getFromLinkDirection(
                            UNKNOWN_FIELDS_CONTAINER_ID, ORIGIN_TYPE_ID,
                            DESTINATION_TYPE_ID);
            fail("Invalid Identifier exception expected");
        }
        catch (InvalidIdentifierException e) {
            //ok
        }
    }
}
