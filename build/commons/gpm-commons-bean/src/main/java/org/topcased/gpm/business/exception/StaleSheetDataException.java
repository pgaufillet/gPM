/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;

/**
 * Exception raised when a client tries to update sheet data while corresponding
 * sheet has been modified concurrently.
 * 
 * @author llatil
 */
public class StaleSheetDataException extends BusinessException {

    /**
     * Generated UID
     */
    private static final long serialVersionUID = 2490480789502759397L;

    /** Default user message */
    private static final String DEFAULT_STALE_SHEET_ERROR_MSG =
            "The sheet was modified by an other user, "
                    + "please try to close it and open it again to get the correct data.";

    /**
     * Constructs a StaleSheetDataException
     * 
     * @param pExpectedVersion
     *            Sheet version expected.
     * @param pSheetVersion
     *            Sheet version actually received.
     */
    public StaleSheetDataException(int pExpectedVersion, int pSheetVersion) {

        super(MessageFormat.format(
                "Stale sheet data. Sheet version {1} modified since it has been read "
                        + "(expected version {0})", pExpectedVersion,
                pSheetVersion));

        expectedVersion = pExpectedVersion;
        version = pSheetVersion;
        setUserMessage(DEFAULT_STALE_SHEET_ERROR_MSG);
    }

    /**
     * Get the sheet version number expected for the update
     * 
     * @return Expected sheet version number
     */
    public int getExpectedVersion() {
        return expectedVersion;
    }

    /**
     * Get the actual sheet version number passed for the sheet update.
     * <p>
     * This version is different from the expected one (this is the cause of
     * this exception after all)
     * 
     * @return Caller sheet version number
     */
    public int getSheetVersion() {
        return version;
    }

    private int expectedVersion;

    private int version;
}
