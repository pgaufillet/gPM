/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.SheetLink;
import org.topcased.gpm.domain.link.Link;
import org.topcased.gpm.domain.link.LinkDao;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetDao;

/**
 * Manager used to export sheet links.
 * 
 * @author tpanuel
 */
public class SheetLinkExportManager extends
        AbstractValuesContainerExportManager<SheetLink> {
    private LinkDao linkDao;

    private SheetDao sheetDao;

    /**
     * Create a sheet link export manager.
     */
    public SheetLinkExportManager() {
        super("sheetLinks");
    }

    /**
     * Setter for spring injection.
     * 
     * @param pSheetDao
     *            The DAO.
     */
    public void setSheetDao(final SheetDao pSheetDao) {
        sheetDao = pSheetDao;
    }

    /**
     * Setter for spring injection.
     * 
     * @param pLinkDao
     *            The DAO.
     */
    public void setLinkDao(final LinkDao pLinkDao) {
        linkDao = pLinkDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#getAllElementsId(java.lang.String,
     *      java.util.Collection, java.util.Collection)
     */
    @Override
    protected Iterator<String> getAllElementsId(final String pRoleToken,
            final Collection<String> pProductsName,
            final Collection<String> pTypesName) {
        return linkDao.sheetLinksIterator(
                authorizationServiceImpl.getProcessName(pRoleToken),
                pProductsName, pTypesName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#isValidIdentifier(java.lang.String)
     */
    @Override
    protected boolean isValidIdentifier(final String pId) {
        return linkDao.isSheetLink(pId);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getSheetLinksFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#marshal(org.topcased.gpm.business.fields.impl.CacheableValuesContainer,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected SheetLink marshal(final CacheableValuesContainer pCacheable,
            final ExportProperties pExportProperties) {
        final SheetLink lSheetLink = new SheetLink();

        pCacheable.marshal(lSheetLink);

        // Look for UserName/Login and replace it by the obfuscated one
        // Same operation for Product name
        // Operation is performed in FieldValueData and SubFieldValueData
        for (FieldValueData lFieldValueData : getNonNullList(lSheetLink.getFieldValues())) {
            replaceUserNameLogin(lFieldValueData);
            replaceProductName(lFieldValueData);
            for (FieldValueData lSubFieldValueData : getNonNullList(lFieldValueData.getFieldValues())) {
                replaceUserNameLogin(lSubFieldValueData);
                replaceProductName(lSubFieldValueData);
            }
        }

        // If no export UID
        if (!pExportProperties.isExportUID()) {
            final Sheet lSheetOrigin = sheetDao.load(lSheetLink.getOriginId());
            final Sheet lSheetDestination =
                    sheetDao.load(lSheetLink.getDestinationId());

            // Replace product id by product name
            lSheetLink.setOriginId(null);
            lSheetLink.setDestinationId(null);
            lSheetLink.setOriginReference(lSheetOrigin.getReference());
            lSheetLink.setDestinationReference(lSheetDestination.getReference());

            if (ReadProperties.getInstance().isObfProducts()) {
                //Replace Sheet Product name by the obfuscated one
                String lObfOriginProductName =
                        getObfuscatedSheetProductName(lSheetOrigin);
                String lObfDestProductName =
                        getObfuscatedSheetProductName(lSheetDestination);
                if (lObfOriginProductName != null
                        && lObfDestProductName != null) {
                    lSheetLink.setOriginProductName(lObfOriginProductName);
                    lSheetLink.setDestinationProductName(lObfDestProductName);
                }
            }
            else {
                lSheetLink.setOriginProductName(lSheetOrigin.getProduct().getName());
                lSheetLink.setDestinationProductName(lSheetDestination.getProduct().getName());
            }

            // Evict for limit hibernate session size
            sheetDao.evict(lSheetOrigin);
            sheetDao.evict(lSheetDestination);
        }

        return lSheetLink;
    }

    /**
     * Check if a FieldValueData is a product name and replace it by the
     * obfuscated one
     * 
     * @param lFieldValueData
     *            containing the obfuscated product
     */
    private void replaceProductName(FieldValueData lFieldValueData) {
        if (ReadProperties.getInstance().isObfProducts()) {
            String lObfProduct =
                    ExportationData.getInstance().getProductNames().get(
                            lFieldValueData.getValue());
            if (lObfProduct != null) {
                lFieldValueData.setValue(lObfProduct);
            }
        }
    }

    /**
     * Check if a FieldValueData is a userName/Login and replace it by the
     * obfuscated one
     * 
     * @param lFieldValueData
     *            containing the obfuscated userName/Login
     */
    private void replaceUserNameLogin(FieldValueData lFieldValueData) {
        if (ReadProperties.getInstance().isObfUsers()) {
            String lObfUserNameLogin =
                    getObfuscateUserNameLogin(lFieldValueData.getValue());
            if (!lObfUserNameLogin.isEmpty()) {
                lFieldValueData.setValue(lObfUserNameLogin);
            }
        }
    }

    /**
     * Check if a List is null to avoid to check if a list is null each time
     * 
     * @param pFieldValuesData
     *            the List to test
     * @return an empty collection if the list is null, or pFieldValuesData if
     *         the list is not null
     */
    private List<FieldValueData> getNonNullList(
            List<FieldValueData> pFieldValuesData) {
        if (pFieldValuesData == null) {
            return Collections.emptyList();
        }
        return pFieldValuesData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    @Override
    protected List<String> getProductNames(final String pElementId) {
        final Link lLink = linkDao.load(pElementId);
        final List<String> lProductNames = new ArrayList<String>();

        lProductNames.add(((Sheet) lLink.getOrigin()).getProduct().getName());
        lProductNames.add(((Sheet) lLink.getDestination()).getProduct().getName());

        return lProductNames;
    }

    /**
     * Replace a given user login by a new obfuscated login
     * 
     * @param pUserLogin
     *            the user login
     * @return the user login obfuscated
     */
    private String getObfuscateUserNameLogin(String pUserLogin) {
        StringBuilder lUserLoginFinal = new StringBuilder();
        if (StringUtils.contains(pUserLogin, "(")
                && StringUtils.contains(pUserLogin, ")")) {
            // Get the Obfuscated user name
            String lUserName = StringUtils.substringBefore(pUserLogin, " (");
            String lObfUserName =
                    ExportationData.getInstance().getUserName().get(lUserName);
            // Get the obfuscated login
            String lLogin = StringUtils.substringBetween(pUserLogin, "(", ")");
            String lObfLogin =
                    ExportationData.getInstance().getUserLogin().get(lLogin);

            // Create the obfuscated user name login
            if (lObfUserName != null && lObfLogin != null) {
                lUserLoginFinal.append(lObfUserName);
                lUserLoginFinal.append(" (");
                lUserLoginFinal.append(lObfLogin);
                lUserLoginFinal.append(")");
            }
        }
        return lUserLoginFinal.toString();
    }

    /**
     * Retrieve the obfuscated sheet product
     * 
     * @param pSheet
     *            The sheet
     * @return the obfuscated product name corresponding to the sheet
     */
    private String getObfuscatedSheetProductName(final Sheet pSheet) {
        return ExportationData.getInstance().getProductNames().get(
                pSheet.getProduct().getName());
    }
}