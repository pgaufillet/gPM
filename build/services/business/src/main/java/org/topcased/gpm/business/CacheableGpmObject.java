/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business;

import java.io.Serializable;

import org.topcased.gpm.business.exception.MethodNotImplementedException;

/**
 * The Class CacheableGpmObject.
 * 
 * @author llatil
 */
public abstract class CacheableGpmObject implements Serializable {
    /** Generated UID */
    private static final long serialVersionUID = 5280235420945700260L;

    /** The cache id */
    private String id;

    /**
     * Empty constructor for mutable/immutable transformation and WS
     */
    public CacheableGpmObject() {
        super();
    }

    /**
     * Constructor defining all the fields
     * 
     * @param pId
     *            The cache id
     */
    public CacheableGpmObject(String pId) {
        id = pId;
    }

    /**
     * Get identifier of this object
     * 
     * @return Technical identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Set identifier of this object
     * 
     * @param pId
     *            The new technical identifier
     */
    public void setId(String pId) {
        id = pId;
    }

    /**
     * This method does nothing. It is defined to ensure all derived classes can
     * (and should) always call the marshal method of their super class.
     * 
     * @param pContent Serializable content
     */
    protected void marshal(Object pContent) {
    }

    /**
     * Unused
     * 
     * @return unused (throw exception)
     */
    public Object marshal() {
        throw new MethodNotImplementedException("marshal()");
    }
}
