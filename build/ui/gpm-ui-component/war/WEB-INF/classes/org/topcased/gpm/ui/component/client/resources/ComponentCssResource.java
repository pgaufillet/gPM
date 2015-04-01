/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas Panuel (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.component.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.CellTable.Style;

/**
 * Bundle for retrieving CSS.
 * 
 * @author tpanuel
 */
public interface ComponentCssResource extends CssResource, Style {
    /* GPM BUTTONS */
    public String gpmImageButton();

    public String gpmImageButtonDisabled();

    public String gpmMenuImageButton();

    @ClassName("gpmImageButton-hover")
    public String gpmImageButtonHover();

    @ClassName("gpmImageButton-active")
    public String gpmImageButtonActive();

    public String gpmDoubleImageButton();

    public String gpmTabCloseButton();

    public String gpmTabAddButton();

    public String gpmTextButton();

    @ClassName("gpmTextButton-hover")
    public String gpmTextButtonHover();

    public String gpmRightAlignButton();

    /* MENUS */
    public String gpmMenuFloat();

    public String gpmMenu();

    public String gpmRightAlignMenuBar();

    public String gpmMenuButton();

    @ClassName("gpmMenuButton-selected")
    public String gpmMenuButtonSelected();

    @ClassName("gpmMenuButton-active")
    public String gpmMenuButtonActive();

    public String gpmMenuItem();

    @ClassName("gpmMenuItem-selected")
    public String gpmMenuItemSelected();

    @ClassName("gpmSubMenuIcon")
    public String gpmSubMenuIcon();

    @ClassName("gpmSubMenuIcon-selected")
    public String gpmSubMenuIconSelected();

    public String gpmMenuItemSeparator();

    public String gpmMenuPopup();

    public String gpmToolBar();

    public String gpmMenuTitles();

    public String gpmDecoratedMenu();

    public String gpmMenuTitle();

    public String gpmMenuSubTitle();

    public String gpmMinimizedMenu();

    /* VALUES CONTAINER */
    public String gpmDisclosurePanel();

    public String gpmLightDisclosurePanel();

    public String gpmLightDisclosurePanelOpened();

    public String gpmLightDisclosurePanelHeader();

    public String gpmLightDisclosurePanelHeaderCenter();

    public String header();

    public String content();

    public String gpmLightDisclosurePanelHeaderRight();

    public String gpmValuesContainerPanel();

    public String gpmGroupName();

    @ClassName("gpmGroupName-hover")
    public String gpmGroupNameHover();

    public String gpmFieldLabel();

    /* TABS */
    public String gpmTabLayoutPanelContent();

    public String gpmTabLayoutPanelTabs();

    public String gpmTabHeader();

    @ClassName("gpmTabHeader-selected")
    public String gpmTabHeaderSelected();

    /* STACK */
    public String gpmStackLayoutPanel();

    public String gpmStackContent();

    public String gpmStackHeader();

    @ClassName("gpmStackHeader-first")
    public String gpmStackFirstHeader();

    /* DECORATOR */
    public String left();

    public String center();

    public String right();

    /* LAYOUT */
    public String gpmHorizontalPanel();

    public String gpmHorizontalPanelContent();

    /* POPUP */
    public String gpmPopupPanel();

    public String gpmPopupHeader();

    public String gpmPopupHeaderTitle();

    public String gpmPopupHeaderButton();

    public String gpmPopupContent();

    public String gpmPopupFooter();

    public String gpmPopupFooterButton();

    public String gpmPopupGlass();

    public String gpmBasicPopupPanel();

    public String gpmPopupErrorMessage();

    /* TREE */
    public String gpmTree();

    public String gpmTreeItem();

    @ClassName("gpmTreeItem-selecteable")
    public String gpmTreeItemSelecteable();

    @ClassName("gpmTreeItem-selected")
    public String gpmTreeItemSelected();

    @ClassName("gpmTreeItem-hover")
    public String gpmTreeItemHover();

    public String gpmTreeButton();

    /* DROPDOWN LIST */
    public String gpmDropDownListBoxWidget();

    public String gpmDropDownText();

    public String gpmDropDownTextReadOnly();

    public String gpmDropDownButtonReadOnly();

    public String gpmDropDownButton();

    public String gpmDropDownListPopup();

    public String gpmListPopupElement();

    @ClassName("gpmListPopupElement-hover")
    public String gpmListPopupElementHover();

    @ClassName("gpmListPopupElement-selected")
    public String gpmListPopupElementSelected();

    /* GPM DATE BOX */

    public String gpmDateBoxFormatError();

    public String gpmDateBox();

    /* GPM DATE PICKER */
    public String gpmDatePickerPopup();

    //public String gpmDatePickerToday();

    /* TimePicker */
    public String gpmTimePicker();

    public String gpmTimePickerPopup();

    public String gpmTimePickerPopupElement();

    @ClassName("gpmTimePickerPopupElement-hover")
    public String gpmTimePickerPopupElementHover();

    @ClassName("gpmTimePickerPopupElement-large")
    public String gpmTimePickerPopupElementLarge();

    @ClassName("gpmTimePickerPopupElement-tiny")
    public String gpmTimePickerPopupElementTiny();

    @ClassName("gpmTimePickerPopupElement-active")
    public String gpmTimePickerPopupElementActive();

    //Field style
    @ClassName("gpm-field-error")
    public String gpmFieldError();

    @ClassName("gpm-field-error-message")
    public String gpmFieldErrorMessage();

    /* GPM CALENDAR VIEW (for date picker) */
    public String gpmCalendarViewDay();

    public String gpmCalendarViewDays();

    public String gpmCalendarViewDayIsWeekend();

    public String gpmCalendarViewDayIsFiller();

    public String gpmCalendarViewDayIsHighlighted();

    public String gpmCalendarViewDayIsDisabled();

    public String gpmCalendarViewDayIsValueAndHighlighted();

    public String gpmCalendarViewDayIsSelected();

    /* GPM MONTH SELECTOR (for date picker) */

    public String gpmMonthSelectorPreviousButton();

    public String gpmMonthSelectorNextButton();

    public String gpmMonthSelectorMonth();

    public String gpmMonthSelector();

    /* SPLIT LAYOUT */
    public String gpmSplitLayoutPanel();

    public String gpmSplitLayoutHorizontalDragger();

    public String gpmSplitLayoutVerticalDragger();

    /* WORKSPACE LAYOUT */
    public String gpmWorkspaceLayout();

    public String gpmWorkspaceLayoutChild();

    /* MULTIVALUED WIDGET (and MULTIPLE MULTIVALUED) */
    public String gpmMultivaluedWidgetButton();

    public String gpmMultivaluedWidgetLine();

    public String gpmMultivaluedWidgetLineDeleted();

    public String gpmMultivaluedWidget();

    /* TABLE */
    public String gpmTable();

    public String gpmTableImage();

    public String gpmFieldGrid();

    public String gpmFieldGridField();

    public String gpmMultiple();

    public String gpmMultipleUnderline();

    public String gpmTablePager();

    public String gpmTablePagerLabel();

    public String gpmTablePagerTextBox();

    /* FILTER TABLE */
    public String gpmFilterTable();

    /* FILTER EDITION POPUP DOCK LAYOUT */
    public String gpmFilterEditionDockTitle();

    public String gpmFilterEditionDock();

    public String gpmFilterEditionCriteria();

    public String gpmFilterEditionCriteriaLeft();

    public String gpmFilterEditionCriteriaRight();

    /* GPM IMAGE TOGGLE BUTTON */
    public String gpmImageToggleButton();

    public String gpmImageToggleButtonDown();

    public String gpmImageToggleButtonUp();

    public String gpmImageToggleButtonUpHover();

    public String gpmImageToggleButtonDownHover();

    /* GPM RICH TEXT BPX */
    public String gpmRichTextBoxWidget();

    public String gpmRichTextArea();

    public String gpmRichTextToolbar();

    public String gpmRichTextToolbarLine();

    /* GPM MENU LIST BOX */
    public String gpmMenuListBox();

    /* GPM BANNER */
    public String gpmBanner();

    public String gpmBannerContainer();

    public String gpmBannerElement();

    public String gpmBannerVersion();

    public String gpmBannerProcess();

    public String gpmBannerLogin();

    /* GPM IFRAME */
    public String gpmIFrame();

    /* GPM BORDER */
    public String gpmSmallBorder();

    public String gpmBigBorder();

    /* GPM LOADING */
    public String gpmLoadingLabel();

    public String gpmLoadingGlass();

    /*  GPM EXECUTION REPORT */
    public String gpmExecutionReportContent();

    public String gpmExecutionReportTitle();

    public String gpmExecutionReportTabLevel1();

    public String gpmExecutionReportTabLevel2();

    /* GPM TREE CHOICE WIDGET */
    public String gpmTreeChoiceField();

    public String gpmTreeChoiceFieldPopup();

    public String gpmTreeShifterItemSelected();

    public String gpmTreeShifterItemNotSelected();

    public String gpmTreeShifterSelectorItem();

    /* GPM LIST SHIFTER */
    public String gpmListShifterSelector();

    public String gpmListShifterSelectorList();

    public String gpmListShifterSelectorItem();

    public String gpmListShifterSelectorListTitle();

    public String gpmListShifterItemSelected();

    public String gpmListShifterItemNotSelected();

    /* TRANSITION HISTORY CONTENT (SHEET STATUS CHANGES) */
    public String transitionHistoryContent();

    public String gpmTextArea();
}