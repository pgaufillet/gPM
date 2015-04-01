/***************************************************************
 * Copyright (c) 2009 AIRBUS FRANCE. All rights reserved. This
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL)which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/

package org.topcased.gpm.business.display;

import java.util.List;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.fields.impl.CacheableFieldsContainer;
import org.topcased.gpm.business.link.impl.CacheableLinkType;
import org.topcased.gpm.business.serialization.data.AttachedDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceStringDisplayHint;
import org.topcased.gpm.business.serialization.data.ChoiceTreeDisplayHint;
import org.topcased.gpm.business.serialization.data.DateDisplayHint;
import org.topcased.gpm.business.serialization.data.DisplayHint;
import org.topcased.gpm.business.serialization.data.GridColumn;
import org.topcased.gpm.business.serialization.data.GridDisplayHint;
import org.topcased.gpm.business.serialization.data.TextDisplayHint;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * Test display hint on link's fields
 * 
 * @author tpanuel
 */
public class TestDisplayHintOnLinks extends AbstractBusinessServiceTestCase {
    private final static String LINK_TYPE_NAME = "TEST_DISPLAY_HINT_ON_LINKS";

    private final static String CHOICE_STRING_FIELD_LABEL = "CHOICE_STRING";

    private final static String CHOICE_STRING_EXT_POINT_NAME =
            "getChoiceStrings";

    /**
     * Test that ChoiceStringDisplayHint can be defined on links
     */
    public void testChoiceStringDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lChoiceString =
                getAndTestDisplayHint(lTestCacheableLink,
                        CHOICE_STRING_FIELD_LABEL,
                        ChoiceStringDisplayHint.class);

        assertTrue("Choice string display hint associed to field "
                + CHOICE_STRING_FIELD_LABEL + " must be strict",
                ((ChoiceStringDisplayHint) lChoiceString).isStrict());
        assertEquals(
                "Bad extension point used by the choice string "
                        + "display hint of the field "
                        + CHOICE_STRING_FIELD_LABEL,
                CHOICE_STRING_EXT_POINT_NAME,
                ((ChoiceStringDisplayHint) lChoiceString).getExtensionPointName());
    }

    private final static String CHOICE_FIELD_LABEL = "CHOICE";

    /**
     * Test that ChoiceDisplayHint can be defined on links
     */
    public void testChoiceDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lChoice =
                getAndTestDisplayHint(lTestCacheableLink, CHOICE_FIELD_LABEL,
                        ChoiceDisplayHint.class);

        assertTrue("Choice display hint associed to field "
                + CHOICE_FIELD_LABEL + " must be a list",
                ((ChoiceDisplayHint) lChoice).isList());
    }

    private final static String FULL_DATE_FIELD_LABEL = "FULL_DATE";

    private final static String FULL_DATE_TYPE = "FULL";

    private final static String MEDIUM_DATE_FIELD_LABEL = "MEDIUM_DATE";

    private final static String MEDIUM_DATE_TYPE = "MEDIUM";

    private final static String LONG_DATE_FIELD_LABEL = "LONG_DATE";

    private final static String LONG_DATE_TYPE = "LONG";

    /**
     * Test that DateDisplayHint can be defined on links
     */
    public void testDateDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lFullDate =
                getAndTestDisplayHint(lTestCacheableLink,
                        FULL_DATE_FIELD_LABEL, DateDisplayHint.class);

        assertEquals("Bad date display hint format for field " + lFullDate,
                FULL_DATE_TYPE, ((DateDisplayHint) lFullDate).getFormat());

        final DisplayHint lMediumDate =
                getAndTestDisplayHint(lTestCacheableLink,
                        MEDIUM_DATE_FIELD_LABEL, DateDisplayHint.class);

        assertEquals("Bad date display hint format for field " + lMediumDate,
                MEDIUM_DATE_TYPE, ((DateDisplayHint) lMediumDate).getFormat());

        final DisplayHint lLongDate =
                getAndTestDisplayHint(lTestCacheableLink,
                        LONG_DATE_FIELD_LABEL, DateDisplayHint.class);

        assertEquals("Bad date display hint format for field " + lLongDate,
                LONG_DATE_TYPE, ((DateDisplayHint) lLongDate).getFormat());
    }

    private final static String EXTERN_URL_FIELD_LABEL = "EXTERN_URL";

    private final static String EXTERN_URL_TYPE = "URL";

    private final static String INTERNAL_URL_FIELD_LABEL = "INTERNAL_URL";

    private final static String INTERNAL_URL_TYPE = "INTERNAL_URL";

    private final static String MULTI_LINE_FIELD_LABEL = "MULTI_LINE";

    private final static String MULTI_LINE_TYPE = "MULTI_LINE";

    private final static String RICH_TEXT_FIELD_LABEL = "RICH_TEXT";

    private final static String RICH_TEXT_TYPE = "RICH_TEXT";

    private final static int TEXT_WIDTH = 200;

    private final static int TEXT_HEIGHT = 200;

    /**
     * Test that TextDisplayHint can be defined on links
     */
    public void testTextDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lExternUrl =
                getAndTestDisplayHint(lTestCacheableLink,
                        EXTERN_URL_FIELD_LABEL, TextDisplayHint.class);

        assertEquals("Unexpected type for display hint of field "
                + EXTERN_URL_FIELD_LABEL, EXTERN_URL_TYPE,
                ((TextDisplayHint) lExternUrl).getDisplayType());

        final DisplayHint lInternalUrl =
                getAndTestDisplayHint(lTestCacheableLink,
                        INTERNAL_URL_FIELD_LABEL, TextDisplayHint.class);

        assertEquals("Unexpected type for display hint of field "
                + INTERNAL_URL_FIELD_LABEL, INTERNAL_URL_TYPE,
                ((TextDisplayHint) lInternalUrl).getDisplayType());

        final DisplayHint lMultiLine =
                getAndTestDisplayHint(lTestCacheableLink,
                        MULTI_LINE_FIELD_LABEL, TextDisplayHint.class);

        assertEquals("Unexpected width for display hint of field "
                + MULTI_LINE_FIELD_LABEL, TEXT_WIDTH,
                ((TextDisplayHint) lMultiLine).getWidth());
        assertEquals("Unexpected height for display hint of field "
                + MULTI_LINE_FIELD_LABEL, TEXT_HEIGHT,
                ((TextDisplayHint) lMultiLine).getHeight());
        assertEquals("Unexpected type for display hint of field "
                + MULTI_LINE_FIELD_LABEL, MULTI_LINE_TYPE,
                ((TextDisplayHint) lMultiLine).getDisplayType());

        final DisplayHint lRich =
                getAndTestDisplayHint(lTestCacheableLink,
                        RICH_TEXT_FIELD_LABEL, TextDisplayHint.class);

        assertEquals("Unexpected width for display hint of field "
                + RICH_TEXT_FIELD_LABEL, TEXT_WIDTH,
                ((TextDisplayHint) lRich).getWidth());
        assertEquals("Unexpected height for display hint of field "
                + RICH_TEXT_FIELD_LABEL, TEXT_HEIGHT,
                ((TextDisplayHint) lRich).getHeight());
        assertEquals("Unexpected type for display hint of field "
                + RICH_TEXT_FIELD_LABEL, RICH_TEXT_TYPE,
                ((TextDisplayHint) lRich).getDisplayType());
    }

    private final static String GRID_FIELD_LABEL = "GRID";

    private final static int GRID_WIDTH = 200;

    private final static int GRID_HEIGHT = 200;

    private final static String GRID_SEPARATOR = "::";

    private final static String[] GRID_COLUMNS_NAME = { "GRID_A", "GRID_B" };

    /**
     * Test that GridDisplayHint can be defined on links
     */
    public void testGridDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lGrid =
                getAndTestDisplayHint(lTestCacheableLink, GRID_FIELD_LABEL,
                        GridDisplayHint.class);

        assertEquals("Unexpected width for display hint of field "
                + GRID_FIELD_LABEL, GRID_WIDTH,
                ((GridDisplayHint) lGrid).getWidth());
        assertEquals("Unexpected height for display hint of field "
                + GRID_FIELD_LABEL, GRID_HEIGHT,
                ((GridDisplayHint) lGrid).getHeight());
        assertEquals("Unexpected separtor for display hint of field "
                + GRID_FIELD_LABEL, GRID_SEPARATOR,
                ((GridDisplayHint) lGrid).getColumnSeparator());

        final List<GridColumn> lGridColumns =
                ((GridDisplayHint) lGrid).getColumns();

        assertNotNull("No columns on the grid display hint "
                + "associated to the field " + GRID_FIELD_LABEL, lGridColumns);
        assertEquals("Unexpected number of columns on the grid display hint "
                + "associated to the field " + GRID_FIELD_LABEL,
                lGridColumns.size(), 2);
        assertEquals("Unexpected name for the first column of the grid "
                + "display hint associated to the field " + GRID_FIELD_LABEL,
                lGridColumns.get(0).getName(), GRID_COLUMNS_NAME[0]);
        assertEquals("Unexpected name for the second column of the grid "
                + "display hint associated to the field " + GRID_FIELD_LABEL,
                lGridColumns.get(1).getName(), GRID_COLUMNS_NAME[1]);
    }

    private final static String ATTACHED_IMAGE_FIELD_LABEL = "ATTACHED_IMAGE";

    private final static int ATTACHED_IMAGE_WIDTH = 200;

    private final static int ATTACHED_IMAGE_HEIGHT = 200;

    private final static String ATTACHED_IMAGE_TYPE = "IMAGE";

    /**
     * Test that AttachedDisplayHint can be defined on links
     */
    public void testAttachedDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getCacheableLinkTypeByName(adminRoleToken,
                        getProcessName(), LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lAttachedImage =
                getAndTestDisplayHint(lTestCacheableLink,
                        ATTACHED_IMAGE_FIELD_LABEL, AttachedDisplayHint.class);

        assertEquals("Unexpected width for display hint of field "
                + ATTACHED_IMAGE_FIELD_LABEL, ATTACHED_IMAGE_WIDTH,
                ((AttachedDisplayHint) lAttachedImage).getWidth());
        assertEquals("Unexpected height for display hint of field "
                + ATTACHED_IMAGE_FIELD_LABEL, ATTACHED_IMAGE_HEIGHT,
                ((AttachedDisplayHint) lAttachedImage).getHeight());
        assertEquals("Unexpected type for display hint of field "
                + ATTACHED_IMAGE_FIELD_LABEL, ATTACHED_IMAGE_TYPE,
                ((AttachedDisplayHint) lAttachedImage).getDisplayType());
    }

    private static String CHOICE_TREE_FIELD_LABEL = "CHOICE_TREE";

    private static String SEPARATOR = "|";

    /**
     * Test that ChoiceTreeDisplayHint can be defined on links
     */
    public void testChoiceTreeDisplayHintOnLinks() {
        final CacheableLinkType lTestCacheableLink =
                linkService.getLinkTypeByName(adminRoleToken, LINK_TYPE_NAME,
                        CacheProperties.IMMUTABLE);
        final DisplayHint lChoiceTree =
                getAndTestDisplayHint(lTestCacheableLink,
                        CHOICE_TREE_FIELD_LABEL, ChoiceTreeDisplayHint.class);

        assertEquals("Wrong separator value in field "
                + CHOICE_TREE_FIELD_LABEL, SEPARATOR,
                ((ChoiceTreeDisplayHint) lChoiceTree).getSeparator());
    }

    private DisplayHint getAndTestDisplayHint(
            CacheableFieldsContainer pFieldsContainer, String pFieldLabelKey,
            Class<? extends DisplayHint> pExpectedType) {
        final DisplayHint lDisplayHint =
                pFieldsContainer.getDisplayHint(pFieldLabelKey);

        assertNotNull("No display hint associated to the field "
                + pFieldLabelKey, lDisplayHint);
        assertTrue("Bad display hint type associated to the field "
                + pFieldLabelKey, pExpectedType.isInstance(lDisplayHint));

        return lDisplayHint;
    }
}
