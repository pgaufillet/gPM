/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.admin.dictionary.navigation;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCommand;
import org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnEditionCommand;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.container.GpmDisclosurePanel;
import org.topcased.gpm.ui.component.client.layout.stack.GpmStackLayoutPanel;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * View for listing of the dictionary management space.
 * 
 * @author nveillet
 */
public class DictionaryNavigationView extends GpmStackLayoutPanel implements
        DictionaryNavigationDisplay {

    private final GpmTextButton dictionaryButton;

    private FlowPanel environments;

    private GpmDisclosurePanel environmentsDisclosurePanel;

    private boolean environmentsOddEven;

    /**
     * Constructor
     */
    public DictionaryNavigationView() {
        environmentsOddEven = true;

        FlowPanel lPanel = new FlowPanel();

        // The dictionary button
        dictionaryButton =
                new GpmTextButton(CONSTANTS.dictionary(),
                        CONSTANTS.dictionary());
        dictionaryButton.setVisible(false);
        lPanel.add(dictionaryButton);

        environmentsDisclosurePanel = new GpmDisclosurePanel();
        environments = new FlowPanel();
        environments.setStylePrimaryName(INSTANCE.css().gpmFilterTable());
        environmentsDisclosurePanel.setButtonText(CONSTANTS.environments());
        environmentsDisclosurePanel.setContent(environments);
        environmentsDisclosurePanel.open();
        environmentsDisclosurePanel.setVisible(false);
        lPanel.add(environmentsDisclosurePanel);

        ScrollPanel lScrollable = new ScrollPanel();
        lScrollable.setWidget(lPanel);

        addPanel(CONSTANTS.navigationDictionaryTitle(), lScrollable, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay#addEnvironment(java.lang.String,
     *      org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenEnvironmentOnEditionCommand)
     */
    @Override
    public void addEnvironment(String pEnvironment,
            OpenEnvironmentOnEditionCommand pOpenEnvironment) {

        // The environment name
        final GpmTextButton lEnvironment =
                new GpmTextButton(pEnvironment, pEnvironment);

        lEnvironment.addClickHandler(pOpenEnvironment);
        environments.add(lEnvironment);

        if (environmentsOddEven) {
            lEnvironment.addStyleName(INSTANCE.css().evenRow());
            lEnvironment.removeStyleName(INSTANCE.css().oddRow());
        }
        else {
            lEnvironment.addStyleName(INSTANCE.css().oddRow());
            lEnvironment.removeStyleName(INSTANCE.css().evenRow());
        }
        environmentsOddEven = !environmentsOddEven;

        environmentsDisclosurePanel.setVisible(true);
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
     * @see com.google.gwt.user.client.ui.StackLayoutPanel#clear()
     */
    @Override
    public void clear() {
        environments.clear();
        environmentsOddEven = true;
        environmentsDisclosurePanel.setVisible(false);
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
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getHeight()
     */
    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinHeight()
     */
    @Override
    public int getMinHeight() {
        return environments.getOffsetHeight();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getMinWidth()
     */
    @Override
    public int getMinWidth() {
        return environments.getOffsetWidth();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.component.client.layout.ISizeAware#getWidth()
     */
    @Override
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.admin.dictionary.navigation.DictionaryNavigationDisplay#setDictionaryButtonHandler(org.topcased.gpm.ui.application.client.command.admin.dictionary.OpenDictionaryCommand)
     */
    @Override
    public void setDictionaryButtonHandler(OpenDictionaryCommand pOpenDictionary) {
        dictionaryButton.addClickHandler(pOpenDictionary);
        dictionaryButton.setVisible(true);
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