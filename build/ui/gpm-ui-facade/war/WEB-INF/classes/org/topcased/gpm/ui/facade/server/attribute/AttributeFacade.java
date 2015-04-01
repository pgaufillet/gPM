/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.facade.server.attribute;

import java.util.List;

import org.topcased.gpm.business.attributes.service.AttributesService;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.shared.attribute.UiAttribute;

/**
 * AttributeFacade
 * 
 * @author nveillet
 */
public interface AttributeFacade {

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
            String pAttributeName);

    /**
     * Get attributes for a given element.
     * 
     * @param pSession
     *            Current user session.
     * @param pElementId
     *            The element identifier.
     * @return List of attributes.
     */
    public List<UiAttribute> getAttributes(UiSession pSession, String pElementId);

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
    public List<String> getAuthenticationSystem();

    /**
     * get the help Url
     * 
     * @return the help Url
     */
    public String getHelpUrl();

    /**
     * Get the homepage url
     * 
     * @param pSession
     *            the session
     * @return the homepage url
     */
    public String getHomepageUrl(UiSession pSession);

    /**
     * Get the contact URL
     * 
     * @return the contact URL
     */
    public String getContactUrl();

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
            List<UiAttribute> pAttributes);

}