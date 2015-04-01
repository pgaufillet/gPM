/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.xml;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.topcased.gpm.business.exception.SchemaValidationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Helper class used to check the syntax of XML document with the schema
 * 
 * @author llatil
 */
public class SchemaValidator {

    private Schema gpmSchema;

    /**
     * Default constructor
     */
    public SchemaValidator() {
        SchemaFactory lFactory =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        ClassLoader lClazzLoader = SchemaValidator.class.getClassLoader();
        URL lSchemaLocation =
                lClazzLoader.getResource("instantiation_model.xsd");

        try {
            gpmSchema = lFactory.newSchema(lSchemaLocation);
        }
        catch (SAXException e) {
            throw new SchemaValidationException(
                    "Internal error in parsing the XML Schema resource:\n"
                            + e.getMessage());
        }
    }

    /**
     * Validate an XML input stream against the gPM instance tool schema.
     * 
     * @param pXmlStream
     *            Stream used to read the XML document
     * @param pWithInstantiateErrorHandler
     *            Define if the validation use the instantiate error handler or
     *            not. This specific handler stop the system at the first error.
     * @throws SchemaValidationException
     *             In case of validation error
     */
    public void validate(InputStream pXmlStream,
            Boolean pWithInstantiateErrorHandler) {
        Validator lValidator = gpmSchema.newValidator();
        if (pWithInstantiateErrorHandler) {
            lValidator.setErrorHandler(new MyErrorHandler());
        }
        Source lSource = new StreamSource(cloneInputStream(pXmlStream));

        // Check the document
        try {
            lValidator.validate(lSource);
        }
        catch (SAXException ex) {
            // In case of validation error, throw an exception
            throw new SchemaValidationException(
                    "Syntax error in XML document:\n" + ex.getMessage());
        }
        catch (IOException e) {
            throw new SchemaValidationException("Cannot read XML document:\n"
                    + e.getMessage());
        }
    }

    /**
     * Clone an InputStream
     */
    private InputStream cloneInputStream(InputStream pSource) {

        InputStream lInputStreamSource = pSource;
        if (!lInputStreamSource.markSupported()) {
            lInputStreamSource = new BufferedInputStream(pSource);
        }

        ByteArrayOutputStream lByteArrayOutputStream =
                new ByteArrayOutputStream();

        InputStream lInputStreamOut = null;
        try {
            lInputStreamSource.mark(lInputStreamSource.available() + 1);

            ObjectOutputStream lObjectOutputStream =
                    new ObjectOutputStream(lByteArrayOutputStream);
            int lVal;
            while ((lVal = lInputStreamSource.read()) != -1) {
                lObjectOutputStream.write(lVal);
            }
            lObjectOutputStream.flush();
            lObjectOutputStream.close();
            lInputStreamSource.reset();
            lInputStreamOut =
                    new ObjectInputStream(new ByteArrayInputStream(
                            lByteArrayOutputStream.toByteArray()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return lInputStreamOut;
    }

    /**
     * Implementation of an error handler. Currently the error handling methods
     * just write out the parse error message and the corresponding line number,
     * and exit the application.
     * 
     * @author llatil
     */
    private static class MyErrorHandler implements ErrorHandler {

        /**
         * Display the error message on stderr, and exit the application
         * 
         * @param pException
         *            Parse error exception
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void error(SAXParseException pException) throws SAXException {
            printError(pException.getMessage(), pException.getLineNumber(),
                    pException.getColumnNumber());
        }

        /**
         * Display the error message on stderr, and exit the application
         * 
         * @param pException
         *            Parse error exception
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void fatalError(SAXParseException pException)
            throws SAXException {
            printError(pException.getMessage(), pException.getLineNumber(),
                    pException.getColumnNumber());
        }

        /**
         * Warnings are completely igored currently.
         * 
         * @param pException
         *            Parse warning exception
         */
        public void warning(SAXParseException pException) throws SAXException {
        }

        /**
         * Reports an error message
         * 
         * @param pMsg
         *            Error message text
         * @param pLine
         *            Line number
         * @param pCol
         *            Column number
         */
        public void printError(String pMsg, int pLine, int pCol) {
            String lMsg =
                    "Parse error in line " + pLine + ", col " + pCol + "\n"
                            + pMsg;
            System.err.println(lMsg);
            System.exit(1);
        }
    }
}
