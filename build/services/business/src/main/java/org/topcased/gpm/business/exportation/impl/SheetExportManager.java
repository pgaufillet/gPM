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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.authorization.service.AccessControlContextData;
import org.topcased.gpm.business.exportation.ExportProperties;
import org.topcased.gpm.business.exportation.ExportProperties.ExportFlag;
import org.topcased.gpm.business.fields.impl.CacheableValuesContainer;
import org.topcased.gpm.business.revision.RevisionSummaryData;
import org.topcased.gpm.business.revision.impl.CacheableRevision;
import org.topcased.gpm.business.revision.impl.RevisionServiceImpl;
import org.topcased.gpm.business.serialization.data.FieldValueData;
import org.topcased.gpm.business.serialization.data.RevisionData;
import org.topcased.gpm.business.serialization.data.SheetData;
import org.topcased.gpm.business.serialization.data.TransitionHistoryData;
import org.topcased.gpm.domain.sheet.Sheet;
import org.topcased.gpm.domain.sheet.SheetDao;
import org.topcased.gpm.util.lang.CopyUtils;
import org.topcased.gpm.util.proxy.ImmutableGpmObject;

/**
 * Manager used to export sheets.
 * 
 * @author tpanuel
 */
public class SheetExportManager extends
        AbstractValuesContainerExportManager<SheetData> {
    private RevisionServiceImpl revisionServiceImpl;

    private SheetDao sheetDao;

    /**
     * Create a sheet export manager.
     */
    public SheetExportManager() {
        super("sheets");
    }

    /**
     * Setter for spring injection.
     * 
     * @param pRevisionServiceImpl
     *            The service implementation.
     */
    public void setRevisionServiceImpl(
            final RevisionServiceImpl pRevisionServiceImpl) {
        revisionServiceImpl = pRevisionServiceImpl;
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
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#updateSerializableObject(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected void updateSerializableObject(final String pRoleToken,
            final SheetData pContainer, final ExportProperties pExportProperties) {
        super.updateSerializableObject(pRoleToken, pContainer,
                pExportProperties);
        // If necessary export the revisions
        if (pExportProperties.isExportRevision()) {
            final List<RevisionSummaryData> lRevSummaries =
                    revisionServiceImpl.getRevisionsSummary(pContainer.getId());

            if (!lRevSummaries.isEmpty()) {
                final List<RevisionData> lRevisions =
                        new ArrayList<RevisionData>();

                for (RevisionSummaryData lSummary : lRevSummaries) {
                    final RevisionData lRevisionData = new RevisionData();

                    // get MUTABLE CacheableRevision
                    CacheableRevision lCacheableRevision =
                            revisionServiceImpl.getCacheableRevision(
                                    pContainer.getId(), lSummary.getId());
                    if (lCacheableRevision instanceof ImmutableGpmObject) {
                        lCacheableRevision =
                                CopyUtils.getMutableCopy(lCacheableRevision);
                    }

                    lCacheableRevision.marshal(lRevisionData);
                    lRevisions.add(lRevisionData);
                }
                pContainer.setRevisions(lRevisions);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#getAccessControlContextData(java.lang.String,
     *      org.topcased.gpm.business.serialization.data.ValuesContainerData)
     */
    @Override
    protected AccessControlContextData getAccessControlContextData(
            final String pRoleToken, final SheetData pSerializedObject) {
        final AccessControlContextData lContext =
                super.getAccessControlContextData(pRoleToken, pSerializedObject);

        lContext.setStateName(pSerializedObject.getState());

        return lContext;
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
        return sheetDao.sheetsIterator(
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
        return sheetDao.exist(pId) && (sheetDao.load(pId) instanceof Sheet);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getExportFlag(org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected ExportFlag getExportFlag(final ExportProperties pExportProperties) {
        return pExportProperties.getSheetsFlag();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractValuesContainerExportManager#marshal(org.topcased.gpm.business.fields.impl.CacheableValuesContainer,
     *      org.topcased.gpm.business.exportation.ExportProperties)
     */
    @Override
    protected SheetData marshal(final CacheableValuesContainer pCacheable,
            final ExportProperties pExportProperties) {
        final SheetData lSheetData = new SheetData();

        pCacheable.marshal(lSheetData);
        escapeNewLineInFieldValue(lSheetData);

        if (ReadProperties.getInstance().isObfuscated()) {
            //obfuscate sheet product name and reference
            if (ReadProperties.getInstance().isObfProducts()) {
                lSheetData.setProductName(ExportationData.getInstance().getProductNames().get(
                        lSheetData.getProductName()));
                lSheetData.setReference(replaceReference(lSheetData));
            }

            //user & date & products obfuscation      
            for (FieldValueData lFieldValueData : lSheetData.getFieldValues()) {
                final String lValue = lFieldValueData.getValue();
                if (lFieldValueData.getFieldValues() != null) {
                    for (FieldValueData lSubFieldValue : lFieldValueData.getFieldValues()) {
                        if (isObfuscableSubField(lSubFieldValue)) {
                            obfuscateFields(lSubFieldValue);
                        }
                    }
                }
                if (!StringUtils.isEmpty(lValue)) {
                    boolean lIsModified = false;
                    if (!lIsModified) {
                        // Change User value inside field value
                        lIsModified = setExportedUser(lFieldValueData, lValue);
                    }
                    if (!lIsModified) {
                        // Change Product value inside field value
                        lIsModified =
                                setExportedProduct(lFieldValueData, lValue);
                    }
                    if (!lIsModified
                            && ReadProperties.getInstance().isObfSheets()) {
                        // Change date value inside field value
                        lIsModified = setExportedDate(lFieldValueData);

                        if (!lIsModified && isObfuscable(lFieldValueData)) {
                            obfuscateFields(lFieldValueData);
                        }
                    }
                }
            }

            if (lSheetData.getReferenceValues() != null) {
                for (FieldValueData lFieldValueData : lSheetData.getReferenceValues()) {
                    final String lValue = lFieldValueData.getValue();
                    setExportedUser(lFieldValueData, lValue);
                    setExportedProduct(lFieldValueData, lValue);
                }
            }

            if (lSheetData.getTransitionsHistory() != null) {
                for (TransitionHistoryData lTransitionHistoryData : lSheetData.getTransitionsHistory()) {
                    //handle Date in transitionData
                    if (ReadProperties.getInstance().isObfSheets()
                            && lTransitionHistoryData.getTransitionDate() != null) {
                        Date lTransitionDate =
                                lTransitionHistoryData.getTransitionDate();
                        lTransitionDate.setDate(lTransitionDate.getDate()
                                + ReadProperties.getInstance().getRandomNumber());
                        lTransitionHistoryData.setTransitionDate(lTransitionDate);
                    }
                    //handle user login in transitionData
                    final String lObfUserLogin =
                            ExportationData.getInstance().getUserLogin().get(
                                    lTransitionHistoryData.getLogin());
                    if ((lObfUserLogin != null)
                            && (ReadProperties.getInstance().isObfUsers())) {
                        lTransitionHistoryData.setLogin(lObfUserLogin);
                    }
                }
            }
        }
        return lSheetData;
    }

    /**
     * Check if a subfield is obfuscable and use recursivity
     * 
     * @param pSubFieldValue
     *            the SubFieldValue
     * @return true if the subfield is obfuscable
     */
    private boolean isObfuscableSubField(FieldValueData pSubFieldValue) {
        return !isDate(pSubFieldValue)
                && !isDateISO(pSubFieldValue)
                && !StringUtils.isNumeric(pSubFieldValue.getValue())
                && !ExportationData.getInstance().getCategoryValue().contains(
                        pSubFieldValue.getValue())
                && !isDecimal(pSubFieldValue.getValue());
    }

    /**
     * Check if a fieldValueData is a product and replace it by the obfuscated
     * one
     * 
     * @param lFieldValueData
     *            the fieldValueData
     * @param lValue
     *            the fieldValueData value
     * @return true if the fieldValueData is a product
     */
    private boolean setExportedProduct(FieldValueData lFieldValueData,
            final String lValue) {
        boolean lIsModified = false;
        if (ReadProperties.getInstance().isObfProducts()) {
            final String lObfuscatedProductValue =
                    ExportationData.getInstance().getProductNames().get(lValue);
            if ((lObfuscatedProductValue != null)) {
                lFieldValueData.setValue(lObfuscatedProductValue);
                lIsModified = true;
            }
        }
        return lIsModified;
    }

    /**
     * Check if a fieldValueData value is an user and replace it by the
     * obfuscated one
     * 
     * @param lFieldValueData
     *            the fieldValueData
     * @param lValue
     *            the fieldValueData value
     * @return true if fieldValueData is an user
     */
    private boolean setExportedUser(FieldValueData lFieldValueData,
            final String lValue) {
        boolean lIsModified = false;
        if (ReadProperties.getInstance().isObfUsers()) {
            final String lObfuscatedUserValue =
                    ExportationData.getInstance().getUserName().get(lValue);
            if ((lObfuscatedUserValue != null)) {
                lFieldValueData.setValue(lObfuscatedUserValue);
                lIsModified = true;
            }

        }
        return lIsModified;
    }

    /**
     * Check if a fieldValueData is a date and replace it by the date of the day
     * 
     * @param pFieldValueData
     *            the fieldValueData
     * @return true if the fieldValueData value is a date
     */
    private boolean setExportedDate(final FieldValueData pFieldValueData) {

        boolean lIsModified = false;

        lIsModified = generateDate(pFieldValueData);

        // Escape new line
        if (!lIsModified && pFieldValueData.getFieldValues() != null) {
            for (FieldValueData lSubFieldValueData : pFieldValueData.getFieldValues()) {
                lIsModified = generateDate(lSubFieldValueData);
                if (lIsModified) {
                    continue;
                }
            }
        }

        return lIsModified;
    }

    /**
     * Check if a sub fieldValueData is a date and generate it according to the
     * correct format
     * 
     * @param pSubFieldValueData
     *            the SubfieldValueData
     * @return true if the sub fieldValueData is a date
     */
    private boolean generateDate(FieldValueData pSubFieldValueData) {
        boolean lIsModified = false;

        if ((isDateISO(pSubFieldValueData))
                && (ReadProperties.getInstance().isObfSheets())) {
            lIsModified = true;
            String lDateISO =
                    getDate("yyyy-MM-dd'T'HH:mm:ss", pSubFieldValueData);
            pSubFieldValueData.setValue(lDateISO);
        }
        if (isDate(pSubFieldValueData)) {
            lIsModified = true;
            String lDate = getDate("yyyy-MM-dd", pSubFieldValueData);
            pSubFieldValueData.setValue(lDate);
        }
        return lIsModified;
    }

    /**
     * Return the date of the day according to the given format
     * 
     * @param pFormat
     *            the Date format
     * @return the date of the day with the correct format
     */
    private String getDate(String pFormat, FieldValueData pFieldValueData) {
        Date lDate = new Date();
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(pFormat);
        try {
            lDate = lSimpleDateFormat.parse(pFieldValueData.getValue());
            lDate.setDate(lDate.getDate()
                    + ReadProperties.getInstance().getRandomNumber());
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return lSimpleDateFormat.format(lDate);
    }

    /**
     * Obfuscate subfields for a given FieldValueData
     * 
     * @param pFieldValueData
     *            the fieldValueData to check and obfuscate
     */
    private void obfuscateFields(final FieldValueData pFieldValueData) {
        if (pFieldValueData.getFieldValues() == null) {
            pFieldValueData.setValue(LaunderContent.launderData(pFieldValueData.getValue()));
        }
        else {
            for (FieldValueData lSubFieldValueData : pFieldValueData.getFieldValues()) {
                obfuscateFields(lSubFieldValueData);
            }
        }
    }

    /**
     * Determine if the given field value data is obfuscable or not
     * 
     * @param lFieldValueData
     *            the field value data to test
     * @return true if it is obfuscable
     */
    private boolean isObfuscable(final FieldValueData lFieldValueData) {
        final String lValue = lFieldValueData.getValue();
        return (lValue != null
                && ReadProperties.getInstance().isObfSheets()
                && (!((Boolean.TRUE.toString().equals(lValue))))
                && (!((Boolean.FALSE.toString().equals(lValue))))
                && (!(ExportationData.getInstance().getUserName().containsValue(lValue)))
                && (!(ExportationData.getInstance().getProductNames().containsValue(lValue)))
                && (!(ExportationData.getInstance().getProductType().containsValue(lValue)))
                && (!(ExportationData.getInstance().getSheetType().containsValue(lValue)))
                && (!(isDate(lFieldValueData)))
                && (!(ExportationData.getInstance().getCategoryValue().contains(lValue)))
                && (!StringUtils.isNumeric(lFieldValueData.getValue())) && (!isDecimal(lFieldValueData.getValue())));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.exportation.impl.AbstractExportManager#getProductNames(java.lang.String)
     */
    @Override
    protected List<String> getProductNames(final String pElementId) {
        return Collections.singletonList(sheetDao.load(pElementId).getProduct().getName());
    }

    /**
     * Browse the SheetData and encode new line character This make values
     * registered with "\n" as new line to be encoded as well as those which
     * using "\r\n"
     * 
     * @param pSheetData
     */
    private void escapeNewLineInFieldValue(SheetData pSheetData) {
        if (null != pSheetData && null != pSheetData.getFieldValues()) {
            for (FieldValueData lData : pSheetData.getFieldValues()) {
                escapeNewLineInSubFieldValue(lData);
            }
        }

    }

    /**
     * Perform Newline in sub field
     * 
     * @param pData
     */
    private void escapeNewLineInSubFieldValue(FieldValueData pData) {
        if (null != pData) {
            pData.setValue(org.topcased.gpm.util.lang.StringUtils.escapeNewLine(pData.getValue()));
            if (null != pData.getFieldValues()) {
                for (FieldValueData lCurrentData : pData.getFieldValues()) {
                    escapeNewLineInSubFieldValue(lCurrentData);
                }
            }
        }
    }

    /**
     * Check if the fieldvalue data content is a date
     * 
     * @param pFieldValueData
     *            FieldValueData
     * @return boolean
     */
    private boolean isDate(final FieldValueData pFieldValueData) {
        return ((pFieldValueData.getValue() != null)
                && (pFieldValueData.getValue().length() > 8)
                && (("-").equals(pFieldValueData.getValue().substring(4, 5))) && (("-").equals(pFieldValueData.getValue().substring(
                7, 8))));
    }

    /**
     * Check if the fieldvalue data content is a date (ISO format)
     * 
     * @param pFieldValueData
     *            FieldValueData
     * @return boolean
     */
    private boolean isDateISO(final FieldValueData pFieldValueData) {
        return isDate(pFieldValueData)
                && (pFieldValueData.getValue().length() > 11)
                && (("T").equals(pFieldValueData.getValue().substring(10, 11)));
    }

    /**
     * Replace reference in sheet header (in the xml file). This code is
     * specific to instance, a better way to manage reference should be used.
     * 
     * @param pSheetData
     *            SheetData
     * @return String sheet reference
     */
    private String replaceReference(final SheetData pSheetData) {
        final StringBuilder lSheetRefBuilder = new StringBuilder();

        final String lSheetProduct =
                StringUtils.substringBefore(pSheetData.getReference(), "-");
        final String lProductName =
                ExportationData.getInstance().getProductNames().get(
                        lSheetProduct);
        if (lProductName != null) {
            lSheetRefBuilder.append(lProductName);
            lSheetRefBuilder.append("-");
            lSheetRefBuilder.append(StringUtils.substringAfter(
                    pSheetData.getReference(), "-"));
        }
        return lSheetRefBuilder.toString();
    }

    /**
     * Check if a String is a Decimal value
     * 
     * @param pValue
     *            The value to test
     * @return boolean true if the string matches the pattern
     */
    private boolean isDecimal(final String pValue) {
        return ExportationConstants.DECIMAL_PATTERN.matcher(pValue).matches();
    }
}
