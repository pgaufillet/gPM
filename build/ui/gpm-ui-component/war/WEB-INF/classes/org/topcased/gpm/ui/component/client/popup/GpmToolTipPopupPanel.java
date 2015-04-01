/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien Eballard (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.popup;

import org.topcased.gpm.ui.component.client.button.GpmDoubleImageButton;
import org.topcased.gpm.ui.component.client.resources.ComponentResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * GpmPopupPanel
 * 
 * @author jeballar
 * @deprecated Quick fix for tooltip panels, to be removed and replaced by
 *             GpmPopupPanel
 */
public class GpmToolTipPopupPanel extends PopupPanel {
    private static final int HEADER_SIZE = 30;

    private static final String STYLE_HEADER =
            ComponentResources.INSTANCE.css().gpmPopupHeader();

    private static final String STYLE_CONTENT =
            ComponentResources.INSTANCE.css().gpmPopupContent();

    private static final String STYLE_HEADER_LEFT =
            ComponentResources.INSTANCE.css().gpmPopupHeaderTitle();

    private static final String STYLE_HEADER_RIGHT =
            ComponentResources.INSTANCE.css().gpmPopupHeaderButton();

    private static final String STYLE_POPUP_PANEL =
            ComponentResources.INSTANCE.css().gpmPopupPanel();

    private static final String STYLE_POPUP_GLASS =
            ComponentResources.INSTANCE.css().gpmPopupGlass();

    private final DockPanel mainPanel = new DockPanel();

    /** Header panel */
    private final FlowPanel headerPanel = new FlowPanel();

    /** Content panel */
    protected final ScrollPanel contentPanel = new ScrollPanel();

    /** Panel around the content panel */
    protected final SimplePanel styledContentPanel = new SimplePanel();

    /** Indicates if the Popup has been built for display */
    private boolean isBuilt = false;

    private double widthRatio = -1;

    private double heightRatio = -1;

    /**
     * Indicate if the Popup must be closeable (show or hide "X" button at top
     * right)
     */
    private boolean isCloseable = true;

    private GpmDoubleImageButton closeButton;

    /**
     * Build a new Popup
     * 
     * @param pCloseable
     *            If the popup is closeable.
     */
    public GpmToolTipPopupPanel(boolean pCloseable) {
        super();
        isCloseable = pCloseable;
        setAnimationEnabled(false);
        setModal(true);
        if (pCloseable) {
            enableGlass();
        }
        setAutoHideEnabled(!pCloseable);
        // CSS
        addStyles();
    }

    /**
     * Protected constructor for subclasses
     */
    protected GpmToolTipPopupPanel() {
        super();
        isCloseable = false;
        setAnimationEnabled(false);
        addStyles();
    }

    /**
     * Enable the glass panel.
     */
    protected void enableGlass() {
        setGlassEnabled(true);
        setGlassStyleName(STYLE_POPUP_GLASS);
        getGlassElement().getStyle().setProperty("filter", "alpha(opacity=30)");
    }

    /**
     * Add styles to the popup
     */
    private void addStyles() {
        styledContentPanel.addStyleName(STYLE_CONTENT);
        setStyleName(STYLE_POPUP_PANEL);

        styledContentPanel.addStyleName("STYLED_CONTENT");
        contentPanel.addStyleName("CONTENT_PANEL");
        mainPanel.addStyleName("MAIN_PANEL");
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#show()
     */
    @Override
    public void show() {
        if (!isBuilt) {
            Widget lHeader = buildHeader();
            mainPanel.add(lHeader, DockPanel.NORTH);
            mainPanel.setCellHeight(lHeader, HEADER_SIZE + "px");
            styledContentPanel.add(contentPanel);
            mainPanel.add(styledContentPanel, DockPanel.CENTER);

            setWidget(mainPanel);
            isBuilt = true;
        }
        if (widthRatio != -1 && heightRatio != -1) {
            setPixelSize((int) (Window.getClientWidth() * widthRatio),
                    (int) (Window.getClientHeight() * heightRatio));
        }
        super.show();
        center();
    }

    /**
     * Build header
     */
    private Widget buildHeader() {
        final FlowPanel lHeader = new FlowPanel();

        lHeader.setStyleName(STYLE_HEADER);

        // Title
        headerPanel.addStyleName(STYLE_HEADER_LEFT);
        lHeader.add(headerPanel);

        // Close button : Only if closeable
        if (isCloseable) {
            final SimplePanel lButtonPanel = new SimplePanel();

            closeButton =
                    new GpmDoubleImageButton(
                            ComponentResources.INSTANCE.images().close(),
                            ComponentResources.INSTANCE.images().closeHover());
            closeButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent pEvent) {
                    hide();
                }
            });
            lButtonPanel.setStyleName(STYLE_HEADER_RIGHT);
            lButtonPanel.add(closeButton);
            lHeader.add(lButtonPanel);
        }

        return lHeader;
    }

    /**
     * Get the close button.
     * 
     * @return The close button.
     */
    public HasClickHandlers getCloseButton() {
        return closeButton;
    }

    /**
     * Set the Header widget
     * 
     * @param pHeader
     *            The Widget to put in header
     */
    public void setHeader(final Widget pHeader) {
        headerPanel.clear();
        headerPanel.add(pHeader);
    }

    /**
     * Set the Widget in the Popup
     * 
     * @param pContent
     *            The Widget contained in the Popup
     */
    public void setContent(final Widget pContent) {
        contentPanel.clear();
        contentPanel.add(pContent);
        if (widthRatio != -1 && heightRatio != -1) {
            setPixelSize((int) (Window.getClientWidth() * widthRatio),
                    (int) (Window.getClientHeight() * heightRatio));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#setWidth(java.lang.String)
     */
    @Override
    public void setWidth(final String pWidth) {
        throw new IllegalStateException("Use setPixelSize only");
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.PopupPanel#setHeight(java.lang.String)
     */
    @Override
    public void setHeight(final String pHeight) {
        throw new IllegalStateException("Use setPixelSize only");
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#setSize(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void setSize(final String pWidth, final String pHeight) {
        throw new IllegalStateException("Use setPixelSize only");
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.UIObject#setPixelSize(int, int)
     */
    @Override
    public void setPixelSize(final int pWidth, final int pHeight) {
        super.setWidth(pWidth + "px");
        super.setHeight(pHeight + "px");

        int lContentWidth =
                pWidth
                        - (mainPanel.getElement().getClientWidth() - Math.min(
                                contentPanel.getElement().getOffsetWidth(),
                                contentPanel.getWidget().getElement().getOffsetWidth()));
        int lContentHeight =
                pHeight
                        - (mainPanel.getElement().getClientHeight() - Math.min(
                                contentPanel.getElement().getOffsetHeight(),
                                contentPanel.getWidget().getElement().getOffsetHeight()));
        mainPanel.setPixelSize(pWidth, pHeight);

        contentPanel.setPixelSize(lContentWidth, lContentHeight);
        contentPanel.getWidget().setPixelSize(lContentWidth, lContentHeight);
    }

    /**
     * Set the size as a ratio of the window size.
     * 
     * @param pWidthRatio
     *            The width ratio : 0 to 1.
     * @param pHeightRatio
     *            The width ratio : 0 to 1.
     */
    public void setRatioSize(final double pWidthRatio, final double pHeightRatio) {
        widthRatio = pWidthRatio;
        heightRatio = pHeightRatio;
        setPixelSize((int) (Window.getClientWidth() * pWidthRatio),
                (int) (Window.getClientHeight() * pHeightRatio));
    }

    /**
     * Set style of the Content panel
     * 
     * @param pContentPanelStyle
     *            the style
     */
    public void addContentStyleName(String pContentPanelStyle) {
        contentPanel.addStyleName(pContentPanelStyle);
    }

}
