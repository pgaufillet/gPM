/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.instantiation.options;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.instantiation.AbstractInstantiationTestCase;

/**
 * GpmOptionsTest Test the options of the instance.
 * 
 * @author mkargbo
 */
public class GpmOptionsTest extends AbstractInstantiationTestCase {
    private static final String INSTANCE_FILE_AUTOLOCKING =
            "options/GpmOptionsTest_AutoLocking.xml";

    private static final String AUTOLOCKING_SHEET_TYPE_NAME =
            "Autolocking_Sheet";

    private AttributesService attributesService;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        attributesService = serviceLocator.getAttributesService();
    }

    private static final String AUTOLOCKING_DEFAULT_VALUE = "WRITE";

    private static final String AUTOLOCKING_OPTION_EXPECTED_VALUE = "WRITE";

    private static final String AUTOLOCKING_SHEET_TYPE_EXPECTED_VALUE =
            "READ_WRITE";

    /**
     * Setting autolocking attribute as an option and as an sheetType's
     * attribute
     */
    public void testAulocking() {
        instantiate(INSTANCE_FILE_AUTOLOCKING);

        AttributeData[] lGlobalAttributeData =
                attributesService.getGlobalAttributes(new String[] { AttributesService.AUTOLOCKING });
        assertEquals(1, lGlobalAttributeData.length);

        AttributeData lAutolockingAttribute = lGlobalAttributeData[0];
        assertNotNull(lAutolockingAttribute);
        assertNotNull(lAutolockingAttribute.getValues());
        assertEquals(1, lAutolockingAttribute.getValues().length);
        assertEquals(AUTOLOCKING_OPTION_EXPECTED_VALUE,
                lAutolockingAttribute.getValues()[0]);

        //For the sheet type
        String lSheetTypeId =
                serviceLocator.getFieldsContainerService().getFieldsContainerId(
                        adminRoleToken, AUTOLOCKING_SHEET_TYPE_NAME);
        assertFalse(StringUtils.isBlank(lSheetTypeId));

        AttributeData[] lSheetTypeAttributeData =
                attributesService.get(lSheetTypeId,
                        new String[] { AttributesService.AUTOLOCKING });
        assertEquals(1, lSheetTypeAttributeData.length);

        lAutolockingAttribute = lSheetTypeAttributeData[0];
        assertNotNull(lAutolockingAttribute);
        assertNotNull(lAutolockingAttribute.getValues());
        assertEquals(1, lAutolockingAttribute.getValues().length);
        assertEquals(AUTOLOCKING_SHEET_TYPE_EXPECTED_VALUE,
                lAutolockingAttribute.getValues()[0]);
    }

    /**
     * Setting autolocking attribute default value
     */
    public void testAulockingDefaultValue() {
        AttributeData[] lGlobalAttributeData =
                attributesService.getGlobalAttributes(new String[] { AttributesService.AUTOLOCKING });
        assertEquals(1, lGlobalAttributeData.length);

        AttributeData lAutolockingAttribute = lGlobalAttributeData[0];
        assertNotNull(lAutolockingAttribute);
        assertNotNull(lAutolockingAttribute.getValues());
        assertEquals(1, lAutolockingAttribute.getValues().length);
        assertEquals(AUTOLOCKING_DEFAULT_VALUE,
                lAutolockingAttribute.getValues()[0]);
    }

}