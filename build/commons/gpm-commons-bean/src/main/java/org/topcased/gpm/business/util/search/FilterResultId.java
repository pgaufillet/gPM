/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.util.search;

import java.io.Serializable;

/**
 * The id of a filter result.
 * 
 * @author tpanuel
 */
public class FilterResultId implements Serializable {
    private static final long serialVersionUID = -4365174070799865770L;

    private String id;

    private String visuLocker;

    private String editLocker;

    private boolean onlyInfo;

    /**
     * Empty constructor for serialization.
     */
    public FilterResultId() {

    }

    /**
     * Constructor initializing all fields.
     * 
     * @param pId
     *            the entry id.
     * @param pVisuLocker
     *            the visu locker. Null, if not locked
     * @param pEditLocker
     *            Get the edit locker. Null, if not locked
     * @param pOnlyInfo
     *            If the lock is only info : the container can be opened
     */
    public FilterResultId(final String pId, final String pVisuLocker,
            final String pEditLocker, final boolean pOnlyInfo) {
        id = pId;
        visuLocker = pVisuLocker;
        editLocker = pEditLocker;
        onlyInfo = pOnlyInfo;
    }

    /**
     * Get the entry id.
     * 
     * @return The entry id.
     */
    public String getId() {
        return id;
    }

    /**
     * Get the visu locker. Null, if not locked.
     * 
     * @return The visu locker.
     */
    public String getVisuLocker() {
        return visuLocker;
    }

    /**
     * Get the edit locker. Null, if not locked.
     * 
     * @return The edit locker.
     */
    public String getEditLocker() {
        return editLocker;
    }

    /**
     * If the lock is only info : the container can be opened.
     * 
     * @return If the lock is only info.
     */
    public boolean isOnlyInfo() {
        return onlyInfo;
    }
}