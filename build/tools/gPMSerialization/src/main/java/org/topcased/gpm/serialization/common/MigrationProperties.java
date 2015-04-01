/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.common;

import java.util.List;

/**
 * Properties used for migration
 * 
 * @author nveillet
 */
public abstract class MigrationProperties {

    private List<String> elementsId;

    private MigrationElementType elementType;

    private String fileName;

    /**
     * Constructor
     */
    public MigrationProperties() {
    }

    /**
     * get elements identifier
     * 
     * @return the elements identifier
     */
    public List<String> getElementsId() {
        return elementsId;
    }

    /**
     * get element type
     * 
     * @return the element type
     */
    public MigrationElementType getElementType() {
        return elementType;
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
     * set elements identifier
     * 
     * @param pElementsId
     *            the elements identifier to set
     */
    public void setElementsId(final List<String> pElementsId) {
        elementsId = pElementsId;
    }

    /**
     * set element type
     * 
     * @param pElementType
     *            the element type to set
     */
    public void setElementType(final MigrationElementType pElementType) {
        elementType = pElementType;
    }

    /**
     * set file name
     * 
     * @param pFileName
     *            the file name to set
     */
    public void setFileName(final String pFileName) {
        fileName = pFileName;
    }
}
