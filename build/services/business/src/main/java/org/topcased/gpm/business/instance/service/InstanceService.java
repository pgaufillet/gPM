/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.instance.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.process.service.BusinessProcessData;

/**
 * Instance service
 * 
 * @author llatil
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface InstanceService {

    /**
     * Create a new process definition in the database.
     * 
     * @param pToken
     *            User or role session token. This session requires a global
     *            administrator privilege.
     * @param pBusinessProcess
     *            Business process data
     */
    public void createBusinessProcess(String pToken,
            BusinessProcessData pBusinessProcess);

    /**
     * Create the gPM object, corresponding to the global application, that can
     * contain global attributes for the application
     * 
     * @param pToken
     *            User or role session token. This session requires a global
     *            administrator privilege.
     */
    public void createGpm(String pToken);

    /**
     * Get the business process ID from its name
     * 
     * @param pProcessName
     *            process name.
     * @return The business process ID.
     * @throws InvalidNameException
     *             If the business process name is invalid
     */
    public String getBusinessProcessId(String pProcessName)
        throws InvalidNameException;

    /**
     * Get the containers id
     * 
     * @param pRoleToken
     *            The role token
     * @param pContainerNames
     *            The container list to found
     * @return Container ids list
     */
    public List<String> getContainersIdsByNames(String pRoleToken,
            List<String> pContainerNames);
}
