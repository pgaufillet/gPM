package org.topcased.gpm.business.authorization;

import org.topcased.gpm.business.AbstractBusinessServiceTestCase;
import org.topcased.gpm.business.GpmTestValues;
import org.topcased.gpm.business.exception.AuthorizationException;
import org.topcased.gpm.business.link.service.LinkService;
import org.topcased.gpm.business.sheet.service.SheetService;
import org.topcased.gpm.util.bean.CacheProperties;

/**
 * This test allow to verify overridenRole functionalities. TestOverriddenRole
 * 
 * @author ogehin
 */
public class TestOverriddenRole extends AbstractBusinessServiceTestCase {

    /**
     * Test Setter and Getter.
     */
    public void testSetAndGetOverridenRole() {
        // get services
        SheetService lSheetService = serviceLocator.getSheetService();
        LinkService lLinkService = serviceLocator.getLinkService();

        // get containers (sheet and link) ids
        String lSheetId =
                lSheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.SHEET1_1_PRODUCT,
                        GpmTestValues.SHEET1_1_REF);
        String lLinkId =
                lLinkService.getLinks(adminRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE).get(0).getId();

        // set overridden roles on sheet and link
        getAuthorizationService().setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "restricted");
        getAuthorizationService().setOverriddenRole(adminRoleToken, lLinkId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "restricted");

        // Retrieve overridden role for the sheet
        String lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lSheetId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "restricted", lRetrievedRole);

        // Retrieve overridden role for the link
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lLinkId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "restricted", lRetrievedRole);

        // set overridden roles on sheet and link
        getAuthorizationService().setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "viewer");
        getAuthorizationService().setOverriddenRole(adminRoleToken, lLinkId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "viewer");

        // Retrieve overridden role for the sheet
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lSheetId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "viewer", lRetrievedRole);
        // Retrieve overridden role for the link
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lLinkId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "viewer", lRetrievedRole);
    }

    /**
     * Test with not admin access
     */
    public void testSetAndGetOverridenRoleWithoutAdminAccess() {
        // get services
        SheetService lSheetService = serviceLocator.getSheetService();
        LinkService lLinkService = serviceLocator.getLinkService();

        // get containers sheet id
        String lSheetId =
                lSheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.SHEET1_1_PRODUCT,
                        GpmTestValues.SHEET1_1_REF);
        try {
            getAuthorizationService().setOverriddenRole(normalRoleToken,
                    lSheetId, ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN,
                    "restricted");
        }
        catch (AuthorizationException lException) {
            assertEquals(lException.getMessage(),
                    "Admininistrator role required");
        }
        catch (Exception e) {
            fail("No exception thrown as expected.");
        }

        // get containers link id
        String lLinkId =
                lLinkService.getLinks(adminRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE).get(0).getId();
        try {
            getAuthorizationService().setOverriddenRole(normalRoleToken,
                    lLinkId, ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN,
                    "restricted");
        }
        catch (AuthorizationException lException) {
            assertEquals(lException.getMessage(),
                    "Admininistrator role required");
        }
        catch (Exception e) {
            fail("No exception thrown as expected.");
        }
    }

    /**
     * Test Deleter.
     */
    public void testDeleteOverridenRole() {
        // get services
        SheetService lSheetService = serviceLocator.getSheetService();
        LinkService lLinkService = serviceLocator.getLinkService();

        // get containers (sheet and link) ids
        String lSheetId =
                lSheetService.getSheetIdByReference(getProcessName(),
                        GpmTestValues.SHEET1_1_PRODUCT,
                        GpmTestValues.SHEET1_1_REF);
        String lLinkId =
                lLinkService.getLinks(adminRoleToken, lSheetId,
                        CacheProperties.IMMUTABLE).get(0).getId();

        // set overridden roles on sheet and link
        getAuthorizationService().setOverriddenRole(adminRoleToken, lSheetId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "restricted");
        getAuthorizationService().setOverriddenRole(adminRoleToken, lLinkId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN, "restricted");

        // Retrieve overridden role for the sheet
        String lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lSheetId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "restricted", lRetrievedRole);

        // Retrieve overridden role for the link
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lLinkId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                "restricted", lRetrievedRole);

        // delete overridden roles on sheet and link
        getAuthorizationService().deleteOverriddenRole(adminRoleToken,
                lSheetId, ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN);
        getAuthorizationService().deleteOverriddenRole(adminRoleToken, lLinkId,
                ADMIN_LOGIN[0], GpmTestValues.USER_ADMIN);

        // Retrieve overridden role for the sheet
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lSheetId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                GpmTestValues.USER_ADMIN, lRetrievedRole);

        // Retrieve overridden role for the link
        lRetrievedRole =
                getAuthorizationService().getOverridenRoleName(adminRoleToken,
                        lLinkId);
        assertEquals("The retrieved role isn't the expected overriddenRole",
                GpmTestValues.USER_ADMIN, lRetrievedRole);
    }
}
