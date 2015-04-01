/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.extendedaction;

/**
 * UiFileEAResult
 * 
 * @author nveillet
 */
public class UiFileEAResult extends AbstractUiExtendedActionResult {

    private String fileId;

    private String fileName;

    private String mimeType;

    /**
     * Constructor
     * 
     * @param pFileId
     *            the file identifier
     * @param pFileName
     *            the file name
     * @param pMimeType
     *            the mime type
     */
    public UiFileEAResult(String pFileId, String pFileName, String pMimeType) {
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

    /**
     * set file identifier
     * 
     * @param pExportFileId
     *            the file identifier to set
     */
    public void setExportFileId(String pExportFileId) {
        fileId = pExportFileId;
    }

    /**
     * set file name
     * 
     * @param pFileName
     *            the file name to set
     */
    public void setFileName(String pFileName) {
        fileName = pFileName;
    }

    /**
     * set mime type
     * 
     * @param pMimeType
     *            the mime type to set
     */
    public void setMimeType(String pMimeType) {
        mimeType = pMimeType;
    }

}
