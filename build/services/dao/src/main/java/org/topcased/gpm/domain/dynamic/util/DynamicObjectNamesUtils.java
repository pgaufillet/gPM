/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.domain.dynamic.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.topcased.gpm.domain.attributes.AttributesContainer;
import org.topcased.gpm.domain.businessProcess.BusinessProcess;
import org.topcased.gpm.domain.fields.Field;
import org.topcased.gpm.domain.fields.FieldsContainer;

/**
 * Util's class used to compute table and field names for dynamic model
 * 
 * @author tpanuel
 */
public final class DynamicObjectNamesUtils {
    // Map used to find already computed names
    private final Map<String, String> processNameMaps;

    private final Map<String, String> tableNameMaps;

    private final Map<String, String> revisionTableNameMaps;

    private final Map<String, String> columnNameMaps;

    // Set assuring that two tables haven't the same name
    private final Set<String> tableNameSet;

    // Set assuring that two tables for revision haven't the same name
    private final Set<String> revisionTableNameSet;

    // Set assuring that two columns haven't the same name on a same table
    // Dynamic table and Dynamic table for revision have the same column names
    private final Map<String, Set<String>> columnNameSetByTable;

    // Encoder used to encoding a part of the name
    private final static String ENCODING_ALGO = "MD5";

    // Default size max of a table name
    private final static int DEFAULT_TABLE_NAME_MAX_SIZE = 30;

    // Default size max  of a column name
    private final static int DEFAULT_COLUMN_NAME_MAX_SIZE = 30;

    // Size max of a table name
    private int tableNameMaxSize;

    // Size max of a column name
    private int columnNameMaxSize;

    // Size max of the parent's table part on table name
    private int parentTableNameMaxSize;

    // Size of the business process name used to compute table names
    private int processNameMaxSize;

    private final MessageDigest encoder;

    /**
     * Private constructor for singleton
     */
    private DynamicObjectNamesUtils() {
        processNameMaps = new HashMap<String, String>();
        tableNameMaps = new HashMap<String, String>();
        revisionTableNameMaps = new HashMap<String, String>();
        columnNameMaps = new HashMap<String, String>();
        tableNameSet = new HashSet<String>();
        revisionTableNameSet = new HashSet<String>();
        columnNameSetByTable = new HashMap<String, Set<String>>();
        try {
            encoder = MessageDigest.getInstance(ENCODING_ALGO);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        initDbNameMaxSize(DEFAULT_TABLE_NAME_MAX_SIZE,
                DEFAULT_COLUMN_NAME_MAX_SIZE);
    }

    /**
     * Initialize the max size of db's name size
     * 
     * @param pTableNameMaxSize
     *            The size max of table names
     * @param pColumnNameMaxSize
     *            The size max of column names
     */
    public final void initDbNameMaxSize(int pTableNameMaxSize,
            int pColumnNameMaxSize) {
        tableNameMaxSize = pTableNameMaxSize;
        parentTableNameMaxSize = (2 * tableNameMaxSize / 3) - 1;
        processNameMaxSize = (tableNameMaxSize / 3) - 1;
        columnNameMaxSize = pColumnNameMaxSize;
    }

    // Used to singleton pattern
    private final static DynamicObjectNamesUtils INSTANCE =
            new DynamicObjectNamesUtils();

    /**
     * Access to the singleton's instance
     * 
     * @return The singleton's instance
     */
    public final static DynamicObjectNamesUtils getInstance() {
        return INSTANCE;
    }

    /**
     * Clean table and column names associated to a fields container or a multi
     * valued field
     * 
     * @param pTypeId
     *            The id of the type
     */
    public void cleanDynamicNames(String pTypeId) {
        final String lTableName = tableNameMaps.remove(pTypeId);

        // Clean name of the table
        tableNameSet.remove(lTableName);
        // Clean name of all the columns of the table
        columnNameSetByTable.remove(pTypeId);
    }

    /**
     * Clean table and column names associated to a revision fields container or
     * a revision multi valued field
     * 
     * @param pTypeId
     *            The id of the type
     */
    public void cleanDynamicRevisionNames(String pTypeId) {
        final String lRevisionTableName = revisionTableNameMaps.remove(pTypeId);

        // Clean name of the revision table
        revisionTableNameSet.remove(lRevisionTableName);
        // Clean name of all the columns of the revision table
        columnNameSetByTable.remove(pTypeId);
    }

    // Only this special character is allowed by the data base
    private final static String NAME_SEPARATOR = "_";

    /**
     * Compute the table's name associated to a container -> must be called
     * before used the associated getter
     * 
     * @param pContainer
     *            The container : fields container or multi-valued field
     * @return The computed name
     */
    public String initDynamicTableName(AttributesContainer pContainer) {
        final String lContainerId = pContainer.getId();
        // Don't compute table's name twice
        String lTableName = tableNameMaps.get(lContainerId);

        if (lTableName == null) {
            // Compute table name the first time
            if (pContainer instanceof FieldsContainer) {
                // For fields container
                // <Process name>_<Type name>
                final FieldsContainer lFieldsContainer =
                        (FieldsContainer) pContainer;
                final String lProcessName =
                        getFormattedProcessName(lFieldsContainer.getBusinessProcess());

                lTableName =
                        formatName(lProcessName + lFieldsContainer.getName(),
                                tableNameMaxSize);
            }
            else if (pContainer instanceof Field) {
                // For multi-valued field
                // <Parent table name>_<Field name>
                final Field lField = (Field) pContainer;
                final String lParentTableName =
                        formatName(
                                getDynamicTableName(lField.getContainer().getId()),
                                parentTableNameMaxSize);

                lTableName =
                        formatName(lParentTableName + NAME_SEPARATOR
                                + lField.getLabelKey(), tableNameMaxSize);
            }
            else {
                throw new RuntimeException("Unknow container type "
                        + pContainer.getClass());
            }

            // Check table name unicity for a specific table 
            if (tableNameSet.contains(lTableName)) {
                throw new RuntimeException(
                        "Cannot create database element for " + lContainerId
                                + " : " + lTableName + " table already exists");
            }

            // Store the value for not computed it twice
            tableNameMaps.put(lContainerId, lTableName);
            tableNameSet.add(lTableName);
        }

        return lTableName;
    }

    // Prefix for revision tables
    private final static String REVISION_PREFIX = "R_";

    /**
     * Compute the revision table's name associated to a container -> must be
     * called before used the associated getter
     * 
     * @param pContainer
     *            The container : fields container or multi-valued field
     * @return The computed name
     */
    public String initDynamicRevisionTableName(AttributesContainer pContainer) {
        final String lContainerId = pContainer.getId();
        // Don't compute revision table's name twice
        String lRevisionTableName = revisionTableNameMaps.get(lContainerId);

        if (lRevisionTableName == null) {
            // Compute table name the first time
            if (pContainer instanceof FieldsContainer) {
                // For fields container
                // <Process name>_<Type name>
                final FieldsContainer lFieldsContainer =
                        (FieldsContainer) pContainer;
                final String lProcessName =
                        getFormattedProcessName(lFieldsContainer.getBusinessProcess());

                // Add revision prefix
                lRevisionTableName =
                        formatName(REVISION_PREFIX + lProcessName
                                + lFieldsContainer.getName(), tableNameMaxSize);
            }
            else if (pContainer instanceof Field) {
                // For multi-valued field
                // <Parent table name>_<Field name>
                final Field lField = (Field) pContainer;
                final String lParentRevisionTableName =
                        formatName(
                                getDynamicRevisionTableName(lField.getContainer().getId()),
                                parentTableNameMaxSize);

                lRevisionTableName =
                        formatName(lParentRevisionTableName + NAME_SEPARATOR
                                + lField.getLabelKey(), tableNameMaxSize);
            }
            else {
                throw new RuntimeException("Unknow container type "
                        + pContainer.getClass());
            }

            // Check table name unicity for a specific revision table 
            if (revisionTableNameSet.contains(lRevisionTableName)) {
                throw new RuntimeException(
                        "Cannot create database element for " + lContainerId
                                + " : " + lRevisionTableName
                                + " revision table already exists");
            }

            // Store the value for not computed it twice
            revisionTableNameMaps.put(lContainerId, lRevisionTableName);
            revisionTableNameSet.add(lRevisionTableName);
        }

        return lRevisionTableName;
    }

    // Prefix of on column name -> assure that key word are not used
    private final static String COLUMN_PREFIX = "F_";

    /**
     * Compute the column's name associated to a field -> must be called before
     * used the associated getter
     * 
     * @param pField
     *            The field : mono-valued
     * @return The computed name
     */
    public String initDynamicColumnName(Field pField) {
        final FieldsContainer lFieldsContainer = pField.getContainer();
        final String lFieldId = pField.getId();
        // Don't compute column's name twice
        String lColumnName = columnNameMaps.get(lFieldId);

        if (lColumnName == null) {
            Set<String> lColumnNameSet =
                    columnNameSetByTable.get(lFieldsContainer.getId());

            // First generated column name for this table 
            if (lColumnNameSet == null) {
                lColumnNameSet = new HashSet<String>();
                columnNameSetByTable.put(lFieldsContainer.getId(),
                        lColumnNameSet);
            }

            // Compute column name the first time
            lColumnName =
                    formatName(COLUMN_PREFIX + pField.getLabelKey(),
                            columnNameMaxSize);
            // Check column name unicity for a specific table 
            if (lColumnNameSet.contains(lColumnName)) {
                throw new RuntimeException(
                        "Cannot create database element for " + lFieldId
                                + " : " + lColumnName
                                + " column already exists for table "
                                + tableNameMaps.get(lFieldsContainer.getId()));
            }

            // Store the value for not computed it twice
            columnNameMaps.put(lFieldId, lColumnName);
            lColumnNameSet.add(lColumnName);
        }

        return lColumnName;
    }

    /**
     * Format the process name
     * 
     * @param pBusinessProcess
     *            The name of business process
     * @return <Formatted process name>_
     */
    private String getFormattedProcessName(BusinessProcess pBusinessProcess) {
        final String lProcessId = pBusinessProcess.getId();
        // Don't compute process' name twice
        String lProcessName = processNameMaps.get(lProcessId);

        if (lProcessName == null) {
            // Compute process name the first time
            lProcessName =
                    formatName(pBusinessProcess.getName(), processNameMaxSize)
                            + NAME_SEPARATOR;
            // Store the value for not computed it twice
            processNameMaps.put(lProcessId, lProcessName);
        }

        return lProcessName;
    }

    // Only alphanumeric characters and '_' are supported by the data base
    private final static String NON_ALPHANUMERIC_CHAR_REGEX = "[^a-zA-Z0-9]";

    // Size of encoded part used to truncate string
    private final static int NB_ENCODING_CHAR_MAX = 3;

    // USed if the name starts by a specific character
    private final static char NAME_PREFIX = 'X';

    /**
     * Format a new for data base constraints
     * 
     * @param pName
     *            The name to format
     * @param pNbCharMax
     *            The number max of character
     * @return The formatted name
     */
    public String formatName(String pName, int pNbCharMax) {
        String lFormattedName = pName;

        // Truncate the name if two long
        if (lFormattedName.length() > pNbCharMax) {
            // Use encoding on the name
            int lEncodedNameSum = 0;

            for (byte lEncodedByte : encoder.digest(lFormattedName.getBytes())) {
                lEncodedNameSum += lEncodedByte;
            }

            // Convert encoding part to hexa
            String lEncodedNameToHexa = Integer.toHexString(lEncodedNameSum);

            // Truncate encode part too
            if (lEncodedNameToHexa.length() > NB_ENCODING_CHAR_MAX) {
                // Use the end of the string for not have only FFFFF...
                lEncodedNameToHexa =
                        lEncodedNameToHexa.substring(lEncodedNameToHexa.length()
                                - NB_ENCODING_CHAR_MAX);
            }

            // <Truncate part><Truncated encoded part>
            lFormattedName =
                    lFormattedName.substring(0, pNbCharMax
                            - lEncodedNameToHexa.length())
                            + lEncodedNameToHexa;
        }

        // Replace invalid characters
        lFormattedName =
                lFormattedName.toUpperCase().replaceAll(
                        NON_ALPHANUMERIC_CHAR_REGEX, NAME_SEPARATOR);

        // Can only begin start by an alphanumeric character
        if (lFormattedName.startsWith(NAME_SEPARATOR)) {
            lFormattedName =
                    NAME_PREFIX
                            + lFormattedName.substring(NAME_SEPARATOR.length());
        }

        return lFormattedName;
    }

    /**
     * Getter on the name of a table
     * 
     * @param pTableId
     *            The id of a fields container or a multi-valued field
     * @return The name of the table
     */
    public String getDynamicTableName(String pTableId) {
        final String lTableName = tableNameMaps.get(pTableId);

        // Table name must be initialized first
        if (lTableName == null) {
            throw new RuntimeException("Data base table for container "
                    + pTableId + " is not initialized");
        }

        return lTableName;
    }

    /**
     * Getter on the name of a revision table
     * 
     * @param pRevisionTableId
     *            The id of a fields container or a multi-valued field
     * @return The name of the revision table
     */
    public String getDynamicRevisionTableName(String pRevisionTableId) {
        final String lRevisionTableName =
                revisionTableNameMaps.get(pRevisionTableId);

        // Revision table name must be initialized first
        if (lRevisionTableName == null) {
            throw new RuntimeException(
                    "Data base revision table for container "
                            + pRevisionTableId + " is not initialized");
        }

        return lRevisionTableName;
    }

    /**
     * Getter on the name of a column
     * 
     * @param pColumnId
     *            The id of a mono-valued field
     * @return The name of the column
     */
    public String getDynamicColumnName(String pColumnId) {
        final String lColumnName = columnNameMaps.get(pColumnId);

        // Column name must be initialized first
        if (lColumnName == null) {
            throw new RuntimeException("Data base column for field "
                    + pColumnId + " is not initialized");
        }

        return lColumnName;
    }

    private final static String GETTER_PREFIX = "get";

    /**
     * Getter on the name of the getter on the name on a column
     * 
     * @param pColumnId
     *            The id of a mono-valued field
     * @return The name of the getter
     */
    public String getDynamicColumnGetterName(String pColumnId) {
        return GETTER_PREFIX + getDynamicColumnName(pColumnId);
    }

    private final static String SETTER_PREFIX = "set";

    /**
     * Getter on the name of the setter on the name on a column
     * 
     * @param pColumnId
     *            The id of a mono-valued field
     * @return The name of the setter
     */
    public String getDynamicColumnSetterName(String pColumnId) {
        return SETTER_PREFIX + getDynamicColumnName(pColumnId);
    }
}