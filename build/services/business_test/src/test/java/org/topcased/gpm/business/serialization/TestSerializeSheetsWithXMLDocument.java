/***************************************************************
 * Copyright (c) 2007 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Anne Haugommard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.business.serialization;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.extensions.service.ContextBase;
import org.topcased.gpm.business.serialization.service.SerializationService;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * TestSerializeSheetWithXMLDocument Test the extension point preExport
 * 
 * @author ahaugomm
 */
public class TestSerializeSheetsWithXMLDocument extends
AbstractBusinessServiceTestCase {

    public static final String[] SHEET_REF_1 =
    { "Tom", GpmTestValues.PRODUCT_HAPPY_MOUSE_NAME };

    public static final String[] SHEET_REF_2 =
    { "Lassie", GpmTestValues.PRODUCT_BERNARD_STORE_NAME };

    /**
     * Tests the method in a normal case
     * 
     * @throws ParserConfigurationException
     *             if the XML Document cannot be created.
     * @throws XPathExpressionException
     *             if the XPath expression is invalid
     */
    public void testNormalCase() throws ParserConfigurationException,
    XPathExpressionException {

        // Create the test environment
        /*
         * # get the service
         * # get two sheets :
         *  - Tom : a Cat with an extension point preExport that modifies the XML document
         *  - Lassie : a Dog without extension point preExport
         * # create an empty XML Document
         */

        SerializationService lSerializationService =
            serviceLocator.getSerializationService();

        List<String> lSheetIds = new ArrayList<String>();
        final String lFirstId =
            sheetService.getSheetIdByReference(getProcessName(),
                    SHEET_REF_1[1], SHEET_REF_1[0]);

        final String lSecondId =
            sheetService.getSheetIdByReference(getProcessName(),
                    SHEET_REF_2[1], SHEET_REF_2[0]);

        lSheetIds.add(lFirstId);
        lSheetIds.add(lSecondId);

        sheetService.changeState(adminRoleToken, lFirstId, "Validate", null);
        sheetService.changeState(adminRoleToken, lFirstId, "Close", null);

        DocumentBuilder lDocumentBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document lXMLDocument = lDocumentBuilder.newDocument();

        // Call the method to be tested
        lSerializationService.serializeSheets(adminRoleToken, lSheetIds,
                lXMLDocument, ContextBase.getContext());

        // Check the result : the XML Document
        /*
         * Check that the modifications due to the preExport extension point on
         * 'Tom' sheet have been effective. and that 'Lassie' XML content has no
         * modification.
         */

        String lExpression =
            "/gpm/sheets/sheet[@reference='" + SHEET_REF_1[0]
                                                           + "']/sheetHistory/sheetHistoryData";
        javax.xml.xpath.XPath lXpath = XPathFactory.newInstance().newXPath();
        NodeList lSheetHistoryDatas =
            (NodeList) lXpath.evaluate(lExpression, lXMLDocument,
                    XPathConstants.NODESET);

        assertNotNull("No history found for sheet '" + SHEET_REF_1[0] + "'.",
                lSheetHistoryDatas);
        assertTrue("Less than 2 transitions found in this sheet history.",
                lSheetHistoryDatas.getLength() >= 2);

        lExpression =
            "/gpm/sheets/sheet[@reference='" + SHEET_REF_2[0]
                                                           + "']/sheetHistory/sheetHistoryData";
        lXpath = XPathFactory.newInstance().newXPath();
        lSheetHistoryDatas =
            (NodeList) lXpath.evaluate(lExpression, lXMLDocument,
                    XPathConstants.NODESET);

        assertTrue(
                "Modifications have occured on extensionPoint preExport on the wrong sheet type.",
                lSheetHistoryDatas.getLength() == 0);
    }

    /**
     * Tests the method with null sheet ids.
     */
    public void testWithNullSheetIdsCase() throws ParserConfigurationException {
        SerializationService lSerializationService =
            serviceLocator.getSerializationService();

        DocumentBuilder lDocumentBuilder =
            DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document lXMLDocument = lDocumentBuilder.newDocument();

        try {
            // Call the method to be tested
            lSerializationService.serializeSheets(adminRoleToken, null,
                    lXMLDocument, ContextBase.getContext());
            fail("The exception has not been thrown.");
        }
        catch (IllegalArgumentException lIllegalArgumentException) {
            // ok
        }
        catch (Throwable e) {
            fail("The exception thrown is not an IllegalArgumentException.");
        }
    }

}
