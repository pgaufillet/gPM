/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.resources;

import java.io.InputStream;

/**
 * Resources loader interface used to get a resource stream from a resource
 * name.
 * 
 * @author llatil
 */
public interface IResourcesLoader {

    /**
     * Get a resource as a stream
     * 
     * @param pResourceName
     *            Name of the resource
     * @return Input stream used to get the resource content (or null if
     *         resource does not exist).
     */
    InputStream getAsStream(String pResourceName);
    
    /**
     * Try to get the resource length. Return -1 if it cannot be sorted out.
     * 
     * @param pResourceName Name of the resource
     * @return the resource length
     */
	public long getLength(String pResourceName);
}
