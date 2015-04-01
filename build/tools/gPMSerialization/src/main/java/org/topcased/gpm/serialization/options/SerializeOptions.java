/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization.options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.exportation.service.ExportService;

/**
 * Class used to handle the command line options.
 * 
 * @author llatil
 */
public final class SerializeOptions {
    private static final int LINE_NUMBER = 20;

    private static final String HELP_LONG_OPT = "help";

    private static final String IMPORT_LONG_OPT = "import";

    private static final String EXPORT_FILES_PATH_LONG_OPT =
            "exportedFilesPath";

    private static final String EXPORT_LONG_OPT = "export";

    private static final String PRODUCT_NAMES_LONG_OPT = "productNames";

    private static final String TYPE_NAMES_LONG_OPT = "typeNames";

    private static final String PROCESS_NAME_LONG_OPT = "processName";

    private static final String PASSWD_LONG_OPT = "passwd";

    private static final String USER_LONG_OPT = "user";

    private static final String KIND_LONG_OPT = "kind";

    /** Default Admin login. */
    static private final String ADMIN_LOGIN = "admin";

    /** The options. */
    private final Options options = new Options();

    /** Filename for the exported / imported content */
    private String filename;

    private String filesContentDirectory;

    /** The user login. */
    private String userLogin;

    /** The user password. */
    private String userPwd;

    /** The process name. */
    private String processName;

    /** List of product names. */
    private List<String> productNames;

    /** List of sheet types */
    private List<String> sheetTypeNames;

    private long exportFlags = ExportService.EXPORT_ALL;

    public final long getExportFlags() {
        return exportFlags;
    }

    /** The init db. */
    private boolean initDb = false;

    /** True if invoked to import an XML content */
    private boolean doImport = false;

    /** True if invoked to export content into an XML file */
    private boolean doExport = false;

    static public final String DELETE_ACCESS_OPTION = "delete_acl";

    public static final String DELETE_CATEGORY_VALUES_OPTION =
            "delete_catValues";

    public static final String DISABLE_XML_SCHEMA = "disable_schema_parsing";

    static private Collection<KindOption> staticKindOptsDef =
            new LinkedHashSet<KindOption>();

    static final String ALL_KIND_OPTION = "all";

    static final String SHEET_KIND_OPTION = "sheets";

    static final String PRODUCT_KIND_OPTION = "products";

    static final String LINK_KIND_OPTION = "links";

    static final String REVISION_KIND_OPTION = "revisions";

    static final String ATTACHED_FILE_KIND_OPTION = "attachedFiles";

    static final String USER_KIND_OPTION = "users";

    static final String DICTIONARY_OPTION = "dictionary";

    static final String FILTER_KIND_OPTION = "filter";

    static {
        createKindOption(ALL_KIND_OPTION,
                "Export any element  (default value)", ExportService.EXPORT_ALL);

        createKindOption(SHEET_KIND_OPTION, "Export sheets",
                ExportService.EXPORT_SHEETS);

        createKindOption(PRODUCT_KIND_OPTION, "Export products",
                ExportService.EXPORT_PRODUCTS);
        createKindOption(
                LINK_KIND_OPTION,
                "Export links (applicable only when sheets / products are exported)",
                ExportService.EXPORT_LINKS);
        createKindOption(REVISION_KIND_OPTION, "Export revisions",
                ExportService.EXPORT_REVISIONS);

        createKindOption(
                ATTACHED_FILE_KIND_OPTION,
                "Attached files content (applicable only if sheets / products / revisions are exported)",
                ExportService.EXPORT_FILE_CONTENT);

        createKindOption(USER_KIND_OPTION,
                "Export users and users roles affectations",
                ExportService.EXPORT_USERS);

        createKindOption(DICTIONARY_OPTION,
                "Export all categories and environments",
                ExportService.EXPORT_DICTIONARY);

        createKindOption(FILTER_KIND_OPTION, "Export filters",
                ExportService.EXPORT_FILTERS);
    }

    static void createKindOption(String pName, String pDescr, long pFlag) {
        staticKindOptsDef.add(new KindOption(pName, pDescr, pFlag));
    }

    /**
     * Constructs a new options container. This class parses the cmd line
     * arguments, validate them, and make the options available through getters
     * functions.
     * 
     * @param pArgs
     *            Command line arguments
     */
    @SuppressWarnings("static-access")
    public SerializeOptions(String[] pArgs) {

        options.addOption(OptionBuilder.withLongOpt(USER_LONG_OPT).withDescription(
                "The user login (default to 'admin')").hasArg().withArgName(
                "user").create('U'));
        options.addOption(OptionBuilder.withLongOpt(PASSWD_LONG_OPT).withDescription(
                "User password").hasArg().withArgName("password").create('P'));

        options.addOption(OptionBuilder.withLongOpt(PROCESS_NAME_LONG_OPT).withDescription(
                "The name of process to export").hasArg().withArgName(
                "instance name").create('N'));

        options.addOption(OptionBuilder.withLongOpt(TYPE_NAMES_LONG_OPT).withDescription(
                "List of sheet type names to consider").hasArgs().withArgName(
                "type name").withValueSeparator(',').create('T'));
        options.addOption(OptionBuilder.withLongOpt(PRODUCT_NAMES_LONG_OPT).withDescription(
                "List of product names to consider").hasArgs().withArgName(
                "product name").withValueSeparator(',').create('p'));

        options.addOption(OptionBuilder.withLongOpt(EXPORT_LONG_OPT).withDescription(
                "Export instance in an XML file").hasArgs().withArgName(
                "filename").create('f'));

        options.addOption(OptionBuilder.withLongOpt(EXPORT_FILES_PATH_LONG_OPT).withDescription(
                "Path to use for attached files exported content").hasArgs().withArgName(
                "directory").create('D'));

        options.addOption(OptionBuilder.withLongOpt(IMPORT_LONG_OPT).withDescription(
                "Import instance from an XML file").hasArgs().withArgName(
                "filename").create('r'));

        options.addOption(OptionBuilder.withLongOpt(KIND_LONG_OPT).withDescription(
                "Kind of elements to export").hasArgs().withValueSeparator(',').withArgName(
                "Kind list").create('k'));

        options.addOption("h", HELP_LONG_OPT, false, "Display help usage.");

        parseOptions(pArgs);
    }

    /**
     * Constructs a new instantiate options.
     */
    public SerializeOptions() {
    }

    /**
     * Check & parse the command line arguments.
     * 
     * @param pArgs
     *            Command line arguments
     */
    private void parseOptions(String[] pArgs) {
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

        if (lCmd.hasOption('h')) {
            displayUsage();
            System.exit(0);
        }

        userPwd = lCmd.getOptionValue('P');
        processName = lCmd.getOptionValue('N');
        userLogin = lCmd.getOptionValue('U', ADMIN_LOGIN);

        filesContentDirectory = lCmd.getOptionValue('D');

        if (null == userPwd) {
            optionError("The password for user login '" + userLogin
                    + "' is required.");
        }

        if (null == processName) {
            optionError("The name of process (instance) is required.");
        }

        if (null != lCmd.getOptionValue('f')) {
            filename = lCmd.getOptionValue('f');
            doExport = true;
        }
        if (null != lCmd.getOptionValue('r')) {
            filename = lCmd.getOptionValue('r');
            doImport = true;
            if (doExport) {
                optionError(MessageFormat.format(
                        "Options ''{0}'' & ''{1}'' are exclusive",
                        EXPORT_LONG_OPT, IMPORT_LONG_OPT));
            }
        }

        if (!doImport && !doExport) {
            optionError("One of the options ''{0}'' & ''{1}'' must be specified");
        }
        if (lCmd.hasOption('p')) {
            productNames = getValues(lCmd.getOptionValues('p'));
        }

        if (lCmd.hasOption('T')) {
            sheetTypeNames = getValues(lCmd.getOptionValues('T'));
        }

        if (lCmd.hasOption('k')) {
            Collection<String> lKindOptions =
                    Arrays.asList(lCmd.getOptionValues('k'));
            exportFlags = getKindOptionsFlags(lKindOptions);
        }
    }

    /**
     * Display the usage help for this program.
     */
    public void displayUsage() {
        PrintWriter lWriter = new PrintWriter(System.out);
        displayUsage(lWriter);
    }

    /**
     * Display the usage help for this program.
     * 
     * @param pWriter
     *            Writer to use for text output.
     */
    public void displayUsage(PrintWriter pWriter) {
        HelpFormatter lFormatter = new HelpFormatter();

        lFormatter.printHelp(pWriter, HelpFormatter.DEFAULT_WIDTH, "serialize", /* header */
        "", options, HelpFormatter.DEFAULT_LEFT_PAD,
                HelpFormatter.DEFAULT_DESC_PAD, /* footer */"");

        lFormatter.printWrapped(pWriter, HelpFormatter.DEFAULT_WIDTH,
                "\nKind list of elements to export: ");
        for (AdditionalOption lAddOpt : staticKindOptsDef) {
            StringBuilder lSb = new StringBuilder();
            lSb.append(' ');
            lSb.append(lAddOpt.getOptionName());
            lSb.append('\t');
            lSb.append(lAddOpt.getDescription());

            lFormatter.printWrapped(pWriter, HelpFormatter.DEFAULT_WIDTH,
                    LINE_NUMBER, lSb.toString());
        }
        pWriter.flush();
    }

    /**
     * Option error.
     * 
     * @param pMsg
     *            the msg
     */
    private void optionError(String pMsg) {
        System.err.println("Error: " + pMsg + "\n");
        displayUsage();
        System.exit(1);
    }

    /**
     * Checks if is inits the db.
     * 
     * @return true, if is inits the db
     */
    public boolean isInitDb() {
        return initDb;
    }

    /**
     * Sets the inits the db.
     * 
     * @param pInitDb
     *            the new inits the db
     */
    public void setInitDb(boolean pInitDb) {
        initDb = pInitDb;
    }

    /**
     * Gets the process name.
     * 
     * @return the process name
     */
    public String getProcessName() {
        return processName;
    }

    /**
     * Sets the process name.
     * 
     * @param pProcessName
     *            the new process name
     */
    public void setProcessName(String pProcessName) {
        processName = pProcessName;
    }

    /**
     * Sets the user.
     * 
     * @param pLogin
     *            the login
     * @param pPwd
     *            the pwd
     */
    public void setUser(String pLogin, String pPwd) {
        setUserLogin(pPwd);
        setUserPassword(pPwd);
    }

    /**
     * Gets the user login.
     * 
     * @return the user login
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * Sets the user login.
     * 
     * @param pUserLogin
     *            the new user login
     */
    public void setUserLogin(String pUserLogin) {
        userLogin = pUserLogin;
    }

    /**
     * Gets the user pwd.
     * 
     * @return the user pwd
     */
    public String getUserPwd() {
        return userPwd;
    }

    /**
     * Gets the type name.
     * 
     * @return the type name
     */
    public List<String> getSheetTypeNames() {
        return sheetTypeNames;
    }

    /**
     * Sets the type name.
     * 
     * @param pNames
     *            List of sheet type names
     */
    public void setSheetTypeNames(List<String> pNames) {
        sheetTypeNames = pNames;
    }

    /**
     * Gets the product name.
     * 
     * @return the product name
     */
    public List<String> getProductNames() {
        return productNames;
    }

    /**
     * Sets the list of product names to consider.
     * 
     * @param pProductNames
     *            List of product names
     */
    public final void setProductNames(List<String> pProductNames) {
        productNames = pProductNames;
    }

    /**
     * Sets the user password.
     * 
     * @param pUserPwd
     *            the new user password
     */
    public void setUserPassword(String pUserPwd) {
        userPwd = pUserPwd;
    }

    public final boolean isImport() {
        return doImport;
    }

    public final boolean isExport() {
        return doExport;
    }

    /** The Constant FILE_PREFIX. */
    static final String FILE_PREFIX = "@";

    /** The Constant COMMENT_PREFIX. */
    static final String COMMENT_PREFIX = "#";

    /**
     * Gets the values.
     * 
     * @param pOpt
     *            the opt
     * @return the values
     */
    private List<String> getValues(String[] pRawValues) {
        if (null == pRawValues || pRawValues.length == 0) {
            return Collections.emptyList();
        }

        List<String> lResult = new ArrayList<String>();

        for (String lCurrentValue : pRawValues) {
            if (lCurrentValue.startsWith(FILE_PREFIX)) {
                String lFilename =
                        lCurrentValue.substring(FILE_PREFIX.length());

                File lFile = new File(lFilename);
                if (!lFile.exists()) {
                    optionError("File '" + lFilename + "' not found");
                }
                if (!lFile.canRead()) {
                    optionError("Cannot read file '" + lFilename + "'");
                }
                try {
                    InputStream lIs = new FileInputStream(lFile);

                    InputStreamReader lReader = new InputStreamReader(lIs);
                    BufferedReader lBufferedReader = new BufferedReader(lReader);

                    String lLineValue;
                    do {
                        lLineValue = lBufferedReader.readLine();
                        if (null != lLineValue) {
                            lLineValue = lLineValue.trim();
                            if (StringUtils.isNotBlank(lLineValue)
                                    && !lLineValue.startsWith(COMMENT_PREFIX)) {
                                lResult.add(lLineValue);
                            }
                        }
                    } while (lLineValue != null);
                    lBufferedReader.close();
                }
                catch (FileNotFoundException e) {
                    optionError("File '" + lFilename + "' not found");
                }
                catch (IOException e) {
                    optionError("Cannot read file '" + lFilename + "'");
                }
            }
            else {
                lResult.add(lCurrentValue);
            }
        }
        return lResult;
    }

    public final String getFilename() {
        return filename;
    }

    public final void setFilename(String pFilename) {
        filename = pFilename;
    }

    public final String getFilesContentDirectory() {
        return filesContentDirectory;
    }

    public final void setFilesContentDirectory(String pFilesContentDirectory) {
        filesContentDirectory = pFilesContentDirectory;

    }

    private long getKindOptionsFlags(Collection<String> pKindOptions) {
        long lFlag = 0;

        for (String lOpt : pKindOptions) {
            boolean lOptValid = false;
            for (KindOption lOptionDef : staticKindOptsDef) {
                if (lOptionDef.equals(lOpt)) {
                    lOptValid = true;
                    lFlag |= lFlag | lOptionDef.getFlag();
                    continue;
                }
            }
            if (!lOptValid) {
                optionError("Invalid additional option '" + lOpt + "'");
            }
        }
        if (0 == lFlag) {
            return ExportService.EXPORT_ALL;
        } // else
        return lFlag;
    }
}
