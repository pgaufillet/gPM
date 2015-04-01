/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.filter;

import java.io.Serializable;

import org.topcased.gpm.business.util.Translation;

/**
 * UiFilterContainerType
 * 
 * @author nveillet
 */
public class UiFilterContainerType implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1042604494012511256L;

    private String id;

    private Translation name;

    /**
     * Constructor
     */
    public UiFilterContainerType() {
        // Empty constructor        
    }

    /**
     * Constructor with container identifier and translation
     * 
     * @param pId
     *            the container identifier
     * @param pTranslation
     *            the container translation (containing the container name, and
     *            its translation).
     */
    public UiFilterContainerType(final String pId,
            final Translation pTranslation) {
        this.id = pId;
        this.name = pTranslation;
    }

    /**
     * get container identifier
     * 
     * @return the container identifier
     */
    public String getId() {
        return id;
    }

    /**
     * get container name
     * 
     * @return the container name
     */
    public Translation getName() {
        return name;
    }

    /**
     * set container identifier
     * 
     * @param pId
     *            the container identifier to set
     */
    public void setId(final String pId) {
        id = pId;
    }

    /**
     * set container name
     * 
     * @param pName
     *            the container name to set
     */
    public void setName(final Translation pName) {
        name = pName;
    }

}
