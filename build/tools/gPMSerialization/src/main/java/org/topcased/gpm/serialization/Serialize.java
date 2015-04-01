/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Laurent Latil (Atos Origin), Sophian Idjellidaine (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.serialization;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.topcased.gpm.business.ServiceLocator;
import org.topcased.gpm.business.authorization.service.AuthorizationService;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.exception.GDMException;
import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.business.exportation.service.ExportService;
import org.topcased.gpm.business.extensions.service.Context;
import org.topcased.gpm.business.importation.ImportProperties;
import org.topcased.gpm.business.importation.ImportProperties.ImportFlag;
import org.topcased.gpm.business.importation.service.ImportService;
import org.topcased.gpm.business.instance.service.InstanceService;
import org.topcased.gpm.business.process.service.BusinessProcessData;
import org.topcased.gpm.serialization.options.SerializeOptions;

/**
 * Main class of the serialization tool.
 * 
 * @author llatil
 * @deprecated
 * @since 1.8.4
 */
public final class Serialize {

    private ServiceLocator serviceLocator;

    private AuthorizationService authService;

    private SerializeOptions options;

    private InstanceService instanceService;

    /**
     * Constructs a new instantiate object.
     */
    private Serialize(String[] pArgs) {
        options = new SerializeOptions(pArgs);

        serviceLocator = ServiceLocator.instance();
        authService = serviceLocator.getAuthorizationService();
        instanceService = serviceLocator.getInstanceService();
    }

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

    private String login() {
        String lLoginToken = null;

        try {
            lLoginToken =
                    authService.login(options.getUserLogin(),
                            options.getUserPwd());
        }
        catch (GDMException e) {
            error("Invalid login or password.");
        }
        if (null == lLoginToken) {
            error("Invalid login or password.");
        }
        return lLoginToken;
    }

    /**
     * Export the content of a gPM instance
     */
    public void importContent() {
        final String lLoginToken = login();
        String lRoleToken = null;

        Context lCtx = createContext();
        lCtx.put(Context.GPM_SKIP_EXT_PTS, Boolean.TRUE);

        final ImportService lImportService = serviceLocator.getImportService();

        BusinessProcessData lInstanceData =
                new BusinessProcessData(options.getProcessName());
        instanceService.createBusinessProcess(lLoginToken, lInstanceData);

        try {
            // Select the admin role.
            lRoleToken =
                    authService.selectRole(lLoginToken,
                            AuthorizationService.ADMIN_ROLE_NAME, null,
                            options.getProcessName());
        }
        catch (AuthorizationException e) {
            error("User does not have 'admin' privilege.");
        }

        InputStream lInStream = null;
        try {
            lInStream = new FileInputStream(options.getFilename());
        }
        catch (IOException e) {
            error("Cannot open file '" + options.getFilename() + "'");
        }

        ImportProperties lImportProperties = new ImportProperties();
        lImportProperties.setAllFlags(ImportFlag.ERASE);
        try {
            lImportService.importData(lRoleToken, lInStream, lImportProperties,
                    lCtx);
        }
        catch (SchemaValidationException e) {
            error("Invalid schema: " + e.getMessage());
        }
        catch (ImportException e) {
            error("Importation error: " + e.getMessage() + " on element "
                    + e.getObject());
        }
    }

    /**
     * Do the import or export
     */
    public void doImportExport() {
        if (options.isImport()) {
            System.err.println("Warning: "
                    + "Import part of ''Serialize'' tool is deprecated. "
                    + "See ''Import'' tool.");
            importContent();
        }
        else {
            error("Export part of ''Serialize'' tool is deprecated and bloqued. "
                    + "Use ''Export'' tool.");
        }
    }

    private Context createContext() {
        Context lContext = Context.getEmptyContext();

        if (options.getProductNames() != null) {
            lContext.put(ExportService.CONTEXT_PRODUCT_NAMES,
                    options.getProductNames());
        }
        if (options.getSheetTypeNames() != null) {
            lContext.put(ExportService.CONTEXT_SHEET_TYPE_NAMES,
                    options.getSheetTypeNames());
        }
        return lContext;
    }

    /**
     * Application entry point.
     * 
     * @param pArgs
     *            Cmd line arguments
     */
    public static void main(String[] pArgs) {
        Serialize lSerializer = new Serialize(pArgs);

        lSerializer.doImportExport();
    }
}
