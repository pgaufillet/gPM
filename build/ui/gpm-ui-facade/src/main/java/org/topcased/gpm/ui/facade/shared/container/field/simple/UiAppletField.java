/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (Atos)
 ******************************************************************/
package org.topcased.gpm.ui.facade.shared.container.field.simple;

import java.util.List;

/**
 * UiAppletField
 * 
 * @author phtsaan
 */
public class UiAppletField extends UiStringField {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5350995664193440249L;

    /*sheet id*/
    private String sheetId;

    /*java Applet class (byte code)*/
    private String appletCode;

    /*java Applet path*/
    private String appletCodeBase;

    /*java Applet alternative message*/
    private String appletAlter;

    /*java Applet name*/
    private String appletName;

    /*java Applet Archive*/
    private String appletArchive;

    /*Parameter's name list*/
    private List<String> appletParameterList;

    /**
     * constructor
     */
    public UiAppletField() {
        super();
    }

    public UiAppletField(String pSheetId, String pAppletCode,
            String pAppletCodeBase, String pAppletAlter, String pAppletName,
            String pAppletArchive, List<String> pAppletParameterList) {
        super();
        this.sheetId = pSheetId;
        this.appletCode = pAppletCode;
        this.appletCodeBase = pAppletCodeBase;
        this.appletAlter = pAppletAlter;
        this.appletName = pAppletName;
        this.appletArchive = pAppletArchive;
        this.appletParameterList = pAppletParameterList;

    }

    public String getAppletAlter() {
        return appletAlter;
    }

    /**
     * Set applet alternative message
     * 
     * @param pAppletAlter
     */
    public void setAppletAlter(String pAppletAlter) {
        this.appletAlter = pAppletAlter;
    }

    /**
     * Retrieves applet name
     * 
     * @return appletName
     */
    public String getAppletName() {
        return appletName;
    }

    /**
     * Set applet name
     * 
     * @param pAppletName
     */
    public void setAppletName(String pAppletName) {
        this.appletName = pAppletName;
    }

    /**
     * Retrieves applet Archive
     * 
     * @return appletArchive
     */
    public String getAppletArchive() {
        return appletArchive;
    }

    /**
     * Set applet Archive
     * 
     * @param pAppletArchive
     */
    public void setAppletArchive(String pAppletArchive) {
        this.appletArchive = pAppletArchive;
    }

    /**
     * Retrieves applet parameters
     * 
     * @return appletParameterList
     */
    public List<String> getAppletParameterList() {
        return appletParameterList;
    }

    /**
     * Set applet parameters
     * 
     * @param pAppletParameterList
     */
    public void setAppletParameterList(List<String> pAppletParameterList) {
        this.appletParameterList = pAppletParameterList;
    }

    /**
     * get sheet ID
     * 
     * @return sheetId
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * set sheet ID
     * 
     * @param pSheetId
     */
    public void setSheetId(String pSheetId) {
        this.sheetId = pSheetId;
    }

    /**
     * get applet class
     * 
     * @return appletClass
     */
    public String getAppletCode() {
        return appletCode;
    }

    /**
     * set applet class
     * 
     * @param pAppletClass
     */
    public void setAppletCode(String pAppletCode) {
        this.appletCode = pAppletCode;
    }

    /**
     * get applet code base
     * 
     * @return appletCodeBase
     */
    public String getAppletCodeBase() {
        return appletCodeBase;
    }

    /**
     * set applet code base
     * 
     * @param pAppletCodeBase
     */
    public void setAppletCodeBase(String pAppletCodeBase) {
        this.appletCodeBase = pAppletCodeBase;
    }
}
