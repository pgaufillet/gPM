/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.common.workspace.detail;

import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for an empty space displayed on the workspace.
 * 
 * @author nveillet
 */
public class EmptyDetailView extends GpmLayoutPanelWithMenu implements
        EmptyDetailDisplay {

    /**
     * Constructor
     */
    public EmptyDetailView() {
        super(true);
        getMenu().addTitle(new GpmMenuTitle(true));
        setContent(new ScrollPanel());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMaximize()
     */
    @Override
    public void doMaximize() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doMinimize()
     */
    @Override
    public void doMinimize() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.IResizableLayoutPanel#doRestore()
     */
    @Override
    public void doRestore() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu#setContent(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void setContent(Widget pPanel) {
        super.setContent(pPanel);
        pPanel.setStylePrimaryName(ComponentResources.INSTANCE.css().gpmTabLayoutPanelContent());
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#startProcessing()
     */
    @Override
    public void startProcessing() {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
        // TODO Auto-generated method stub
    }
}
