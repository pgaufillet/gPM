/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exception;

/**
 * Handle error during importation.
 * <p>
 * Additional information is the 'object' which has cause this error.
 * 
 * @author mkargbo
 */
public class ImportException extends Exception {

    /** serialVersionUID */
    private static final long serialVersionUID = -3934008748014931907L;

    private Object object;

    /**
     * Default constructor
     */
    public ImportException() {
        super();
    }

    /**
     * Importation error constructor
     * 
     * @param pMessage
     *            Error message
     * @param pObject
     *            Object which cause this error
     * @param pCause
     *            Cause
     */
    public ImportException(ImportMessage pMessage, Object pObject,
            Throwable pCause) {
        super(pMessage.toString(), pCause);
        object = pObject;
    }

    /**
     * Importation error constructor
     * 
     * @param pMessage
     *            Error message
     * @param pObject
     *            Object which cause this error
     * @param pCause
     *            Cause
     */
    public ImportException(String pMessage, Object pObject, Throwable pCause) {
        super(pMessage, pCause);
        object = pObject;
    }

    /**
     * Importation error constructor
     ** 
     * @param pMessage
     *            Error message
     * @param pObject
     *            Object which cause this error
     */
    public ImportException(ImportMessage pMessage, Object pObject) {
        super(pMessage.toString());
        object = pObject;
    }

    /**
     * Importation error constructor
     ** 
     * @param pMessage
     *            Error message
     * @param pObject
     *            Object which cause this error
     */
    public ImportException(String pMessage, Object pObject) {
        super(pMessage);
        object = pObject;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object pObject) {
        object = pObject;
    }

    /**
     * Predefined error message for import exception
     * 
     * @author mkargbo
     */
    public enum ImportMessage {
        DO_NOT_IMPORT_TYPE("import.exception.bad.type"), OBJECT_EXISTS(
                "import.exception.object.exists"), OBJECT_NOT_EXISTS(
                "import.exception.object.not.exists"), LINK_LINKED_NOT_EXISTS(
                "import.exception.linked.not.exists"), CANNOT_IMPORT_ELEMENT(
                "import.exception.cannot.import");

        private String value;

        private ImportMessage(String pValue) {
            value = pValue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String pValue) {
            value = pValue;
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Enum#toString()
         */
        public String toString() {
            return value;
        }
    }
}
