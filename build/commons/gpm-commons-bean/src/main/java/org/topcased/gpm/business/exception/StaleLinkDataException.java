/***************************************************************
 * Copyright (c) 2012 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thibault Landré (Atos)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;

/**
 * Exception raised when a client tries to update link data while corresponding
 * link has been modified concurrently.
 * 
 * @author tlandre
 */
public class StaleLinkDataException extends BusinessException {

    /**
     * Generated UIDA
     */
    private static final long serialVersionUID = 2490480789502759397L;

    /** Default user message */
    private static final String DEFAULT_STALE_LINK_ERROR_MSG =
            "The link was modified, "
                    + "please try to close it and open it again to get the correct data.";

    /**
     * Constructs a StaleSheetDataException
     * 
     * @param pExpectedVersion
     *            link version expected.
     * @param pSheetVersion
     *            link version actually received.
     */
    public StaleLinkDataException(int pExpectedVersion, int pSheetVersion) {

        super(MessageFormat.format(
                "Stale link data. Link version {1} modified since it has been read "
                        + "(expected version {0})", pExpectedVersion,
                pSheetVersion));

        expectedVersion = pExpectedVersion;
        version = pSheetVersion;
        setUserMessage(DEFAULT_STALE_LINK_ERROR_MSG);
    }

    /**
     * Get the link version number expected for the update
     * 
     * @return Expected link version number
     */
    public int getExpectedVersion() {
        return expectedVersion;
    }

    /**
     * Get the actual link version number passed for the link update.
     * <p>
     * This version is different from the expected one (this is the cause of
     * this exception after all)
     * 
     * @return Caller link version number
     */
    public int getSheetVersion() {
        return version;
    }

    private int expectedVersion;

    private int version;
}
