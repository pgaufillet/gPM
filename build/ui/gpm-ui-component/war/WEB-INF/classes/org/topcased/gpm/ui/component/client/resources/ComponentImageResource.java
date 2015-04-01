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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree.Resources;

/**
 * Bundle for retrieving images.
 * 
 * @author mkargbo
 */
public interface ComponentImageResource extends ClientBundle, Resources {
    /* COMMON */
    /**
     * Loading picture.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/loading.gif")
    public ImageResource loading();

    /**
     * Loading picture.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/loadingTop.gif")
    public ImageResource loadingTop();

    /**
     * Add button.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/add.gif")
    public ImageResource add();

    /**
     * Add button - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/addHover.gif")
    public ImageResource addHover();

    /**
     * Close button.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/close.gif")
    public ImageResource close();

    /**
     * Close button - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/closeHover.gif")
    public ImageResource closeHover();

    /**
     * Refresh button.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/refresh.gif")
    public ImageResource refresh();

    /**
     * Email.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/email.gif")
    public ImageResource email();

    /**
     * Open a popup to zoom.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/zoom.gif")
    public ImageResource zoom();

    /**
     * Small black arrow up
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/arrowUp.gif")
    public ImageResource arrowUpBlackSmall();

    /**
     * Small black arrow left
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/arrowLeft.gif")
    public ImageResource arrowLeftBlackSmall();

    /* GROUP */
    /**
     * Open a group.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/group/show.gif")
    public ImageResource show();

    /**
     * Open a group - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/group/showHover.gif")
    public ImageResource showHover();

    /**
     * Close a group.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/group/hide.gif")
    public ImageResource hide();

    /**
     * Close a group - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/group/hideHover.gif")
    public ImageResource hideHover();

    /**
     * Image for extended action.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/common/extendedAction.gif")
    public ImageResource extendedAction();

    /* WORKSPACE */
    /**
     * Maximize a sub panel.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/workspace/maximize.gif")
    public ImageResource maximize();

    /**
     * Move a sub panel to the left.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/workspace/minimizeLeft.gif")
    public ImageResource minimizeLeft();

    /**
     * Move a sub panel to the right.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/workspace/minimizeRight.gif")
    public ImageResource minimizeRight();

    /**
     * Move a sub panel to the top.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/workspace/minimizeUp.gif")
    public ImageResource minimizeUp();

    /**
     * Move a sub panel to the bottom.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/workspace/minimizeDown.gif")
    public ImageResource minimizeDown();

    /* TOOL BAR */
    /**
     * Bold text image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextBold.gif")
    public ImageResource richTextBold();

    /**
     * Italic text image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextItalic.gif")
    public ImageResource richTextItalic();

    /**
     * Underline text image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextUnderline.gif")
    public ImageResource richTextUnderline();

    /**
     * Align left image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextAlignLeft.gif")
    public ImageResource richTextAlignLeft();

    /**
     * Align right image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextAlignRight.gif")
    public ImageResource richTextAlignRight();

    /**
     * Align middle image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextAlignMiddle.gif")
    public ImageResource richTextAlignMiddle();

    /**
     * Ordered list image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextOrderList.gif")
    public ImageResource richTextOrderList();

    /**
     * Unordered list image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextUnorderList.gif")
    public ImageResource richTextUnorderList();

    /**
     * Indent left image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextIndentLeft.gif")
    public ImageResource richTextIndentLeft();

    /**
     * Indent right image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextIndentRight.gif")
    public ImageResource richTextIndentRight();

    /**
     * Insert URL image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/toolbar/richTextUrl.gif")
    public ImageResource richTextInsertUrl();

    /**
     * Insert line image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextInsertLine.gif")
    public ImageResource richTextInsertLine();

    /**
     * Insert image image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextInsertImage.gif")
    public ImageResource richTextInsertImage();

    /**
     * Remove formatting image
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/toolbar/richTextRemoveFormatting.gif")
    public ImageResource richTextRemoveFormatting();

    /* MULTIVALUED */
    /**
     * Restore a delete line.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/multivalued/back.gif")
    public ImageResource undo();

    /**
     * Restore a delete line - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/multivalued/back_hover.gif")
    public ImageResource undoHover();

    /**
     * Move up a line.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/multivalued/arrow_up.gif")
    public ImageResource arrowUp();

    /**
     * Move up a line - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/multivalued/arrow_up_hover.gif")
    public ImageResource arrowUpHover();

    /**
     * Move down a line.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/multivalued/arrow_down.gif")
    public ImageResource arrowDown();

    /**
     * Move down a line - hover.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH
            + "img/multivalued/arrow_down_hover.gif")
    public ImageResource arrowDownHover();

    /* HEADER */
    /**
     * The banner of the header.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/header/banner.gif")
    public ImageResource banner();

    /**
     * The logout button of the banner.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/header/logout.gif")
    public ImageResource logout();

    /**
     * The button to switch space from the banner.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/header/switch.gif")
    public ImageResource switchSpace();

    /**
     * The button to open help.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/header/help.gif")
    public ImageResource help();

    /* TREE */
    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Tree.Resources#treeOpen()
     */
    @Source(ComponentResources.THEME_PATH + "img/tree/minus.gif")
    public ImageResource treeOpen();

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Tree.Resources#treeClosed()
     */
    @Source(ComponentResources.THEME_PATH + "img/tree/plus.gif")
    public ImageResource treeClosed();

    /**
     * {@inheritDoc}
     * 
     * @see com.google.gwt.user.client.ui.Tree.Resources#treeLeaf()
     */
    @Source(ComponentResources.THEME_PATH + "img/tree/leaf.gif")
    public ImageResource treeLeaf();

    /**
     * An icon representing a tree window
     * 
     * @return An icon representing a tree window
     */
    @Source(ComponentResources.THEME_PATH + "img/tree/treeIcon.gif")
    public ImageResource treeIcon();

    /* SHEET */
    /**
     * Open a sheet on visualization mode.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetVisu.gif")
    public ImageResource sheetVisu();

    /**
     * Open a sheet on edition mode.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetEdit.gif")
    public ImageResource sheetEdit();

    /**
     * Add a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetAdd.gif")
    public ImageResource sheetAdd();

    /**
     * Save a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetSave.gif")
    public ImageResource sheetSave();

    /**
     * Delete a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetDelete.gif")
    public ImageResource sheetDelete();

    /**
     * Export a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetExport.gif")
    public ImageResource sheetExport();

    /**
     * Import a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetImport.gif")
    public ImageResource sheetImport();

    /**
     * Duplicate a sheet.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetDuplicate.gif")
    public ImageResource sheetDuplicate();

    /**
     * Change a sheet state.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetState.gif")
    public ImageResource sheetState();

    /**
     * Change a sheet state.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetTransitions.gif")
    public ImageResource sheetTransitions();

    /**
     * The sheet is locked.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetLock.gif")
    public ImageResource sheetLock();

    /**
     * Close all sheets.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/sheet/sheetsClose.gif")
    public ImageResource sheetsClose();

    /* LINK */
    /**
     * Add a link.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/link/linkAdd.gif")
    public ImageResource linkAdd();

    /**
     * Delete a link.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/link/linkDelete.gif")
    public ImageResource linkDelete();

    /* FILTER */
    /**
     * Edit a filter.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/filter/filterEdit.gif")
    public ImageResource filterEdit();

    /**
     * Add a filter.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/filter/filterAdd.gif")
    public ImageResource filterAdd();

    /**
     * Delete a filter.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/filter/filterDelete.gif")
    public ImageResource filterDelete();

    /**
     * Export a filter.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/filter/filterExport.gif")
    public ImageResource filterExport();

    /**
     * The filter report icon
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/filter/filterReport.gif")
    public ImageResource filterReport();

    /* MENU */
    /**
     * Button for group on menu.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/menu/menuHide.gif")
    public ImageResource menuHide();

    /**
     * The menu separator.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/menu/separator.gif")
    public ImageResource menuSeparator();

    /* MESSAGE */
    /**
     * The info message
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/message/info.gif")
    public ImageResource info();

    /**
     * The error message
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/message/error.gif")
    public ImageResource error();

    /**
     * The confirmation message
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/message/question.gif")
    public ImageResource question();

    /* PAGER */
    /**
     * The button to go to the first page.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/pager/firstPage.gif")
    public ImageResource firstPage();

    /**
     * The button to go to the previous page.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/pager/previousPage.gif")
    public ImageResource previousPage();

    /**
     * The button to go to the next page.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/pager/nextPage.gif")
    public ImageResource nextPage();

    /**
     * The button to go to the last page.
     * 
     * @return The image.
     */
    @Source(ComponentResources.THEME_PATH + "img/pager/lastPage.gif")
    public ImageResource lastPage();

}