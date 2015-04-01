/***************************************************************
 * Copyright (c) 2008 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 ******************************************************************/
package utils;

import java.util.ArrayList;
import java.util.List;

import org.topcased.dbutils.DbUtils;
import org.topcased.gpm.instantiation.Instantiate;

/**
 * @author jlouisy
 */
public class ResetTestInstance {

    private static final String ADMIN_PWD = "admin";

    private static final String FIRST_INSTANCE_NAME = "TestInstance";

    /**
     * Useful method to drop all tables of the database.
     */
    public static void dropTables() {
        DbUtils.main(new String[] { "dropTable.properties" });
        Instantiate.main(new String[] { "-I", "-P", ADMIN_PWD });
    }

    /**
     * Useful method to generate the dynamic schema. This method should be
     * called between metadata instantiation and data instantiation.
     */
    private static void generateDynamicSchema() {
        Instantiate.main(new String[] { "-P", ADMIN_PWD });
    }

    /**
     * Useful method to instantiate a light version of the data and some test
     * data.
     * 
     * @param pProcess
     *            process name
     */
    public static void instantiateData4Test(String pProcess) {
        ArrayList<String> lFiles = new ArrayList<String>();
        lFiles.add("data/products/test-instance_products.xml");
        lFiles.add("data/sheets/test-instance_sheets.xml");
        lFiles.add("data/users/test-instance_users.xml");
        instantiateFileList(lFiles, pProcess);
    }

    /**
     * Useful method to instantiate a file list.
     * 
     * @param pFiles
     *            The files to instantiate.
     * @param pProcess
     *            process name
     */
    public static void instantiateFileList(List<String> pFiles, String pProcess) {
        for (String lFile : pFiles) {
            System.out.print(lFile + " : ");
            Instantiate.main(new String[] { "-P", ADMIN_PWD, "-N", pProcess,
                                           "-f", lFile });
        }
    }

    /**
     * Useful method to instantiate all metadata.
     * 
     * @param pProcess
     *            process name
     */
    public static void instantiateMetadata(String pProcess) {
        ArrayList<String> lFiles = new ArrayList<String>();
        lFiles.add("metadata/dictionary/test-instance_dico.xml");
        lFiles.add("metadata/dictionary/test-instance_translations.xml");
        lFiles.add("metadata/commands/test-instance_commands.xml");
        lFiles.add("metadata/commands/test-instance_extended_actions.xml");
        lFiles.add("metadata/producttypes/test-instance_product_types.xml");
        lFiles.add("metadata/sheettypes/test-instance_sheet_type.xml");
        lFiles.add("metadata/linktypes/test-instance_linktypes.xml");
        lFiles.add("metadata/authorization/test-instance_roles.xml");
        lFiles.add("metadata/filters/test-instance_filters.xml");
        lFiles.add("metadata/authorization/test-instance_sheet_access.xml");
        lFiles.add("metadata/reports/test-instance_reports.xml");

        instantiateFileList(lFiles, pProcess);

    }

    /**
     * Main method.
     * 
     * @param pArgs
     *            default arguments (no taken into account)
     */
    public static void main(String[] pArgs) {

        dropTables();

        //instantiate first INSTANCE
        instantiateMetadata(FIRST_INSTANCE_NAME);
        generateDynamicSchema();
        instantiateData4Test(FIRST_INSTANCE_NAME);

        //instantiate second INSTANCE
        //        instantiateMetadata(SECOND_INSTANCE_NAME);
        //        generateDynamicSchema();
        //        instantiateData4Test(SECOND_INSTANCE_NAME);
    }

}