/***************************************************************
 * Copyright (c) 2011 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Pierre Hubert TSAAN (ATOS)
 ******************************************************************/

package org.topcased.gpm.business.serialization.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Applet DisplayHint
 * 
 * @author phtsaan
 */

@XStreamAlias("jAppletDisplayHint")
public class JAppletDisplayHint extends DisplayHint implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = -5086254839324860702L;

    /* sheet id */
    @XStreamAsAttribute
    private String sheetId;

    /* java Applet class (byte code) */
    @XStreamAsAttribute
    private String appletCode;

    /* java Applet path */
    @XStreamAsAttribute
    private String appletCodeBase;

    /* java Applet alternative message */
    @XStreamAsAttribute
    private String appletAlter;

    /* java Applet name */
    @XStreamAsAttribute
    private String appletName;

    /* java Applet Archive */
    @XStreamAsAttribute
    private String appletArchive;

    /** The display type. */
    @XStreamAsAttribute
    private String displayType;

    /** Applet parameter's List */
    @XStreamImplicit(itemFieldName = "appletParameter")
    private List<AppletParameter> appletParameterList;

    /**
     * Default constructor
     */
    public JAppletDisplayHint() {
        super();
    }

    /**
     * Applet display Hint constructor with out sheet ID
     * 
     * @param pAppletCode applet code
     * @param pAppletCodeBase applet code base
     * @param pAppletAlter applet alter
     * @param pAppletName applet name
     * @param pAppletArchive applet archive
     * @param pAppletParameterList applet parameter list
     */
    public JAppletDisplayHint(String pAppletCode, String pAppletCodeBase,
            String pAppletAlter, String pAppletName, String pAppletArchive,
            List<AppletParameter> pAppletParameterList) {
        super();
        this.appletCode = pAppletCode;
        this.appletCodeBase = pAppletCodeBase;
        this.appletAlter = pAppletAlter;
        this.appletName = pAppletName;
        this.appletArchive = pAppletArchive;
        this.appletParameterList = pAppletParameterList;
    }

    /**
     * Applet display Hint constructor with sheet ID
     * 
     * @param pSheetId sheet Id
     * @param pAppletCode applet code
     * @param pAppletCodeBase applet code base
     * @param pAppletAlter applet alter
     * @param pAppletName applet name
     * @param pAppletArchive applet archive
     * @param pAppletParameterList applet parameter list
     */
    public JAppletDisplayHint(String pSheetId, String pAppletCode,
            String pAppletCodeBase, String pAppletAlter, String pAppletName,
            String pAppletArchive, List<AppletParameter> pAppletParameterList) {
        super();
        this.sheetId = pSheetId;
        this.appletCode = pAppletCode;
        this.appletCodeBase = pAppletCodeBase;
        this.appletAlter = pAppletAlter;
        this.appletName = pAppletName;
        this.appletArchive = pAppletArchive;
        this.appletParameterList = pAppletParameterList;
    }

    /**
     * Get java Applet byte code
     * 
     * @return appletCode
     */
    public String getAppletCode() {

        return appletCode;
    }

    /**
     * Set java Applet code
     * 
     * @param pAppletCode applet code
     */
    public void setAppletCode(String pAppletCode) {
        this.appletCode = pAppletCode;
    }

    /**
     * Get the java Applet path
     * 
     * @return appletCodeBase
     */
    public String getAppletCodeBase() {
        return appletCodeBase;
    }

    /**
     * Set java Applet path
     * 
     * @param pAppletCodeBase applet code base
     */
    public void setAppletCodeBase(String pAppletCodeBase) {
        this.appletCodeBase = pAppletCodeBase;
    }

    /**
     * Get the sheet Id
     * 
     * @return sheetId
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * Set the sheet Id value
     * 
     * @param pSheetId sheet id
     */
    public void setSheetId(String pSheetId) {
        this.sheetId = pSheetId;
    }

    /**
     * Retrieves the applet alternative message
     * 
     * @return appletAlter as String
     */
    public String getAppletAlter() {
        return appletAlter;
    }

    /**
     * Set applet alternative message
     * 
     * @param pAppletAlter applet alter
     */
    public void setAppletAlter(String pAppletAlter) {
        this.appletAlter = pAppletAlter;
    }

    /**
     * Retrieves applet Name
     * 
     * @return appletName
     */
    public String getAppletName() {
        return appletName;
    }

    /**
     * Set applet Name
     * 
     * @param pAppletName applet name
     */
    public void setAppletName(String pAppletName) {
        this.appletName = pAppletName;
    }

    /**
     * Retrieve applet archive
     * 
     * @return appletArchive
     */
    public String getAppletArchive() {
        return appletArchive;
    }

    /**
     * Set applet archive
     * 
     * @param pAppletArchive applet archive
     */
    public void setAppletArchive(String pAppletArchive) {
        this.appletArchive = pAppletArchive;
    }

    /**
     * Retrieves the applet display type
     * 
     * @return APPLET as String
     */
    public String getDisplayType() {
        if (null == displayType) {
            return "APPLET";
        }
        else {
            return displayType;
        }
    }

    /**
     * Set applet display type
     * 
     * @param pDisplayType display type
     */
    public void setDisplayType(String pDisplayType) {
        this.displayType = pDisplayType;
    }

    /**
     * Retrieves applet parameters list
     * 
     * @return appletParameterList
     */
    public List<AppletParameter> getAppletParameterList() {
        return appletParameterList;
    }

    /**
     * Retrieves applet parameters as List of String used by UIAppletField
     * 
     * @return List <CODE><\String\></CODE>
     */
    public List<String> getAppletParameterAsStringList() {
        List<String> lParams = null;
        if (appletParameterList != null) {
            lParams = new ArrayList<String>(appletParameterList.size());
            for (int lIter = 0; lIter < appletParameterList.size(); lIter++) {
                lParams.add(appletParameterList.get(lIter).getParamName());
            }
        }
        return lParams;
    }

    /**
     * Set applet parameter list
     * 
     * @param pAppletParameterList applet parameter list
     */
    public void setAppletParameterList(
            List<AppletParameter> pAppletParameterList) {
        this.appletParameterList = pAppletParameterList;
    }
}
