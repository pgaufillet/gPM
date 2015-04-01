/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all exceptions generated for an invalid name.
 * 
 * @author llatil
 */
public class InvalidNameException extends GDMException {

    /** generated UID */
    private static final long serialVersionUID = 3460713665116905883L;

    /** The key for the empty password message error internalization. */
    public static final String PWD_EMPTY_KEY =
            "Exception.CreateUser.PasswordEmpty";

    /** The key for the default message error internalization. */
    public static final String DEFAULT_KEY =
            "Exception.CreateUser.InvalidNameException";

    /** The invalid name */
    protected String invalidName;

    /** Invalid names list */
    protected Collection<String> invalidNames;

    /**
     * Constructs a new InvalidNameException
     * 
     * @param pInvalidName
     *            Name which is invalid
     */
    public InvalidNameException(String pInvalidName) {
        this(pInvalidName, "Invalid name ''{0}''");
    }

    /**
     * Constructs a new InvalidNameException
     * 
     * @param pInvalidNames
     *            List of invalid names
     */
    public InvalidNameException(Collection<String> pInvalidNames) {
        this(pInvalidNames, "Invalid name(s) ''{0}''");
    }

    /**
     * Constructs a new InvalidNameException.
     * <p>
     * The exception message can be tailored through the pMessage pattern. The
     * pMessage pattern is processed through a MessageFormat with the invalid
     * name as {0} argument.
     * 
     * @param pInvalidName
     *            Name which is invalid
     * @param pMessage
     *            Message pattern (ex: "The field name {0} is invalid")
     * @see MessageFormat
     */
    public InvalidNameException(String pInvalidName, String pMessage) {
        super(MessageFormat.format(pMessage, pInvalidName));
        invalidName = pInvalidName;
        invalidNames = Collections.singleton(pInvalidName);
    }

    /**
     * Constructs a new InvalidNameException.
     * <p>
     * The exception message can be tailored through the pMessage pattern. The
     * pMessage pattern is processed through a MessageFormat with the invalid
     * name as {0} argument.
     * 
     * @param pInvalidNames
     *            List of invalid names
     * @param pMessage
     *            Message pattern (ex: "Theses field names ''{0}'' are invalid")
     * @see MessageFormat
     */
    public InvalidNameException(Collection<String> pInvalidNames,
            String pMessage) {
        super(MessageFormat.format(pMessage, pInvalidNames));
        if (pInvalidNames.size() > 0) {
            invalidName = pInvalidNames.iterator().next();
        }
        List<String> lInvalidNamesList = new ArrayList<String>(pInvalidNames);
        Collections.sort(lInvalidNamesList);
        invalidNames = lInvalidNamesList;
    }

    /**
     * Get the invalid name associated with this exception
     * <p>
     * This name is the first name of the list if this exception contains
     * several invalid names.
     * 
     * @return Invalid name.
     */
    public String getInvalidName() {
        return invalidName;
    }

    /**
     * Get the list of invalid names associated with this exception
     * <p>
     * This list contains a single element if only one name is associated with
     * this exception.
     * 
     * @return Invalid names list.
     */
    public Collection<? extends String> getInvalidNames() {
        return invalidNames;
    }
}
