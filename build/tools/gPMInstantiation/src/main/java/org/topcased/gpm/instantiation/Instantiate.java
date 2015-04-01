/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.topcased.dbutils.DbUtils;
import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.process.service.BusinessProcessData;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.business.transformation.service.DataTransformationService;
import org.topcased.gpm.instantiation.options.InstantiateOptions;
import org.topcased.gpm.util.session.GpmSessionFactory;

/**
 * Main class of the instance creation tool.
 * 
 * @author llatil
 */
public final class Instantiate {
    /** MILLISEC_CONVERSION */
    private static final int MILLISEC_CONVERSION = 1000;

    /**
     * Constructs a new instantiate.
     */
    private Instantiate() {
    }

    /**
     * Report an error.
     * 
     * @param pMsg
     *            Error message
     */
    private static void error(String pMsg) {
        System.err.println("Error: " + pMsg);
        System.exit(1);
    }

    /**
     * Initialize the database structure and create the administrator user.
     * 
     * @param pAdminPwd
     *            Admin password.
     */
    public static void createAdminUser(String pAdminPwd) {
        ServiceLocator lServiceLocator = ServiceLocator.instance();
        AuthorizationService lAuthService =
                lServiceLocator.getAuthorizationService();

        try {
            lAuthService.createAdminUser(pAdminPwd);
        }
        catch (AuthorizationException e) {
            error("Administrator user 'admin' already exists. Initialization disabled.");
        }

        System.out.println("Administrator user 'admin' created.");
    }

    /**
     * Initialize the database structure and create the administrator user.
     * 
     * @param pOpts
     *            Options container.
     */
    private static void initDb(InstantiateOptions pOpts) {
        final GpmSessionFactory lGpmSessionFactory =
                GpmSessionFactory.getInstance();

        // Create data base schema only if necessary
        if (!lGpmSessionFactory.hasTables()) {
            lGpmSessionFactory.dropDatabaseSchema();
            lGpmSessionFactory.createDatabaseSchema();
        }
        // Create administrator user
        createAdminUser(pOpts.getUserPwd());
    }

    /**
     * Export data base schema including dynamic model part on DDL format
     * 
     * @param pOpts
     *            Options container
     */
    public static void exportDbSchema(InstantiateOptions pOpts) {
        final GpmSessionFactory lGpmSessionFactory =
                GpmSessionFactory.getInstance();

        createDbScriptFile("CreateStatements.ddl",
                lGpmSessionFactory.getDynamicSchemaCreationScript());
        createDbScriptFile("DropStatements.ddl",
                lGpmSessionFactory.getDynamicDropSchemaScript());
        System.out.println("Data base schema has been exported");
    }

    private static void createDbScriptFile(String pFileName,
            List<String> pScriptCommands) {
        final File lCreateStatementsFile = new File(pFileName);

        try {

            final OutputStream lCreateStatementsStream =
                    new FileOutputStream(lCreateStatementsFile);
            try {
                if (!lCreateStatementsFile.exists()) {
                    lCreateStatementsFile.createNewFile();
                }
                if (!lCreateStatementsFile.canWrite()) {
                    throw new GDMException(
                            lCreateStatementsFile.getAbsolutePath()
                                    + " can not be written");
                }
                for (String lCommand : pScriptCommands) {
                    lCreateStatementsStream.write(lCommand.getBytes());
                    lCreateStatementsStream.write(';');
                    lCreateStatementsStream.write('\n');
                }
            }
            catch (IOException lIOEx) {
                throw new GDMException(lCreateStatementsFile.getAbsolutePath()
                        + " can not be created");
            }
            finally {
                lCreateStatementsStream.close();
            }
        }
        catch (IOException lIOEx) {
            throw new GDMException(lCreateStatementsFile.getAbsolutePath()
                    + " can not be closed");
        }

        System.out.println(lCreateStatementsFile.getAbsolutePath()
                + " file has been generated");
    }

    /**
     * Generate the dynamic schema
     * 
     * @param pOpts
     *            Options container.
     */
    public static void generateDynamicDbSchema(InstantiateOptions pOpts) {
        long lTimeMillis = System.currentTimeMillis();
        String lLoginToken = null;
        AuthorizationService lAuthService =
                ServiceLocator.instance().getAuthorizationService();

        try {
            lLoginToken =
                    lAuthService.login(pOpts.getUserLogin(), pOpts.getUserPwd());
        }
        catch (GDMException e) {
            error("Invalid login or password.");
        }
        if (null == lLoginToken) {
            error("Invalid login or password.");
        }

        GpmSessionFactory.getInstance().updateDatabaseSchema();

        lTimeMillis = System.currentTimeMillis() - lTimeMillis;
        System.out.println("Dynamic schema generated successfully in "
                + lTimeMillis / MILLISEC_CONVERSION + " seconds.");
    }

    /**
     * Populate the Database.
     * 
     * @param pOpts
     *            Options container.
     */
    public static void populateDb(final InstantiateOptions pOpts) {
        System.out.println("Generating " + pOpts.getAutoCreatedSheetsCount()
                + " sheets in the '" + pOpts.getProductName() + "' product...");

        long lTimeMillis = System.currentTimeMillis();

        String lLoginToken = null;
        String lRoleToken = null;

        ServiceLocator lServiceLocator = ServiceLocator.instance();
        AuthorizationService lAuthService =
                lServiceLocator.getAuthorizationService();

        try {
            lLoginToken =
                    lAuthService.login(pOpts.getUserLogin(), pOpts.getUserPwd());
        }
        catch (GDMException e) {
            error("Invalid password.");
        }
        if (null == lLoginToken) {
            error("Invalid password.");
        }
        // Select the admin role.
        lRoleToken =
                lAuthService.selectRole(lLoginToken,
                        AuthorizationService.ADMIN_ROLE_NAME, null,
                        pOpts.getProcessName());

        SheetService lSheetServ = lServiceLocator.getSheetService();
        DataTransformationService lTransService =
                lServiceLocator.getDataTransformationService();

        SheetsPopulater lSheetsPopulater = new SheetsPopulater(lRoleToken);
        lSheetsPopulater.populate(lSheetServ, lTransService,
                pOpts.getProcessName(), pOpts.getTypeName(),
                pOpts.getProductName(), pOpts.getAutoCreatedSheetsCount());

        lTimeMillis = System.currentTimeMillis() - lTimeMillis;
        System.out.println(pOpts.getAutoCreatedSheetsCount()
                + " sheets created successfully in " + lTimeMillis
                / MILLISEC_CONVERSION + " seconds.");
    }

    /**
     * Create or update a business process instance.
     * 
     * @param pOpts
     *            Options container.
     */
    public static void createInstance(InstantiateOptions pOpts) {
        long lTimeMillis = System.currentTimeMillis();
        String lLoginToken = null;
        String lRoleToken = null;

        ServiceLocator lServiceLocator = ServiceLocator.instance();
        AuthorizationService lAuthService =
                lServiceLocator.getAuthorizationService();
        InstanceService lInstanceService = lServiceLocator.getInstanceService();

        try {
            lLoginToken =
                    lAuthService.login(pOpts.getUserLogin(), pOpts.getUserPwd());
        }
        catch (GDMException e) {
            error("Invalid login or password.");
        }
        if (null == lLoginToken) {
            error("Invalid login or password.");
        }

        BusinessProcessData lProcessData =
                new BusinessProcessData(pOpts.getProcessName());
        lInstanceService.createBusinessProcess(lLoginToken, lProcessData);

        // Assign the admin role to the admin user for this process.
        // Admin user has the admin role for all processes created (as we need
        // at least one user having a role for all processes).
        lAuthService.addRole(lLoginToken, AuthorizationService.ADMIN_LOGIN,
                pOpts.getProcessName(), AuthorizationService.ADMIN_ROLE_NAME);

        try {
            // Select the admin role.
            lRoleToken =
                    lAuthService.selectRole(lLoginToken,
                            AuthorizationService.ADMIN_ROLE_NAME, null,
                            pOpts.getProcessName());
        }
        catch (AuthorizationException e) {
            error("User does not have 'admin' rights.");
        }

        if (pOpts.hasExtendedOption(InstantiateOptions.DELETE_ACCESS_OPTION)) {
            System.out.println("Delete all access controls defined for instance '"
                    + pOpts.getProcessName() + "' !");
            lAuthService.deleteAllAccessControls(lLoginToken,
                    pOpts.getProcessName());
        }

        Context lInstantiationContext = createContext(pOpts);

        InstanceCreator lInstanceCreator =
                new InstanceCreator(lRoleToken, lInstantiationContext,
                        pOpts.isSyntaxChecked());
        for (String lFilename : pOpts.getFilenames()) {
            lInstanceCreator.parseXML(pOpts.getProcessName(), lFilename);
        }

        lTimeMillis = System.currentTimeMillis() - lTimeMillis;
        System.out.println("Instance created / updated successfully in "
                + lTimeMillis / MILLISEC_CONVERSION + " seconds.");
    }

    private static Context createContext(final InstantiateOptions pOpts) {
        Context lInstantiationContext = Context.getEmptyContext();

        if (pOpts.isExtensionsDisabled()) {
            lInstantiationContext.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);
        }
        else {
            if (!pOpts.getDisabledExtensions().isEmpty()) {
                lInstantiationContext.put(Context.GPM_SKIP_EXT_PTS,
                        pOpts.getDisabledExtensions());
            }
        }

        if (!pOpts.getDisabledCommands().isEmpty()) {
            lInstantiationContext.put(Context.GPM_SKIP_COMMANDS,
                    pOpts.getDisabledCommands());
        }

        if (pOpts.hasExtendedOption(InstantiateOptions.DELETE_CATEGORY_VALUES_OPTION)) {
            lInstantiationContext.put(
                    InstantiateOptions.DELETE_CATEGORY_VALUES_OPTION, true);
        }
        else {
            lInstantiationContext.put(
                    InstantiateOptions.DELETE_CATEGORY_VALUES_OPTION, false);
        }

        // Set in the instantiation context, if the dynamic part of the schema
        // must be generated
        lInstantiationContext.put(
                InstantiateOptions.AUTO_GENERATE_DYNAMIC_MODEL,
                Boolean.valueOf(pOpts.isAutoGenerateDynamicDb()));

        return lInstantiationContext;
    }

    /**
     * Drop the tables and reset dynamic session
     * 
     * @param pPropertyFileURL
     *            URL of the properties config file.
     */
    public static void dropDataBase(String pPropertyFileURL) {
        final DbUtils lDbUtils = new DbUtils();

        lDbUtils.dropTables(pPropertyFileURL);
    }

    /**
     * Application entry point.
     * 
     * @param pArgs
     *            Cmd line arguments
     */
    public static void main(String[] pArgs) {
        final InstantiateOptions lOpts = new InstantiateOptions(pArgs);

        if (lOpts.isInitDb()) {
            initDb(lOpts);
        }
        else if (lOpts.isPopulateDb()) {
            populateDb(lOpts);
        }
        else if (lOpts.isExportDynamicSchema()) {
            exportDbSchema(lOpts);
        }
        // The dynamic model can be generate during a file instantiation
        else if (lOpts.isAutoGenerateDynamicDb()
                && lOpts.getFilenames().isEmpty()) {
            generateDynamicDbSchema(lOpts);
        }
        else {
            createInstance(lOpts);
        }
    }
}
