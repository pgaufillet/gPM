/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package org.topcased.gpm.business.sheet.service;

import org.topcased.gpm.business.fields.LineFieldData;
import org.topcased.gpm.business.sheet.impl.CacheableSheet;

/**
 * Old sheet data structure
 * 
 * @author generated
 * @deprecated
 * @since 1.8.3
 * @see CacheableSheet
 */
public class SheetData implements java.io.Serializable {
    private static final long serialVersionUID = 4358240901200685496L;

    /**
     * Default constructor.
     */
    public SheetData() {
    }

    /**
     * Constructor taking all properties.
     */
    public SheetData(final String pId, final String pProductName,
            final String pSheetTypeName, final String pProcessName,
            final String pSheetReference, final int pVersion,
            final String pSheetTypeId, final String[] pEnvironmentNames,
            final FieldGroupData[] pFieldGroupDatas,
            final LineFieldData pReference, final LockData pLock) {
        id = pId;
        productName = pProductName;
        sheetTypeName = pSheetTypeName;
        processName = pProcessName;
        sheetReference = pSheetReference;
        version = pVersion;
        sheetTypeId = pSheetTypeId;
        environmentNames = pEnvironmentNames;
        fieldGroupDatas = pFieldGroupDatas;
        reference = pReference;
        lock = pLock;
    }

    /**
     * Copies constructor from other SheetData
     */
    public SheetData(SheetData pOtherBean) {
        if (pOtherBean != null) {
            id = pOtherBean.getId();
            productName = pOtherBean.getProductName();
            sheetTypeName = pOtherBean.getSheetTypeName();
            processName = pOtherBean.getProcessName();
            sheetReference = pOtherBean.getSheetReference();
            version = pOtherBean.getVersion();
            sheetTypeId = pOtherBean.getSheetTypeId();
            environmentNames = pOtherBean.getEnvironmentNames();
            fieldGroupDatas = pOtherBean.getFieldGroupDatas();
            reference = pOtherBean.getReference();
            lock = pOtherBean.getLock();
        }
    }

    private String id;

    /**
     * 
     */
    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    private java.lang.String productName;

    /**
     * 
     */
    public String getProductName() {
        return productName;
    }

    public void setProductName(final String pProductName) {
        productName = pProductName;
    }

    private java.lang.String sheetTypeName;

    /**
     * 
     */
    public String getSheetTypeName() {
        return sheetTypeName;
    }

    public void setSheetTypeName(final String pSheetTypeName) {
        sheetTypeName = pSheetTypeName;
    }

    private String processName;

    /**
     * 
     */
    public java.lang.String getProcessName() {
        return processName;
    }

    public void setProcessName(final String pProcessName) {
        processName = pProcessName;
    }

    private String sheetReference;

    /**
     * 
     */
    public String getSheetReference() {
        return sheetReference;
    }

    public void setSheetReference(final String pSheetReference) {
        sheetReference = pSheetReference;
    }

    private int version;

    /**
     * 
     */
    public int getVersion() {
        return version;
    }

    public void setVersion(final int pVersion) {
        version = pVersion;
    }

    private String sheetTypeId;

    /**
     * 
     */
    public String getSheetTypeId() {
        return sheetTypeId;
    }

    public void setSheetTypeId(final String pSheetTypeId) {
        sheetTypeId = pSheetTypeId;
    }

    private String[] environmentNames;

    /**
     * 
     */
    public String[] getEnvironmentNames() {
        return environmentNames;
    }

    public void setEnvironmentNames(String[] pEnvironmentNames) {
        environmentNames = pEnvironmentNames;
    }

    private org.topcased.gpm.business.sheet.service.FieldGroupData[] fieldGroupDatas;

    /**
     * Get the fieldGroupDatas
     */
    public FieldGroupData[] getFieldGroupDatas() {
        return fieldGroupDatas;
    }

    /**
     * Set the fieldGroupDatas
     */
    public void setFieldGroupDatas(FieldGroupData[] pFieldGroupDatas) {
        fieldGroupDatas = pFieldGroupDatas;
    }

    private org.topcased.gpm.business.fields.LineFieldData reference;

    /**
     * Get the reference
     */
    public LineFieldData getReference() {
        return reference;
    }

    /**
     * Set the reference
     */
    public void setReference(final LineFieldData pReference) {
        reference = pReference;
    }

    private org.topcased.gpm.business.sheet.service.LockData lock;

    /**
     * Get the lock
     */
    public final LockData getLock() {
        return lock;
    }

    /**
     * Set the lock
     */
    public void setLock(final LockData pLock) {
        lock = pLock;
    }

}