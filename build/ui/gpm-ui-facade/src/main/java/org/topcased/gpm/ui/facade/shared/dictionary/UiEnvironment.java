/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.dictionary;

import java.io.Serializable;

/**
 * Category for management.
 * 
 * @author jlouisy
 */
public class UiEnvironment implements Serializable {

    private static final long serialVersionUID = 1682064817868279958L;

    private boolean isPublic;

    private String name;

    /**
     * Empty constructor for serialization.
     */
    public UiEnvironment() {
    }

    /**
     * Create UiEnvironment.
     * 
     * @param pName
     *            Environment Name.
     * @param pIsPublic
     *            Environment visibility.
     */
    public UiEnvironment(String pName, boolean pIsPublic) {
        super();
        name = pName;
        isPublic = pIsPublic;
    }

    public String getName() {
        return name;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setName(String pName) {
        name = pName;
    }

    public void setPublic(boolean pIsPublic) {
        isPublic = pIsPublic;
    }

}
