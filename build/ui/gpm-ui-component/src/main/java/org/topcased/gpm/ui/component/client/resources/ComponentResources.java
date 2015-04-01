/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Michael Kargbo (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.resources;

import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

/**
 * Resources for components module.
 * 
 * @author mkargbo
 */
public interface ComponentResources extends ClientBundle {
    /** Singleton for resources component module. */
    public static final ComponentResources INSTANCE =
            GWT.create(ComponentResources.class);

    public static final Ui18n I18N = GWT.create(Ui18n.class);

    public static final String THEME_PATH =
            "org/topcased/gpm/ui/component/client/resources/theme/default/";

    /**
     * Image bundle of Component module.
     * 
     * @return Image bundle.
     */
    public ComponentImageResource images();

    /**
     * CSS bundle of Component module.
     * 
     * @return CSS bundle.
     */
    @Source(THEME_PATH + "index.css")
    public ComponentCssResource css();

    /* GROUP */
    /**
     * The left image of the disclosure panels.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/group/groupLeft.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmDisclosurePanelHeaderLeft();

    /**
     * The center image of the disclosure panels.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/group/group.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmDisclosurePanelHeaderCenter();

    /**
     * The right image of the disclosure panels.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/group/groupRight.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmDisclosurePanelHeaderRight();

    /**
     * The center image of the light disclosure panels.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/group/lightGroup.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmDisclosurePanelLightHeaderCenter();

    /**
     * The right image of the light disclosure panels.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/group/lightGroupRight.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmDisclosurePanelLightHeaderRight();

    /* TAB */
    /**
     * The left image for the not selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabLeftNotSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmTabHeaderLeft();

    /**
     * The middle image for the not selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabNotSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmTabHeaderCenter();

    /**
     * The right image for the not selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabRightNotSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmTabHeaderRight();

    /**
     * The left image for the selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabLeftSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmTabHeaderLeftSelected();

    /**
     * The center image for the selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmTabHeaderCenterSelected();

    /**
     * The right image for the selected tab.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabRightSelected.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmTabHeaderRightSelected();

    /**
     * The image for the tabs panel.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/tab/tabBarBottom.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    public ImageResource gpmTabLayoutPanelTabBottom();

    /* STACK */
    /**
     * The left image of the stack header.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/stack/stackLeft.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmStackLeft();

    /**
     * The center image of the stack header.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/stack/stack.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmStackCenter();

    /**
     * The right image of the stack header.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/stack/stackRight.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmStackRight();

    /* MENU */
    /**
     * The left image of the decorated menu.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/menu/menuLeft.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmMenuLeft();

    /**
     * The right image of the decorated menu.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/menu/menuRight.png")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmMenuRight();

    /* BANNER */
    /**
     * Banner
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/header/banner.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    public ImageResource gpmBanner();

    /* POPUP */
    /**
     * The image of the popup header.
     * 
     * @return The image.
     */
    @Source(THEME_PATH + "img/popup/popup.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    public ImageResource gpmPopup();
}
