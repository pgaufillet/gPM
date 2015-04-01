/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.main;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;
import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.main.GpmBanner;
import org.topcased.gpm.ui.component.client.main.GpmMainClientFrame;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The main view.
 * 
 * @author tpanuel
 */
public class MainView extends GpmMainClientFrame implements MainDisplay {

    private static final int LOADING_TEXT_MARGIN = 10;

    private static final int LOADING_TEXT_TOP_SHIFT = -3;

    private FlowPanel loadingPanel;

    private HTML glassPanel;

    private GpmImageButton switchButton;

    private HandlerRegistration switchButtonRegistration;

    /**
     * Create the main view.
     */
    public MainView() {
        super(new GpmBanner(), new LayoutPanel());
        Window.setTitle(CONSTANTS.applicationTitle());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setLoadingPanelVisibility(boolean)
     */
    @Override
    public void setLoadingPanelVisibility(final boolean pVisibility) {
        // Initialize loading panel
        if (loadingPanel == null) {
            final InlineLabel lLoadingLabel = new InlineLabel();
            lLoadingLabel.setText(CONSTANTS.loading());
            lLoadingLabel.getElement().getStyle().setPosition(Position.RELATIVE);
            lLoadingLabel.getElement().getStyle().setTop(
                    LOADING_TEXT_TOP_SHIFT, Unit.PX);
            lLoadingLabel.getElement().getStyle().setMarginLeft(
                    LOADING_TEXT_MARGIN, Unit.PX);
            loadingPanel = new FlowPanel();
            loadingPanel.setStylePrimaryName(INSTANCE.css().gpmLoadingLabel());
            loadingPanel.add(new Image(
                    ComponentResources.INSTANCE.images().loadingTop()));
            loadingPanel.add(lLoadingLabel);

            glassPanel = new HTML("&nbsp;");
            glassPanel.setStyleName(INSTANCE.css().gpmLoadingGlass());
            Window.addResizeHandler(new ResizeHandler() {
                @Override
                public void onResize(ResizeEvent pEvent) {
                    Style lStyle = glassPanel.getElement().getStyle();
                    lStyle.setPosition(Position.ABSOLUTE);
                    lStyle.setProperty("filter", "alpha(opacity=0)");
                    lStyle.setWidth(Window.getClientWidth(), Unit.PX);
                    lStyle.setHeight(Window.getClientHeight(), Unit.PX);
                }
            });
        }
        if (pVisibility) {
            RootPanel.get().add(glassPanel);
            RootPanel.get().add(loadingPanel);
            RootPanel.getBodyElement().getStyle().setCursor(Cursor.WAIT);
        }
        else {
            RootPanel.getBodyElement().getStyle().setCursor(Cursor.DEFAULT);
            RootPanel.get().remove(loadingPanel);
            RootPanel.get().remove(glassPanel);
        }
        Style lStyle = glassPanel.getElement().getStyle();
        lStyle.setPosition(Position.ABSOLUTE);
        lStyle.setProperty("filter", "alpha(opacity=0)");
        lStyle.setWidth(Window.getClientWidth(), Unit.PX);
        lStyle.setHeight(Window.getClientHeight(), Unit.PX);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setContent(com.google.gwt.user.client.ui.Widget)
     */
    @Override
    public void setContent(final Widget pContent) {
        final LayoutPanel lWorkspace = (LayoutPanel) getWorkspace();

        lWorkspace.clear();
        lWorkspace.add(pContent);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#addUserProfileEditionHandler(com.google.gwt.event.dom.client.ClickHandler)
     */
    public void addUserProfileEditionHandler(ClickHandler pHandler) {
        getBanner().addUserProfileEditionHandler(pHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#addLogoutButton(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addLogoutButton(final ClickHandler pClickHandler) {
        final GpmImageButton lSwitchButton =
                new GpmImageButton(
                        ComponentResources.INSTANCE.images().logout());

        lSwitchButton.setTitle(Ui18n.CONSTANTS.bannerLogout());
        lSwitchButton.addClickHandler(pClickHandler);
        getBanner().addButton(lSwitchButton);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#addSwitchButton(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void setSwitchButtonHandler(final ClickHandler pClickHandler) {
        if (switchButton == null) {
            switchButton =
                    new GpmImageButton(
                            ComponentResources.INSTANCE.images().switchSpace());
            switchButton.setTitle(Ui18n.CONSTANTS.bannerAdministration());
            getBanner().addButton(switchButton);
        }
        if (switchButtonRegistration != null) {
            switchButtonRegistration.removeHandler();
        }
        switchButtonRegistration = switchButton.addClickHandler(pClickHandler);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#addHelpButton(com.google.gwt.event.dom.client.ClickHandler)
     */
    @Override
    public void addHelpButton(final ClickHandler pClickHandler) {
        final GpmImageButton lSwitchButton =
                new GpmImageButton(ComponentResources.INSTANCE.images().help());

        lSwitchButton.addClickHandler(pClickHandler);
        lSwitchButton.setTitle(Ui18n.CONSTANTS.bannerHelp());
        getBanner().addButton(lSwitchButton);
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
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.Display#stopProcessing()
     */
    @Override
    public void stopProcessing() {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setLoginAndLanguage(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setLoginAndLanguage(String pLogin, String pLanguage) {
        getBanner().setLoginAndLanguage(pLogin, pLanguage);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setProcessName(java.lang.String)
     */
    @Override
    public void setProcessName(String pProcessName) {
        getBanner().setProcessName(pProcessName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setVersion(java.lang.String)
     */
    @Override
    public void setVersion(String pVersion) {
        getBanner().setVersion(pVersion);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.main.MainDisplay#setContactUrl(java.lang.String)
     */
    @Override
    public void setContactUrl(String pContactUrl) {
        if (pContactUrl != null) {
            getBanner().setContactUrl(pContactUrl);
        }
    }
}