/***************************************************************
 * Copyright (c) 2012 ATOS. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Romain Ranzato (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.exportation.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Define a singleton to use correspondence maps for laundered export
 * 
 * @author rranzato
 */
public final class ExportationData {

    private static ExportationData instance;

    private HashMap<String, String> productName = new HashMap<String, String>();

    private HashMap<String, String> productType = new HashMap<String, String>();

    private HashMap<String, String> userName = new HashMap<String, String>();

    private HashMap<String, String> userLogin = new HashMap<String, String>();

    private HashMap<String, String> environmentName =
            new HashMap<String, String>();

    private HashMap<String, String> sheetType = new HashMap<String, String>();

    private List<String> categoryValue = new ArrayList<String>();

    private int productCounter = 1;

    private int userCounter = 0;

    /**
     * private default constructor (singleton)
     */
    private ExportationData() {

    }

    /**
     * Provide the single ExportationData instance
     * 
     * @return ExportationData instance
     */
    public static synchronized ExportationData getInstance() {
        if (instance == null) {
            instance = new ExportationData();
        }
        return instance;
    }

    /**
     * Reset an HashMap
     * 
     * @param pMap
     *            the HashMap to reset
     */
    public void resetHashMap(HashMap<String, String> pMap) {
        pMap.clear();
    }

    /**
     * Get the correspondence map containing product names/obfuscated product
     * names
     * 
     * @return the productName hashmap
     */
    public HashMap<String, String> getProductNames() {
        return productName;
    }

    /**
     * Get the correspondence map containing product types/obfuscated product
     * types
     * 
     * @return the productType hashmap
     */
    public HashMap<String, String> getProductType() {
        return productType;
    }

    /**
     * Get the correspondence map containing usernames/obfuscated usernames
     * 
     * @return the userName hashmap
     */
    public HashMap<String, String> getUserName() {
        return userName;
    }

    /**
     * Get the correspondence map containing login/obfuscated login
     * 
     * @return the userLogin hashmap
     */
    public HashMap<String, String> getUserLogin() {
        return userLogin;
    }

    /**
     * Get the correspondence map containing environment elements/obfuscated
     * environment elements
     * 
     * @return the environmentName hashmap
     */
    public HashMap<String, String> getEnvironmentName() {
        return environmentName;
    }

    /**
     * Get the correspondence map containing sheet types/obfuscated sheet types
     * 
     * @return the sheetType hashmap
     */
    public HashMap<String, String> getSheetType() {
        return sheetType;
    }

    /**
     * Get the correspondence map containing dictionary elements/obfuscated
     * dictionary elements
     * 
     * @return the categoryValue the dictionary hashmap
     */
    public List<String> getCategoryValue() {
        return categoryValue;
    }

    /**
     * Get the product counter in order to set obfuscated product name
     * (Product1, Product2, ..., ProductN)
     * 
     * @return the productCounter
     */
    public int getProductCounter() {
        return productCounter;
    }

    /**
     * Set the product counter value
     * 
     * @param pProductCounter
     *            the productCounter to set
     */
    public void setProductCounter(int pProductCounter) {
        this.productCounter = pProductCounter;
    }
    
    /**
     * Increment the product counter by 1
     */
    public void incrementProductCounter(){
       this.productCounter = this.productCounter + 1; 
    }

    /**
     * Get the user counter in order to set obfuscated username (User1, User2,
     * ..., UserN)
     * 
     * @return the userCounter
     */
    public int getUserCounter() {
        return userCounter;
    }

    /**
     * Set the user counter value
     * 
     * @param pUserCounter
     *            the userCounter to set
     */
    public void setUserCounter(int pUserCounter) {
        this.userCounter = pUserCounter;
    }
    
    /**
     * Increment the user counter by 1
     */
    public void incrementUserCounter(){
       this.userCounter = this.userCounter + 1; 
    }

}