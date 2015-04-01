/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Thomas PANUEL (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.menu;

import static org.topcased.gpm.ui.component.client.resources.ComponentResources.INSTANCE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.builder.AbstractDynamicCommandBuilder;
import org.topcased.gpm.ui.application.client.command.extended.ExecuteExtendedActionCommand;
import org.topcased.gpm.ui.application.shared.util.ExtendedActionType;
import org.topcased.gpm.ui.component.client.menu.GpmRightAlignMenuBar;
import org.topcased.gpm.ui.component.client.menu.GpmToolBar;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionExtended;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;

/**
 * A builder that can be used to create a menu.
 * 
 * @author tpanuel
 */
abstract public class AbstractMenuBuilder {
    private final static String EMPTY = "";

    protected final static Map<String, ImageResource> IMAGES =
            new HashMap<String, ImageResource>();

    private final static ImageResource DEFAULT_IMAGE =
            INSTANCE.images().extendedAction();

    private final static Map<String, String> ACTIONS_TRANSLATIONS =
            new HashMap<String, String>();

    static {
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_CREATION,
                Ui18n.CONSTANTS.sheetCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_SAVE,
                Ui18n.CONSTANTS.sheetSaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_DISPLAY,
                Ui18n.CONSTANTS.sheetDisplayButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_EDIT,
                Ui18n.CONSTANTS.sheetEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_REFRESH,
                Ui18n.CONSTANTS.sheetRefreshButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_CHANGE_STATE,
                Ui18n.CONSTANTS.sheetChangeStateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_DELETE,
                Ui18n.CONSTANTS.sheetDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_EXPORT,
                Ui18n.CONSTANTS.sheetExportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_INITIALIZE,
                Ui18n.CONSTANTS.sheetInitializeButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_DUPLICATE,
                Ui18n.CONSTANTS.sheetDuplicateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_MAIL,
                Ui18n.CONSTANTS.sheetMailButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_LINK,
                Ui18n.CONSTANTS.sheetLinkButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEET_UNLINK,
                Ui18n.CONSTANTS.sheetUnlinkButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_SHEET_CREATE,
                Ui18n.CONSTANTS.sheetFilterCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_SHEET_REFRESH,
                Ui18n.CONSTANTS.sheetFilterRefreshButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_SHEET_EDIT,
                Ui18n.CONSTANTS.sheetFilterEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_SHEET_DELETE,
                Ui18n.CONSTANTS.sheetFilterDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_SHEET_EXPORT,
                Ui18n.CONSTANTS.sheetFilterExportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEETS_DELETE,
                Ui18n.CONSTANTS.sheetsDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEETS_EXPORT,
                Ui18n.CONSTANTS.sheetsExportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEETS_MAIL,
                Ui18n.CONSTANTS.sheetsMailButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_CREATION,
                Ui18n.CONSTANTS.productCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_SAVE,
                Ui18n.CONSTANTS.productSaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_DISPLAY,
                Ui18n.CONSTANTS.productDisplayButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_EDIT,
                Ui18n.CONSTANTS.productEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_REFRESH,
                Ui18n.CONSTANTS.productRefreshButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_DELETE,
                Ui18n.CONSTANTS.productDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_EXPORT,
                Ui18n.CONSTANTS.productExportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_LINK,
                Ui18n.CONSTANTS.productLinkButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_UNLINK,
                Ui18n.CONSTANTS.productUnlinkButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_ATTRIBUTES_DISPLAY,
                Ui18n.CONSTANTS.productAttributesDisplayButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCT_ATTRIBUTES_EDIT,
                Ui18n.CONSTANTS.productAttributesEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_PRODUCT_CREATE,
                Ui18n.CONSTANTS.productFilterCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_PRODUCT_REFRESH,
                Ui18n.CONSTANTS.productFilterRefreshButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_PRODUCT_EDIT,
                Ui18n.CONSTANTS.productFilterEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.FILTER_PRODUCT_DELETE,
                Ui18n.CONSTANTS.productFilterDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCTS_DELETE,
                Ui18n.CONSTANTS.productsDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCTS_EXPORT,
                Ui18n.CONSTANTS.productsExportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.PRODUCTS_IMPORT,
                Ui18n.CONSTANTS.productsImportButton());
        ACTIONS_TRANSLATIONS.put(ActionName.USER_CREATION,
                Ui18n.CONSTANTS.userCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.USER_SAVE,
                Ui18n.CONSTANTS.userSaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.USER_AFFECTATION,
                Ui18n.CONSTANTS.userAffectationButton());
        ACTIONS_TRANSLATIONS.put(ActionName.USER_DELETE,
                Ui18n.CONSTANTS.userDeleteButton());
        ACTIONS_TRANSLATIONS.put(ActionName.DICTIONARY_EDITION,
                Ui18n.CONSTANTS.userDictionaryEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.ENVIRONMENT_EDITION,
                Ui18n.CONSTANTS.userEnvironmentEditButton());
        ACTIONS_TRANSLATIONS.put(ActionName.ENVIRONMENT_CREATION,
                Ui18n.CONSTANTS.userEnvironmentCreateButton());
        ACTIONS_TRANSLATIONS.put(ActionName.ENVIRONMENT_SAVE,
                Ui18n.CONSTANTS.userEnvironmentSaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.DICTIONARY_CATEGORY_SAVE,
                Ui18n.CONSTANTS.userDictionaryCategorySaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.DICTIONARY_CATEGORY_SORT,
                Ui18n.CONSTANTS.userDictionaryCategorySortButton());
        ACTIONS_TRANSLATIONS.put(ActionName.ENVIRONMENT_CATEGORY_SAVE,
                Ui18n.CONSTANTS.userEnvironmentCategorySaveButton());
        ACTIONS_TRANSLATIONS.put(ActionName.SHEETS_CLOSE,
                Ui18n.CONSTANTS.sheetsCloseButton());
    }

    protected GpmToolBar toolBar;

    private final Map<String, Command> standardCommands;

    private final Map<String, AbstractDynamicCommandBuilder> dynamicCommandBuilders;

    /**
     * Create a menu builder.
     */
    public AbstractMenuBuilder() {
        toolBar = new GpmToolBar();
        standardCommands = new HashMap<String, Command>();
        dynamicCommandBuilders =
                new HashMap<String, AbstractDynamicCommandBuilder>();
    }

    /**
     * Register a standard command.
     * 
     * @param pActionName
     *            The action name.
     * @param pCommand
     *            The command.
     */
    protected void registerStandardCommand(final String pActionName,
            final Command pCommand) {
        standardCommands.put(pActionName, pCommand);
    }

    /**
     * Register a dynamic command.
     * 
     * @param pActionName
     *            The action name.
     * @param pCommandBuilder
     *            The command builder.
     */
    protected void registerDynamicCommandBuilder(final String pActionName,
            final AbstractDynamicCommandBuilder pCommandBuilder) {
        dynamicCommandBuilders.put(pActionName, pCommandBuilder);
    }

    /**
     * Reset the tool bar.
     */
    protected void resetToolBar() {
        toolBar = new GpmToolBar();
    }

    /**
     * Create a tool bar for a list of action.
     * 
     * @param pActions
     *            The actions.
     */
    protected void addToolBar(final List<UiAction> pActions) {
        toolBar.addSeparatedToolBar();
        for (final UiAction lAction : pActions) {
            // Ignore null action
            if (lAction != null) {
                // Convert action to button
                final String lActionName = lAction.getName();

                switch (lAction.getType()) {
                    case WITH_SUB_MENU:
                        final UiActionWithSubMenu lSubMenuAction =
                                (UiActionWithSubMenu) lAction;

                        if (lSubMenuAction.getActions() != null
                                && !lSubMenuAction.getActions().isEmpty()) {
                            toolBar.addSubMenu(getImage(lActionName),
                                    getActionName(lAction), buildSubMenu(
                                            lSubMenuAction, lActionName));
                        }
                        break;
                    default:
                        toolBar.addEntry(getImage(lActionName),
                                getActionName(lAction), getCommand(EMPTY,
                                        lAction));
                }
            }
        }
    }

    private GpmRightAlignMenuBar buildSubMenu(
            final UiActionWithSubMenu pSubMenu, final String pHierarchyName) {
        final GpmRightAlignMenuBar lMenu = new GpmRightAlignMenuBar();

        for (final UiAction lSubAction : pSubMenu.getActions()) {
            // Convert action to sub menu
            final String lSubActionName = lSubAction.getName();

            switch (lSubAction.getType()) {
                case WITH_SUB_MENU:
                    final UiActionWithSubMenu lSubMenuAction =
                            (UiActionWithSubMenu) lSubAction;

                    if (lSubMenuAction.getActions() != null
                            && !lSubMenuAction.getActions().isEmpty()) {
                        lMenu.addSubMenu(getActionName(lSubAction),
                                buildSubMenu((UiActionWithSubMenu) lSubAction,
                                        pHierarchyName + lSubActionName));
                    }
                    break;
                default:
                    lMenu.addEntry(getActionName(lSubAction), getCommand(
                            pHierarchyName, lSubAction));
            }
        }

        return lMenu;
    }

    private Command getCommand(final String pHierarchyName,
            final UiAction pAction) {
        switch (pAction.getType()) {
            case STANDARD:
                return standardCommands.get(pHierarchyName + pAction.getName());
            case DYNAMIC:
                return dynamicCommandBuilders.get(pHierarchyName).generateCommand(
                        pAction);
            case EXTENDED_ACTION:
                final ExecuteExtendedActionCommand lExtendedCommand =
                        Application.INJECTOR.getExecuteExtendedActionCommand();
                final UiActionExtended lExtendedAction =
                        (UiActionExtended) pAction;

                lExtendedCommand.setActionName(lExtendedAction.getExtendedActionName());
                lExtendedCommand.setExtensionContainerId(lExtendedAction.getExtensionContainerId());
                lExtendedCommand.setType(getExtendedActionType());
                lExtendedCommand.setConfirmationMessage(lExtendedAction.getConfirmationMessage());                    

                return lExtendedCommand;
            default:
                // No action
                return null;
        }
    }

    /**
     * Get the extended action type.
     * 
     * @return The extended action type.
     */
    protected abstract ExtendedActionType getExtendedActionType();

    private ImageResource getImage(final String pMenuName) {
        final ImageResource lImage = IMAGES.get(pMenuName);

        if (lImage == null) {
            return DEFAULT_IMAGE;
        }
        else {
            return lImage;
        }
    }

    /**
     * Replace Standard actions with Extended actions when their labels
     * ("names") are the same. When corresponding, extended actions will replace
     * the standard actions in the standard actions list, and will be removed
     * from extended actions list.
     * 
     * @param pActions
     *            standard actions
     * @param pExtendedActions
     *            extended actions
     */
    protected final void mergeExtendedActionsWithActions(
            final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions) {
        if (pExtendedActions != null) {
            Iterator<UiAction> lIte = pExtendedActions.iterator();
            while (lIte.hasNext()) {
                UiAction lA = lIte.next();
                if (pActions.containsKey(lA.getName())) {
                    pActions.put(lA.getName(), lA);
                    lIte.remove();
                }
            }
        }
    }

    /**
     * Build the menu.
     * 
     * @param pActions
     *            The actions. Can be null.
     * @param pExtendedActions
     *            The extended actions. Can be null.
     * @return The menu.
     */
    abstract public GpmToolBar buildMenu(final Map<String, UiAction> pActions,
            final List<UiAction> pExtendedActions);

    private String getActionName(UiAction pAction) {
        if (pAction.getTranslatedName() != null) {
            return pAction.getTranslatedName();
        }
        else {
            final String lStaticTranslation =
                    ACTIONS_TRANSLATIONS.get(pAction.getName());

            if (lStaticTranslation == null) {
                return pAction.getName();
            }
            else {
                return lStaticTranslation;
            }
        }
    }
}