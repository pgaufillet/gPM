/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.instance.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.process.service.BusinessProcessData;
import org.topcased.gpm.domain.attributes.Gpm;
import org.topcased.gpm.domain.attributes.GpmDao;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;

/**
 * InstanceServiceImpl
 * 
 * @author llatil
 */
public class InstanceServiceImpl extends ServiceImplBase implements
        InstanceService {

    /** The gpmDao */
    private GpmDao gpmDao;

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.instance.service.InstanceService
     *      #createBusinessProcess(java.lang.String,
     *      org.topcased.gpm.business.authorization.service.BusinessProcessData)
     */
    public void createBusinessProcess(String pToken,
            BusinessProcessData pBusinessProcessData) {
        authorizationService.assertGlobalAdminRole(pToken);

        if (pBusinessProcessData == null) {
            throw new GDMException(
                    "Cannot create a Business Process with null data.");
        }
        if (pBusinessProcessData.getName() == null) {
            throw new InvalidValueException(
                    "Cannot create a Business Process with name: null.");
        }

        // Create the business process with this name
        BusinessProcess lBusinessProcess;
        String lBusinessProcessName = pBusinessProcessData.getName();

        lBusinessProcess =
                getBusinessProcessDao().getBusinessProcess(lBusinessProcessName);

        if (null == lBusinessProcess) {
            lBusinessProcess = BusinessProcess.newInstance();
            lBusinessProcess.setName(lBusinessProcessName);
            getBusinessProcessDao().create(lBusinessProcess);
        }

        // Create the admin role for the process
        authorizationService.createRole(pToken,
                AuthorizationService.ADMIN_ROLE_NAME,
                pBusinessProcessData.getName());
        authorizationService.addRole(pToken, AuthorizationService.ADMIN_LOGIN,
                lBusinessProcessName, AuthorizationService.ADMIN_ROLE_NAME);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.instance.service.InstanceService#getBusinessProcessId(java.lang.String)
     */
    public String getBusinessProcessId(String pProcessName) {
        return getBusinessProcess(pProcessName).getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.instance.service.InstanceService
     *      #createGpm(java.lang.String)
     */
    public void createGpm(String pToken) {
        authorizationService.assertGlobalAdminRole(pToken);

        try {
            String lGpmId = getGpmDao().getGpmId();
            if (lGpmId == null) {
                getGpmDao().create(Gpm.newInstance());
            }
        }
        catch (NonUniqueResultException e) {
            throw new GDMException("More than one gPM object defined.");
        }
    }

    /**
     * getGpmDao
     * 
     * @return the GpmDao
     */
    public GpmDao getGpmDao() {
        return gpmDao;
    }

    /**
     * setGpmDao
     * 
     * @param pGpmDao
     *            the GpmDao to set
     */
    public void setGpmDao(GpmDao pGpmDao) {
        this.gpmDao = pGpmDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.instance.service.InstanceService#getContainersIdsByNames(java.lang.String,
     *      java.util.List)
     */
    public List<String> getContainersIdsByNames(String pRoleToken,
            List<String> pContainerNames) {

        List<String> lIds = new ArrayList<String>();

        for (String lName : pContainerNames) {
            lIds.add(fieldsContainerServiceImpl.getFieldsContainerId(
                    pRoleToken, lName));
        }

        return lIds;
    }
}
