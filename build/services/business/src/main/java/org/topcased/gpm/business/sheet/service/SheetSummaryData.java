/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
//
// Attention: Generated code! Do not modify by hand!
// Generated by: ValueObject.vsl in andromda-java-cartridge.
//
package org.topcased.gpm.business.sheet.service;

/**
 * @author Atos
 */
public class SheetSummaryData extends
        org.topcased.gpm.business.fields.SummaryData implements
        java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SheetSummaryData() {
    }

    /**
     * Constructor taking all properties.
     */
    public SheetSummaryData(final java.lang.String pSheetName,
            final java.lang.String pSheetType, final boolean pSelectable,
            final java.lang.String pSheetTypeId,
            final java.lang.String pSheetReference) {
        this.sheetName = pSheetName;
        this.sheetType = pSheetType;
        this.selectable = pSelectable;
        this.sheetTypeId = pSheetTypeId;
        this.sheetReference = pSheetReference;
    }

    /**
     * Copies constructor from other SheetSummaryData
     */
    public SheetSummaryData(SheetSummaryData pOtherBean) {
        if (pOtherBean != null) {
            this.sheetName = pOtherBean.getSheetName();
            this.sheetType = pOtherBean.getSheetType();
            this.selectable = pOtherBean.isSelectable();
            this.sheetTypeId = pOtherBean.getSheetTypeId();
            this.sheetReference = pOtherBean.getSheetReference();
        }
    }

    private java.lang.String sheetName;

    /**
     * <p>
     * <html>
     * </p>
     * <p>
     * <head>
     * </p>
     * <p>
     * </head>
     * </p>
     * <p>
     * <body>
     * </p>
     * <p>
     * <p>
     * </p>
     * <p>
     * The sheet name.
     * </p>
     * <p>
     * </p>
     * </p>
     * <p>
     * </body>
     * </p>
     * <p>
     * </html>
     * </p>
     */
    public java.lang.String getSheetName() {
        return this.sheetName;
    }

    public void setSheetName(java.lang.String pSheetName) {
        this.sheetName = pSheetName;
    }

    private java.lang.String sheetType;

    /**
     * <p>
     * <html>
     * </p>
     * <p>
     * <head>
     * </p>
     * <p>
     * </head>
     * </p>
     * <p>
     * <body>
     * </p>
     * <p>
     * <p>
     * </p>
     * <p>
     * The sheet type.
     * </p>
     * <p>
     * </p>
     * </p>
     * <p>
     * </body>
     * </p>
     * <p>
     * </html>
     * </p>
     */
    public java.lang.String getSheetType() {
        return this.sheetType;
    }

    public void setSheetType(java.lang.String pSheetType) {
        this.sheetType = pSheetType;
    }

    private boolean selectable;

    /**
     * 
     */
    public boolean isSelectable() {
        return this.selectable;
    }

    public void setSelectable(boolean pSelectable) {
        this.selectable = pSelectable;
    }

    private java.lang.String sheetTypeId;

    /**
     * <p>
     * Identifier of the sheet type.
     * </p>
     */
    public java.lang.String getSheetTypeId() {
        return this.sheetTypeId;
    }

    public void setSheetTypeId(java.lang.String pSheetTypeId) {
        this.sheetTypeId = pSheetTypeId;
    }

    private java.lang.String sheetReference;

    /**
     * 
     */
    public java.lang.String getSheetReference() {
        return this.sheetReference;
    }

    public void setSheetReference(java.lang.String pSheetReference) {
        this.sheetReference = pSheetReference;
    }

}