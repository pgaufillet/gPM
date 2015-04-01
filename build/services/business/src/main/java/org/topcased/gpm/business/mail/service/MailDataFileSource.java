/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Szadel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.mail.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

import org.topcased.gpm.business.mail.MailDataFile;

/**
 * The datasource for MailDataFile.
 * 
 * @author tszadel
 */
public class MailDataFileSource implements DataSource {
    /** The default MIME Type. */
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

    /** The source. */
    private MailDataFile dataFile;

    /**
     * Constructor.
     * 
     * @param pDataFile
     *            The source.
     */
    public MailDataFileSource(MailDataFile pDataFile) {
        dataFile = pDataFile;
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.activation.DataSource#getContentType()
     */
    public String getContentType() {
        if (dataFile.getMimetype() == null
                || dataFile.getMimetype().trim().length() == 0) {
            return DEFAULT_MIME_TYPE;
        }
        return dataFile.getMimetype();
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.activation.DataSource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(dataFile.getAttachedFile());
    }

    /**
     * {@inheritDoc}
     * 
     * @see javax.activation.DataSource#getName()
     */
    public String getName() {
        return dataFile.getName();
    }

    /**
     * This method always throws an IOException.
     * 
     * @return Nothing, as the method throws an exception
     * @see javax.activation.DataSource#getOutputStream()
     * @throws IOException
     *             Throws always this exception
     */
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Method cannot be used.");
    }
}
