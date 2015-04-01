/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.attributes.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.business.exception.InvalidIdentifierException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.domain.attributes.Attribute;
import org.topcased.gpm.domain.attributes.AttributeDao;
import org.topcased.gpm.domain.attributes.AttributeValue;
import org.topcased.gpm.domain.attributes.AttributeValueDao;
import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.attributes.AttributesContainerDao;
import org.topcased.gpm.domain.attributes.GpmDao;
import org.topcased.gpm.util.bean.CacheProperties;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Attributes service implementation.
 * 
 * @author llatil
 */
public class AttributesServiceImpl extends ServiceImplBase implements
        AttributesService {

    final static AttributeData[] EMPTY_ATTRIBUTES = new AttributeData[0];

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#get(java.lang.String[])
     */
    public AttributeData[] get(final String pElemId, final String[] pAttrNames) {
        if (pAttrNames.length == 0) {
            return EMPTY_ATTRIBUTES;
        }
        CacheableAttributesContainer lCachedAttrsContainer;

        // Use the cached attributes when available.
        lCachedAttrsContainer =
                getCachedElement(CacheableAttributesContainer.class, pElemId,
                        CACHE_IMMUTABLE_OBJECT);

        if (null != lCachedAttrsContainer) {
            return getFromCache(lCachedAttrsContainer, pAttrNames);
        }
        // else
        return getFromDb(pElemId, pAttrNames);
    }

    @SuppressWarnings("unchecked")
    private AttributeData[] getFromDb(final String pElemId,
            final String[] pAttrNames) {
        AttributeData[] lAttrsData = new AttributeData[pAttrNames.length];
        AttributesContainer lContainer = getAttrContainer(pElemId);

        if (pAttrNames.length == 1) {
            lAttrsData[0] =
                    createAttributeData(attributesContainerDao.getAttribute(
                            lContainer, pAttrNames[0]));
        }
        else {
            List<Attribute> lAttrs =
                    attributesContainerDao.getAttributes(lContainer, pAttrNames);
            fillAttributeData(lAttrsData, lAttrs);
        }
        return lAttrsData;
    }

    private AttributeData[] getFromCache(
            CacheableAttributesContainer pContainer, String[] pAttrNames) {
        AttributeData[] lAttrsData = new AttributeData[pAttrNames.length];
        int i = 0;
        for (String lAttrName : pAttrNames) {
            String[] lValues = pContainer.getAttributeValues(lAttrName);
            if (null == lValues) {
                lAttrsData[i] = null;
            }
            else {
                lAttrsData[i] = new AttributeData(lAttrName, lValues);
            }
            ++i;
        }
        return lAttrsData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#get(java.lang.String[])
     */
    @SuppressWarnings("unchecked")
    public AttributeData[] getAll(final String pElemId) {
        AttributesContainer lContainer = getAttrContainer(pElemId);

        Query lQuery =
                GpmSessionFactory.getHibernateSession().createFilter(
                        lContainer.getAttributes(), "order by this.name");
        Collection<Attribute> lAttrs = lQuery.list();
        AttributeData[] lAttrsData = new AttributeData[lAttrs.size()];

        fillAttributeData(lAttrsData, lAttrs);
        return lAttrsData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#removeAll(java.lang.String)
     */
    public void removeAll(final String pElemId) {
        AttributesContainer lContainer = getAttrContainer(pElemId);
        lContainer.getAttributes().clear();
        removeElementFromCache(pElemId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#getAttrNames(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public String[] getAttrNames(final String pElemId) {
        AttributesContainer lAttrsCont = getAttrContainer(pElemId);
        List<String> lAttrsNames;
        lAttrsNames = attributesContainerDao.getAllAttrNames(lAttrsCont);

        return lAttrsNames.toArray(new String[lAttrsNames.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#set(org.topcased.gpm.business.attributes.AttributeData[])
     */
    public void set(final String pElemId, final AttributeData[] pNewDataArray) {
        AttributesContainer lContainer = getAttrContainer(pElemId);

        if (pNewDataArray == null) {
            return;
        }

        for (AttributeData lNewData : pNewDataArray) {

            // Create or update the attribute
            Attribute lAttribute =
                    attributesContainerDao.getAttribute(lContainer,
                            lNewData.getName());

            // Check if the values array is null or empty (in this case,
            // that means 'remove the attribute').
            if (ArrayUtils.isEmpty(lNewData.getValues())) {
                if (lAttribute != null) {
                    // The attribute exists, we remove it.
                    lContainer.getAttributes().remove(lAttribute);
                }
                continue;
            }

            if (lAttribute == null) {
                // Create a new attribute if it does not exist yet.
                lAttribute = Attribute.newInstance();
                lAttribute.setName(lNewData.getName());
                attributeDao.create(lAttribute);
                lContainer.getAttributes().add(lAttribute);
            }

            // lCommonSize represents the number of value ranks
            // the old attribute and the new one have in common
            List<AttributeValue> lOldValues = lAttribute.getAttributeValues();
            int lCommonSize =
                    Math.min(lOldValues.size(), lNewData.getValues().length);
            for (int i = 0; i < lCommonSize; i++) {
                lOldValues.get(i).setValue(lNewData.getValues()[i]);
            }

            // If there are more new values, add them
            if (lOldValues.size() < lNewData.getValues().length) {
                for (int i = lCommonSize; i < lNewData.getValues().length; i++) {
                    AttributeValue lAttrValue = AttributeValue.newInstance();
                    lAttrValue.setValue(lNewData.getValues()[i]);
                    attributeValueDao.create(lAttrValue);
                    lOldValues.add(lAttrValue);
                }
            }
            else { // or if old values were more numerous, remove them.
                for (int i = lOldValues.size() - 1; i >= lCommonSize; i--) {
                    attributeValueDao.remove(lOldValues.get(i));
                    lOldValues.remove(i);
                }
            }
        }

        // Remove the attributes container from the cache (as attributes list is changed).
        removeElementFromCache(pElemId);
    }

    /**
     * Create/set the attributes for an element.
     * 
     * @param pElemId
     *            Identifier of the element containing the attributes
     * @param pAttrs
     *            Attributes list
     */
    public void set(
            final String pElemId,
            final List<? extends org.topcased.gpm.business.serialization.data.Attribute> pAttrs) {
        if (null == pAttrs) {
            return;
        }
        AttributeData[] lAttrData = getAttributesData(pAttrs);
        set(pElemId, lAttrData);
    }

    /**
     * Get an AttributeData array from a list of Create/set the attributes for
     * an element.
     * 
     * @param pAttrs
     *            Attributes list
     * @return the attributes data
     */
    private AttributeData[] getAttributesData(
            final List<? extends org.topcased.gpm.business.serialization.data.Attribute> pAttrs) {
        if (null == pAttrs) {
            return null;
        }

        AttributeData[] lAttrData = new AttributeData[pAttrs.size()];
        int i = 0;

        for (org.topcased.gpm.business.serialization.data.Attribute lAttr : pAttrs) {
            lAttrData[i] =
                    new AttributeData(lAttr.getName(), lAttr.getValues());
            ++i;
        }
        return lAttrData;
    }

    /**
     * Get an attribute container object from its technical identifier.
     * 
     * @param pIdent
     *            Technical identifier
     * @return The attribute container
     * @throws InvalidIdentifierException
     *             If the identifier is invalid.
     */
    private AttributesContainer getAttrContainer(final String pIdent) {
        return attributesContainerDao.load(pIdent);
    }

    /**
     * Create an AttributeData object from a domain Attribute object.
     * 
     * @param pAttr
     *            Attribute object (domain object)
     * @return The AttributeData object
     */
    private static AttributeData createAttributeData(final Attribute pAttr) {
        if (null == pAttr) {
            return null;
        }
        AttributeData lAttrData = new AttributeData();
        lAttrData.setName(pAttr.getName());

        String[] lValues = new String[pAttr.getAttributeValues().size()];
        int i = 0;

        for (AttributeValue lValue : pAttr.getAttributeValues()) {
            lValues[i] = lValue.getValue();
            if (lValues[i] == null) {
                lValues[i] = new String();
            }
            i++;
        }
        lAttrData.setValues(lValues);
        return lAttrData;
    }

    /**
     * Fill an AttributeData array from a domain Attribute list.
     * 
     * @param pAttrs
     *            Attribute object array (domain object)
     * @param pAttrsData
     *            the attrs data
     * @return The AttributeData object
     */
    private static void fillAttributeData(final AttributeData[] pAttrsData,
            final Collection<Attribute> pAttrs) {
        int i = 0;
        for (Attribute lAttr : pAttrs) {
            pAttrsData[i++] = AttributesUtils.createAttributeData(lAttr);
        }
    }

    /**
     * Get the 'global' attributes container.
     * <p>
     * This container is used to store the global attributes (options) of a gPM
     * server.
     * 
     * @param pProperties
     *            the cache properties
     * @return Global attributes container.
     */
    private synchronized CacheableAttributesContainer getGlobalAttrs(
            CacheProperties pProperties) {
        CacheableAttributesContainer lGlobalAttrs;

        // FIXME all to use immutable object
        String lGpmId = getGpmDao().getGpmId();
        lGlobalAttrs =
                getCachedElement(CacheableAttributesContainer.class, lGpmId,
                        pProperties.getCacheFlags());
        if (null == lGlobalAttrs) {
            lGlobalAttrs =
                    new CacheableAttributesContainer(getGpmDao().getGpmId());
            if (null != lGpmId) {
                addElementInCache(lGlobalAttrs);
            }
        }
        return lGlobalAttrs;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#getGlobalAttrNames()
     */
    public String[] getGlobalAttrNames() {
        Collection<String> lAttrNames =
                getGlobalAttrs(CacheProperties.IMMUTABLE).getAllAttributeNames();

        return lAttrNames.toArray(new String[lAttrNames.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#getGlobalAttributes(java.lang.String[])
     */
    public synchronized AttributeData[] getGlobalAttributes(String[] pAttrNames) {
        return getFromCache(getGlobalAttrs(CacheProperties.IMMUTABLE),
                pAttrNames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#setGlobalAttributes(java.lang.String,
     *      org.topcased.gpm.business.attributes.AttributeData[])
     */
    public synchronized void setGlobalAttributes(String pRoleToken,
            AttributeData[] pAttributesData) {
        // An admin role is required for setting global attributes
        getAuthService().assertGlobalAdminRole(pRoleToken);

        set(getGpmDao().getGpmId(), pAttributesData);

        removeElementFromCache(getGpmDao().getGpmId());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#atomicIncrement(java.lang.String,
     *      java.lang.String, java.lang.String, int)
     */
    public long atomicIncrement(String pRoleToken, String pElemId,
            String pAttrName, long pInitial) {
        String[] lAttributes = new String[] { pAttrName };
        AttributeData[] lAttrValues = getFromDb(pElemId, lAttributes);

        String lCurrentValue = null;

        if (lAttrValues[0] != null && lAttrValues[0].getValues().length != 0) {
            lCurrentValue = lAttrValues[0].getValues()[0];
        }

        long lNewValue;

        while (true) {
            if (lCurrentValue == null) {
                // If the attribute is not set yet, create it to the initial value
                lNewValue = pInitial;
            }
            else {
                try {
                    lNewValue = Long.parseLong(lCurrentValue);
                    lNewValue += 1;
                }
                catch (NumberFormatException e) {
                    // If the current value is not formatted as an integer value, replace its
                    // content with the initial value
                    lNewValue = pInitial;
                }
            }
            String lNewStringValue = Long.toString(lNewValue);

            String lValueBeforeUpdate =
                    atomicTestAndSet(pRoleToken, pElemId, pAttrName,
                            lCurrentValue, lNewStringValue);

            // if attribute is incremented
            if (lValueBeforeUpdate == null && lCurrentValue == null
                    || lValueBeforeUpdate != null && lCurrentValue != null
                    && lValueBeforeUpdate.equals(lCurrentValue)) {
                break;
            }
            else {
                lCurrentValue = lValueBeforeUpdate;
            }
        }

        return lNewValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#atomicTestAndSet(java.lang.String,
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public String atomicTestAndSet(final String pRoleToken,
            final String pElementId, final String pAttributeName,
            final String pOldValue, final String pNewValue) {
        final String lPreviousValue =
                atomicActionsManager.atomicTestAndSet(pRoleToken, pElementId,
                        pAttributeName, pOldValue, pNewValue);

        // If the value has been updated
        if (StringUtils.equals(lPreviousValue, pOldValue)) {
            // Remove cache on attribute container
            removeElementFromCache(pElementId);

            //Refresh a cache hibernate
            final Session lHibernateSession =
                    GpmSessionFactory.getHibernateSession();
            lHibernateSession.flush();
            lHibernateSession.clear();

        }

        return lPreviousValue;
    }

    /** The gpmDao. */
    private GpmDao gpmDao;

    /**
     * getGpmDao.
     * 
     * @return the GpmDao
     */
    @Override
    public GpmDao getGpmDao() {
        return gpmDao;
    }

    /**
     * setGpmDao.
     * 
     * @param pGpmDao
     *            the GpmDao to set
     */
    @Override
    public void setGpmDao(GpmDao pGpmDao) {
        gpmDao = pGpmDao;
    }

    /** The attributes container dao. */
    private AttributesContainerDao attributesContainerDao;

    /**
     * Gets the attributes container dao.
     * 
     * @return the attributes container dao
     */
    public AttributesContainerDao getAttributesContainerDao() {
        return attributesContainerDao;
    }

    /**
     * Sets the attributes container dao.
     * 
     * @param pAttributesContainerDao
     *            the new attributes container dao
     */
    public void setAttributesContainerDao(
            final AttributesContainerDao pAttributesContainerDao) {
        attributesContainerDao = pAttributesContainerDao;
    }

    /** The attribute value dao. */
    private AttributeValueDao attributeValueDao;

    /**
     * Gets the attribute value dao.
     * 
     * @return the attribute value dao
     */
    public AttributeValueDao getAttributeValueDao() {
        return attributeValueDao;
    }

    /**
     * Sets the attribute value dao.
     * 
     * @param pAttributeValueDao
     *            the new attribute value dao
     */
    public void setAttributeValueDao(final AttributeValueDao pAttributeValueDao) {
        attributeValueDao = pAttributeValueDao;
    }

    /** The attribute dao. */
    private AttributeDao attributeDao;

    /**
     * Gets the attribute dao.
     * 
     * @return the attribute dao
     */
    public AttributeDao getAttributeDao() {
        return attributeDao;
    }

    /**
     * Sets the attribute dao.
     * 
     * @param pAttributeDao
     *            the new attribute dao
     */
    public void setAttributeDao(final AttributeDao pAttributeDao) {
        attributeDao = pAttributeDao;
    }

    /**
     * Import element attributes content
     * 
     * @param pRoleToken
     *            The role token
     * @param pElemId
     *            The element ID
     * @param pAttributes
     *            The attributes list
     * @param pCtx
     *            The context
     */
    public void importAttributes(
            final String pRoleToken,
            final String pElemId,
            final List<org.topcased.gpm.business.serialization.data.Attribute> pAttributes,
            final Context pCtx) {

        // Clear attributes
        removeAll(pElemId);

        if (pAttributes != null && !pAttributes.isEmpty()) {
            set(pElemId, pAttributes);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.attributes.service.AttributesService#update(java.lang.String,
     *      org.topcased.gpm.business.attributes.AttributeData[])
     */
    public synchronized void update(String pElemId, AttributeData[] pAttrs) {
        List<AttributeData> lOldAttributes = Arrays.asList(getAll(pElemId));
        List<AttributeData> lNewAttributes =
                new ArrayList<AttributeData>(Arrays.asList(pAttrs));
        List<AttributeData> lObsoleteAttributes =
                new ArrayList<AttributeData>();

        boolean lFound;
        for (AttributeData lOldAttribute : lOldAttributes) {
            lFound = false;
            Iterator<AttributeData> lIt = lNewAttributes.iterator();
            while (lIt.hasNext()) {
                AttributeData lNewAttribute = lIt.next();
                if (lOldAttribute.getName().equals(lNewAttribute.getName())) {
                    lFound = true;
                    if (Arrays.equals(lOldAttribute.getValues(),
                            lNewAttribute.getValues())) {
                        // Attribute has not changed. Discard entry in the 'update' list
                        lIt.remove();
                    }
                    // The attribute was found. Proceed to the next one
                    break;
                }
            }
            // Attribute was not found in the updated list:
            // This attribute is obsolete
            if (!lFound) {
                lObsoleteAttributes.add(lOldAttribute);
            }
        }

        // Remove unused attributes
        AttributesContainer lAttrsCont = getAttrContainer(pElemId);
        for (AttributeData lAttributeData : lObsoleteAttributes) {
            Attribute lAttributeToRemove =
                    attributesContainerDao.getAttribute(lAttrsCont,
                            lAttributeData.getName());
            lAttrsCont.removeFromAttributeList(lAttributeToRemove);
        }

        // Add new attributes
        set(
                pElemId,
                lNewAttributes.toArray(new AttributeData[lNewAttributes.size()]));
        removeElementFromCache(pElemId);
    }

    /**
     * Update attributes of an element.
     * 
     * @param pElemId
     *            Identifier of the attributes
     * @param pAttrs
     *            pAttrs Attributes to define
     */
    public void update(
            String pElemId,
            final List<? extends org.topcased.gpm.business.serialization.data.Attribute> pAttrs) {
        if (null == pAttrs) {
            return;
        }
        AttributeData[] lAttrData = getAttributesData(pAttrs);
        update(pElemId, lAttrData);
    }
}
