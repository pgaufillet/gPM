/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors:
 ******************************************************************/

package org.topcased.gpm.util.config;

import java.io.File;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * Custom implementation of Spring configurer.
 * <p>
 * This implementation can read properties files located in a directory
 * specified in an environment variable.
 * 
 * @author llatil
 */
public class Configurer extends PropertyPlaceholderConfigurer implements
        InitializingBean {
    private String localConfigDirProp;

    private String[] fileNames;

    private Resource[] locations;

    /**
     * {@inheritDoc}
     * 
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

        if (StringUtils.isNotBlank(localConfigDirProp)) {
            String lDirPath = System.getProperty(localConfigDirProp);

            if (StringUtils.isNotBlank(lDirPath)) {
                // If a local conf dir is specified, try to read
                // the properties file here also.

                for (String lFilename : fileNames) {
                    String lConfigPropsPath =
                            lDirPath + File.separator + lFilename;

                    File lFile = new File(lConfigPropsPath);
                    if (lFile.canRead()) {
                        locations =
                                (Resource[]) ArrayUtils.add(locations,
                                        new FileSystemResource(lFile));
                    }
                }
            }
        }
        super.setLocations(locations);
    }

    /**
     * Set the path of the local config dir.
     * 
     * @param pDirNameProperty
     *            the property containg the config dir
     */
    public final void setLocalConfigDirProp(String pDirNameProperty) {
        localConfigDirProp = pDirNameProperty;
    }

    /**
     * Set the list of config file names.
     * 
     * @param pFileNames
     *            the fileNames to set
     */
    public final void setFileNames(String[] pFileNames) {
        fileNames = pFileNames.clone();
    }

    /**
     * Set locations of properties files to be loaded.
     * <p>
     * This method is the same as parent class, and need to be redefined for
     * technical reason only.
     * 
     * @param pLocations
     *            the locations to set
     * @see PropertyPlaceholderConfigurer#setLocations(Resource[])
     */
    public final void setLocations(Resource[] pLocations) {
        locations = pLocations.clone();
    }
}
