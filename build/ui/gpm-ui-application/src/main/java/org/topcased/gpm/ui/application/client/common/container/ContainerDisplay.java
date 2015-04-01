/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.container;

import org.topcased.gpm.ui.application.client.command.link.AbstractLoadLinkCommand;
import org.topcased.gpm.ui.application.client.common.tab.TabElementDisplay;
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.GpmFieldSet;
import org.topcased.gpm.ui.component.client.container.GpmLinkGroupPanel;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

/**
 * Display interface for the ContainerView.
 * 
 * @author tpanuel
 */
public interface ContainerDisplay extends TabElementDisplay {

    /**
     * Set the container reference.
     * 
     * @param pContainerReference
     *            The container reference.
     */
    public void setContainerReference(String pContainerReference);

    /**
     * Add a group.
     * 
     * @param pGroupName
     *            The group name.
     * @param pHandler
     *            The handler for create fields.
     * @param pIsOpen
     *            If the group is open.
     */
    public void addFieldGroup(String pGroupName, FieldCreationHandler pHandler,
            boolean pIsOpen);

    /**
     * Add a group of link.
     * 
     * @param pLinkTypeName
     *            The link type name.
     * @param pGroupName
     *            The group name : link type name translated.
     * @param pLoadLink
     *            The load link command.
     * @return The group.
     */
    public GpmLinkGroupPanel addLinkGroup(String pLinkTypeName,
            String pGroupName, AbstractLoadLinkCommand pLoadLink);

    /**
     * get a link group
     * 
     * @param pLinkTypeName
     *            the link type name
     * @return the link group
     */
    public GpmLinkGroupPanel getLinkGroup(String pLinkTypeName);

    /**
     * Set the tool bar.
     * 
     * @param pToolBars
     *            The tool bar.
     */
    public void setToolBar(final GpmToolBar pToolBars);

    /**
     * Get the field set associate to the container.
     * 
     * @return The container field set.
     */
    public GpmFieldSet getContainerFieldSet();
}