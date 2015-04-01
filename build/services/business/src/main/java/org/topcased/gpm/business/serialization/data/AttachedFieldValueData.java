/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization.data;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * AttachedFieldValueData.
 * <p>
 * The attributes:
 * <ul>
 * <li>filename: Contains the name of the file with its entire path.</li>
 * <li>value: Contains only the name of the file.</li>
 * </ul>
 * </p>
 * 
 * @author llatil
 */
@XStreamAlias("attachedFieldValue")
public class AttachedFieldValueData extends FieldValueData {

    /** Generated UID. */
    private static final long serialVersionUID = 8231478697264128920L;

    /** The MIME type. */
    @XStreamAsAttribute
    private String mimeType;

    /** Identifier of this value. */
    @XStreamAsAttribute
    private String id;

    /** The filename. Name of the file with the entire path. */
    @XStreamAsAttribute
    private String filename;

    /**
     * New content to set for this value (used only for element creation /
     * update).
     */
    private byte[] newContent;

    /**
     * Constructs a new AttachedFieldValueData (default ctor).
     */
    public AttachedFieldValueData() {
    }

    /**
     * Constructs a new AttachedFieldValueData.
     * 
     * @param pName
     *            Name of the field
     */
    public AttachedFieldValueData(String pName) {
        super(pName);
    }

    /**
     * Get the file name used to get the actual content This file is only used
     * during the creation / update of the field data (the filename is not
     * exported during the XML serialization).
     * 
     * @return Filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the file name.
     * 
     * @param pFilename
     *            Filename
     */
    public void setFilename(String pFilename) {
        filename = pFilename;
    }

    /**
     * Get MIME type of the content.
     * 
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Set MIME type of the content.
     * 
     * @param pMimeType
     *            MIME type
     */
    public void setMimeType(String pMimeType) {
        mimeType = pMimeType;
    }

    /**
     * Get the value technical identifier. It is used to retrieve the actual
     * file content.
     * 
     * @return Value identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Set the sheet technical identifier.
     * 
     * @param pId
     *            Sheet identifier
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * Gets the new content.
     * 
     * @return the new content
     */
    public byte[] getNewContent() {
        return newContent;
    }

    /**
     * Sets the new file content.
     * 
     * @param pNewContent
     *            the new new content
     */
    public void setNewContent(byte[] pNewContent) {
        newContent = pNewContent;
    }

    /** {@inheritDoc} */
    @Override
    public String getValue() {
        if (StringUtils.isNotEmpty(value)) {
            return value;
        }
        if (null == filename) {
            return StringUtils.EMPTY;
        }
        File lFile = new File(filename);
        return lFile.getName();
    }
}
