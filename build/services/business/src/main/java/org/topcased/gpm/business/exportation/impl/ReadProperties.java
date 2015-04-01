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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * ReadProperties Load the properties values used in migration.properties file
 * 
 * @author rranzato
 */
public class ReadProperties {

    public static final String ATTACHED_FILES = "attachedFiles";

    public static final String DICTIONARY = "dictionary";

    public static final String ENVIRONMENTS = "environments";

    public static final String FILTERS = "filters";

    public static final String PRODUCTS = "products";

    public static final String PRODUCT_LINKS = "productLinks";

    public static final String SHEETS = "sheets";

    public static final String SHEET_LINKS = "sheetLinks";

    public static final String USERS = "users";

    public static final String PRODUCT_MAPPING = "productMapping";

    public static final String PRODUCT_MAPPING_FILE = "/product_mapping.csv";

    public static final String DIRECTORY = "directory";

    public static final String PROCESS = "PROCESS";

    private static ReadProperties instance;

    private boolean obfAttachedFiles;

    private boolean obfDictionary;

    private boolean obfEnvironments;

    private boolean obfFilters;

    private boolean obfProductLinks;

    private boolean obfProducts;

    private boolean obfSheetLinks;

    private boolean obfSheets;

    private boolean obfUsers;

    private boolean productMapping;

    private String directory;

    private Map<String, String> lPropertiesMap = new HashMap<String, String>();

    /**
     * Private default constructor (singleton)
     */
    private ReadProperties() {

    }

    /**
     * Provide the single ReadProperties instance
     * 
     * @return ReadProperties instance
     */
    public static synchronized ReadProperties getInstance() {
        if (instance == null) {
            instance = new ReadProperties();
        }
        return instance;
    }

    /**
     * Get the boolean to test if attached files must be obfuscated
     * 
     * @return the boolean for attached files
     */
    public boolean isObfAttachedFiles() {
        return obfAttachedFiles;
    }

    /**
     * Set the boolean allowing or not to obfuscate attached files
     * 
     * @param obfAttachedFiles
     *            the boolean for attached files to set
     */
    public void setObfAttachedFiles(boolean obfAttachedFiles) {
        this.obfAttachedFiles = obfAttachedFiles;
    }

    /**
     * Get the boolean to test if dictionary must be obfuscated
     * 
     * @return the boolean for dictionary
     */
    public boolean isObfDictionary() {
        return obfDictionary;
    }

    /**
     * Set the boolean allowing or not to obfuscate dictionary
     * 
     * @param obfDictionary
     *            the boolean for dictionary to set
     */
    public void setObfDictionary(boolean obfDictionary) {
        this.obfDictionary = obfDictionary;
    }

    /**
     * Get the boolean to test if environment must be obfuscated
     * 
     * @return the boolean for environment
     */
    public boolean isObfEnvironments() {
        return obfEnvironments;
    }

    /**
     * Set the boolean allowing or not to obfuscate environment
     * 
     * @param obfEnvironments
     *            the boolean for environment to set
     */
    public void setObfEnvironments(boolean obfEnvironments) {
        this.obfEnvironments = obfEnvironments;
    }

    /**
     * Get the boolean to test if filters must be obfuscated
     * 
     * @return the boolean for filters
     */
    public boolean isObfFilters() {
        return obfFilters;
    }

    /**
     * Set the boolean allowing or not to obfuscate filters
     * 
     * @param obfFilters
     *            the boolean for filters to set
     */
    public void setObfFilters(boolean obfFilters) {
        this.obfFilters = obfFilters;
    }

    /**
     * Get the boolean to test if product links must be obfuscated
     * 
     * @return the boolean for product links
     */
    public boolean isObfProductLinks() {
        return obfProductLinks;
    }

    /**
     * Set the boolean allowing or not to obfuscate product links
     * 
     * @param obfProductLinks
     *            the boolean for product links to set
     */
    public void setObfProductLinks(boolean obfProductLinks) {
        this.obfProductLinks = obfProductLinks;
    }

    /**
     * Get the boolean to test if products must be obfuscated
     * 
     * @return the boolean for products
     */
    public boolean isObfProducts() {
        return obfProducts;
    }

    /**
     * Set the boolean allowing or not to obfuscate products
     * 
     * @param obfProducts
     *            the boolean for products to set
     */
    public void setObfProducts(boolean obfProducts) {
        this.obfProducts = obfProducts;
    }

    /**
     * Get the boolean to test if sheet links must be obfuscated
     * 
     * @return the boolean for sheet links
     */
    public boolean isObfSheetLinks() {
        return obfSheetLinks;
    }

    /**
     * Set the boolean allowing or not to obfuscate sheet links
     * 
     * @param obfSheetLinks
     *            the boolean for sheet links to set
     */
    public void setObfSheetLinks(boolean obfSheetLinks) {
        this.obfSheetLinks = obfSheetLinks;
    }

    /**
     * Get the boolean to test if sheets must be obfuscated
     * 
     * @return the boolean for sheets
     */
    public boolean isObfSheets() {
        return obfSheets;
    }

    /**
     * Set the boolean allowing or not to obfuscate sheets
     * 
     * @param obfSheets
     *            the boolean for sheets to set
     */
    public void setObfSheets(boolean obfSheets) {
        this.obfSheets = obfSheets;
    }

    /**
     * Get the boolean to test if users must be obfuscated
     * 
     * @return the boolean for users
     */
    public boolean isObfUsers() {
        return obfUsers;
    }

    /**
     * Set the boolean allowing or not to obfuscate users
     * 
     * @param obfUsers
     *            the boolean for users to set
     */
    public void setObfUsers(boolean obfUsers) {
        this.obfUsers = obfUsers;
    }

    /**
     * Get the boolean to test if a mapping products file must be generated
     * 
     * @return the boolean for products mapping
     */
    public boolean isProductMapping() {
        return productMapping;
    }

    /**
     * Set the boolean allowing or not to generate a mapping products file
     * between products and obfuscated products
     * 
     * @param productMapping
     *            the boolean for productMapping to set
     */
    public void setProductMapping(boolean productMapping) {
        this.productMapping = productMapping;
    }

    /**
     * Get the export directory set in the configuration file
     * 
     * @return the String corresponding to the export directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Set the configuration directory according to the directory filled in the
     * configuration file (used for the product mapping)
     * 
     * @param directory
     *            the directory filled in the configuration file
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Get the map containing the obfuscation properties
     * 
     * @return the lPropertiesMap map
     */
    public Map<String, String> getlPropertiesMap() {
        return lPropertiesMap;
    }

    public boolean isObfuscated() {
        return obfAttachedFiles || obfDictionary || obfEnvironments
                || obfFilters || obfProductLinks || obfProducts
                || obfSheetLinks || obfSheets || obfUsers;
    }

    /**
     * Determine the boolean to set according to the properties file read
     * Properties set to true will be obfuscated
     * 
     * @param lPropertiesMap
     *            the map containing all the obfuscation properties
     */
    public void setPropertiesMap(Map<String, String> lPropertiesMap) {
        this.lPropertiesMap = lPropertiesMap;
        this.setObfAttachedFiles(Boolean.valueOf(getlPropertiesMap().get(
                ATTACHED_FILES)));
        this.setObfDictionary(Boolean.valueOf(getlPropertiesMap().get(
                DICTIONARY)));
        this.setObfEnvironments(Boolean.valueOf(getlPropertiesMap().get(
                ENVIRONMENTS)));
        this.setObfFilters(Boolean.valueOf(getlPropertiesMap().get(FILTERS)));
        this.setObfProducts(Boolean.valueOf(getlPropertiesMap().get(PRODUCTS)));
        this.setObfProductLinks(Boolean.valueOf(getlPropertiesMap().get(
                PRODUCT_LINKS)));
        this.setObfSheets(Boolean.valueOf(getlPropertiesMap().get(SHEETS)));
        this.setObfSheetLinks(Boolean.valueOf(getlPropertiesMap().get(
                SHEET_LINKS)));
        this.setObfUsers(Boolean.valueOf(getlPropertiesMap().get(USERS)));
        this.setProductMapping(Boolean.valueOf(getlPropertiesMap().get(
                PRODUCT_MAPPING)));
        this.setDirectory(getlPropertiesMap().get(DIRECTORY));
    }

    /**
     * Generate a random number which will be added to the original date of the
     * sheet
     * 
     * @return a random number between 1 and 40
     */
    public int getRandomNumber() {
        Random lRandom = new Random();
        final int lRandomValue = (lRandom.nextInt(39) + 1);
        return lRandomValue;
    }
}
