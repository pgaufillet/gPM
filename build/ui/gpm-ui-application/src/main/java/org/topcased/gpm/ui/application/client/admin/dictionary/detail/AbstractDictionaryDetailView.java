/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.detail;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.component.client.container.GpmFormPanel;
import org.topcased.gpm.ui.component.client.layout.GpmLayoutPanelWithMenu;
import org.topcased.gpm.ui.component.client.menu.GpmMenuTitle;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for a set of dictionary/environment category displayed on the dictionary
 * management space.
 * 
 * @author nveillet
 */
public class AbstractDictionaryDetailView extends GpmLayoutPanelWithMenu
        implements AbstractDictionaryDetailDisplay {

    private final GpmFormPanel formPanel;

    private GpmMenuTitle menuTitle;

    private String title;

    /**
     * Constructor
     */
    public AbstractDictionaryDetailView() {
        super(true);
        menuTitle = new GpmMenuTitle(true);
        getMenu().addTitle(menuTitle);

        formPanel = new GpmFormPanel(2);

        setContent(new ScrollPanel(formPanel));
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Panel#clear()
     */
    @Override
    public void clear() {
        // TODO Auto-generated method stub
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
     * get form panel
     * 
     * @return the form panel
     */
    public GpmFormPanel getFormPanel() {
        return formPanel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
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
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.AbstractDictionaryDetailDisplay#setName(java.lang.String)
     */
    @Override
    public void setName(String pName) {
        setTitle(CONSTANTS.adminDictionaryFieldCategory() + ": " + pName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.user.detail.UserEditionDetailDisplay#setLogin(java.lang.String)
     */
    @Override
    public void setTitle(String pTitle) {
        title = pTitle;
        menuTitle.setHTML(title);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.detail.AbstractDictionaryDetailDisplay#setToolBar(org.topcased.gpm.ui.component.client.menu.GpmToolBar)
     */
    @Override
    public void setToolBar(GpmToolBar pToolBar) {
        getMenu().setToolBar(pToolBar);
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
