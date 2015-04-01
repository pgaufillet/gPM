/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exportation.impl.ReadProperties;
import org.topcased.gpm.util.resources.BasicResourcesLoader;

/**
 * Migration options
 * 
 * @author nveillet
 */
public class MigrationOptions {

    private static final String ADMIN_LOGIN = "admin";

    private static final String HELP_OPT_DESCRIPTION = "Display help usage.";

    private static final String HELP_OPT_LONG = "help";

    private static final String HELP_OPT_SHORT = "h";

    private static final String PASSWD_OPT_DESCRIPTION = "User password";

    private static final String PASSWD_OPT_LONG = "password";

    private static final String PASSWD_OPT_NAME = "password";

    private static final String PASSWD_OPT_SHORT = "P";

    private static final String PROCESS_NAME_OPT_DESCRIPTION =
            "The name of process";

    private static final String PROCESS_NAME_OPT_LONG = "processName";

    private static final String PROCESS_NAME_OPT_NAME = "process name";

    private static final String PROCESS_NAME_OPT_SHORT = "N";

    private static final String PROPERTIES_OPT_DESCRIPTION =
            "The properties file";

    private static final String PROPERTIES_OPT_LONG = "properties";

    private static final String PROPERTIES_OPT_NAME = "properties";

    private static final String PROPERTIES_OPT_SHORT = "F";

    private static final String USER_OPT_DESCRIPTION =
            "The user login (default to 'admin')";

    private static final String USER_OPT_LONG = "user";

    private static final String USER_OPT_NAME = "user";

    private static final String USER_OPT_SHORT = "U";

    /**
     * Report an error.
     * 
     * @param pMsg
     *            Error message
     */
    private static void error(String pMsg, Object... pArguments) {
        String lMsg = MessageFormat.format(pMsg, pArguments);
        System.err.println("Error: " + lMsg);
        System.exit(1);
    }

    private String baseDirectory;

    private int consumerCount;

    private Map<MigrationElementType, Integer> elementCount;

    private Map<MigrationElementType, String> elementDirectories;

    private Map<String, String> dataLaundering = new HashMap<String, String>();

    private boolean exportAttachedFiles;

    private boolean exportErrorStop;

    private List<String> exportLimitedProductsName;

    private List<String> exportLimitedSheetTypes;

    private String fileProperties;

    private List<String> importDisableDirectories;

    private boolean importTypeControl;

    private final Options options = new Options();

    private String processName;

    private String roleToken;

    private String userLogin;

    private String userPassword;

    /**
     * Constructor
     * 
     * @param pArgs
     *            Arguments
     */
    @SuppressWarnings("static-access")
    public MigrationOptions(String[] pArgs) {

        options.addOption(OptionBuilder.withLongOpt(PROCESS_NAME_OPT_LONG).withDescription(
                PROCESS_NAME_OPT_DESCRIPTION).hasArg().withArgName(
                PROCESS_NAME_OPT_NAME).create(PROCESS_NAME_OPT_SHORT));

        options.addOption(OptionBuilder.withLongOpt(USER_OPT_LONG).withDescription(
                USER_OPT_DESCRIPTION).hasArg().withArgName(USER_OPT_NAME).create(
                USER_OPT_SHORT));

        options.addOption(OptionBuilder.withLongOpt(PASSWD_OPT_LONG).withDescription(
                PASSWD_OPT_DESCRIPTION).hasArg().withArgName(PASSWD_OPT_NAME).create(
                PASSWD_OPT_SHORT));

        options.addOption(OptionBuilder.withLongOpt(PROPERTIES_OPT_LONG).withDescription(
                PROPERTIES_OPT_DESCRIPTION).hasArg().withArgName(
                PROPERTIES_OPT_NAME).create(PROPERTIES_OPT_SHORT));

        options.addOption(HELP_OPT_SHORT, HELP_OPT_LONG, false,
                HELP_OPT_DESCRIPTION);

        parseArgs(pArgs);

        login();

        parseFileProperties();
    }

    /**
     * Display the usage help for this program.
     */
    private void displayUsage() {
        PrintWriter lWriter = new PrintWriter(System.out);
        displayUsage(lWriter);
    }

    /**
     * Display the usage help for this program.
     * 
     * @param pWriter
     *            Writer to use for text output.
     */
    private void displayUsage(final PrintWriter pWriter) {
        HelpFormatter lFormatter = new HelpFormatter();

        lFormatter.printHelp(pWriter, HelpFormatter.DEFAULT_WIDTH, "[NAME]", /* header */
        "", options, HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD, /* footer */"");

        pWriter.flush();
    }

    /**
     * get base directory
     * 
     * @return the base directory
     */
    public String getBaseDirectory() {
        return baseDirectory;
    }

    /**
     * get consumer thread count
     * 
     * @return the consumer thread count
     */
    public int getConsumerCount() {
        return consumerCount;
    }

    /**
     * get elements count
     * 
     * @return the elements count
     */
    public Map<MigrationElementType, Integer> getElementCount() {
        return elementCount;
    }

    /**
     * get elements directories
     * 
     * @return the elements directories
     */
    public Map<MigrationElementType, String> getElementDirectories() {
        return elementDirectories;
    }

    /**
     * get the limited products name for export
     * 
     * @return the limited products name
     */
    public List<String> getExportLimitedProductsName() {
        return exportLimitedProductsName;
    }

    /**
     * get the limited sheet types for export
     * 
     * @return the limited sheet types
     */
    public List<String> getExportLimitedSheetTypes() {
        return exportLimitedSheetTypes;
    }

    /**
     * get the disable directories for import
     * 
     * @return the disable directories
     */
    public List<String> getImportDisableDirectories() {
        return importDisableDirectories;
    }

    /**
     * get process name
     * 
     * @return the process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * get role token
     * 
     * @return the role token
     */
    public String getRoleToken() {
        return roleToken;
    }

    /**
     * Get dataLaundering Map This map contains the attribute name as a key, and
     * a boolean for the value. This map determines if the data exported needs
     * to be obfuscated
     * 
     * @return Map the map containing the pair key/value
     */
    public Map<String, String> getDataLaundering() {
        return dataLaundering;
    }

    /**
     * get user login
     * 
     * @return the user login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * get export attached files option
     * 
     * @return the export attached files option
     */
    public boolean isExportAttachedFiles() {
        return exportAttachedFiles;
    }

    /**
     * get export stop on error option
     * 
     * @return the export stop on error option
     */
    public boolean isExportErrorStop() {
        return exportErrorStop;
    }

    /**
     * get import type control option
     * 
     * @return the import type control option
     */
    public boolean isImportTypeControl() {
        return importTypeControl;
    }

    /**
     * Login the user
     */
    private void login() {
        AuthorizationService lAuthorizationService =
                ServiceLocator.instance().getAuthorizationService();

        String lUserToken = null;

        try {
            lUserToken = lAuthorizationService.login(userLogin, userPassword);
        }
        catch (GDMException e) {
            error("Invalid login or password.");
        }
        if (null == lUserToken) {
            error("Invalid login or password.");
        }

        roleToken = null;

        try {
            // Select the admin role.
            roleToken =
                    lAuthorizationService.selectRole(lUserToken,
                            AuthorizationService.ADMIN_ROLE_NAME, null,
                            processName);
        }
        catch (AuthorizationException e) {
            error("User does not have '" + AuthorizationService.ADMIN_ROLE_NAME
                    + "' privilege.");
        }
    }

    /**
     * Option error.
     * 
     * @param pMsg
     *            the message
     */
    private void optionError(final String pMsg) {
        System.err.println("Error: " + pMsg + "\n");
        displayUsage();
        System.exit(1);
    }

    /**
     * Check & parse the command line arguments.
     * 
     * @param pArgs
     *            Command line arguments
     */
    private void parseArgs(final String[] pArgs) {
        CommandLineParser lParser = new PosixParser();
        CommandLine lCmd = null;
        try {
            lCmd = lParser.parse(options, pArgs);
        }
        catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            System.exit(1);
        }

        if (null == lCmd) {
            System.err.println("Cannot parse command line options");
            System.exit(1);
            // Note: This useless return is only used to prevent a 'possible null' warning.
            return;
        }

        if (lCmd.hasOption(HELP_OPT_SHORT)) {
            displayUsage();
            System.exit(0);
        }

        userPassword = lCmd.getOptionValue(PASSWD_OPT_SHORT);
        processName = lCmd.getOptionValue(PROCESS_NAME_OPT_SHORT);
        userLogin = lCmd.getOptionValue(USER_OPT_SHORT, ADMIN_LOGIN);
        fileProperties = lCmd.getOptionValue(PROPERTIES_OPT_SHORT);

        if (null == userPassword) {
            optionError("The password for user login '" + userLogin
                    + "' is required.");
        }

        if (null == processName) {
            optionError("The name of process (instance) is required.");
        }

        if (null == fileProperties) {
            optionError("The file properties is required.");
        }
    }

    /**
     * Parse options on file properties
     */
    private void parseFileProperties() {
        // Load properties used to locate configuration
        Properties lProps = new Properties();

        InputStream lInputStream =
                new BasicResourcesLoader().getAsStream(fileProperties);
        if (null == lInputStream) {
            throw new GDMException("Cannot find " + fileProperties);
        }

        try {
            lProps.load(lInputStream);
        }
        catch (IOException e) {
            throw new GDMException(e);
        }

        // Consumer count
        if (lProps.getProperty("thread.count") == null) {
            error("Invalid property 'thread.count'.");
        }
        consumerCount = Integer.parseInt(lProps.getProperty("thread.count"));
        if (consumerCount < 1) {
            error("Invalid property 'thread.count'.");
        }

        // Initialize the dataLaundering map
        initializeDataLaundering(lProps);

        ReadProperties.getInstance().setPropertiesMap(dataLaundering);

        // Migration directory
        if (lProps.getProperty("directory") == null) {
            error("Invalid property 'directory'.");
        }
        baseDirectory = lProps.getProperty("directory");
        File lBaseDirectory = new File(baseDirectory);
        if (!lBaseDirectory.exists() || !lBaseDirectory.isDirectory()) {
            error("Invalid property 'directory'. Directory ''{0}'' is invalid",
                    lBaseDirectory);
        }

        // Items properties
        elementCount = new HashMap<MigrationElementType, Integer>();
        elementDirectories = new HashMap<MigrationElementType, String>();
        for (MigrationElementType lElementType : MigrationElementType.values()) {
            if (!lElementType.equals(MigrationElementType.ATTACHED_FILE)) {
                elementCount.put(
                        lElementType,
                        Integer.parseInt(lProps.getProperty(
                                lElementType.getName() + ".export.count", "20")));
            }
            else {
                exportAttachedFiles =
                        Boolean.parseBoolean(lProps.getProperty(
                                lElementType.getName() + ".export",
                                Boolean.TRUE.toString()));
            }

            // Get the directory
            String lItemDirectory =
                    lProps.getProperty(lElementType.getName() + ".directory",
                            lElementType.getDefaultDirectory());

            if (elementDirectories.values().contains(lItemDirectory)) {
                error("Invalid property '" + lElementType.getName()
                        + ".directory'. Directory ''{0}'' already used.",
                        lItemDirectory);
            }

            elementDirectories.put(lElementType, lItemDirectory);
        }

        // Exportation stop option
        exportErrorStop =
                Boolean.parseBoolean(lProps.getProperty("export.errorstop",
                        Boolean.FALSE.toString()));

        // Exportation limitation sheet types
        exportLimitedSheetTypes =
                new ArrayList<String>(Arrays.asList(lProps.getProperty(
                        "export.limited.sheettypes", StringUtils.EMPTY).split(
                        ",")));
        exportLimitedSheetTypes.remove(StringUtils.EMPTY);

        // Exportation limitation products name
        exportLimitedProductsName =
                new ArrayList<String>(Arrays.asList(lProps.getProperty(
                        "export.limited.productname", StringUtils.EMPTY).split(
                        ",")));
        exportLimitedProductsName.remove(StringUtils.EMPTY);

        // Importation options
        importTypeControl =
                Boolean.parseBoolean(lProps.getProperty("import.typecontrol",
                        Boolean.TRUE.toString()));
        importDisableDirectories = new ArrayList<String>();
        for (String lDirectory : elementDirectories.values()) {
            if (!Boolean.parseBoolean(lProps.getProperty("import.directory."
                    + lDirectory, Boolean.TRUE.toString()))) {
                importDisableDirectories.add(lDirectory);
            }
        }
    }

    /**
     * Initialize the dataLaundering map according to the given properties
     * 
     * @param pProperties
     *            the migration properties
     */
    private void initializeDataLaundering(Properties pProperties) {
        initializeDataLaunderingProperties(pProperties, ReadProperties.ATTACHED_FILES);
        initializeDataLaunderingProperties(pProperties, ReadProperties.DICTIONARY);
        initializeDataLaunderingProperties(pProperties, ReadProperties.ENVIRONMENTS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.FILTERS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.PRODUCTS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.PRODUCT_LINKS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.SHEETS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.SHEET_LINKS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.USERS);
        initializeDataLaunderingProperties(pProperties, ReadProperties.PRODUCT_MAPPING);
        dataLaundering.put(ReadProperties.DIRECTORY,
                pProperties.getProperty("directory"));
    }

    /**
     * Initialize the dataLaundering for a specific dataType
     * @param pProperties the migration properties
     */
    private void initializeDataLaunderingProperties(Properties pProperties, String pDataType) {
        dataLaundering.put(pDataType, Boolean.toString(isObfuscated(
                pProperties, pDataType)));
    }

    private boolean isObfuscated(Properties pProperties, String pDataType) {
        return !(pProperties.getProperty(pDataType) == null)
                && (Boolean.parseBoolean(pProperties.getProperty(pDataType)));
    }
}