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
import org.topcased.gpm.ui.component.client.container.FieldCreationHandler;
import org.topcased.gpm.ui.component.client.container.GpmDisplayGroupPanel;
import org.topcased.gpm.ui.component.client.container.GpmFieldSet;
import org.topcased.gpm.ui.component.client.container.GpmLinkGroupPanel;
import org.topcased.gpm.ui.component.client.container.GpmValuesContainerPanel;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract view for a sheet displayed on the user space.
 * 
 * @author tpanuel
 */
public abstract class ContainerView extends GpmLayoutPanelWithMenu implements
        ContainerDisplay {

    private final GpmMenuTitle titlePanel;

    private String title;

    /**
     * Create an abstract view for display a sheet.
     */
    public ContainerView() {
        super(false);
        setContent(new ScrollPanel(new GpmValuesContainerPanel()));
        titlePanel = new GpmMenuTitle(true);
        getMenu().addTitle(titlePanel);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#setContainerReference(java.lang.String)
     */
    @Override
    public void setContainerReference(final String pSheetTitle) {
        title = pSheetTitle;
        titlePanel.setHTML(title);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.tab.TabElementDisplay#getTabTitle()
     */
    @Override
    public String getTabTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#addFieldGroup(java.lang.String,
     *      org.topcased.gpm.ui.component.client.container.FieldCreationHandler,
     *      boolean)
     */
    @Override
    public void addFieldGroup(final String pGroupName,
            final FieldCreationHandler pHandler, final boolean pIsOpen) {
        final GpmDisplayGroupPanel lGroup =
                getGpmValuesContainerPanel().addFieldGroup(pGroupName);

        lGroup.setFieldCreationHandler(pHandler);
        if (pIsOpen) {
            lGroup.open();
        }
        else {
            lGroup.close();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#addLinkGroup(java.lang.String,
     *      java.lang.String,
     *      org.topcased.gpm.ui.application.client.command.link.AbstractLoadLinkCommand)
     */
    @Override
    public GpmLinkGroupPanel addLinkGroup(final String pLinkTypeName,
            final String pGroupName, final AbstractLoadLinkCommand pLoadLink) {
        return getGpmValuesContainerPanel().addLinkGroup(pLinkTypeName,
                pGroupName, pLoadLink);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#getLinkGroup(java.lang.String)
     */
    @Override
    public GpmLinkGroupPanel getLinkGroup(final String pLinkTypeName) {
        return getGpmValuesContainerPanel().getLinkGroup(pLinkTypeName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#getContainerFieldSet()
     */
    @Override
    public GpmFieldSet getContainerFieldSet() {
        return getGpmValuesContainerPanel();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.common.container.ContainerDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(final GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
    }

    /**
     * Get the values container panel.
     * 
     * @return The values container panel.
     */
    protected GpmValuesContainerPanel getGpmValuesContainerPanel() {
        return (GpmValuesContainerPanel) ((ScrollPanel) getContent()).getWidget();
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.widget.WidgetDisplay#asWidget()
     */
    @Override
    public Widget asWidget() {
        return this;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // Nothing to do
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // Nothing to do
    }
}