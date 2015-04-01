/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Thomas Szadel, pierre Hubert TSAAN
 * (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.display.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceImplBase;
import org.topcased.gpm.business.display.service.DisplayService;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.InvalidNameException;
import org.topcased.gpm.business.exception.InvalidValueException;
import org.topcased.gpm.business.facilities.AttachedDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceFieldDisplayHintData;
import org.topcased.gpm.business.facilities.ChoiceTreeDisplayHintData;
import org.topcased.gpm.business.facilities.DateDisplayHintData;
import org.topcased.gpm.business.facilities.DisplayGroupData;
import org.topcased.gpm.business.facilities.DisplayHintData;
import org.topcased.gpm.business.facilities.GridDisplayHintData;
import org.topcased.gpm.business.facilities.TextFieldDisplayHintData;
import org.topcased.gpm.business.fields.FieldSummaryData;
import org.topcased.gpm.business.fields.JAppletDisplayHintData;
import org.topcased.gpm.business.i18n.service.I18nService;
import org.topcased.gpm.business.util.GridObjectsUtil;
import org.topcased.gpm.domain.facilities.AttachedDisplayHint;
import org.topcased.gpm.domain.facilities.AttachedFieldDisplayType;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayHint;
import org.topcased.gpm.domain.facilities.ChoiceFieldDisplayType;
import org.topcased.gpm.domain.facilities.ChoiceTreeDisplayHint;
import org.topcased.gpm.domain.facilities.DateDisplayHint;
import org.topcased.gpm.domain.facilities.DateDisplayHintDao;
import org.topcased.gpm.domain.facilities.DateDisplayType;
import org.topcased.gpm.domain.facilities.DisplayGroup;
import org.topcased.gpm.domain.facilities.DisplayGroupDao;
import org.topcased.gpm.domain.facilities.DisplayHint;
import org.topcased.gpm.domain.facilities.DisplayHintDao;
import org.topcased.gpm.domain.facilities.FieldDisplayHint;
import org.topcased.gpm.domain.facilities.GridDisplayHint;
import org.topcased.gpm.domain.facilities.GridDisplayHintDao;
import org.topcased.gpm.domain.facilities.JAppletDisplayHint;
import org.topcased.gpm.domain.facilities.TextFieldDisplayHint;
import org.topcased.gpm.domain.facilities.TextFieldDisplayHintDao;
import org.topcased.gpm.domain.facilities.TextFieldDisplayType;
import org.topcased.gpm.domain.fields.AttachedField;
import org.topcased.gpm.domain.fields.ChoiceField;
import org.topcased.gpm.domain.fields.ChoiceFieldDao;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldType;
import org.topcased.gpm.domain.fields.FieldsContainer;
import org.topcased.gpm.domain.fields.SimpleField;
import org.topcased.gpm.domain.fields.SimpleFieldDao;

/**
 * Implementation of DisplayGroupService.
 * 
 * @author tszadel
 * @author mkargbo
 */
public class DisplayServiceImpl extends ServiceImplBase implements
        DisplayService {

    /** The display group DAO. */
    private DisplayGroupDao displayGroupDao;

    /** The I18nService. */
    private I18nService i18nService;

    /**
     * Gets the display group list.
     * 
     * @param pUserToken
     *            The user Token.
     * @param pTypeId
     *            The associated container type.
     * @return List of display group.
     */
    public List<DisplayGroupData> getDisplayGroupList(String pUserToken,
            String pTypeId) {
        i18nService = getI18nService();

        // First, retrieve the fieldsContainer
        FieldsContainer lType =
                (FieldsContainer) getFieldsContainerDao().load(pTypeId);
        if (lType == null) {
            throw new GDMException("The type id " + pTypeId + " doesn't exist");
        }

        List<DisplayGroupData> lListDisplayGroupData =
                new ArrayList<DisplayGroupData>();

        String lPrefLanguage = i18nService.getPreferredLanguage(pUserToken);

        // Gets the displayGroups
        List<DisplayGroup> lDisplayGroupList =
                displayGroupDao.getDisplayGroup(lType);

        // Constructing the groups
        if (lDisplayGroupList != null) {
            for (DisplayGroup lDisplayGroup : lDisplayGroupList) {
                DisplayGroupData lDisplayGroupData = new DisplayGroupData();
                lDisplayGroupData.setLabelKey(lDisplayGroup.getName());
                lDisplayGroupData.setI18nName(i18nService.getValue(
                        lDisplayGroup.getName(), lPrefLanguage));

                List<FieldSummaryData> lFieldSummaryDataList =
                        new ArrayList<FieldSummaryData>();

                for (Field lField : lDisplayGroup.getFields()) {
                    FieldSummaryData lFieldSummaryData = new FieldSummaryData();
                    lFieldSummaryData.setLabelKey(lField.getLabelKey());
                    lFieldSummaryData.setI18nName(i18nService.getValue(
                            lField.getLabelKey(), lPrefLanguage));

                    // Not useful in this context.
                    lFieldSummaryData.setValue(null);
                    lFieldSummaryDataList.add(lFieldSummaryData);
                }
                int lSummaryCount = lFieldSummaryDataList.size();
                FieldSummaryData[] lFieldSummaryDataArray =
                        lFieldSummaryDataList.toArray(new FieldSummaryData[lSummaryCount]);
                lDisplayGroupData.setFieldSummaryDatas(lFieldSummaryDataArray);
                lListDisplayGroupData.add(lDisplayGroupData);
            }
        }
        return lListDisplayGroupData;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.fields.service.FieldsService#
     *      createDisplayGroup(org.topcased.gpm.business.facilities.DisplayGroupData)
     */
    public String createDisplayGroup(DisplayGroupData pDisplayGroupData) {

        if (pDisplayGroupData.getLabelKey() == null) {
            throw new InvalidNameException(pDisplayGroupData.getLabelKey());
        }

        if (pDisplayGroupData.getContainerId() == null) {
            throw new InvalidValueException(pDisplayGroupData.getContainerId()
                    + " for container ID.");
        }

        FieldsContainer lContainer =
                getFieldsContainer(pDisplayGroupData.getContainerId());
        boolean lCreate = false;

        DisplayGroup lDisplayGroup;
        lDisplayGroup =
                displayGroupDao.getDisplayGroup(lContainer,
                        pDisplayGroupData.getLabelKey());

        if (null == lDisplayGroup) {
            lDisplayGroup = DisplayGroup.newInstance();
            lDisplayGroup.setContainer(lContainer);
            lDisplayGroup.setName(pDisplayGroupData.getLabelKey());
            lDisplayGroup.setOpened(pDisplayGroupData.isOpened());

            lCreate = true;
        }

        if (lDisplayGroup.getDisplayOrder() != pDisplayGroupData.getDisplayOrder()) {
            lDisplayGroup.setDisplayOrder(pDisplayGroupData.getDisplayOrder());
        }

        // Clear the list of fields
        lDisplayGroup.getFields().clear();

        if (pDisplayGroupData.getFieldSummaryDatas() != null) {
            // Recreate the fields list.
            for (FieldSummaryData lFieldSummary : pDisplayGroupData.getFieldSummaryDatas()) {
                Field lField =
                        getFieldsManager().getField(lContainer,
                                lFieldSummary.getLabelKey());
                lDisplayGroup.addToFieldList(lField);
            }
        }

        if (lCreate) {
            getDisplayGroupDao().create(lDisplayGroup);
        }
        removeElementFromCache(pDisplayGroupData.getContainerId(), true);
        return lDisplayGroup.getId();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#removeDisplayGroup(java.lang.String,
     *      java.lang.String)
     */
    public void removeDisplayGroup(String pLabelKey, String pContainerId) {
        FieldsContainer lContainer = getFieldsContainer(pContainerId);

        DisplayGroup lDisplayGroup =
                displayGroupDao.getDisplayGroup(lContainer, pLabelKey);
        getDisplayGroupDao().remove(lDisplayGroup);

        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService
     *      #setTextFieldDisplayHint(java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.facilities.TextFieldDisplayHintData)
     */
    public void setTextFieldDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, TextFieldDisplayHintData pHint) {
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);
        Field lField = fieldsManager.getField(lContainer, pFieldName);

        if (!(lField instanceof SimpleField)
                || ((SimpleField) lField).getType() != FieldType.STRING) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        SimpleField lSimpleField = (SimpleField) lField;

        TextFieldDisplayHint lFieldDisplayHint;
        lFieldDisplayHint =
                (TextFieldDisplayHint) lSimpleField.getFieldDisplayHint();
        TextFieldDisplayType lDisplayType = TextFieldDisplayType.SINGLE_LINE;
        if (pHint.getDisplayType() != null) {
            lDisplayType =
                    TextFieldDisplayType.fromString(pHint.getDisplayType());
        }

        if (lFieldDisplayHint != null) {
            lFieldDisplayHint.setHeight(pHint.getHeight());
            lFieldDisplayHint.setWidth(pHint.getWidth());
            lFieldDisplayHint.setDisplay(lDisplayType);
        }
        else {
            lFieldDisplayHint = TextFieldDisplayHint.newInstance();

            lFieldDisplayHint.setHeight(pHint.getHeight());
            lFieldDisplayHint.setWidth(pHint.getWidth());
            lFieldDisplayHint.setDisplay(lDisplayType);

            lSimpleField.setFieldDisplayHint(lFieldDisplayHint);
        }
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#setAttachedDisplayHint(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.facilities.AttachedDisplayHintData)
     */
    public void setAttachedDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, AttachedDisplayHintData pHint) {
        FieldsContainer lContainer = getFieldsContainerDao().load(pContainerId);
        Field lField = fieldsManager.getField(lContainer, pFieldName);

        if (!(lField instanceof AttachedField)) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        AttachedField lAttachedField = (AttachedField) lField;

        AttachedDisplayHint lFieldDisplayHint;
        lFieldDisplayHint =
                (AttachedDisplayHint) lAttachedField.getFieldDisplayHint();
        AttachedFieldDisplayType lDisplayType = null;
        if (pHint.getDisplayType() != null) {
            lDisplayType =
                    AttachedFieldDisplayType.fromString(pHint.getDisplayType());
        }

        if (lFieldDisplayHint != null) {
            lFieldDisplayHint.setHeight(pHint.getHeight());
            lFieldDisplayHint.setWidth(pHint.getWidth());
            lFieldDisplayHint.setDisplayType(lDisplayType);
        }
        else {
            lFieldDisplayHint = AttachedDisplayHint.newInstance();

            lFieldDisplayHint.setHeight(pHint.getHeight());
            lFieldDisplayHint.setWidth(pHint.getWidth());
            lFieldDisplayHint.setDisplayType(lDisplayType);

            lAttachedField.setFieldDisplayHint(lFieldDisplayHint);
        }
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#setDateDisplayHint(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.facilities.DateDisplayHintData)
     */
    public void setDateDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, DateDisplayHintData pHint) {
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);

        Field lField = fieldsManager.getField(lContainer, pFieldName);
        if (null == lField || !(lField instanceof SimpleField)
                || ((SimpleField) lField).getType() != FieldType.DATE) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        SimpleField lSimpleField = (SimpleField) lField;

        DateDisplayHint lFieldDisplayHint;
        lFieldDisplayHint =
                (DateDisplayHint) lSimpleField.getFieldDisplayHint();

        DateDisplayType lDisplayType = DateDisplayType.SHORT;
        if (pHint.getFormat() != null) {
            lDisplayType = DateDisplayType.fromString(pHint.getFormat());
        }

        if (lFieldDisplayHint != null) {
            lFieldDisplayHint.setFormat(lDisplayType);
            lFieldDisplayHint.setIncludeTime(pHint.isTimeIncluded());
        }
        else {
            lFieldDisplayHint = DateDisplayHint.newInstance();
            lFieldDisplayHint.setIncludeTime(pHint.isTimeIncluded());
            lFieldDisplayHint.setFormat(lDisplayType);

            lSimpleField.setFieldDisplayHint(lFieldDisplayHint);
        }
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#
     *      setChoiceFieldDisplayHint(java.lang.String, java.lang.String, null)
     */
    public void setChoiceFieldDisplayHint(String pRoleToken,
            String pContainerId, String pFieldName,
            ChoiceFieldDisplayHintData pHint) {
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);

        Field lField = fieldsManager.getField(lContainer, pFieldName);
        if (!(lField instanceof ChoiceField)) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        ChoiceField lChoiceField = (ChoiceField) lField;

        ChoiceFieldDisplayHint lFieldDisplayHint =
                (ChoiceFieldDisplayHint) lChoiceField.getFieldDisplayHint();
        ChoiceFieldDisplayType lDisplayType;
        if (StringUtils.isEmpty(pHint.getImageType())) {
            if (pHint.isList()) {
                if (lChoiceField.isMultiValued()) {
                    lDisplayType = ChoiceFieldDisplayType.LIST;
                }
                else {
                    lDisplayType = ChoiceFieldDisplayType.COMBO;
                }
            }
            else {
                if (lChoiceField.isMultiValued()) {
                    lDisplayType = ChoiceFieldDisplayType.CHECKBOX;
                }
                else {
                    lDisplayType = ChoiceFieldDisplayType.RADIO;
                }
            }
        }
        else {
            lDisplayType =
                    ChoiceFieldDisplayType.fromString(pHint.getImageType());
        }

        if (lFieldDisplayHint != null) {
            lFieldDisplayHint.setDisplayType(lDisplayType);
        }
        else {
            lFieldDisplayHint = ChoiceFieldDisplayHint.newInstance();
            lFieldDisplayHint.setDisplayType(lDisplayType);
            lChoiceField.setFieldDisplayHint(lFieldDisplayHint);
        }
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#
     *      setChoiceTreeDisplayHint(java.lang.String, java.lang.String, null)
     */
    public void setChoiceTreeDisplayHint(String pRoleToken,
            String pContainerId, String pFieldName,
            ChoiceTreeDisplayHintData pHint) {

        //Check separator value
        if (pHint.getSeparator() == null || pHint.getSeparator().isEmpty()) {
            throw new InvalidValueException("Separator cannot be null or empty");
        }

        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);

        Field lField = fieldsManager.getField(lContainer, pFieldName);
        if (null == lField || !(lField instanceof ChoiceField)) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        ChoiceField lChoiceField = (ChoiceField) lField;

        ChoiceTreeDisplayHint lFieldDisplayHint =
                (ChoiceTreeDisplayHint) lChoiceField.getFieldDisplayHint();

        if (lFieldDisplayHint == null) {
            lFieldDisplayHint = ChoiceTreeDisplayHint.newInstance();
            lChoiceField.setFieldDisplayHint(lFieldDisplayHint);
        }
        lFieldDisplayHint.setSeparator(pHint.getSeparator());
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#setDisplayHint(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.facilities.DisplayHintData)
     */
    public void setDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, DisplayHintData pHint) {
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);
        Field lField = fieldsManager.getField(lContainer, pFieldName);
        FieldDisplayHint lFieldDisplayHint = lField.getFieldDisplayHint();

        if (lFieldDisplayHint != null) {
            if (lFieldDisplayHint instanceof DisplayHint) {
                DisplayHint lDisplayHint = (DisplayHint) lFieldDisplayHint;

                lDisplayHint.setType(pHint.getType());
            }
        }
        else {
            DisplayHint lDisplayHint = DisplayHint.newInstance();
            lDisplayHint.setType(pHint.getType());

            lField.setFieldDisplayHint(lDisplayHint);
        }
        // Create the extended attributes for the displayHint (if any)
        getAttributesService().set(lField.getId(), pHint.getAttributes());
        removeElementFromCache(pContainerId, true);
    }

    /**
     * {@inheritDoc}
     */
    public void setGridDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, GridDisplayHintData pGridDisplayHintData) {
        // Get the field (simple field)
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);
        Field lField = fieldsManager.getField(lContainer, pFieldName);

        if (!(lField instanceof SimpleField)
                || ((SimpleField) lField).getType() != FieldType.STRING) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }

        SimpleField lSimpleField = (SimpleField) lField;

        // Get current field display hint
        GridDisplayHint lGridDisplayHint;
        lGridDisplayHint = (GridDisplayHint) lSimpleField.getFieldDisplayHint();

        if (lGridDisplayHint == null) {
            // create a new grid display hint for the simple field
            lGridDisplayHint =
                    GridObjectsUtil.createGridDisplayHint(pGridDisplayHintData);
            // associate it to simpleField
            lSimpleField.setFieldDisplayHint(lGridDisplayHint);
        }
        else {
            // update the current grid display hint
            GridObjectsUtil.updateGridDisplayHint(pGridDisplayHintData,
                    lGridDisplayHint);
        }
        removeElementFromCache(pContainerId, true);
    }

    /**
     * Returns the display Group DAO.
     * 
     * @return Returns the displayGroup.
     */
    public DisplayGroupDao getDisplayGroupDao() {
        return displayGroupDao;
    }

    /**
     * Sets the display Group DAO.
     * 
     * @param pDisplayGroup
     *            The displayGroup to set.
     */
    public void setDisplayGroupDao(DisplayGroupDao pDisplayGroup) {
        displayGroupDao = pDisplayGroup;
    }

    /** The simple field dao. */
    private SimpleFieldDao simpleFieldDao;

    /**
     * Gets the simple field dao.
     * 
     * @return the simple field dao
     */
    public SimpleFieldDao getSimpleFieldDao() {
        return simpleFieldDao;
    }

    /**
     * Sets the simple field dao.
     * 
     * @param pSimpleFieldDao
     *            the new simple field dao
     */
    public void setSimpleFieldDao(SimpleFieldDao pSimpleFieldDao) {
        simpleFieldDao = pSimpleFieldDao;
    }

    /** The text field display hint dao. */
    private TextFieldDisplayHintDao textFieldDisplayHintDao;

    /**
     * Gets the text field display hint dao.
     * 
     * @return the text field display hint dao
     */
    public TextFieldDisplayHintDao getTextFieldDisplayHintDao() {
        return textFieldDisplayHintDao;
    }

    /**
     * Sets the text field display hint dao.
     * 
     * @param pTextFieldDisplayHintDao
     *            the new text field display hint dao
     */
    public void setTextFieldDisplayHintDao(
            TextFieldDisplayHintDao pTextFieldDisplayHintDao) {
        textFieldDisplayHintDao = pTextFieldDisplayHintDao;
    }

    private DateDisplayHintDao dateDisplayHintDao;

    /**
     * get dateDisplayHintDao
     * 
     * @return the dateDisplayHintDao
     */
    public DateDisplayHintDao getDateDisplayHintDao() {
        return dateDisplayHintDao;
    }

    /**
     * set dateDisplayHintDao
     * 
     * @param pDateDisplayHintDao
     *            the dateDisplayHintDao to set
     */
    public void setDateDisplayHintDao(DateDisplayHintDao pDateDisplayHintDao) {
        dateDisplayHintDao = pDateDisplayHintDao;
    }

    /** The display hint dao */
    private DisplayHintDao displayHintDao;

    /**
     * get displayHintDao
     * 
     * @return the displayHintDao
     */
    public DisplayHintDao getDisplayHintDao() {
        return displayHintDao;
    }

    /**
     * set displayHintDao
     * 
     * @param pDisplayHintDao
     *            the displayHintDao to set
     */
    public void setDisplayHintDao(DisplayHintDao pDisplayHintDao) {
        displayHintDao = pDisplayHintDao;
    }

    /** The choice field dao. */
    private ChoiceFieldDao choiceFieldDao;

    /**
     * Gets the choice field dao.
     * 
     * @return the choice field dao
     */
    public ChoiceFieldDao getChoiceFieldDao() {
        return choiceFieldDao;
    }

    /**
     * Sets the choice field dao.
     * 
     * @param pChoiceFieldDao
     *            the new choice field dao
     */
    public void setChoiceFieldDao(ChoiceFieldDao pChoiceFieldDao) {
        choiceFieldDao = pChoiceFieldDao;
    }

    private GridDisplayHintDao gridDisplayHintDao;

    public GridDisplayHintDao getGridDisplayHintDao() {
        return gridDisplayHintDao;
    }

    public void setGridDisplayHintDao(GridDisplayHintDao pGridDisplayHintDao) {
        gridDisplayHintDao = pGridDisplayHintDao;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.business.display.service.DisplayService#setDateDisplayHint(java.lang.String,
     *      java.lang.String, java.lang.String,
     *      org.topcased.gpm.business.facilities.DateDisplayHintData)
     */
    @Override
    public void setJAppletDisplayHint(String pRoleToken, String pContainerId,
            String pFieldName, JAppletDisplayHintData pHint) {
        final FieldsContainer lContainer =
                getFieldsContainerDao().load(pContainerId);

        Field lField = fieldsManager.getField(lContainer, pFieldName);
        if (!(lField instanceof SimpleField)) {
            throw new InvalidNameException(pFieldName,
                    "Invalid field name ''{0}''");
        }
        SimpleField lSimpleField = (SimpleField) lField;

        JAppletDisplayHint lFieldDisplayHint = JAppletDisplayHint.newInstance();
        lFieldDisplayHint.setAppletCode(pHint.getAppletCode());
        lFieldDisplayHint.setAppletCodeBase(pHint.getAppletCodeBase());
        lFieldDisplayHint.setAppletAlter(pHint.getAppletAlter());
        lFieldDisplayHint.setAppletArchive(pHint.getAppletArchive());

        lFieldDisplayHint.setAppletName(pHint.getAppletName());

        Set<org.topcased.gpm.domain.facilities.AppletParameter> lParamSet =
                null;
        if (pHint.getAppletParamNames() != null) {
            lParamSet =
                    new HashSet<org.topcased.gpm.domain.facilities.AppletParameter>(
                            pHint.getAppletParamNames().length);
            for (int lIter = 0; lIter < pHint.getAppletParamNames().length; lIter++) {

                org.topcased.gpm.domain.facilities.AppletParameter lAppletParam =
                        org.topcased.gpm.domain.facilities.AppletParameter.newInstance();
                lAppletParam.setParameterName(pHint.getAppletParamNames()[lIter]);
                lParamSet.add(lAppletParam);
            }
        }
        lFieldDisplayHint.setAppletParameters(lParamSet);
        lSimpleField.setFieldDisplayHint(lFieldDisplayHint);
        removeElementFromCache(pContainerId, true);
    }
}
