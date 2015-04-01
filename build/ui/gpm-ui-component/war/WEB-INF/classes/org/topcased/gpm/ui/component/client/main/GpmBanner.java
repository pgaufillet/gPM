/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.main;

import org.topcased.gpm.ui.component.client.button.GpmImageButton;
import org.topcased.gpm.ui.component.client.button.GpmTextButton;
import org.topcased.gpm.ui.component.client.layout.GpmFlowLayoutPanel;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.GpmAnchorTarget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmBanner
 * 
 * @author jeballar
 */
public class GpmBanner extends GpmFlowLayoutPanel {

    /** Style & CSS */
    private static final String STYLE_GPM_BANNER =
            ComponentResources.INSTANCE.css().gpmBanner();

    private static final String STYLE_GPM_BANNER_CONTAINER =
            ComponentResources.INSTANCE.css().gpmBannerContainer();

    private static final String STYLE_GPM_BANNER_ELEMENT =
            ComponentResources.INSTANCE.css().gpmBannerElement();

    private static final String STYLE_GPM_BANNER_VERSION =
            ComponentResources.INSTANCE.css().gpmBannerVersion();

    private static final String STYLE_GPM_BANNER_PROCESS =
            ComponentResources.INSTANCE.css().gpmBannerProcess();

    private static final String STYLE_GPM_BANNER_LOGIN =
            ComponentResources.INSTANCE.css().gpmBannerLogin();

    private static final String TEXT_LOGIN_TOOLTIP = Ui18n.CONSTANTS.user();

    private static final String TEXT_CONTACT_URL_TOOLTIP =
            Ui18n.CONSTANTS.contactUrl();

    private static final String TEXT_VERSION_TOOLTIP =
            Ui18n.CONSTANTS.version();

    private static final String TEXT_PROCESS_TOOLTIP =
            Ui18n.CONSTANTS.process();

    /** Minimize button */
    private final GpmImageButton minimizeButton =
            new GpmImageButton(
                    ComponentResources.INSTANCE.images().minimizeUp());

    /** Restore button */
    private final GpmImageButton restoreButton =
            new GpmImageButton(
                    ComponentResources.INSTANCE.images().minimizeDown());

    /** Picture panel */
    private final LayoutPanel banner = new LayoutPanel();

    /** Informations panel */
    private final FlowPanel infoPanel = new FlowPanel();

    private final Label versionLabel = new Label();

    private final GpmTextButton loginLabel = new GpmTextButton();

    private final FlowPanel processPanel = new FlowPanel();

    private final Label processLabel = new Label();

    private final GpmTextButton contactUrlLabel = new GpmTextButton();

    private String contactUrl;

    /** Buttons panel */
    private final FlowPanel buttonsPanel = new FlowPanel();

    /** Container handler, to call when resized (minimize, restore) */
    private GpmMainClientFrame.ContentResizeHandler containerHandler = null;

    private boolean minimized = false;

    /**
     * Constructor
     */
    public GpmBanner() {
        build();
    }

    /**
     * Define the banner content and behavior
     */
    private void build() {
        setStyleName(STYLE_GPM_BANNER_CONTAINER);
        versionLabel.setStyleName(STYLE_GPM_BANNER_VERSION);
        processLabel.setStyleName(STYLE_GPM_BANNER_PROCESS);
        loginLabel.addStyleName(STYLE_GPM_BANNER_LOGIN);
        contactUrlLabel.addStyleName(STYLE_GPM_BANNER_LOGIN);
        processPanel.addStyleName(STYLE_GPM_BANNER_ELEMENT);

        banner.setStyleName(STYLE_GPM_BANNER);
        banner.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        add(banner);

        buttonsPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        add(buttonsPanel);

        infoPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        add(infoPanel);

        minimizeButton.setTitle(Ui18n.CONSTANTS.minimize());
        addButton(minimizeButton);
        restoreButton.setTitle(Ui18n.CONSTANTS.maximize());
        addButton(restoreButton);

        processPanel.add(processLabel);

        addInfo(versionLabel, true);
        addInfo(loginLabel, true);
        addInfo(contactUrlLabel, true);
        addInfo(processPanel, false);

        versionLabel.setTitle(TEXT_VERSION_TOOLTIP);
        loginLabel.setTitle(TEXT_LOGIN_TOOLTIP);
        contactUrlLabel.setText(TEXT_CONTACT_URL_TOOLTIP);
        processLabel.setTitle(TEXT_PROCESS_TOOLTIP);

        restoreButton.setVisible(false);
        minimizeButton.setVisible(false);
        contactUrlLabel.setVisible(false);

        moveToRight(minimizeButton.getElement());
        moveToRight(restoreButton.getElement());

        // Add minimize and restore handlers
        minimizeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                minimize();
            }
        });
        restoreButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                restore();
            }
        });
        contactUrlLabel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent pEvent) {
                Window.open(contactUrl, GpmAnchorTarget.BLANK.getValue(), "");
            }
        });
    }

    /**
     * Add a click handler to the login label
     * 
     * @param pHandler
     *            the login label click handler
     */
    public void addUserProfileEditionHandler(ClickHandler pHandler) {
        loginLabel.addClickHandler(pHandler);
    }

    /**
     * Minimize the banner
     */
    private void minimize() {
        if (minimized) {
            return;
        }

        banner.setVisible(false);

        for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
            cancelMoveToRight(buttonsPanel.getWidget(i).getElement());
        }
        for (int i = 0; i < infoPanel.getWidgetCount(); i++) {
            cancelMoveToRight(infoPanel.getWidget(i).getElement());
        }
        minimizeButton.setVisible(false);
        restoreButton.setVisible(true);

        processPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.LEFT);
        infoPanel.remove(processPanel);
        add(processPanel);

        minimized = true;
        containerHandler.contentResized(getMinimumHeight());
    }

    /**
     * Restore the banner
     */
    private void restore() {
        if (!minimized) {
            return;
        }

        remove(processPanel);
        infoPanel.add(processPanel);
        moveToRight(processPanel.getElement());
        processPanel.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);

        banner.setVisible(true);

        for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
            moveToRight(buttonsPanel.getWidget(i).getElement());
        }
        for (int i = 0; i < infoPanel.getWidgetCount(); i++) {
            moveToRight(infoPanel.getWidget(i).getElement());
        }

        minimizeButton.setVisible(true);
        restoreButton.setVisible(false);

        minimized = false;
        containerHandler.contentResized(getMaximizedHeight());
    }

    /**
     * Add a banner in the panel
     * 
     * @param pButton
     *            the button to add in the Banner
     */
    public void addButton(GpmImageButton pButton) {
        buttonsPanel.insert(pButton, buttonsPanel.getWidgetCount());
        pButton.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        pButton.addStyleName(ComponentResources.INSTANCE.css().gpmMenuImageButton());
        if (minimized) {
            cancelMoveToRight(pButton.getElement());
        }
        else {
            moveToRight(pButton.getElement());
            if (containerHandler != null) {
                int lNewSize = getMaximizedHeight();
                containerHandler.contentResized(lNewSize);
            }
        }
        // Set the minimize button visible
        minimizeButton.setVisible(true);
    }

    /**
     * Add an info widget into the info panel. If pSurround is true, add a
     * surrounding widget with banner element style (padding)
     * 
     * @param pInfo
     *            the info widget to add
     * @param pSurround
     *            indicates if a surrounding widget must be added
     */
    private void addInfo(Widget pInfo, boolean pSurround) {
        Widget lToAdd = pInfo;

        if (pSurround) {
            FlowPanel lPanel = new FlowPanel();
            lPanel.addStyleName(STYLE_GPM_BANNER_ELEMENT);
            lPanel.add(pInfo);
            lToAdd = lPanel;
        }

        infoPanel.add(lToAdd);
        lToAdd.getElement().getStyle().setFloat(
                com.google.gwt.dom.client.Style.Float.RIGHT);
        lToAdd.getElement().getStyle().setProperty("clear", "right");
        moveToRight(lToAdd.getElement());
    }

    /**
     * Set the process name text displayed in banner
     * 
     * @param pProcessName
     *            the process name
     */
    public void setProcessName(String pProcessName) {
        processLabel.setText(pProcessName);
    }

    /**
     * Set version displayed in banner
     * 
     * @param pVersion
     *            the version text
     */
    public void setVersion(String pVersion) {
        versionLabel.setText(pVersion);
    }

    /**
     * Set the login and associated language displayed in banner
     * 
     * @param pLogin
     *            login
     * @param pLanguage
     *            language
     */
    public void setLoginAndLanguage(String pLogin, String pLanguage) {
        String lLanguage = pLanguage;
        loginLabel.setText(pLogin + " (" + lLanguage + ")");
    }

    /**
     * Add the "float right" and "clear both" style attributes
     * 
     * @param pElement
     *            the element onto add the attributes
     */
    private void moveToRight(Element pElement) {
        pElement.getStyle().setProperty("clear", "right");
    }

    /**
     * Remove the float right" and "clear both" style attributes
     * 
     * @param pElement
     *            the element onto add the attributes
     */
    private void cancelMoveToRight(Element pElement) {
        pElement.getStyle().setProperty("clear", "none");
    }

    /**
     * get minimumHeight
     * 
     * @return the minimumHeight
     */
    private int getMinimumHeight() {

        // Begin with width computation
        int lWidth = 0;
        for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
            lWidth += 2 + buttonsPanel.getWidget(i).getOffsetWidth();
        }
        buttonsPanel.setWidth(lWidth + "px");
        lWidth = 0;
        for (int i = 0; i < infoPanel.getWidgetCount(); i++) {
            lWidth += infoPanel.getWidget(i).getOffsetWidth();
        }
        infoPanel.setWidth(lWidth + "px");

        // Max height computation
        int lMax = 0;
        for (Widget lChild : getChildren()) {
            if (lMax < lChild.getOffsetHeight()) {
                lMax = lChild.getOffsetHeight();
            }
        }
        return lMax;
    }

    /**
     * Calculate the sum of height of all buttons. <br />
     * Also reset the width of widget to handle wrong clear css property
     * behavior in IE by setting the widget width
     * 
     * @return the sum of height of all buttons
     */
    private int getMaximizedHeight() {
        // Begin with width computation
        int lWidth = 0;
        for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
            if (buttonsPanel.getWidget(i).getOffsetWidth() > lWidth) {
                lWidth = buttonsPanel.getWidget(i).getOffsetWidth();
            }
        }
        buttonsPanel.setWidth(lWidth + 2 + "px");

        infoPanel.setWidth("auto");

        // Max Height computation
        int lSum = 0;
        for (int i = 0; i < buttonsPanel.getWidgetCount(); i++) {
            Widget lChild = buttonsPanel.getWidget(i);
            if (lChild.isVisible() && (lChild != banner)) {
                lSum += lChild.getOffsetHeight();
            }
        }
        if (!minimized) {
            lSum = Math.max(lSum, banner.getOffsetHeight());
        }
        return lSum + 2;
    }

    /**
     * Set the content resize handler
     * 
     * @param pHandler
     *            the content resize handler
     */
    public void setContentResizeHandler(
            GpmMainClientFrame.ContentResizeHandler pHandler) {
        containerHandler = pHandler;

        // Add a new DeferredCommand to initialize banner size when first displayed
        if (containerHandler != null) {
            DeferredCommand.addCommand(new Command() {
                @Override
                public void execute() {
                    int lNewSize = getMaximizedHeight();
                    containerHandler.contentResized(lNewSize);
                }
            });
        }
    }

    public LayoutPanel getBanner() {
        return banner;
    }

    /**
     * Set the contact URL in banner
     * 
     * @param pContactUrl
     *            the contact URL to set
     */
    public void setContactUrl(String pContactUrl) {
        contactUrlLabel.setVisible(true);
        contactUrlLabel.setTitle(pContactUrl);
        contactUrl = pContactUrl;
    }

}
