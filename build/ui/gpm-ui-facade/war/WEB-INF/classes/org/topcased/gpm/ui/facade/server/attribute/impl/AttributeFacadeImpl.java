/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.attribute.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.attributes.AttributeData;
import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.ui.facade.server.AbstractFacade;
import org.topcased.gpm.ui.facade.server.attribute.AttributeFacade;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

/**
 * AttributeFacade
 * 
 * @author nveillet
 */
public class AttributeFacadeImpl extends AbstractFacade implements
        AttributeFacade {

    private final static String PRODUCT_HOMEPAGE_URL = "product_url";

    /**
     * Convert a attribute from business for UI
     * 
     * @param pAttribute
     *            the attribute from business
     * @return the attribute for UI
     */
    private UiAttribute convertAttribute(AttributeData pAttribute) {
        List<String> lValues = new ArrayList<String>();
        for (String lValue : pAttribute.getValues()) {
            if (lValue != null) {
                lValues.add(lValue);
            }
        }

        return new UiAttribute(pAttribute.getName(), lValues);
    }

    /**
     * Convert a attribute from UI for business
     * 
     * @param pAttribute
     *            the attribute from UI
     * @return the attribute for business
     */
    private AttributeData convertAttribute(UiAttribute pAttribute) {
        String[] lValues = null;
        if (pAttribute.getValues() != null) {
            lValues = pAttribute.getValues().toArray(new String[0]);
        }
        return new AttributeData(pAttribute.getName(), lValues);
    }

    /**
     * Get current attribute from name for a given element.
     * 
     * @param pSession
     *            Current user session
     * @param pElementId
     *            The element identifier.
     * @param pAttributeName
     *            Attribute name.
     * @return The attribute.
     */
    public UiAttribute getAttribute(UiSession pSession, String pElementId,
            String pAttributeName) {
        AttributeData lAttributeData =
                getAttributesService().get(pElementId,
                        new String[] { pAttributeName })[0];
        if (lAttributeData == null) {
            return null;
        }
        return convertAttribute(lAttributeData);
    }

    /**
     * Get attributes for a given element.
     * 
     * @param pSession
     *            Current user session.
     * @param pElementId
     *            The element identifier.
     * @return List of attributes.
     */
    public List<UiAttribute> getAttributes(UiSession pSession, String pElementId) {
        AttributeData[] lAttributeDatas =
                getAttributesService().getAll(pElementId);
        List<UiAttribute> lAttributes = new ArrayList<UiAttribute>();
        for (AttributeData lAttributeData : lAttributeDatas) {
            lAttributes.add(convertAttribute(lAttributeData));
        }
        return lAttributes;
    }

    /**
     * Retrieve the authentication system.
     * <p>
     * Setting as global attributes, the authentication system groups two
     * attributes {@link AttributesService#AUTHENTICATION} and
     * {@link AttributesService#USER_ID_PARAMETER_NAME}
     * </p>
     * 
     * @return <ul>
     *         <li>Empty list if no attributes</li>
     *         <li>One element in 'internal' authentication case</li>
     *         <li>Two elements for 'external' authentication case. The first is
     *         the authentication type and the second is the user identifier
     *         parameter.</li>
     *         </ul>
     */
    public List<String> getAuthenticationSystem() {
        AttributeData[] lGlobalAttrs =
                getAttributesService().getGlobalAttributes(
                        new String[] { AttributesService.AUTHENTICATION,
                                      AttributesService.USER_ID_PARAMETER_NAME });
        final List<String> lAuthSystem;
        final AttributeData lAuthentication = lGlobalAttrs[0];
        if (lAuthentication == null) {
            lAuthSystem = Collections.emptyList();
        }
        else {
            if (StringUtils.equals(lAuthentication.getValues()[0],
                    AttributesService.INTERNAL_AUTHENTICATION)) {
                lAuthSystem =
                        Collections.singletonList(AttributesService.INTERNAL_AUTHENTICATION);
            }
            else if (StringUtils.equals(lAuthentication.getValues()[0],
                    AttributesService.EXTERNAL_AUTHENTICATION)) {
                final AttributeData lUserIdParam = lGlobalAttrs[1];
                if (lUserIdParam == null) {
                    lAuthSystem = Collections.emptyList();
                }
                else {
                    lAuthSystem = new ArrayList<String>(2);
                    lAuthSystem.add(AttributesService.EXTERNAL_AUTHENTICATION);
                    lAuthSystem.add(lUserIdParam.getValues()[0]);
                }
            }
            else {
                lAuthSystem = Collections.emptyList();
            }
        }
        return lAuthSystem;
    }

    /**
     * get the help Url
     * 
     * @return the help Url
     */
    public String getHelpUrl() {
        String lHelpUrl = getHelpService().getHelpContentUrl();

        if (StringUtils.isBlank(lHelpUrl)) {
            lHelpUrl = null;
        }

        return lHelpUrl;
    }

    /**
     * Get the homepage url
     * 
     * @param pSession
     *            the session
     * @return the homepage url
     */
    public String getHomepageUrl(UiSession pSession) {
        String lProductId =
                getProductService().getProductId(pSession.getRoleToken(),
                        pSession.getProductName());

        AttributeData[] lAttributeDatas =
                getAttributesService().get(lProductId,
                        new String[] { PRODUCT_HOMEPAGE_URL });

        if (lAttributeDatas[0] != null) {
            return lAttributeDatas[0].getValues()[0];
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.facade.server.attribute.AttributeFacade#getContactUrl()
     */
    @Override
    public String getContactUrl() {
        String lUrl = null;
        AttributeData[] lAttributeDatas =
                getAttributesService().getGlobalAttributes(
                        new String[] { AttributesService.CONTACT_URL });
        if (lAttributeDatas != null && lAttributeDatas.length == 1
                && lAttributeDatas[0] != null
                && lAttributeDatas[0].getValues() != null
                && lAttributeDatas[0].getValues().length == 1) {
            lUrl = lAttributeDatas[0].getValues()[0];
        }
        return lUrl;
    }

    /**
     * Set all attributes for a given element.
     * 
     * @param pSession
     *            Current user session
     * @param pElementId
     *            The element identifier.
     * @param pAttributes
     *            Map of attribute names as keys and attributes values as
     *            values.
     */
    public void setAttributes(UiSession pSession, String pElementId,
            List<UiAttribute> pAttributes) {

        List<AttributeData> lAttributeDatas = new ArrayList<AttributeData>();

        for (UiAttribute lAttribute : pAttributes) {
            lAttributeDatas.add(convertAttribute(lAttribute));
        }

        getAttributesService().update(pElementId,
                lAttributeDatas.toArray(new AttributeData[0]));
    }
}