/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.exportimport;

import java.io.InputStream;

import org.topcased.gpm.business.exception.ImportException;
import org.topcased.gpm.business.exception.SchemaValidationException;
import org.topcased.gpm.ui.facade.AbstractFacadeTestCase;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.exportimport.ExportImportFacade;
import org.topcased.gpm.util.resources.BasicResourcesLoader;
import org.topcased.gpm.util.resources.IResourcesLoader;

/**
 * TestExportFilterResultFacade
 * 
 * @author jlouisy
 */
public class TestImportProductFacade extends AbstractFacadeTestCase {

    private static final String IMPORTED_PRODUCT_NAME = "PRODUCT_TO_IMPORT";

    private ExportImportFacade lExportImportFacade;

    private UiSession lUiSession;

    /**
     * Normal case
     */
    public void testNormalCase() {

        lUiSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        lExportImportFacade = getFacadeLocator().getExportImportFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Import product.");
        }
        InputStream lInputStream;
        try {

            IResourcesLoader lResourcesLoader = new BasicResourcesLoader();
            lInputStream =
                    lResourcesLoader.getAsStream("data/products/products_to_import.xml");

            lExportImportFacade.importProduct(lUiSession, lInputStream);

            assertTrue(getProductService().isProductExists(
                    lUiSession.getRoleToken(), IMPORTED_PRODUCT_NAME));
        }
        catch (SchemaValidationException e) {
            fail(e);
        }
        catch (ImportException e) {
            fail(e);
        }
    }

    /**
     * Not only products test.
     */
    public void testNotOnlyProductsInFileCase() {

        lUiSession = adminUserSession.getSession(DEFAULT_PRODUCT_NAME);
        lExportImportFacade = getFacadeLocator().getExportImportFacade();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Import product.");
        }
        InputStream lInputStream;
        try {

            IResourcesLoader lResourcesLoader = new BasicResourcesLoader();
            lInputStream =
                    lResourcesLoader.getAsStream("data/products/products_to_import_with_userRoles.xml");

            lExportImportFacade.importProduct(lUiSession, lInputStream);

            fail("Expected Exception : import.exception.bad.type");
        }
        catch (SchemaValidationException e) {
            fail(e);
        }
        catch (ImportException e) {
            assertEquals("import.exception.bad.type", e.getMessage());
        }
    }
}
