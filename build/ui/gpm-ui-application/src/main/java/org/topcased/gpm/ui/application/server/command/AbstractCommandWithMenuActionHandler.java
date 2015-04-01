/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Nicolas VEILLET (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.server.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.customware.gwt.dispatch.shared.Action;
import net.customware.gwt.dispatch.shared.Result;

import org.apache.commons.lang.StringUtils;
import org.topcased.gpm.business.util.DisplayMode;
import org.topcased.gpm.business.util.FieldsContainerType;
import org.topcased.gpm.business.util.action.ActionName;
import org.topcased.gpm.business.util.action.AdministrationAction;
import org.topcased.gpm.ui.facade.server.FacadeLocator;
import org.topcased.gpm.ui.facade.server.authorization.UiSession;
import org.topcased.gpm.ui.facade.server.i18n.I18nTranslationManager;
import org.topcased.gpm.ui.facade.shared.action.ActionType;
import org.topcased.gpm.ui.facade.shared.action.UiAction;
import org.topcased.gpm.ui.facade.shared.action.UiActionWithSubMenu;
import org.topcased.gpm.ui.facade.shared.container.UiContainer;
import org.topcased.gpm.ui.facade.shared.container.product.UiProduct;
import org.topcased.gpm.ui.facade.shared.container.sheet.UiSheet;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;

import com.google.inject.Provider;

/**
 * AbstractCommandWithMenuActionHandler
 * 
 * @param <A>
 *            The {@link Action} implementation.
 * @param <R>
 *            The {@link Result} implementation.
 * @author nveillet
 */
public abstract class AbstractCommandWithMenuActionHandler<A extends Action<R>, R extends Result>
        extends AbstractCommandActionHandler<A, R> {

    /**
     * Action handler constructor that initialize the httpSession object.
     * 
     * @param pHttpSession
     *            Http session
     */
    public AbstractCommandWithMenuActionHandler(
            Provider<HttpSession> pHttpSession) {
        super(pHttpSession);
    }

    /**
     * Apply action access to a actions list
     * 
     * @param pSession
     *            the session
     * @param pActions
     *            the action list
     * @param pParentMenuName
     *            the parent menu name
     * @param pContainerTypes
     *            the current container types (filter view)
     * @param pContainer
     *            the current container (container view)
     * @param pTranslationManager
     *            the translation manager
     */
    private void applyActionAccess(final UiSession pSession,
            final List<UiAction> pActions, final String pParentMenuName,
            final List<UiFilterContainerType> pContainerTypes,
            final UiContainer pContainer,
            final I18nTranslationManager pTranslationManager) {

        for (int i = pActions.size() - 1; i >= 0; i--) {
            String lActionName = pParentMenuName + pActions.get(i).getName();

            if (!getFacadeLocator().getAuthorizationFacade().getActionAccess(
                    pSession, lActionName, pContainerTypes, pContainer)) {
                pActions.remove(i);
            }
            else {
                // Translate action name
                String lTranslatedName =
                        pTranslationManager.getTextTranslation(pActions.get(i).getName());
                if (!pActions.get(i).getName().equals(lTranslatedName)) {
                    pActions.get(i).setTranslatedName(lTranslatedName);
                }

                if (pActions.get(i) instanceof UiActionWithSubMenu) {
                    UiActionWithSubMenu lActionMenu =
                            (UiActionWithSubMenu) pActions.get(i);
                    applyActionAccess(pSession, lActionMenu.getActions(),
                            lActionName + ".", pContainerTypes, pContainer,
                            pTranslationManager);
                }
            }
        }
    }

    /**
     * Get a action by its name from an action list
     * 
     * @param pActions
     *            the action list
     * @param pActionName
     *            the action name
     * @return the action
     */
    private UiAction getAction(final List<UiAction> pActions,
            final String pActionName) {
        for (UiAction lAction : pActions) {
            if (lAction.getName().equals(pActionName)) {
                return lAction;
            }
        }
        return null;
    }

    /**
     * Get standard actions for the filter results
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter
     * @return the actions
     */
    protected Map<String, UiAction> getFilterActions(UiSession pSession,
            UiFilter pFilter) {
        switch (pFilter.getFilterType()) {
            case PRODUCT:
                return getFilterProductActions(pSession, pFilter);
            default:
                return getFilterSheetActions(pFilter);
        }
    }

    /**
     * Get standard actions for the filter product results
     * 
     * @param pSession
     *            the session
     * @param pFilter
     *            the filter
     * @return the actions
     */
    private Map<String, UiAction> getFilterProductActions(UiSession pSession,
            UiFilter pFilter) {
        final Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        List<UiAction> lSubActions;

        // Refresh filter
        lActions.put(ActionName.FILTER_PRODUCT_REFRESH, new UiAction(
                ActionName.FILTER_PRODUCT_REFRESH));

        // Edit filter
        UiAction lEditAction = null;
        if (pFilter.isEditable()
                && getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                        pSession, AdministrationAction.PRODUCT_SEARCH_NEW_EDIT)) {
            lEditAction = new UiAction(ActionName.FILTER_PRODUCT_EDIT);
        }
        lActions.put(ActionName.FILTER_PRODUCT_EDIT, lEditAction);

        // Delete products
        UiAction lAction = null;
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.PRODUCT_DELETE)) {
            lAction = new UiAction(ActionName.PRODUCTS_DELETE);
        }
        lActions.put(ActionName.PRODUCTS_DELETE, lAction);

        // Export products
        lAction = null;
        if (getFacadeLocator().getAuthorizationFacade().hasAnyAdminAccess(
                pSession, AdministrationAction.PRODUCT_EXPORT)) {
            lSubActions = new ArrayList<UiAction>();
            lSubActions.add(new UiAction(ActionName.EXPORT_XML));
            lAction =
                    new UiActionWithSubMenu(ActionName.PRODUCTS_EXPORT,
                            lSubActions);
        }
        lActions.put(ActionName.PRODUCTS_EXPORT, lAction);

        return lActions;
    }

    /**
     * Get standard actions for the filter sheet results
     * 
     * @param pFilter
     *            the filter
     * @return the actions
     */
    private Map<String, UiAction> getFilterSheetActions(UiFilter pFilter) {
        final Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        List<UiAction> lSubActions;

        // Refresh filter
        lActions.put(ActionName.FILTER_SHEET_REFRESH, new UiAction(
                ActionName.FILTER_SHEET_REFRESH));

        // Edit filter
        UiAction lEditAction = null;
        if (pFilter.isEditable()) {
            lEditAction = new UiAction(ActionName.FILTER_SHEET_EDIT);
        }
        lActions.put(ActionName.FILTER_SHEET_EDIT, lEditAction);

        // Export filter
        lSubActions = new ArrayList<UiAction>();
        lSubActions.add(new UiAction(ActionName.EXPORT_PDF));
        lSubActions.add(new UiAction(ActionName.EXPORT_XML));
        lSubActions.add(new UiAction(ActionName.EXPORT_XLS));
        lSubActions.add(new UiAction(ActionName.EXPORT_CSV));
        lActions.put(ActionName.FILTER_SHEET_EXPORT, new UiActionWithSubMenu(
                ActionName.FILTER_SHEET_EXPORT, lSubActions));

        // Delete sheets
        lActions.put(ActionName.SHEETS_DELETE, new UiAction(
                ActionName.SHEETS_DELETE));

        // Export sheets
        lSubActions = new ArrayList<UiAction>();
        lSubActions.add(new UiAction(ActionName.EXPORT_PDF));
        lSubActions.add(new UiAction(ActionName.EXPORT_XML));
        lSubActions.add(new UiAction(ActionName.EXPORT_XLS));
        lActions.put(ActionName.SHEETS_EXPORT, new UiActionWithSubMenu(
                ActionName.SHEETS_EXPORT, lSubActions));

        // Mail sheets
        lActions.put(ActionName.SHEETS_MAIL, new UiAction(
                ActionName.SHEETS_MAIL));

        return lActions;
    }

    /**
     * Get standard actions for the current product
     * 
     * @param pSession
     *            the session
     * @param pProduct
     *            the current product
     * @param pDisplayMode
     *            the display mode
     * @return the actions
     */
    protected Map<String, UiAction> getProductActions(UiSession pSession,
            UiProduct pProduct, DisplayMode pDisplayMode) {
        final Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        List<UiAction> lSubActions;

        // VISUALIZATION actions
        if (DisplayMode.VISUALIZATION.equals(pDisplayMode)) {
            // Refresh product
            lActions.put(ActionName.PRODUCT_REFRESH, new UiAction(
                    ActionName.PRODUCT_REFRESH));

            // Open in edition
            UiAction lEditAction = null;
            if (pProduct.isUpdatable()
                    && (getFacadeLocator().getAuthorizationFacade().hasSpecifiedAdminAccess(
                    		pProduct.getName(), pSession, AdministrationAction.PRODUCT_UPDATE))) {
                lEditAction = new UiAction(ActionName.PRODUCT_EDIT);
            }
            lActions.put(ActionName.PRODUCT_EDIT, lEditAction);

            // Display product attribute
            if (getFacadeLocator().getAttributeFacade().getAttributes(pSession,
                    pProduct.getId()).size() > 0) {
                lActions.put(ActionName.PRODUCT_ATTRIBUTES_DISPLAY,
                        new UiAction(ActionName.PRODUCT_ATTRIBUTES_DISPLAY));
            }
        }

        // EDITION actions
        if (DisplayMode.EDITION.equals(pDisplayMode)) {
            // Open in visualization
            UiAction lViewAction = null;
            if (getFacadeLocator().getAuthorizationFacade().hasCurrentAdminAccess(
                    pSession, AdministrationAction.PRODUCT_VIEW)) {
                lViewAction = new UiAction(ActionName.PRODUCT_DISPLAY);
            }
            lActions.put(ActionName.PRODUCT_DISPLAY, lViewAction);

            // Add link
            lSubActions =
                    getCreatableFieldsContainers(pSession,
                            FieldsContainerType.LINK, pProduct.getTypeId());
            lActions.put(ActionName.PRODUCT_LINK, new UiActionWithSubMenu(
                    ActionName.PRODUCT_LINK, lSubActions));

            // Remove link
            lSubActions = new ArrayList<UiAction>();
            for (String lLinkType : getFacadeLocator().getLinkFacade().getDeletableLinkTypes(
                    pSession, pProduct.getTypeId())) {
                lSubActions.add(new UiAction(lLinkType, ActionType.DYNAMIC));
            }
            lActions.put(ActionName.PRODUCT_UNLINK, new UiActionWithSubMenu(
                    ActionName.PRODUCT_UNLINK, lSubActions));

            // Edit product attributes
            if (getFacadeLocator().getAuthorizationFacade().hasGlobalAdminAccess(pSession)) {
            	lActions.put(ActionName.PRODUCT_ATTRIBUTES_EDIT, new UiAction(ActionName.PRODUCT_ATTRIBUTES_EDIT));
            }
        }

        // CREATION and EDITION commons actions
        if (DisplayMode.CREATION.equals(pDisplayMode)
                || DisplayMode.EDITION.equals(pDisplayMode)) {
            // Save product
            lActions.put(ActionName.PRODUCT_SAVE, new UiAction(
                    ActionName.PRODUCT_SAVE));
        }

        // VISUALIZATION and EDITION commons actions
        if (DisplayMode.VISUALIZATION.equals(pDisplayMode)
                || DisplayMode.EDITION.equals(pDisplayMode)) {
            // Delete product
            UiAction lDeleteAction = null;
            if (pProduct.isDeletable()
                    && getFacadeLocator().getAuthorizationFacade().hasSpecifiedAdminAccess(
                    		pProduct.getName(), pSession, AdministrationAction.PRODUCT_DELETE)) {
                lDeleteAction = new UiAction(ActionName.PRODUCT_DELETE);
            }
            lActions.put(ActionName.PRODUCT_DELETE, lDeleteAction);

            // Export product
            UiAction lExportAction = null;
            if (getFacadeLocator().getAuthorizationFacade().hasSpecifiedAdminAccess(
            		pProduct.getName(), pSession, AdministrationAction.PRODUCT_EXPORT)) {
                lSubActions = new ArrayList<UiAction>();
                lSubActions.add(new UiAction(ActionName.EXPORT_XML));
                lExportAction =
                        new UiActionWithSubMenu(ActionName.PRODUCT_EXPORT,
                                lSubActions);
            }
            lActions.put(ActionName.PRODUCT_EXPORT, lExportAction);
        }

        return lActions;
    }

    /**
     * Get standard actions for the current sheet
     * 
     * @param pSession
     *            the session
     * @param pSheet
     *            the current sheet
     * @param pDisplayMode
     *            the display mode
     * @return the actions
     */
    protected Map<String, UiAction> getSheetActions(UiSession pSession,
            UiSheet pSheet, DisplayMode pDisplayMode) {
        final Map<String, UiAction> lActions = new HashMap<String, UiAction>();
        List<UiAction> lSubActions;

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        // CREATION actions
        if (DisplayMode.CREATION.equals(pDisplayMode)) {
            // Initialize sheet
            lSubActions = new ArrayList<UiAction>();
            for (String lSheetType : getFacadeLocator().getSheetFacade().getInitializableSheetTypes(
                    pSession, pSheet.getTypeId())) {
                lSubActions.add(new UiAction(lSheetType, ActionType.DYNAMIC));
            }
            lActions.put(ActionName.SHEET_INITIALIZE, new UiActionWithSubMenu(
                    ActionName.SHEET_INITIALIZE, lSubActions));
        }

        // VISUALIZATION actions
        if (DisplayMode.VISUALIZATION.equals(pDisplayMode)) {
            // Refresh sheet
            lActions.put(ActionName.SHEET_REFRESH, new UiAction(
                    ActionName.SHEET_REFRESH));

            // Open in edition
            UiAction lEditAction = null;
            if (pSheet.isUpdatable()) {
                lEditAction = new UiAction(ActionName.SHEET_EDIT);
            }
            lActions.put(ActionName.SHEET_EDIT, lEditAction);
        }

        // EDITION actions
        if (DisplayMode.EDITION.equals(pDisplayMode)) {

            // Refresh sheet
            lActions.put(ActionName.SHEET_REFRESH, new UiAction(
                    ActionName.SHEET_REFRESH));

            // Open in visualization
            lActions.put(ActionName.SHEET_DISPLAY, new UiAction(
                    ActionName.SHEET_DISPLAY));

            // Transitions
            lSubActions = new ArrayList<UiAction>();
            List<String> lAvailableTransitions =
                    getFacadeLocator().getSheetFacade().getAvailableTransitions(
                            pSession, pSheet);
            Map<String, String> lTransitionConfirmationMessages =
                    getFacadeLocator().getSheetFacade().getTransitionConfirmationMessages(
                            pSession, pSheet);
            for (String lTransitionsName : lAvailableTransitions) {
                String lConfirmationMessageKey =
                        lTransitionConfirmationMessages.get(lTransitionsName);
                lSubActions.add(new UiAction(
                        lTransitionsName,
                        ActionType.DYNAMIC,
                        lTranslationManager.getTextTranslation(lConfirmationMessageKey)));
            }
            lActions.put(ActionName.SHEET_CHANGE_STATE,
                    new UiActionWithSubMenu(ActionName.SHEET_CHANGE_STATE,
                            lSubActions));

            // Add link
            lSubActions =
                    getCreatableFieldsContainers(pSession,
                            FieldsContainerType.LINK, pSheet.getTypeId());
            lActions.put(ActionName.SHEET_LINK, new UiActionWithSubMenu(
                    ActionName.SHEET_LINK, lSubActions));

            // Remove link
            lSubActions = new ArrayList<UiAction>();
            for (String lLinkType : getFacadeLocator().getLinkFacade().getDeletableLinkTypes(
                    pSession, pSheet.getTypeId())) {
                lSubActions.add(new UiAction(lLinkType, ActionType.DYNAMIC));
            }
            lActions.put(ActionName.SHEET_UNLINK, new UiActionWithSubMenu(
                    ActionName.SHEET_UNLINK, lSubActions));
        }

        // CREATION and EDITION commons actions
        if (DisplayMode.CREATION.equals(pDisplayMode)
                || DisplayMode.EDITION.equals(pDisplayMode)) {
            // Save sheet
            lActions.put(ActionName.SHEET_SAVE, new UiAction(
                    ActionName.SHEET_SAVE));
        }

        // VISUALIZATION and EDITION commons actions
        if (DisplayMode.VISUALIZATION.equals(pDisplayMode)
                || DisplayMode.EDITION.equals(pDisplayMode)) {

            // Delete sheet
            UiAction lDeleteAction = null;
            if (pSheet.isDeletable()) {
                lDeleteAction = new UiAction(ActionName.SHEET_DELETE);
            }
            lActions.put(ActionName.SHEET_DELETE, lDeleteAction);

            // Duplicate sheet
            UiAction lDuplicationAction = null;
            if (pSheet.isDuplicable()) {
                lDuplicationAction = new UiAction(ActionName.SHEET_DUPLICATE);
            }
            lActions.put(ActionName.SHEET_DUPLICATE, lDuplicationAction);

            // Export sheet
            lSubActions = new ArrayList<UiAction>();
            lSubActions.add(new UiAction(ActionName.EXPORT_PDF));
            lSubActions.add(new UiAction(ActionName.EXPORT_XML));
            lSubActions.add(new UiAction(ActionName.EXPORT_XLS));
            lActions.put(ActionName.SHEET_EXPORT, new UiActionWithSubMenu(
                    ActionName.SHEET_EXPORT, lSubActions));

            // Send mail
            lActions.put(ActionName.SHEET_MAIL, new UiAction(
                    ActionName.SHEET_MAIL));
        }

        // LOCK
        if (pSheet.getLockUserLogin() != null) {
            UiAction lLockAction = new UiAction(ActionName.SHEET_LOCKED);
            lLockAction.setTranslatedName(pSheet.getLockUserLogin());
            lActions.put(ActionName.SHEET_LOCKED, lLockAction);
        }

        return lActions;
    }

    /**
     * merge two actions
     * 
     * @param pStandardAction
     *            the standard action
     * @param pExtendedAction
     *            the extended action
     */
    private void mergeAction(UiAction pStandardAction, UiAction pExtendedAction) {
        if (pStandardAction instanceof UiActionWithSubMenu
                && pExtendedAction instanceof UiActionWithSubMenu) {
            UiActionWithSubMenu lStandardAction =
                    (UiActionWithSubMenu) pStandardAction;
            UiActionWithSubMenu lExtendedAction =
                    (UiActionWithSubMenu) pExtendedAction;
            for (UiAction lSubExtendedAction : lExtendedAction.getActions()) {
                UiAction lSubStandardAction =
                        getAction(lStandardAction.getActions(),
                                lSubExtendedAction.getName());
                if (lSubStandardAction == null) {
                    lStandardAction.getActions().add(lSubExtendedAction);
                }
                else {
                    mergeAction(lSubStandardAction, lSubExtendedAction);
                }
            }
        }
    }

    /**
     * Merge standard actions and extended action and apply action access
     * 
     * @param pSession
     *            the session
     * @param pStandardActions
     *            the standard actions
     * @param pExtendedActions
     *            the extended actions
     * @param pContainerTypes
     *            the current container types (filter view)
     * @param pContainer
     *            the current container (container view)
     */
    protected void mergeActions(final UiSession pSession,
            final Map<String, UiAction> pStandardActions,
            final List<UiAction> pExtendedActions,
            final List<UiFilterContainerType> pContainerTypes,
            final UiContainer pContainer) {

        // Merge actions
        LinkedList<Integer> lExtendedActionToRemove = new LinkedList<Integer>();
        for (int i = 0; i < pExtendedActions.size(); i++) {
            if (pStandardActions.containsKey(pExtendedActions.get(i).getName())) {
                mergeAction(
                        pStandardActions.get(pExtendedActions.get(i).getName()),
                        pExtendedActions.get(i));
                lExtendedActionToRemove.addFirst(i);
            }
        }

        // Remove transfered extended actions
        for (Integer lIndex : lExtendedActionToRemove) {
            pExtendedActions.remove(lIndex);
        }

        // Clean Standard actions
        List<String> lMenuKeys =
                new ArrayList<String>(pStandardActions.keySet());
        for (String lMenuKey : lMenuKeys) {
            UiAction lMenuValue = pStandardActions.get(lMenuKey);
            if (lMenuValue == null
                    || (lMenuValue instanceof UiActionWithSubMenu && ((UiActionWithSubMenu) lMenuValue).getActions().isEmpty())) {
                pStandardActions.remove(lMenuKey);
            }
        }

        I18nTranslationManager lTranslationManager =
                FacadeLocator.instance().getI18nFacade().getTranslationManager(
                        pSession.getParent().getLanguage());

        // Apply actions access to Standard actions
        lMenuKeys = new ArrayList<String>(pStandardActions.keySet());
        for (String lMenuKey : lMenuKeys) {
            UiAction lAction = pStandardActions.get(lMenuKey);
            if (!getFacadeLocator().getAuthorizationFacade().getActionAccess(
                    pSession, lAction.getName(), pContainerTypes, pContainer)) {
                pStandardActions.remove(lMenuKey);
            }
            else {
                // Translate action name
                String lActionName = lAction.getName();
                String lTranslatedName =
                        lTranslationManager.getTextTranslation(lActionName);
                if (!lActionName.equals(lTranslatedName)) {
                    lAction.setTranslatedName(lTranslatedName);
                }

                if (lAction instanceof UiActionWithSubMenu) {
                    UiActionWithSubMenu lActionMenu =
                            (UiActionWithSubMenu) lAction;
                    applyActionAccess(pSession, lActionMenu.getActions(),
                            lActionMenu.getName() + ".", pContainerTypes,
                            pContainer, lTranslationManager);
                }
            }
        }

        // Apply actions access  to Extended actions
        applyActionAccess(pSession, pExtendedActions, StringUtils.EMPTY,
                pContainerTypes, pContainer, lTranslationManager);
    }
}
