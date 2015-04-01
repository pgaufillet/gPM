/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.atomic.impl;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.atomic.service.AtomicActionsManager;
import org.topcased.gpm.business.exception.LockException;
import org.topcased.gpm.business.serialization.data.Lock;
import org.topcased.gpm.common.valuesContainer.LockType;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeDao;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.attributes.AttributeValueDao;
import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.attributes.AttributesContainerDao;
import org.topcased.gpm.domain.fields.LockDao;

/**
 * Manager for atomic actions. All action realized by the implementation are out
 * of transaction and can be synchronized by an interceptor.
 * 
 * @author tpanuel
 */
public class AtomicActionsManagerImpl implements AtomicActionsManager {
    private AttributesContainerDao attributesContainerDao;

    private AttributeDao attributeDao;

    private AttributeValueDao attributeValueDao;

    private LockDao lockDao;

    /**
     * Setter used by Spring injection.
     * 
     * @param pDao
     *            The DAO.
     */
    public void setAttributesContainerDao(final AttributesContainerDao pDao) {
        attributesContainerDao = pDao;
    }

    /**
     * Setter used by Spring injection.
     * 
     * @param pDao
     *            The DAO.
     */
    public void setAttributeDao(final AttributeDao pDao) {
        attributeDao = pDao;
    }

    /**
     * Setter used by Spring injection.
     * 
     * @param pDao
     *            The DAO.
     */
    public void setAttributeValueDao(final AttributeValueDao pDao) {
        attributeValueDao = pDao;
    }

    /**
     * Setter used by Spring injection.
     * 
     * @param pDao
     *            The DAO.
     */
    public void setLockDao(final LockDao pDao) {
        lockDao = pDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.atomic.service.AtomicActionsManager#atomicTestAndSet(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public String atomicTestAndSet(final String pRoleToken,
            final String pElementId, final String pAttributeName,
            final String pOldValue, final String pNewValue) {
        final AttributesContainer lContainer =
                attributesContainerDao.load(pElementId);
        Attribute lAttribute = null;
        AttributeValue lValue = null;

        if (lContainer != null) {
            lAttribute =
                    attributesContainerDao.getAttribute(lContainer,
                            pAttributeName);
        }
        if (lAttribute != null && !lAttribute.getAttributeValues().isEmpty()) {
            lValue = lAttribute.getAttributeValues().get(0);
        }

        final String lCurrentValue;

        if (lValue == null) {
            lCurrentValue = null;
            // Test and set
            if (pOldValue == null) {
                // Create the attribute value
                if (lAttribute == null) {
                    lAttribute = Attribute.newInstance();
                    lAttribute.setName(pAttributeName);
                    attributeDao.create(lAttribute);
                    lContainer.getAttributes().add(lAttribute);
                }
                lValue = AttributeValue.newInstance();
                lValue.setValue(pNewValue);
                attributeValueDao.create(lValue);
                lAttribute.getAttributeValues().add(lValue);
            }
        }
        else {
            lCurrentValue = lValue.getValue();
            // Test and set
            if (StringUtils.equals(lCurrentValue, pOldValue)) {
                // Update the attribute value
                lValue.setValue(pNewValue);
            }
        }

        return lCurrentValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.atomic.service.AtomicActionsManager#atomicLockSheet(java.lang.String,
     *      org.topcased.gpm.common.valuesContainer.LockType, java.lang.String,
     *      java.lang.String, boolean)
     */
    public void lockSheet(final String pUserLogin, final LockType pLockType,
            final String pSessionToken, final String pSheetId,
            final boolean pIsAdmin) throws LockException {
        lock(pUserLogin, pLockType, pSessionToken, pSheetId, pIsAdmin);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.atomic.service.AtomicActionsManager#lock(java.lang.String,
     *      org.topcased.gpm.common.valuesContainer.LockType, java.lang.String,
     *      java.lang.String, boolean)
     */
    public void lock(String pUserLogin, LockType pLockType,
            String pSessionToken, String pValuesContainerId, boolean pIsAdmin)
        throws LockException {
        org.topcased.gpm.domain.fields.Lock lLockEntity =
                lockDao.getLock(pValuesContainerId);

        if (pLockType == null) {
            // If no lock, nothing to do
            if (lLockEntity != null) {
                // Check if current user is admin or currently owns the lock
                if (pIsAdmin
                        || StringUtils.equals(pUserLogin,
                                lLockEntity.getOwner())) {
                    // Remove the lock
                    lockDao.remove(lLockEntity);
                }
                else {
                    throw new LockException("Locked by "
                            + lLockEntity.getOwner(), pValuesContainerId);
                }
            }
        }
        else {
            // If no lock, nothing to do
            if (lLockEntity == null) {
                // Create the lock
                lLockEntity = org.topcased.gpm.domain.fields.Lock.newInstance();

                lLockEntity.setOwner(pUserLogin);
                lLockEntity.setLockType(pLockType);
                lLockEntity.setSessionToken(pSessionToken);
                lLockEntity.setContainerId(pValuesContainerId);

                lockDao.create(lLockEntity);
            }
            else {
                // Check if current user is admin or currently owns the lock
                if (pIsAdmin
                        || StringUtils.equals(pUserLogin,
                                lLockEntity.getOwner())) {
                    // Update the lock
                    lLockEntity.setOwner(pUserLogin);
                    lLockEntity.setLockType(pLockType);
                    lLockEntity.setSessionToken(pSessionToken);
                }
                else {
                    throw new LockException("Locked by "
                            + lLockEntity.getOwner(), pValuesContainerId);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.atomic.service.AtomicActionsManager#getSheetLock(java.lang.String)
     */
    public Lock getSheetLock(final String pSheetId) {
        return getLock(pSheetId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.atomic.service.AtomicActionsManager#getLock(java.lang.String)
     */
    public Lock getLock(String pValuesContainerId) {
        final org.topcased.gpm.domain.fields.Lock lEntityLock =
                lockDao.getLock(pValuesContainerId);

        if (lEntityLock == null) {
            return null;
        }
        else {
            return Lock.createLock(lEntityLock);
        }
    }
}