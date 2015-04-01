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

import java.io.Serializable;

/**
 * The Class Code.
 * 
 * @author llatil
 */
public class Code implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -3814591414558275523L;

    /** The content. */
    private String content;

    /** The filename. */
    private String filename;

    /**
     * Sets the content.
     * 
     * @param pContent
     *            the content
     */
    public void setContent(String pContent) {
        content = pContent;
    }

    /**
     * Gets the content.
     * 
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the filename.
     * 
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the filename.
     * 
     * @param pFilename
     *            the filename
     */
    public void setFilename(String pFilename) {
        filename = pFilename;
    }
}
