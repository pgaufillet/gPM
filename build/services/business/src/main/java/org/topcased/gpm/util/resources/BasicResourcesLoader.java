/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.util.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/**
 * Basic implementation of resources loader
 * 
 * @author llatil
 */
public class BasicResourcesLoader implements IResourcesLoader {
    // The log4j logger object for this class.
//    private static Logger staticLogger =
//            org.apache.log4j.Logger.getLogger(BasicResourcesLoader.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.instantiation.IResourcesLoader#getAsStream(java.lang.String)
     */
    @Override
    @SuppressWarnings("resource")
	public InputStream getAsStream(String pResourceName) {
        InputStream lInStream = null;

        // First, try to open a file
        try {
            lInStream = new FileInputStream(pResourceName);
        }
        catch (java.io.FileNotFoundException e) {
        }

        // if the file is not found, try again as a resource.
        if (null == lInStream) {
            lInStream = this.getClass().getClassLoader().getResourceAsStream(pResourceName);
        }
        return lInStream;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public long getLength(String pResourceName) {
        File lFile = new File(pResourceName);
        
        if (lFile.isFile()) {
        	return lFile.length();
        }
        
        return -1;
    }
}
