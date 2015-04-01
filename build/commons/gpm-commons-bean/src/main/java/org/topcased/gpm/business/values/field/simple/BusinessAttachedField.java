/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 *
 ******************************************************************/
package org.topcased.gpm.business.values.field.simple;

import org.topcased.gpm.business.values.field.BusinessField;

/**
 * Interface used to access on an attached field.
 * 
 * @author tpanuel
 */
public interface BusinessAttachedField extends BusinessField {
    /**
     * Get the id.
     * 
     * @return The id.
     */
    public String getId();

    /**
     * Set the id.
     * 
     * @param pId
     *            The new id.
     */
    public void setId(final String pId);

    /**
     * Get the file name.
     * 
     * @return The file name.
     */
    public String getFileName();

    /**
     * Set the file name.
     * 
     * @param pFileName
     *            The new file name.
     */
    public void setFileName(final String pFileName);

    /**
     * Get the mime type.
     * 
     * @return The mime type.
     */
    public String getMimeType();

    /**
     * Set the mime type.
     * 
     * @param pMimeType
     *            The new mime type.
     */
    public void setMimeType(final String pMimeType);

    /**
     * Get the content.
     * 
     * @return The content.
     */
    public byte[] getContent();

    /**
     * Set the content.
     * 
     * @param pContent
     *            The new content.
     */
    public void setContent(final byte[] pContent);
}