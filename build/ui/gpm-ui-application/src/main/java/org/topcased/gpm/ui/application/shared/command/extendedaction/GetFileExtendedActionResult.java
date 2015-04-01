/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.shared.command.extendedaction;

/**
 * GetSheetsExtendedActionResult
 * 
 * @author jlouisy
 */
public class GetFileExtendedActionResult extends
        AbstractExecuteExtendedActionResult {

    /** serialVersionUID */
    private static final long serialVersionUID = -869470914203017124L;

    private String fileId;

    private String fileName;

    private String mimeType;

    /**
     * Empty constructor for serialization.
     */
    public GetFileExtendedActionResult() {
    }

    /**
     * Constructor.
     * 
     * @param pFileId
     *            File Id in cache.
     * @param pFileName
     *            File Name
     * @param pMimeType
     *            the mime type
     */
    public GetFileExtendedActionResult(String pFileId, String pFileName,
            String pMimeType) {
        super();
        fileId = pFileId;
        fileName = pFileName;
        mimeType = pMimeType;
    }

    /**
     * get file identifier
     * 
     * @return the file identifier
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * get file name
     * 
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * get mime type
     * 
     * @return the mime type
     */
    public String getMimeType() {
        return mimeType;
    }
}
