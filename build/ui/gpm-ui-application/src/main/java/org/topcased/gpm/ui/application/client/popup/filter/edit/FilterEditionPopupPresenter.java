/***************************************************************
 * Copyright (c) 2010 AIRBUS FRANCE. All rights reserved. This 
 * program and the accompanying materials are made available
 * under the terms of the Lesser Gnu Public License (LGPL) which
 * accompanies this distribution, and is available
 * at http://www.gnu.org/licenses/lgpl.html
 *
 * Contributors: Julien LOUISY (Atos Origin)
 ******************************************************************/
package org.topcased.gpm.ui.application.client.popup.filter.edit;

import static org.topcased.gpm.ui.component.client.resources.i18n.Ui18n.CONSTANTS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.customware.gwt.presenter.client.EventBus;

import org.topcased.gpm.business.util.FieldType;
import org.topcased.gpm.business.util.FilterType;
import org.topcased.gpm.business.util.Translation;
import org.topcased.gpm.business.values.field.BusinessField;
import org.topcased.gpm.business.values.field.multivalued.BusinessMultivaluedField;
import org.topcased.gpm.business.values.field.simple.BusinessSimpleField;
import org.topcased.gpm.business.values.filter.FilterFieldValue;
import org.topcased.gpm.ui.application.client.Application;
import org.topcased.gpm.ui.application.client.command.popup.ClosePopupAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AbstractFilterEditionExecuteCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.AddCriterionAction;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionPopupNextCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionPopupPreviousCommand;
import org.topcased.gpm.ui.application.client.command.popup.filter.edit.FilterEditionPreSaveCommand;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionContainerManager;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldItem;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldManager;
import org.topcased.gpm.ui.application.client.common.tree.TreeFilterEditionUsableFieldManager.TreeFilterEditionMode;
import org.topcased.gpm.ui.application.client.event.ActionEventHandler;
import org.topcased.gpm.ui.application.client.event.GlobalEvent;
import org.topcased.gpm.ui.application.client.popup.PopupPresenter;
import org.topcased.gpm.ui.application.client.util.validation.IntegerRule;
import org.topcased.gpm.ui.application.client.util.validation.PositiveIntegerRule;
import org.topcased.gpm.ui.application.client.util.validation.RealRule;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetCategoryValuesAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetCategoryValuesResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetUsableFieldsAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.GetUsableFieldsResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.PreSaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SaveFilterResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectContainerResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectCriteriaFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectResultFieldResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectScopeResult;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectSortingFieldAction;
import org.topcased.gpm.ui.application.shared.command.filter.edit.SelectSortingFieldResult;
import org.topcased.gpm.ui.component.client.container.field.AbstractGpmField;
import org.topcased.gpm.ui.component.client.container.field.GpmCheckBox;
import org.topcased.gpm.ui.component.client.container.field.GpmChoiceBoxValue;
import org.topcased.gpm.ui.component.client.container.field.GpmDateBox;
import org.topcased.gpm.ui.component.client.container.field.GpmDropDownListBox;
import org.topcased.gpm.ui.component.client.container.field.GpmGenericField;
import org.topcased.gpm.ui.component.client.container.field.GpmLabelledTextBox;
import org.topcased.gpm.ui.component.client.container.field.GpmTextBox;
import org.topcased.gpm.ui.component.client.field.formater.GpmDoubleFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmIntegerFormatter;
import org.topcased.gpm.ui.component.client.field.formater.GpmStringFormatter;
import org.topcased.gpm.ui.component.client.field.multivalued.GpmMultipleMultivaluedElement;
import org.topcased.gpm.ui.component.client.resources.i18n.Ui18n;
import org.topcased.gpm.ui.component.client.util.validation.IRule;
import org.topcased.gpm.ui.component.client.util.validation.Validator;
import org.topcased.gpm.ui.facade.shared.filter.UiFilter;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerHierarchy;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterContainerType;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterScope;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterUsage;
import org.topcased.gpm.ui.facade.shared.filter.UiFilterVisibility;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldName;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterFieldNameType;
import org.topcased.gpm.ui.facade.shared.filter.field.UiFilterUsableField;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriteriaGroup;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterCriterion;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterOperator;
import org.topcased.gpm.ui.facade.shared.filter.field.criteria.UiFilterPeriod;
import org.topcased.gpm.ui.facade.shared.filter.field.result.UiFilterResultField;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSorting;
import org.topcased.gpm.ui.facade.shared.filter.field.sort.UiFilterSortingField;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.inject.Inject;

/**
 * The presenter for the FilterEditionView.
 * 
 * @author jlouisy
 */
public class FilterEditionPopupPresenter extends
        PopupPresenter<FilterEditionPopupDisplay> {

    private enum FilterEditionMode {
        CREATION_MODE, EDITION_MODE
    };

    private String productName;

    private FilterEditionStep currentStep;

    private FilterEditionPopupNextCommand filterEditionPopupNextCommand;

    private FilterEditionPopupPreviousCommand filterEditionPopupPreviousCommand;

    private FilterEditionPreSaveCommand filterEditionPreSaveCommand;

    private TreeFilterEditionUsableFieldManager usableFieldTreeManager;

    private TreeFilterEditionContainerManager containerTreeManager;

    private UiFilterContainerType[] selectedContainers;

    private boolean isSelectContainerPopupLoaded;

    private boolean isSelectScopePopupLoaded;

    private boolean isSelectResultFieldsPopupLoaded;

    private boolean isSelectCriteriasPopupLoaded;

    private boolean isSelectSortingFieldsPopupLoaded;

    private String filterId;

    private FilterType filterType;

    private FilterEditionMode mode;

    private Map<String, LinkedList<UiFilterFieldName>> fieldsHierarchy;

    private Map<String, String> fieldsCategoryName;

    private Map<String, FieldType> fieldTypes;

    private FilterEditionSavePopupPresenter filterEditionSavePopup;

    private FilterEditionSelectCriteriaGroupPopupPresenter filterEditionSelectCriteriaGroupPopup;

    private boolean containerListHasChanged;

    private Map<String, List<String>> categoryValues;

    private boolean templateFilter;

    //field validator
    private Validator validator;

    private boolean resetNeeded = false;

    private final static Map<UiFilterOperator, String> OPERATOR_TRANSLATIONS =
            new HashMap<UiFilterOperator, String>();

    private final static Map<UiFilterSorting, String> ORDER_TRANSLATIONS =
            new HashMap<UiFilterSorting, String>();

    static {
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.EQUAL,
                Ui18n.CONSTANTS.filterEditionOperatorEqual());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.NOT_EQUAL,
                Ui18n.CONSTANTS.filterEditionOperatorNotEqual());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.GREATER,
                Ui18n.CONSTANTS.filterEditionOperatorGreater());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.GREATER_OR_EQUALS,
                Ui18n.CONSTANTS.filterEditionOperatorGreaterOrEquals());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.LESS,
                Ui18n.CONSTANTS.filterEditionOperatorLess());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.LESS_OR_EQUALS,
                Ui18n.CONSTANTS.filterEditionOperatorLessOrEquals());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.LIKE,
                Ui18n.CONSTANTS.filterEditionOperatorLike());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.NOT_LIKE,
                Ui18n.CONSTANTS.filterEditionOperatorNotLike());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.SINCE,
                Ui18n.CONSTANTS.filterEditionOperatorSince());
        OPERATOR_TRANSLATIONS.put(UiFilterOperator.OTHER,
                Ui18n.CONSTANTS.filterEditionOperatorOther());

        ORDER_TRANSLATIONS.put(UiFilterSorting.ASCENDANT,
                Ui18n.CONSTANTS.filterEditionOrderAscendant());
        ORDER_TRANSLATIONS.put(UiFilterSorting.ASCENDANT_DEFINED,
                Ui18n.CONSTANTS.filterEditionOrderAscendantDefined());
        ORDER_TRANSLATIONS.put(UiFilterSorting.DESCENDANT,
                Ui18n.CONSTANTS.filterEditionOrderDescendant());
        ORDER_TRANSLATIONS.put(UiFilterSorting.DESCENDANT_DEFINED,
                Ui18n.CONSTANTS.filterEditionOrderDescendantDefined());
    }

    /**
     * Create a presenter for the FilterView.
     * 
     * @param pDisplay
     *            The display interface.
     * @param pEventBus
     *            The event bus.
     * @param pFilterEditionPopupNextCommand
     *            Command on next button.
     * @param pFilterEditionPopupPreviousCommand
     *            Command on previous button.
     * @param pFilterEditionPreSaveCommand
     *            Command on Save button.
     * @param pFilterEditionSavePopupPresenter
     *            save popup Presenter
     * @param pFilterEditionSelectCriteriaGroupPopupPresenter
     *            criteria edition popup Presenter
     */
    @Inject
    public FilterEditionPopupPresenter(
            final FilterEditionPopupDisplay pDisplay,
            final EventBus pEventBus,
            FilterEditionPopupNextCommand pFilterEditionPopupNextCommand,
            FilterEditionPopupPreviousCommand pFilterEditionPopupPreviousCommand,
            FilterEditionPreSaveCommand pFilterEditionPreSaveCommand,
            final FilterEditionSavePopupPresenter pFilterEditionSavePopupPresenter,
            final FilterEditionSelectCriteriaGroupPopupPresenter pFilterEditionSelectCriteriaGroupPopupPresenter) {
        super(pDisplay, pEventBus);
        filterEditionPopupNextCommand = pFilterEditionPopupNextCommand;
        filterEditionPopupPreviousCommand = pFilterEditionPopupPreviousCommand;
        filterEditionPreSaveCommand = pFilterEditionPreSaveCommand;
        fieldsHierarchy = new HashMap<String, LinkedList<UiFilterFieldName>>();
        fieldsCategoryName = new HashMap<String, String>();
        fieldTypes = new HashMap<String, FieldType>();
        categoryValues = new HashMap<String, List<String>>();
        filterEditionSavePopup = pFilterEditionSavePopupPresenter;
        filterEditionSelectCriteriaGroupPopup =
                pFilterEditionSelectCriteriaGroupPopupPresenter;
        validator = new Validator();
    }

    private void addCriterion(int pIndexOfGroup, String pPath,
            String pFieldTranslatedName, String pFieldName,
            FieldType pFieldType, String pOperator, Object pValue,
            boolean pIsCaseSensitive, List<String> pCategoryValues) {

        Boolean lIsCaseSensitive = null;
        if (FieldType.ATTACHED.equals(pFieldType)
                || FieldType.STRING.equals(pFieldType)
                || FieldType.VIRTUAL.equals(pFieldType)
                || FieldType.POINTER.equals(pFieldType)
                || FieldType.CHOICE.equals(pFieldType)) {
            lIsCaseSensitive = pIsCaseSensitive;
        }

        getDisplay().addCriterion(
                pIndexOfGroup,
                pPath,
                pFieldTranslatedName,
                pFieldName,
                pFieldType,
                getAvailableOperators(pFieldType),
                pOperator,
                getGpmFieldList(pFieldTranslatedName, pFieldType, pValue,
                        pCategoryValues), lIsCaseSensitive,
                !FieldType.BOOLEAN.equals(pFieldType));
    }

    /**
     * Compute UiFilter from View values.
     * 
     * @return UiFilter built from view.
     */
    @SuppressWarnings("unchecked")
    public UiFilter computeFilter() {
        UiFilter lFilter = new UiFilter();

        //Set default values in creation mode
        if (getFilterId() == null) {
            lFilter.setHidden(false);
            lFilter.setVisibility(UiFilterVisibility.USER);
            lFilter.setUsage(UiFilterUsage.TABLE_VIEW);
            lFilter.setName("New Filter");
        }

        lFilter.setId(filterId);
        lFilter.setFilterType(getFilterType());
        lFilter.setEditable(true);

        //Containers
        if (isSelectContainerPopupLoaded) {
            lFilter.setContainerTypes(getDisplay().getSelectedContainers());
        }

        //Scopes
        if (isSelectScopePopupLoaded) {
            lFilter.setScopes(getDisplay().getSelectedScopes());
        }

        //Result fields
        if (isSelectResultFieldsPopupLoaded) {
            BusinessMultivaluedField<GpmMultipleMultivaluedElement> lResultFields =
                    getDisplay().getSelectedResultFields();
            List<UiFilterResultField> lUiFilterResultFields =
                    new ArrayList<UiFilterResultField>();
            for (GpmMultipleMultivaluedElement lResultField : lResultFields) {
                String lLabel =
                        lResultField.getField(
                                FilterEditionPopupView.RESULT_NEW_NAME_FIELD).getAsString();
                if (lLabel.isEmpty()) {
                    lLabel = null;
                }
                UiFilterResultField lUiFilterResultField =
                        new UiFilterResultField(
                                lLabel,
                                fieldsHierarchy.get(lResultField.getField(
                                        FilterEditionPopupView.PATH_FIELD).getAsString()
                                        + "/"
                                        + lResultField.getField(
                                                FilterEditionPopupView.TECHNICAL_NAME_FIELD).getAsString()));
                lUiFilterResultFields.add(lUiFilterResultField);
            }
            lFilter.setResultFields(lUiFilterResultFields);
        }

        // Criterias
        if (isSelectCriteriasPopupLoaded) {
            List<UiFilterCriteriaGroup> lCriteriaGroups =
                    new ArrayList<UiFilterCriteriaGroup>();
            for (int i = 0; i < getDisplay().getSelectedCriteriaGroupsCount(); i++) {
                BusinessMultivaluedField<GpmMultipleMultivaluedElement> lCriterionList =
                        getDisplay().getSelectedCriterias(i);
                if (!lCriterionList.isEmpty()) {
                    List<UiFilterCriterion> lUiFilterCriterionFields =
                            new ArrayList<UiFilterCriterion>();
                    for (GpmMultipleMultivaluedElement lCriterion : lCriterionList) {

                        BusinessField lCompareTo =
                                ((GpmGenericField) lCriterion.getField(FilterEditionPopupView.CRITERION_VALUE_TO_COMPARE_WITH)).getSelectedField();

                        Serializable lValue = null;

                        if (lCompareTo instanceof BusinessSimpleField<?>) {
                            lValue =
                                    ((BusinessSimpleField<Serializable>) lCompareTo).get();
                        }
                        else {
                            lValue = lCompareTo.getAsString();
                        }

                        Boolean lIsCaseSensitive =
                                Boolean.valueOf(lCriterion.getField(
                                        FilterEditionPopupView.CRITERION_CASE_SENSITIVE).getAsString());
                        String lOperator =
                                lCriterion.getField(
                                        FilterEditionPopupView.CRITERION_AVAILABLE_OPERATORS).getAsString();
                        String lKey =
                                lCriterion.getField(
                                        FilterEditionPopupView.PATH_FIELD).getAsString()
                                        + "/"
                                        + lCriterion.getField(
                                                FilterEditionPopupView.TECHNICAL_NAME_FIELD).getAsString();

                        UiFilterCriterion lUiFilterCriterion =
                                new UiFilterCriterion(null,
                                        fieldsHierarchy.get(lKey),
                                        UiFilterOperator.fromKey(lOperator),
                                        lValue, lIsCaseSensitive,
                                        fieldTypes.get(lKey),
                                        fieldsCategoryName.get(lKey));
                        lUiFilterCriterionFields.add(lUiFilterCriterion);
                    }
                    UiFilterCriteriaGroup lUiFilterCriteriaGroup =
                            new UiFilterCriteriaGroup();
                    lUiFilterCriteriaGroup.setCriteria(lUiFilterCriterionFields);
                    lCriteriaGroups.add(lUiFilterCriteriaGroup);
                }
            }
            lFilter.setCriteriaGroups(lCriteriaGroups);
        }

        //Sorting fields
        if (isSelectSortingFieldsPopupLoaded) {
            BusinessMultivaluedField<GpmMultipleMultivaluedElement> lSortingFields =
                    getDisplay().getSelectedSortingFields();
            List<UiFilterSortingField> lUiFilterSortingFields =
                    new ArrayList<UiFilterSortingField>();
            for (GpmMultipleMultivaluedElement lSortingField : lSortingFields) {
                String lOrder =
                        lSortingField.getField(
                                FilterEditionPopupView.AVAILABLE_SORTING).getAsString();
                String lKey =
                        lSortingField.getField(
                                FilterEditionPopupView.PATH_FIELD).getAsString()
                                + "/"
                                + lSortingField.getField(
                                        FilterEditionPopupView.TECHNICAL_NAME_FIELD).getAsString();
                Boolean lVirtualValue =
                        Boolean.valueOf(lSortingField.getField(
                                FilterEditionPopupView.SORTING_VIRTUAL_FIELD).getAsString());

                UiFilterSortingField lUiFilterSortingField =
                        new UiFilterSortingField(null,
                                fieldsHierarchy.get(lKey),
                                UiFilterSorting.fromKey(lOrder),
                                fieldTypes.get(lKey), lVirtualValue);
                lUiFilterSortingFields.add(lUiFilterSortingField);
            }
            lFilter.setSortingFields(lUiFilterSortingFields);
        }

        lFilter.setCategoryValues(categoryValues);
        lFilter.setResetNeeded(resetNeeded);
        return lFilter;
    }

    private LinkedList<UiFilterFieldName> computeName(
            TreeFilterEditionUsableFieldItem pItem) {
        TreeFilterEditionContainerItem lContainerItem =
                getDisplay().getSelectedContainerItem();
        TreeFilterEditionUsableFieldItem lFieldItem = pItem;
        LinkedList<UiFilterFieldName> lReversedHierarchy =
                new LinkedList<UiFilterFieldName>();
        UiFilterFieldName lFieldName =
                new UiFilterFieldName(lFieldItem.getNode().getName(),
                        lFieldItem.getNode().getTranslatedName(),
                        UiFilterFieldNameType.FIELD);
        if (lFieldItem.getParentItem() != null) {
            UiFilterUsableField lNode =
                    ((TreeFilterEditionUsableFieldItem) lFieldItem.getParentItem()).getNode();
            lReversedHierarchy.add(new UiFilterFieldName(lNode.getName(),
                    lNode.getTranslatedName(), UiFilterFieldNameType.MULTIPLE));
        }
        if (lContainerItem != null && selectedContainers.length == 1) {
            lReversedHierarchy.add(new UiFilterFieldName(
                    lContainerItem.getNode().getContainerName(),
                    lContainerItem.getNode().getContainerTranslatedName(),
                    lContainerItem.getNode().getContainerType()));
            while (lContainerItem.getParentItem() != null) {
                lContainerItem =
                        (TreeFilterEditionContainerItem) lContainerItem.getParentItem();
                lReversedHierarchy.add(new UiFilterFieldName(
                        lContainerItem.getNode().getContainerName(),
                        lContainerItem.getNode().getContainerTranslatedName(),
                        lContainerItem.getNode().getContainerType()));
            }
        }
        LinkedList<UiFilterFieldName> lHierarchy =
                new LinkedList<UiFilterFieldName>();
        for (int i = lReversedHierarchy.size() - 2; i >= 0; i--) {
            lHierarchy.add(lReversedHierarchy.get(i));
        }
        lHierarchy.add(lFieldName);
        return lHierarchy;
    }

    private boolean equalsContainers(
            UiFilterContainerType[] pSelectedContainers,
            List<UiFilterContainerType> pSelectedContainers2) {
        boolean lResult = true;
        if (pSelectedContainers.length != pSelectedContainers2.size()) {
            return false;
        }
        for (int i = 0; i < pSelectedContainers.length; i++) {
            lResult =
                    lResult
                            && pSelectedContainers[i].getId().equals(
                                    pSelectedContainers2.get(i).getId());
        }
        return lResult;
    }

    /**
     * Execute action on next and previous button according to the current step.
     */
    public void fireAction() {
        switch (getCurrentStep()) {

            case STEP_1_CHOOSE_CONTAINERS:
                if (!isSelectContainerPopupLoaded) {
                    fireSelectContainersEvent(productName, getFilterId());
                }
                else {
                    getDisplay().displayContainers();
                }
                break;

            case STEP_2_CHOOSE_PRODUCT:
                if (!isSelectScopePopupLoaded) {
                    fireSelectScopeEvent(productName, getFilterId());
                }
                else {
                    getDisplay().displayScopes();
                    isSelectScopePopupLoaded = true;
                }
                break;

            case STEP_3_RESULT_FIELDS:
                usableFieldTreeManager.setMode(TreeFilterEditionMode.SELECT_RESULT_MODE);
                //If a reset is needed, result fields must be cleared
                if (!isSelectResultFieldsPopupLoaded || resetNeeded) {
                    fireSelectResultFieldsEvent(productName);
                    resetNeeded = false;
                }
                else {
                    getDisplay().displayResultFields(
                            selectedContainers.length == 1);
                    isSelectResultFieldsPopupLoaded = true;
                }
                break;

            case STEP_4_CHOOSE_CRITERIA:
                usableFieldTreeManager.setMode(TreeFilterEditionMode.SELECT_CRITERIA_MODE);
                if (!isSelectCriteriasPopupLoaded
                        && FilterEditionMode.EDITION_MODE.equals(mode)) {
                    fireSelectCriteriaFieldsEvent(productName, getFilterId());
                }
                else {
                    getDisplay().displayCriterias();
                    isSelectCriteriasPopupLoaded = true;
                }
                break;

            case STEP_5_CHOOSE_SORTING:
                usableFieldTreeManager.setMode(TreeFilterEditionMode.SELECT_SORTING_MODE);
                if (!isSelectSortingFieldsPopupLoaded
                        && FilterEditionMode.EDITION_MODE.equals(mode)) {
                    fireSelectSortingFieldsEvent(productName, getFilterId());
                }
                else {
                    getDisplay().displaySortingFields();
                    isSelectSortingFieldsPopupLoaded = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Fire SelectContainerAction.
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id (null if filter creation)
     */
    public void fireSelectContainersEvent(String pProductName, String pFilterId) {
        fireEvent(GlobalEvent.NEW_FILTER_SELECT_CONTAINER.getType(),
                new SelectContainerAction(pProductName, getFilterType(),
                        pFilterId));
    }

    /**
     * Fire SelectCriteriaFieldAction.
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id (null if filter creation)
     */
    public void fireSelectCriteriaFieldsEvent(String pProductName,
            String pFilterId) {
        fireEvent(GlobalEvent.NEW_FILTER_SELECT_CRITERIA.getType(),
                new SelectCriteriaFieldAction(pProductName, getFilterType(),
                        getFilterId(), false));
    }

    /**
     * Fire SelectResultFieldAction.
     * 
     * @param pProductName
     *            Product name.
     */
    public void fireSelectResultFieldsEvent(String pProductName) {
        fireEvent(GlobalEvent.NEW_FILTER_SELECT_RESULT.getType(),
                new SelectResultFieldAction(pProductName, getFilterType(),
                        getFilterId(), getDisplay().getSelectedContainers(),
                        true, resetNeeded));
    }

    /**
     * Fire SelectScopeAction.
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id (null if filter creation)
     */
    public void fireSelectScopeEvent(String pProductName, String pFilterId) {
        fireEvent(GlobalEvent.NEW_FILTER_SELECT_PRODUCT.getType(),
                new SelectScopeAction(pProductName, pFilterId));
    }

    /**
     * Fire SelectSortingFieldAction.
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id (null if filter creation)
     */
    public void fireSelectSortingFieldsEvent(String pProductName,
            String pFilterId) {
        fireEvent(GlobalEvent.NEW_FILTER_SELECT_SORTING.getType(),
                new SelectSortingFieldAction(pProductName, getFilterType(),
                        pFilterId));
    }

    /**
     * Fire GetCategoryValuesAction (Category name must be cached first).
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id (null if filter creation)
     * @param pAddCriterionAction
     *            add criterion action.
     */
    public void fireGetCategoryValuesEvent(String pProductName,
            String pFilterId, AddCriterionAction pAddCriterionAction) {
        List<UiFilterScope> lSelectedScopesList = null;
        if (isSelectScopePopupLoaded) {
            lSelectedScopesList = getDisplay().getSelectedScopes();
        }
        fireEvent(GlobalEvent.NEW_FILTER_GET_CATEGORY_VALUES.getType(),
                new GetCategoryValuesAction(pProductName, getFilterType(),
                        pFilterId, lSelectedScopesList, pAddCriterionAction));
    }

    private List<Translation> getAvailableOperators(FieldType pFieldType) {
        List<Translation> lOperators = new ArrayList<Translation>();
        lOperators.add(new Translation(UiFilterOperator.EQUAL.getKey(),
                OPERATOR_TRANSLATIONS.get(UiFilterOperator.EQUAL)));
        lOperators.add(new Translation(UiFilterOperator.NOT_EQUAL.getKey(),
                OPERATOR_TRANSLATIONS.get(UiFilterOperator.NOT_EQUAL)));
        if (FieldType.ATTACHED.equals(pFieldType)
                || FieldType.STRING.equals(pFieldType)
                || FieldType.BOOLEAN.equals(pFieldType)
                || FieldType.CHOICE.equals(pFieldType)) {
            lOperators.add(new Translation(UiFilterOperator.LIKE.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.LIKE)));
            lOperators.add(new Translation(UiFilterOperator.NOT_LIKE.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.NOT_LIKE)));
        }
        else if (FieldType.INTEGER.equals(pFieldType)
                || FieldType.REAL.equals(pFieldType)
                || FieldType.DATE.equals(pFieldType)) {
            lOperators.add(new Translation(
                    UiFilterOperator.GREATER_OR_EQUALS.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.GREATER_OR_EQUALS)));
            lOperators.add(new Translation(UiFilterOperator.GREATER.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.GREATER)));
            lOperators.add(new Translation(
                    UiFilterOperator.LESS_OR_EQUALS.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.LESS_OR_EQUALS)));
            lOperators.add(new Translation(UiFilterOperator.LESS.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.LESS)));
        }
        if (FieldType.DATE.equals(pFieldType)) {
            lOperators.add(new Translation(UiFilterOperator.SINCE.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.SINCE)));
            lOperators.add(new Translation(UiFilterOperator.OTHER.getKey(),
                    OPERATOR_TRANSLATIONS.get(UiFilterOperator.OTHER)));
        }
        return lOperators;
    }

    private List<Translation> getAvailableOrder(FieldType pFieldType,
            boolean pVirtualField) {
        List<Translation> lAvailableOrders = new ArrayList<Translation>();

        lAvailableOrders.add(new Translation(
                UiFilterSorting.ASCENDANT.getKey(),
                ORDER_TRANSLATIONS.get(UiFilterSorting.ASCENDANT)));
        lAvailableOrders.add(new Translation(
                UiFilterSorting.DESCENDANT.getKey(),
                ORDER_TRANSLATIONS.get(UiFilterSorting.DESCENDANT)));

        if (FieldType.CHOICE.equals(pFieldType) && !pVirtualField) {
            lAvailableOrders.add(new Translation(
                    UiFilterSorting.ASCENDANT_DEFINED.getKey(),
                    ORDER_TRANSLATIONS.get(UiFilterSorting.ASCENDANT_DEFINED)));
            lAvailableOrders.add(new Translation(
                    UiFilterSorting.DESCENDANT_DEFINED.getKey(),
                    ORDER_TRANSLATIONS.get(UiFilterSorting.DESCENDANT_DEFINED)));
        }

        return lAvailableOrders;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.topcased.gpm.ui.application.client.popup.PopupPresenter#getClosePopupEvent()
     */
    @Override
    protected GlobalEvent<ClosePopupAction> getClosePopupEvent() {
        return GlobalEvent.CLOSE_FILTER_POPUP;
    }

    public FilterEditionStep getCurrentStep() {
        return currentStep;
    }

    public FilterEditionSelectCriteriaGroupPopupPresenter getFilterEditionSelectCriteriaGroupPopupPresenter() {
        return filterEditionSelectCriteriaGroupPopup;
    }

    public FilterEditionSavePopupPresenter getFilterEditionSavePopupPresenter() {
        return filterEditionSavePopup;
    }

    public String getFilterId() {
        return filterId;
    }

    /**
     * Get next step from current step.
     * 
     * @return Next step.
     */
    public FilterEditionStep getNextStep() {
        FilterEditionStep lResult = null;
        int lIndex = -1;
        int i = 0;
        while (i < FilterEditionStep.values().length && lIndex == -1) {
            if (FilterEditionStep.values()[i].equals(getCurrentStep())) {
                lIndex = i;
            }
            i++;
        }
        if (lIndex != FilterEditionStep.values().length - 1) {
            lResult = FilterEditionStep.values()[lIndex + 1];
        }
        if (FilterType.PRODUCT.equals(getFilterType())
                && FilterEditionStep.STEP_2_CHOOSE_PRODUCT.equals(lResult)) {
            lResult = FilterEditionStep.values()[lIndex + 2];
        }
        return lResult;
    }

    private String getPath(List<UiFilterFieldName> pName) {
        String lPath = "";
        // Root container is ignored => index starts at 1
        for (int i = 0; i < pName.size() - 1; i++) {
            UiFilterFieldName lFieldName = pName.get(i);
            lPath += lFieldName.getTranslatedName();
            if (i != pName.size() - 2) {
                lPath += "/";
            }
        }
        return lPath;
    }

    private String getPath(TreeFilterEditionUsableFieldItem pItem) {
        TreeFilterEditionContainerItem lItem = null;
        if (selectedContainers.length == 1) {
            lItem = getDisplay().getSelectedContainerItem();
        }
        String lPath = "";
        if (pItem.getParentItem() != null) {
            lPath =
                    ((TreeFilterEditionUsableFieldItem) pItem.getParentItem()).getNode().getTranslatedName();
        }
        if (lItem != null && lItem.getParentItem() != null) {
            if (!lPath.isEmpty()) {
                lPath = "/" + lPath;
            }
            lPath = lItem.getNode().getContainerTranslatedName() + lPath;
        }
        while (lItem != null && lItem.getParentItem() != null
                && lItem.getParentItem().getParentItem() != null) {
            lItem = (TreeFilterEditionContainerItem) lItem.getParentItem();
            lPath = lItem.getNode().getContainerTranslatedName() + "/" + lPath;
        }
        return lPath;
    }

    /**
     * Get previous step from current step.
     * 
     * @return Previous step.
     */
    public FilterEditionStep getPreviousStep() {
        FilterEditionStep lResult = null;
        int lIndex = -1;
        int i = 0;
        while (i < FilterEditionStep.values().length && lIndex == -1) {
            if (FilterEditionStep.values()[i].equals(getCurrentStep())) {
                lIndex = i;
            }
            i++;
        }
        if (lIndex != 0) {
            lResult = FilterEditionStep.values()[lIndex - 1];
        }
        if (FilterType.PRODUCT.equals(getFilterType())
                && FilterEditionStep.STEP_2_CHOOSE_PRODUCT.equals(lResult)) {
            lResult = FilterEditionStep.values()[lIndex - 2];
        }
        return lResult;
    }

    /**
     * Has container list changed ?
     * 
     * @return true if container list has changed, false if not.
     */
    public boolean hasContainerListChanged() {
        return containerListHasChanged;
    }

    /**
     * Initialize presenter.
     * 
     * @param pProductName
     *            Product name.
     * @param pFilterId
     *            Filter Id.
     * @param pFilterType
     *            Filter type.
     * @param pExecuteCommand
     *            Command to execute on click on execute button.
     */
    public void init(String pProductName, String pFilterId,
            FilterType pFilterType,
            AbstractFilterEditionExecuteCommand pExecuteCommand) {
        validator.clear();
        categoryValues.clear();
        filterEditionSavePopup.reset();
        reset();
        setProductName(pProductName);
        setFilterId(pFilterId);
        filterType = pFilterType;
        getDisplay().setExecuteButtonHandler(pExecuteCommand);
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.customware.gwt.presenter.client.BasicPresenter#onBind()
     */
    @Override
    protected void onBind() {
        super.onBind();
        if (usableFieldTreeManager == null) {
            usableFieldTreeManager = new TreeFilterEditionUsableFieldManager() {

                @Override
                public void onSelection(TreeFilterEditionUsableFieldItem pItem) {
                    UiFilterUsableField lField = pItem.getNode();
                    String lPath = getPath(pItem);
                    String lName = lField.getName();
                    String lTranslatedName = lField.getTranslatedName();
                    // Store UiFilterFieldName
                    String lKey = lPath + "/" + lName;
                    fieldsHierarchy.put(lKey, computeName(pItem));
                    if (lField.getVirtualPossibleValues() != null) {
                        fieldsCategoryName.put(lKey, lField.getName());
                    }
                    else if (lField.getCategoryName() != null) {
                        fieldsCategoryName.put(lKey, lField.getCategoryName());
                    }
                    fieldTypes.put(lKey, lField.getFieldType());
                    String lEmptyField = "";
                    switch (this.getMode()) {
                        case SELECT_RESULT_MODE:
                            if ("$PRODUCT_HIERARCHY".equals(lField.getName())) {
                                break;
                            }
                            if (!isFieldAlreadyUsed(lKey,
                                    getDisplay().getSelectedResultFields())) {
                                getDisplay().addResultField(lPath,
                                        lTranslatedName, lName, lEmptyField);
                            }
                            break;
                        case SELECT_CRITERIA_MODE:
                            if (getDisplay().getSelectedCriteriaGroupsCount() == 0) {
                                getDisplay().addCriteriaGroup();
                                AddCriterionAction lAddCriterionAction =
                                        new AddCriterionAction(
                                                0,
                                                lPath,
                                                lTranslatedName,
                                                lField.getFieldType(),
                                                lEmptyField,
                                                null,
                                                false,
                                                lField.getCategoryName(),
                                                lField.isVirtualField(),
                                                lField.getVirtualPossibleValues(),
                                                lField.getName());
                                fireAddCriterionAction(lAddCriterionAction);
                            }
                            else {
                                AddCriterionAction lAddCriterionAction =
                                        new AddCriterionAction(
                                                -1,
                                                lPath,
                                                lTranslatedName,
                                                lField.getFieldType(),
                                                lEmptyField,
                                                null,
                                                false,
                                                lField.getCategoryName(),
                                                lField.isVirtualField(),
                                                lField.getVirtualPossibleValues(),
                                                lField.getName());
                                filterEditionSelectCriteriaGroupPopup.setAvailableGroups(getDisplay().getSelectedCriteriaGroupsCount());
                                filterEditionSelectCriteriaGroupPopup.setAddCriterionAction(lAddCriterionAction);
                                filterEditionSelectCriteriaGroupPopup.bind();
                            }
                            break;
                        case SELECT_SORTING_MODE:
                            if (!isFieldAlreadyUsed(lKey,
                                    getDisplay().getSelectedSortingFields())) {
                                getDisplay().addSortingField(
                                        lPath,
                                        lTranslatedName,
                                        lName,
                                        getAvailableOrder(
                                                lField.getFieldType(),
                                                lField.isVirtualField()), "",
                                        lField.isVirtualField());
                            }
                            break;
                        default:
                            break;
                    }

                }

                private boolean isFieldAlreadyUsed(
                        String pKey,
                        BusinessMultivaluedField<GpmMultipleMultivaluedElement> pSelectedFields) {
                    List<String> lSelectedFieldKeys = new ArrayList<String>();
                    for (GpmMultipleMultivaluedElement lResultField : pSelectedFields) {
                        lSelectedFieldKeys.add(lResultField.getField(
                                FilterEditionPopupView.PATH_FIELD).getAsString()
                                + "/"
                                + lResultField.getField(
                                        FilterEditionPopupView.TECHNICAL_NAME_FIELD).getAsString());
                    }
                    return lSelectedFieldKeys.contains(pKey);
                }
            };
        }

        // Update popup state
        fireAction();

        //add listener on container selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_CONTAINER.getType(),
                new ActionEventHandler<SelectContainerResult>() {
                    @Override
                    public void execute(SelectContainerResult pResult) {
                        selectContainers(pResult);
                    }
                });
        //add listener on scope selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_PRODUCT.getType(),
                new ActionEventHandler<SelectScopeResult>() {
                    @Override
                    public void execute(SelectScopeResult pResult) {
                        selectScopes(pResult);
                    }
                });
        //add listener on usable field selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_USABLE_FIELD.getType(),
                new ActionEventHandler<GetUsableFieldsResult>() {
                    @Override
                    public void execute(GetUsableFieldsResult pResult) {
                        selectUsableFields(pResult);
                    }
                });
        //add listener on filter result selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_RESULT.getType(),
                new ActionEventHandler<SelectResultFieldResult>() {
                    @Override
                    public void execute(final SelectResultFieldResult pResult) {
                        selectResultFields(pResult);
                    }
                });
        //add listener on filter criteria selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_CRITERIA.getType(),
                new ActionEventHandler<SelectCriteriaFieldResult>() {
                    @Override
                    public void execute(SelectCriteriaFieldResult pResult) {
                        selectCriteriaFields(pResult);
                    }
                });
        //add listener on filter sorting field selection action
        addEventHandler(GlobalEvent.NEW_FILTER_SELECT_SORTING.getType(),
                new ActionEventHandler<SelectSortingFieldResult>() {
                    @Override
                    public void execute(SelectSortingFieldResult pResult) {
                        selectSortingFields(pResult);
                    }
                });
        //Add listener on presave action
        addEventHandler(GlobalEvent.NEW_FILTER_PRESAVE.getType(),
                new ActionEventHandler<PreSaveFilterResult>() {
                    @Override
                    public void execute(final PreSaveFilterResult pResult) {
                        filterEditionSavePopup.displayPreSave(pResult);
                        filterEditionSavePopup.bind();
                    }
                });
        // Add listener on save filter action
        addEventHandler(GlobalEvent.NEW_FILTER_SAVE.getType(),
                new ActionEventHandler<SaveFilterResult>() {
                    @Override
                    public void execute(final SaveFilterResult pResult) {
                        setFilterId(pResult.getFilterId());
                    }
                });
        // Add listener on criteria adding
        addEventHandler(GlobalEvent.NEW_FILTER_ADD_CRITERION.getType(),
                new ActionEventHandler<AddCriterionAction>() {
                    @Override
                    public void execute(final AddCriterionAction pResult) {
                        // If group index has to be set.
                        if (pResult.getGroupIndex() == -1) {
                            int lIndexOfGroup =
                                    getFilterEditionSelectCriteriaGroupPopupPresenter().getSelectedCriteriaGroup();
                            if (lIndexOfGroup == -1) {
                                lIndexOfGroup = getDisplay().addCriteriaGroup();
                            }
                            pResult.setGroupIndex(lIndexOfGroup);
                        }

                        if (pResult.getCategoryName() != null
                                && !categoryValues.containsKey(pResult.getCategoryName())
                                && pResult.getVirtualPossibleValues() == null) {
                            fireGetCategoryValuesEvent(productName,
                                    getFilterId(), pResult);
                        }
                        else {
                            List<String> lCategoryValues;
                            if (pResult.getVirtualPossibleValues() != null) {
                                lCategoryValues =
                                        pResult.getVirtualPossibleValues();
                                categoryValues.put(
                                        pResult.getNotTranslatedFieldName(),
                                        lCategoryValues);
                            }
                            else {
                                lCategoryValues =
                                        categoryValues.get(pResult.getCategoryName());
                            }
                            addCriterion(pResult.getGroupIndex(),
                                    pResult.getFieldPath(),
                                    pResult.getFieldName(),
                                    pResult.getNotTranslatedFieldName(),
                                    pResult.getFieldType(),
                                    pResult.getOperator(), pResult.getValue(),
                                    pResult.isCaseSensitive(), lCategoryValues);
                            getDisplay().displayCriterias();
                        }
                        filterEditionSelectCriteriaGroupPopup.unbind();
                    }
                });
        // Add listener on get category values
        addEventHandler(GlobalEvent.NEW_FILTER_GET_CATEGORY_VALUES.getType(),
                new ActionEventHandler<GetCategoryValuesResult>() {
                    @Override
                    public void execute(final GetCategoryValuesResult pResult) {
                        categoryValues.put(
                                pResult.getAddCriterionAction().getCategoryName(),
                                pResult.getCategoryValues());
                        fireAddCriterionAction(pResult.getAddCriterionAction());
                    }
                });
    }

    /**
     * called when criterion added
     * 
     * @param pAddCriterionAction
     *            the add action
     */
    public void fireAddCriterionAction(AddCriterionAction pAddCriterionAction) {
        fireEvent(GlobalEvent.NEW_FILTER_ADD_CRITERION.getType(),
                pAddCriterionAction);
    }

    /**
     * Reset presenter and view.
     */
    public void reset() {
        getDisplay().reset();
        selectedContainers = null;
        usableFieldTreeManager = null;
        containerTreeManager = null;
        isSelectContainerPopupLoaded = false;
        isSelectScopePopupLoaded = false;
        isSelectResultFieldsPopupLoaded = false;
        isSelectCriteriasPopupLoaded = false;
        isSelectSortingFieldsPopupLoaded = false;
        fieldsHierarchy.clear();
        fieldsCategoryName.clear();
        fieldTypes.clear();
    }

    /**
     * Reset current filter (called is container list has changed).
     */
    public void resetFilter() {
        isSelectResultFieldsPopupLoaded = false;
        isSelectSortingFieldsPopupLoaded = true;
        isSelectCriteriasPopupLoaded = true;
        resetNeeded = true;
        mode = FilterEditionMode.CREATION_MODE;
        getDisplay().resetCriterias();
        getDisplay().resetSortingFields();
        getDisplay().resetResultFields();

        selectedContainers =
                getDisplay().getSelectedContainers().toArray(
                        new UiFilterContainerType[0]);
    }

    /**
     * Build select containers panel.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectContainers(SelectContainerResult pResult) {
        getDisplay().setSelectContainerPanel(pResult.getAvailableContainers(),
                pResult.getContainers());
        getDisplay().displayContainers();
        isSelectContainerPopupLoaded = true;
    }

    /**
     * Build select criteria panel.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectCriteriaFields(SelectCriteriaFieldResult pResult) {
        if (pResult.getContainerHierarchy() != null) {
            initContainerHierarchy(pResult.getContainerHierarchy(),
                    pResult.getRootContainers(), pResult.getMaxLevel());
        }
        getDisplay().resetCriterias();
        categoryValues = pResult.getCategoryValues();
        if (pResult.getCriteriaGroups() != null) {
            for (UiFilterCriteriaGroup lUiFilterCriteriaGroup : pResult.getCriteriaGroups()) {
                int lIndex = getDisplay().addCriteriaGroup();
                for (UiFilterCriterion lUiFilterCriterion : lUiFilterCriteriaGroup.getCriteria()) {
                    String lPath = getPath(lUiFilterCriterion.getName());
                    String lTranslatedName =
                            lUiFilterCriterion.getName().getLast().getTranslatedName();
                    String lName =
                            lUiFilterCriterion.getName().getLast().getName();
                    String lKey = lPath + "/" + lName;
                    fieldsHierarchy.put(lKey, lUiFilterCriterion.getName());
                    if (lUiFilterCriterion.getCategoryName() != null) {
                        fieldsCategoryName.put(lKey,
                                lUiFilterCriterion.getCategoryName());
                    }
                    fieldTypes.put(lKey, lUiFilterCriterion.getFieldType());
                    addCriterion(
                            lIndex,
                            lPath,
                            lTranslatedName,
                            lName,
                            lUiFilterCriterion.getFieldType(),
                            lUiFilterCriterion.getOperator().getKey(),
                            lUiFilterCriterion.getValue(),
                            lUiFilterCriterion.isCaseSensitive(),
                            categoryValues.get(lUiFilterCriterion.getCategoryName()));
                }
            }
        }
        isSelectCriteriasPopupLoaded = true;
        getDisplay().displayCriterias();
    }

    /**
     * Build select result fields panel.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectResultFields(SelectResultFieldResult pResult) {
        if (pResult.getContainerHierarchy() != null) {
            initContainerHierarchy(pResult.getContainerHierarchy(),
                    pResult.getRootContainers(), pResult.getMaxLevel());
        }
        getDisplay().resetResultFields();
        for (UiFilterResultField lUiFilterResultField : pResult.getResultFields()) {
            String lPath = getPath(lUiFilterResultField.getName());
            String lTranslatedName =
                    lUiFilterResultField.getName().getLast().getTranslatedName();
            String lName = lUiFilterResultField.getName().getLast().getName();
            getDisplay().addResultField(lPath, lTranslatedName, lName,
                    lUiFilterResultField.getLabel());
            fieldsHierarchy.put(lPath + "/" + lName,
                    lUiFilterResultField.getName());
        }
        getDisplay().displayResultFields(selectedContainers.length == 1);
        isSelectResultFieldsPopupLoaded = true;
    }

    private void initContainerHierarchy(
            Map<String, UiFilterContainerHierarchy> pContainerHierarchy,
            List<UiFilterContainerType> pRootContainers, int pMaxLevel) {

        List<UiFilterContainerType> lContainers =
                getDisplay().getSelectedContainers();
        //Update lContainers and selectedContainers every times
        lContainers = pRootContainers;
        selectedContainers = lContainers.toArray(new UiFilterContainerType[0]);

        List<TreeFilterEditionContainerItem> lContainerItems =
                new ArrayList<TreeFilterEditionContainerItem>();
        final List<String> lRootContainersIds = new ArrayList<String>();
        for (UiFilterContainerType lContainer : lContainers) {
            lContainerItems.add(new TreeFilterEditionContainerItem(
                    pContainerHierarchy.get(lContainer.getId()), pMaxLevel,
                    null));
            lRootContainersIds.add(lContainer.getId());
        }

        //Single container filter
        if (selectedContainers.length == 1) {
            containerTreeManager = new TreeFilterEditionContainerManager() {
                @Override
                public void onSelection(TreeFilterEditionContainerItem pItem) {
                    List<String> lContainerList = new ArrayList<String>();
                    lContainerList.add(pItem.getNode().getContainerId());
                    fireEvent(
                            GlobalEvent.NEW_FILTER_SELECT_USABLE_FIELD.getType(),
                            new GetUsableFieldsAction(productName,
                                    getFilterType(), lContainerList));
                }
            };
            containerTreeManager.setHierarchy(pContainerHierarchy);
            getDisplay().setContainerHierarchyPanel(containerTreeManager,
                    lContainerItems);
        }
        //Multicontainer filter
        else {
            //The new hierarchy panel is empty because there are several containers
            getDisplay().setContainerHierarchyPanel(null,
                    new ArrayList<TreeFilterEditionContainerItem>());
            fireEvent(GlobalEvent.NEW_FILTER_SELECT_USABLE_FIELD.getType(),
                    new GetUsableFieldsAction(productName, getFilterType(),
                            lRootContainersIds));
        }
    }

    /**
     * Build select scopes panel.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectScopes(SelectScopeResult pResult) {
        getDisplay().setSelectProductPanel(pResult.getAvailableScopes(),
                pResult.getScopes());
        getDisplay().displayScopes();
        isSelectScopePopupLoaded = true;
    }

    /**
     * Build select sorting fields panel.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectSortingFields(SelectSortingFieldResult pResult) {
        getDisplay().resetSortingFields();
        if (pResult.getSortingFields() != null) {
            for (UiFilterSortingField lUiFilterSortingField : pResult.getSortingFields()) {
                String lPath = getPath(lUiFilterSortingField.getName());
                String lTranslatedName =
                        lUiFilterSortingField.getName().getLast().getTranslatedName();
                String lName =
                        lUiFilterSortingField.getName().getLast().getName();
                getDisplay().addSortingField(
                        lPath,
                        lTranslatedName,
                        lName,
                        getAvailableOrder(lUiFilterSortingField.getFieldType(),
                                lUiFilterSortingField.isVirtualField()),
                        lUiFilterSortingField.getOrder().getKey(),
                        lUiFilterSortingField.isVirtualField());
                String lKey = lPath + "/" + lName;
                fieldsHierarchy.put(lKey, lUiFilterSortingField.getName());
                fieldTypes.put(lKey, lUiFilterSortingField.getFieldType());
            }
        }
        getDisplay().displaySortingFields();
        isSelectSortingFieldsPopupLoaded = true;
    }

    /**
     * Build usable fields tree.
     * 
     * @param pResult
     *            Command result.
     */
    public void selectUsableFields(GetUsableFieldsResult pResult) {
        Map<String, UiFilterUsableField> lFields = pResult.getUsablefields();
        if (lFields.containsKey("$PRODUCT_HIERARCHY")) {
            lFields.remove("$PRODUCT_HIERARCHY");
        }
        List<TreeFilterEditionUsableFieldItem> lRoots =
                new ArrayList<TreeFilterEditionUsableFieldItem>();
        for (UiFilterUsableField lField : lFields.values()) {
            if (!(FieldType.MULTIPLE.equals(lField.getFieldType()) && (lField.getSubFields() == null || lField.getSubFields().isEmpty()))) {
                lRoots.add(new TreeFilterEditionUsableFieldItem(lField));
            }
        }
        usableFieldTreeManager.setUsableFields(pResult.getUsablefields());
        getDisplay().setUsableFieldHierarchy(usableFieldTreeManager, lRoots);
    }

    /**
     * Set current step.
     * 
     * @param pCurrentStep
     *            Current step.
     */
    public void setCurrentStep(FilterEditionStep pCurrentStep) {
        currentStep = pCurrentStep;
        getDisplay().setHeaderText(
                CONSTANTS.filterEditionPopupTitle() + " - "
                        + pCurrentStep.getValue());
        getDisplay().setNextButtonHandler(filterEditionPopupNextCommand,
                getNextStep() != null);
        getDisplay().setPreviousButtonHandler(
                filterEditionPopupPreviousCommand, getPreviousStep() != null);
        getDisplay().setSaveButtonHandler(filterEditionPreSaveCommand, 
                currentStep != FilterEditionStep.STEP_1_CHOOSE_CONTAINERS);
    }

    /**
     * Set if the filter edited is a template filter.
     * 
     * @param pTemplateFilter
     *            If the edited filter is a template filter.
     */
    public void setFilterTemplate(final boolean pTemplateFilter) {
        templateFilter = pTemplateFilter;
    }

    /**
     * Set filter Id.
     * 
     * @param pFilterId
     *            The filter Id.
     */
    public void setFilterId(String pFilterId) {
        if (pFilterId != null) {
            mode = FilterEditionMode.EDITION_MODE;
        }
        else {
            mode = FilterEditionMode.CREATION_MODE;
        }
        filterId = pFilterId;
    }

    public void setProductName(String pProductName) {
        productName = pProductName;
    }

    /**
     * Validate all steps.
     * 
     * @return Validation state.
     */
    public boolean validateAllSteps() {
        boolean lResult = true;
        for (FilterEditionStep lStep : FilterEditionStep.values()) {
            lResult = lResult && validateStep(lStep);
        }
        return lResult;
    }

    /**
     * Validates current step data before changing step.
     * 
     * @return Validation state.
     */
    public boolean validateStep() {
        return validateStep(getCurrentStep());
    }

    /**
     * Validates current step data before changing step.
     * 
     * @param pStep
     *            Step to validate.
     * @return Validation state.
     */
    private boolean validateStep(FilterEditionStep pStep) {
        boolean lStepOK = true;
        containerListHasChanged = false;
        String lMessage = null;
        switch (pStep) {
            case STEP_1_CHOOSE_CONTAINERS:
                if (selectedContainers != null
                        && !getDisplay().getSelectedContainers().isEmpty()
                        && !equalsContainers(selectedContainers,
                                getDisplay().getSelectedContainers())) {
                    containerListHasChanged = true;
                }
                else if (isSelectContainerPopupLoaded) {
                    lStepOK = getDisplay().getSelectedContainers().size() > 0;
                    lMessage =
                            Ui18n.MESSAGES.filterEditionErrorContainersEmpty();
                }

                if (selectedContainers == null
                        && !getDisplay().getSelectedContainers().isEmpty()) {
                    selectedContainers =
                            getDisplay().getSelectedContainers().toArray(
                                    new UiFilterContainerType[0]);
                }

                break;
            case STEP_3_RESULT_FIELDS:
                // Don't check if result fields is empty for template filter
                // while step 3 has not been loaded
                lStepOK =
                        (templateFilter && !isSelectResultFieldsPopupLoaded)
                                || getDisplay().getSelectedResultFieldsCount() > 0;
                lMessage = Ui18n.MESSAGES.filterEditionErrorFieldsEmpty();
                break;
            case STEP_4_CHOOSE_CRITERIA:
                lMessage = validator.validate();
                lStepOK = (lMessage == null);
                break;
            default:
                break;
        }
        lStepOK = lStepOK && !containerListHasChanged;
        if (!lStepOK && lMessage != null) {
            Application.INJECTOR.getErrorMessagePresenter().displayMessage(
                    lMessage);
        }
        return lStepOK;
    }

    private List<AbstractGpmField<?>> getGpmFieldList(String pFieldName,
            FieldType pFieldType, Object pValue, List<String> pCategoryValues) {
        List<IRule> lRules = null;
        List<AbstractGpmField<?>> lGpmFieldList =
                new ArrayList<AbstractGpmField<?>>();
        switch (pFieldType) {
            case BOOLEAN:
                GpmCheckBox lGpmCheckBox = new GpmCheckBox(true);
                if (pValue != null) {
                    lGpmCheckBox.set((Boolean) pValue);
                }
                lGpmCheckBox.getWidget().setText(
                        CONSTANTS.filterEditionBooleanRadioButton());
                lGpmFieldList.add(lGpmCheckBox);
                break;
            case INTEGER:
                GpmTextBox<Integer> lGpmTextBoxInteger =
                        new GpmTextBox<Integer>(
                                GpmIntegerFormatter.getInstance());
                lGpmTextBoxInteger.set((Integer) pValue);
                lGpmFieldList.add(lGpmTextBoxInteger);
                lRules = new ArrayList<IRule>();
                lRules.add(new IntegerRule());
                break;
            case REAL:
                GpmTextBox<Double> lGpmTextBoxDouble =
                        new GpmTextBox<Double>(GpmDoubleFormatter.getInstance());
                lGpmTextBoxDouble.set((Double) pValue);
                lGpmFieldList.add(lGpmTextBoxDouble);
                lRules = new ArrayList<IRule>();
                lRules.add(new RealRule());
                break;
            case STRING:
                GpmDropDownListBox lGpmStringFieldListBox =
                        new GpmDropDownListBox();
                if (pValue == null) {
                    lGpmStringFieldListBox.set("");
                }
                else {
                    lGpmStringFieldListBox.set((String) pValue);
                }
                List<String> lValues = new ArrayList<String>();
                lValues.add(FilterFieldValue.NOT_SPECIFIED.getValue());
                lGpmStringFieldListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(lValues));
                lGpmFieldList.add(lGpmStringFieldListBox);
                break;
            case ATTACHED:
            case VIRTUAL:
            case POINTER:
                GpmTextBox<String> lGpmTextBoxString =
                        new GpmTextBox<String>(GpmStringFormatter.getInstance());
                lGpmTextBoxString.set((String) pValue);
                lGpmFieldList.add(lGpmTextBoxString);
                break;
            case CHOICE:
                GpmDropDownListBox lGpmListBox = new GpmDropDownListBox();
                lGpmListBox.set((String) pValue);
                if (pCategoryValues != null) {
                    lGpmListBox.setPossibleValues(GpmChoiceBoxValue.buildFromStrings(pCategoryValues));
                }
                lGpmFieldList.add(lGpmListBox);
                break;
            case DATE:
                GpmDateBox lGpmDateBox =
                        new GpmDateBox(DateTimeFormat.getMediumDateFormat());
                if (pValue instanceof Date) {
                    lGpmDateBox.set((Date) pValue);
                }
                lGpmFieldList.add(lGpmDateBox);
                GpmDropDownListBox lGpmDateListBox = new GpmDropDownListBox();
                List<Translation> lPeriodValues = new ArrayList<Translation>();
                lPeriodValues.add(new Translation(
                        UiFilterPeriod.THIS_WEEK.getKey(),
                        CONSTANTS.filterEditionDateThisWeek()));
                lPeriodValues.add(new Translation(
                        UiFilterPeriod.LAST_WEEK.getKey(),
                        CONSTANTS.filterEditionDateLastWeek()));
                lPeriodValues.add(new Translation(
                        UiFilterPeriod.THIS_MONTH.getKey(),
                        CONSTANTS.filterEditionDateThisMonth()));
                lPeriodValues.add(new Translation(
                        UiFilterPeriod.LAST_MONTH.getKey(),
                        CONSTANTS.filterEditionDateLastMonth()));
                lGpmDateListBox.setPossibleValues(GpmChoiceBoxValue.buildFromTranslations(lPeriodValues));
                lGpmFieldList.add(lGpmDateListBox);

                GpmLabelledTextBox<Integer> lGpmDateTextBox =
                        new GpmLabelledTextBox<Integer>(
                                GpmIntegerFormatter.getInstance(),
                                CONSTANTS.filterEditionDateDays());
                List<IRule> lRuleList = new ArrayList<IRule>();
                lRuleList.add(new PositiveIntegerRule());
                lGpmDateTextBox.setFieldName(pFieldName);
                validator.addValidation(lGpmDateTextBox, lRuleList);

                if (pValue instanceof String) {
                    lGpmDateListBox.set((String) pValue);
                    if (UiFilterPeriod.THIS_WEEK.getKey().equals(pValue)) {
                        lGpmDateListBox.setTranslatedFieldName(CONSTANTS.filterEditionDateThisWeek());
                    }
                    else if (UiFilterPeriod.LAST_WEEK.getKey().equals(pValue)) {
                        lGpmDateListBox.setTranslatedFieldName(CONSTANTS.filterEditionDateLastWeek());
                    }
                    else if (UiFilterPeriod.THIS_MONTH.getKey().equals(pValue)) {
                        lGpmDateListBox.setTranslatedFieldName(CONSTANTS.filterEditionDateThisMonth());
                    }
                    else if (UiFilterPeriod.LAST_MONTH.getKey().equals(pValue)) {
                        lGpmDateListBox.setTranslatedFieldName(CONSTANTS.filterEditionDateLastMonth());
                    }
                    lGpmDateListBox.getWidget().setValue((String) pValue, true);
                }
                if (pValue instanceof Integer) {
                    lGpmDateTextBox.set((Integer) pValue);
                }
                lGpmFieldList.add(lGpmDateTextBox);
                break;
            default:
                break;
        }
        if (lRules != null) {
            for (AbstractGpmField<?> lField : lGpmFieldList) {
                lField.setFieldName(pFieldName);
                validator.addValidation(lField, lRules);
            }
        }
        return lGpmFieldList;
    }

    /**
     * Get filter type.
     * 
     * @return filter type.
     */
    public FilterType getFilterType() {
        return filterType;
    }

    /**
     * Return true if a reset of fields is needed
     * 
     * @return true if a reset of fields is needed
     */
    public boolean isResetNeeded() {
        return resetNeeded;
    }

    /**
     * set resetNeeded
     * 
     * @param pResetNeeded
     *            the resetNeeded to set
     */
    public void setResetNeeded(boolean pResetNeeded) {
        this.resetNeeded = pResetNeeded;
    }

}